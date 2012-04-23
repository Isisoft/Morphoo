package org.isisoft.morphoo.core;

import org.apache.commons.lang3.ArrayUtils;
import org.isisoft.morphoo.annotation.ContextParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Carlos Munoz
 */
public class SimpleTransformer implements Transformer
{
   private Method transformerMethod;

   private Object transformerInstance;

   private String[] contextParamInjectionNames; // null means a TransformationContext goes there


   public SimpleTransformer(Method transformerMethod)
   {
      this.transformerMethod = transformerMethod;
      this.analyzeTransformerMethod();
   }

   Method getTransformerMethod()
   {
      return transformerMethod;
   }

   private void analyzeTransformerMethod()
   {
      // Method is not static, needs an instance
      if(!Modifier.isStatic( this.transformerMethod.getModifiers() ))
      {
         try
         {
            this.transformerInstance = this.transformerMethod.getDeclaringClass().newInstance();
         }
         catch (InstantiationException e)
         {
            throw new InitializationException("Problem initializing transformer instance for " +
                  this.transformerMethod.getDeclaringClass().getName() + "." +
                  this.transformerMethod.getName(), e);
         }
         catch (IllegalAccessException e)
         {
            throw new InitializationException("Problem initializing transformer instance for " +
                  this.transformerMethod.getDeclaringClass().getName() + "." +
                  this.transformerMethod.getName(), e);
         }
      }

      // Determine the injection points
      // first parameter is always the source object
      Class<?>[] paramTypes = this.transformerMethod.getParameterTypes();
      int paramCount = paramTypes.length;
      Annotation[][] paramAnnotations = this.transformerMethod.getParameterAnnotations();

      this.contextParamInjectionNames = new String[ paramCount ];

      for( int i=1; i<paramCount; i++ )
      {
         // Parameter is the TransformationContext
         if( paramTypes[i] == TransformationContext.class )
         {
            this.contextParamInjectionNames[i] = null;
         }
         // All other parameters are treated as context variables
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
               this.contextParamInjectionNames[i] = paramAnn.name();
            }
            // non-annotated parameters are not allowed
            else
            {
               throw new InitializationException("A parameter of type " + paramTypes[i] + " in Transformer method " +
                     this.transformerMethod.getDeclaringClass().getName() + "." + this.transformerMethod.getName() +
                     " has not been annotated with " + ContextParam.class.getName() + " as expected.");
            }
         }
      }
   }

   private Object[] buildInvocationParameters( Object src, TransformationContext ctx )
   {
      Object[] params = new Object[ this.transformerMethod.getParameterTypes().length ];

      int paramIdx = 0;

      // first parameter is the src always
      params[paramIdx++] = src;
      // all others
      while( paramIdx < params.length )
      {
         // null, inject the whole Transformation context
         if( this.contextParamInjectionNames[paramIdx] == null )
         {
            params[paramIdx] = ctx;
         }
         // not null, inject the Transformation context variable
         else
         {
            params[paramIdx] = ctx.get( this.contextParamInjectionNames[paramIdx] );
         }
         paramIdx++;
      }

      return params;
   }

   @Override
   public Object transform(Object src, TransformationContext ctx)
   {
      try
      {
         Object[] params = this.buildInvocationParameters(src, ctx);
         return this.transformerMethod.invoke(this.transformerInstance, params);
      }
      catch (IllegalAccessException e)
      {
         throw new TransformationException("Error while transforming", e);
      }
      catch (InvocationTargetException e)
      {
         throw new TransformationException("Error while transforming", e);
      }
   }

}
