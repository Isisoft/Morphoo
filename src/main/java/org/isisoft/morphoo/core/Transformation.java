package org.isisoft.morphoo.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * One of the framework's most important class. It offers ways to customize and perform transformations.
 *
 * To begin a transformation the {@link Transformation#from(Object)} method is called. It will initialize a
 * transformation for the given object.
 * The static <code>from</code> method initializes a transformation for the passed argument. Serveral calls to the
 * other customization methods may be done, finalizing with a call to the <code>to</code> method which attempts to
 * perform the transformation into the passed target class.
 *
 * A simple transformation may be of the form
 * <code>Transformation.from(myObj).to(MyOtherClass.class)</code>
 *
 * @author Carlos Munoz
 */
public class Transformation<T>
{

   private Class<T> targetType;

   private TransformationContext context;

   private Set<String> transformerNames;

   private List<Class<?>> transformationSteps;

   private boolean deriveTransformation;

   private Transformation()
   {
      this.context = new TransformationContext();
      this.transformerNames = new HashSet<String>(0);
      this.transformationSteps = new ArrayList<Class<?>>();
      this.deriveTransformation = false;
   }

   public static final <R> Transformation<R> into( Class<R> targetType )
   {
      Transformation<R> transformation = new Transformation<R>();
      transformation.targetType = targetType;
      return transformation;
   }

   public final T performOn( Object src )
   {
      return (T)this.transform(src);
   }

   /**
    * Indicates that this transformation should go through the specified class.
    * If there is a {@link SimpleTransformer} method specified from ClassA to ClassB, and another from ClassB to ClassC.
    * The following call:
    * <code>
    *    Transformation.from(new ClassA()).through(ClassB.class).to(ClassC.class)
    * </code>
    * will chain the two transformer methods together without the need to specify a direct transformer method between
    * ClassA and ClassC.
    *
    * Keep in mind that this method cannot be used together with {@link Transformation#deriving()} in the same
    * transformation.
    *
    * @param intermediateClass The class to go through when transforming.
    * @return A Transformation instance that will go through intermediateClass when performing the transformation.
    * @throws TransformationException If this Transformation is also deriving its transformation path.
    */
   public Transformation<T> through( Class<?> intermediateClass )
   {
      // through cannot be used with derive at the same time
      if( this.deriveTransformation )
      {
         throw new TransformationException("A Transformation path cannot be derived and specified simultaneously");
      }

      this.transformationSteps.add(intermediateClass);
      return this;
   }

   /**
    * Indicates that this Transformation should use a transformer named as transformerName if needed. This is useful to
    * resolve conflicts when there are multiple {@link SimpleTransformer} methods that transform from one type to the other.
    *
    * @param transformerName The name of the transformer method to use if any conflicts arise.
    * @return A Transformation instance that will use transformerName to resolve conflicts.
    */
   public Transformation<T> using(String transformerName)
   {
      this.transformerNames.add(transformerName);
      return this;
   }

   /**
    * Adds a context variable to use when performing this Transformation.
    * Transformations sometimes need external helpers or data, and this method makes sure that Transformations have
    * those dependencies available.
    * All context variables specified by calling this method will be available in the {@link TransformationContext}
    * parameter of the transformer method (if one is present).
    *
    * @param name The name of the context variable to add. This is the way to extract it from the {@link TransformationContext}
    *             in the transformer method.
    * @param ctxVariable The context variable to insert.
    * @return A Transformation that will use the given context variable when performing the transformation.
    */
   public Transformation<T> withContext(String name, Object ctxVariable)
   {
      this.context.put(name, ctxVariable);
      return this;
   }

   /**
    * Indicates that this transformation should derive the shortest possible path to perform the transformation.
    * If the following transformer methods have been defined:
    * ClassA -> ClassB
    * ClassB -> ClassC
    * ClassC -> ClassD
    *
    * The following call
    * <code>
    *    Transformation.from(new ClassA()).deriving().to(ClassD.class);
    * </code>
    * will find the shortest path to perform the transformation. This method saves time by letting the framework decide
    * how to transform an object instead of specifying it with {@link Transformation#through(Class)}.
    *
    * Keep in mind that this method cannot be used together with {@link Transformation#through(Class)} in the same
    * transformation.
    *
    * @return A Transformation instance that will derive the shortest transformation path.
    * @throws TransformationException If {@link Transformation#through(Class)} has already been called on this transformation.
    */
   public Transformation<T> deriving()
   {
      // Deriving cannot be used with through at the same time
      if( !this.transformationSteps.isEmpty() )
      {
         throw new TransformationException("A Transformation path cannot be derived and specified simultaneously");
      }

      this.deriveTransformation = true;
      return this;
   }

   /**
    * Performs the transformation from the src object to the targetClass.
    */
   private Object transform( Object src )
   {
      Transformer transformer = null;

      if( this.transformationSteps.size() > 0 )
      {
         transformer =
            TransformerRegistry.getInstance().getTransformer( this.buildTransformationSteps(src) );
      }
      else
      {
         transformer =
               TransformerRegistry.getInstance().getTransformer(src.getClass(), this.targetType, this.transformerNames, this.deriveTransformation);
      }

      // If still not found then it is not registered
      if( transformer == null )
      {
         throw new TransformationException("No Transformer method found to transform from " + src.getClass().getName() +
               " to " + targetType.getName());
      }

      return transformer.transform(src, this.context);
   }

   private Class<?>[] buildTransformationSteps( Object src )
   {
      Class<?>[] stepArray = new Class<?>[ this.transformationSteps.size() + 2 ];
      int idx = 0;
      stepArray[idx++] = src.getClass();

      for( Class<?> c : this.transformationSteps )
      {
         stepArray[idx++] = c;
      }

      stepArray[idx++] = targetType;
      return stepArray;
   }

}
