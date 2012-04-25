package org.isisoft.morphoo.core;

import org.isisoft.morphoo.annotation.ContextParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.isisoft.morphoo.core.TransformerMethod.TransformerMethodArgument;

/**
 * @author Carlos Munoz
 */
public class TransformerMethodAnalyzer
{

   public static TransformerMethod analyze( Method method )
   {
      TransformerMethod newTransformer = new TransformerMethod();

      org.isisoft.morphoo.annotation.Transformer transformerAnn =
            method.getAnnotation(org.isisoft.morphoo.annotation.Transformer.class);

      if( transformerAnn == null )
      {
         throw new InitializationException("Method " + method.getName() + " on class "
               + method.getDeclaringClass().getName() + " is not a transformer method but was registered as one");
      }

      Class<?> sourceType;
      Class<?> targetType = method.getReturnType();

      Class<?>[] parameterTypes = method.getParameterTypes();

      // return type must not be null
      if( method.getReturnType() == Void.TYPE || method.getReturnType() == void.class )
      {
         throw new InitializationException("Transformer method " + method.getName() + " on class "
               + method.getDeclaringClass().getName() + " must not return void");
      }
      // Must have at least one argument
      if( parameterTypes.length == 0 )
      {
         throw new InitializationException("Transformer method " + method.getName() + " on class "
               + method.getDeclaringClass().getName() + " must have at least one argument");
      }

      // transformer name, by default the method name
      String transformerName = transformerAnn.name().trim();
      if( transformerName.length() == 0 )
      {
         transformerName = method.getName();
      }

      sourceType = parameterTypes[0];

      newTransformer.setDefault( transformerAnn.isDefault() );
      newTransformer.setJavaMethod(method);
      newTransformer.setName( transformerName );
      newTransformer.setStatic( !Modifier.isStatic(method.getModifiers()) );
      newTransformer.setSourceType(sourceType);
      newTransformer.setTargetType(targetType);
      newTransformer.setArguments( analyzeArguments(method) );

      return newTransformer;
   }

   private static TransformerMethodArgument[] analyzeArguments( Method method )
   {
      // Determine the injection points
      // first parameter is always the source object
      Class<?>[] paramTypes = method.getParameterTypes();
      int paramCount = paramTypes.length;
      Annotation[][] paramAnnotations = method.getParameterAnnotations();

      TransformerMethodArgument args[] = new TransformerMethodArgument[ paramCount ];

      for( int i=0; i<paramCount; i++ )
      {
         args[i] = new TransformerMethodArgument();

         // First parameter is always the source object
         if( i == 0 )
         {
            args[i].setSourceInstance(true);
         }
         // Parameter is the Transformation context
         else if( paramTypes[i] == TransformationContext.class )
         {
            args[i].setSourceInstance(false);
            args[i].setNullable(false);
            args[i].setTransformationContext(true);
         }
         // Parameter is annotated with @ContextParam
         else
         {
            // Find the parameter annotation
            ContextParam paramAnn = null;
            for( Annotation a : paramAnnotations[i] )
            {
               if( a instanceof ContextParam )
               {
                  paramAnn = (ContextParam)a;
                  break;
               }
            }

            // anotated parameter
            if(paramAnn != null)
            {
               args[i].setSourceInstance(false);
               args[i].setNullable(paramAnn.nullable());
               args[i].setTransformationContext(false);
               args[i].setCtxVarName(paramAnn.name());
            }
            // non-annotated parameters are not allowed
            else
            {
               throw new InitializationException("A parameter of type " + paramTypes[i] + " in Transformer method " +
                     method.getDeclaringClass().getName() + "." + method.getName() +
                     " has not been annotated with " + ContextParam.class.getName() + " as expected.");
            }
         }
      }

      return args;
   }
}
