Morphoo
=======

Morphoo is a Java object transformation library. The name is based from the greek work 'metamorphoo' which means
to transform. It aims to decouple your java classes from the way they can be transformed into other classes while
at the same time offering a single consistent interface to perform said transformations.

Getting Started
---------------

At the present time, we are trying to get Morphoo into the Maven Central repository. In the meanwhile, you can download
the latest jar file to use in your java project.


### Transformer Methods

Transformer methods are those that are able to transform or 'morph' one object type into another. In the context of
Morphoo they are annotated with the `@Transformer` method annotation. A simple transfomer method could be:

    @Transformer
    String transformToString( MyClass obj )
    {
       return obj.toString();
    }

This method transforms an object of type `MyClass` into a String. The first parameter of a transformer method should
always be the object that will be transformed, and the return type is the type that it is being transformed into.

Now, how is this different from the `toString()` method?

It is different in that with Morphoo, this method can be declared outside of `MyClass`, thus sepparating the transformation
logic from the business logic. You will see more benefits of using Morphoo in later examples.

### How to perform Transformations

To perform a transformation, the framework offers a very simple and straightforward API which can be summed up in a single
class : `Transformation`. To use the transformation method declared above, the following code would suffice:

    String result = Transformation.into(String.class).performOn( new MyClass()  );
    
... and that's it. The code is very self-explanatory, it transforms a `MyClass` object into the `String` class. 
Furthermore, even if your transformation method changes name or signature, the API to transform remains the same.


### Multiple transformer methods

What if there are multiple ways to transform an object into another class. Take for example a `java.util.Date` object. It 
might be transformed into a short string, or a long string. In that case, Morphoo allows for the customization of the
Transformation in a few ways. First lets look at the Transformer methods and their class.

    public class DateTransformers
    {
       @Transformer(isDefault = true)
       public String toShortString(Date aDate)
       {
          DateFormat format = DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT );
          return format.format(date);
       }
       
       @Transformer
       public static String toLongString(Date aDate)
       {
          DateFormat format = DateFormat.getDateTimeInstance( DateFormat.LONG, DateFormat.LONG );
          return format.format(date);
       }
    }
    
It declares two transformer methods for the `Date` class, one of which is static (yes, static transformer methods are 
allowed). One of those methods is declared as being the default transformer method when transforming Dates into Strings.So,
when the following call is made:

    Transformation.into(String.class).performOn(new Date());
    
the `toShortString` method will be used. But what if we want a long String? you can tell Morphoo to use a specific
transformation method like this:

    Transformation.into(String.class).using("toLongString").performOn(new Date());
    
this line will tell Morphoo to try and use a specific transformation method when multiple are available for the same class. 
Transformation method names can also be overriden like this:

    @Transformer(name = "TO_LONG_STRING")
    public static String toLongString(Date aDate)
    {
       DateFormat format = DateFormat.getDateTimeInstance( DateFormat.LONG, DateFormat.LONG );
       return format.format(date);
    }
    
    
### Transformation Context

Sometimes in order to trasnform an object into a different type, there are pieces of data or helper objects that are 
needed. This is where the Transformation context comes in. It allows for the "injection" of these additional variables
into the transformation. The simplest way to make use of the TransformationContext is as follows:

    @Transformer
    public String transformToString( Integer n, TransformationContext ctx )
    {
       // use the transformation context in here to extract variables by their name
       ...
    }
    
By adding a parameter of type `TransformationContext` Morphoo knows to inject a context object into the transformation.
The context can be inspected and even altered inside a transformer method. When invoking the transformation, the following
line will make sure that the context variable is inserted:

    Transformation.into(String.class).withContext("myVariableName", varInstance).performOn(10);
    
The `withContext` method can be called multiple times on the transformation to insert as many variables as needed. But there
is another way to get the context variables into the transformer methods without exposing a `TransformationContext`. The
previous transformer method could also be expressed like this to similar results:

    @Transformer
    public String transformToString( Integer n, @ContextParam(name = "myVariableName", nullable = false) String var  )
    {
       // use var directly
       ...
    }

There are several differences to take into account here:

1. The transformer method is unaware of the existence of a `TransformationContext`.
2. The variable can be altered, but the TransformationContext cannot. (i.e. No new variables can be added)
3. Variables can be automatically checked for nulls. If a  null value is detected for a non-nullable variable during a 
transformation, an Exception will be thrown.
    
    
### Deriving Transformations

Take the following class that has a set of Transformation methods:

    public class Transformers
    {
       @Transformer
       public ClassB transform(ClassA obj)
       {
          ...
          return classB;
       }
       
       @Transformer
       public ClassC transform(ClassB obj)
       {
          ...
          return classC;
       }
    }

This class has one transformer method from ClassA -> ClassB, and another from ClassB -> ClassC. But what if we want to 
transform from ClassA to ClassC? We could always declare a transformation method for this; but it is not necessary, Morphoo
can find an appropriate path to provide objects of type ClassC if it is told. 

    Transformation.into(ClassC.class).deriving().performOn( classAObject );
    
As shown above, by using the `deriving` parameter on the Transformation Morphoo is able to derive an appropriate path to 
transform the object even if there is no direct transformation method declared. In this case, Morphoo will transform from
ClassA into ClassB, and the result of that will be transformed again into a ClassC object using the second transformation
method.

If letting the framework decide the best transformation path is too hands-off, the following line will have the same 
effect as above, with the added bonus that it is the programmer determining how to transform the objects:

    Transformation.into(ClassC.class).through(ClassB.class).performOn( classAObject );
    
The `through` method can be invoked multiple times on a transformation to tell it specifically which path to take to 
transform an object.


### Re-using Transformations
    
Transformations can be reused. Take the following piece of code for example:

    Transformation<String> t = Transformation.into(String.class).deriving();
    
    t.performOn(new Date());
    t.performOn(10);
    t.performOn("A string");
    
The same transformation is being used to transform several object types into Strings. A transformation can accept any type of
object, but will always return a single type. 
    
    
### Finding Transformers
    
In order for Morphoo to find transformer methods, the frameowrk must be informed of where to look. This has to be done just 
once before the framework is used like this:

    Morphoo.registerPackages("com.yourpackage", "com.yourotherpackage");
    
This will tell the framework to look recursively for transformer methods inside specific packages while ignoring the rest. 
Individual classes can also be specified like this:

    Morphoo.registerClasses( com.yourpackage.TransformerClass.class  );
    Morphoo.registerClasses("com.yourotherpackage.AnotherTransformerClass");
    
Alternatively, individual classes are able to tell the framework where their transformation methods are located.

    @Transformers(TransformerClass.class, AnotherTransformerClass.class)
    public class MyClass
    {
       ...
    }

The `Transformers` annotations will let Morphoo know where to look for transformer methods for MyClass. In this case, only methods 
that transform into or from MyClass will be taken, all others will be ignored.
    
### Considerations

1. Classes that declare non-static transformation methods MUST have a no-arg constructor and may not be abstract.