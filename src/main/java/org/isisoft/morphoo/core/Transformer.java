package org.isisoft.morphoo.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Carlos Munoz
 */
public class Transformer
{
   private Method transformerMethod;

   private Object transformerInstance;

   private boolean needsContext;


   public Transformer(Method transformerMethod)
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

      // Determine if it needs context
      if( this.transformerMethod.getParameterTypes().length > 1 )
      {
         this.needsContext = true;
      }
   }

   public Object transform( Object src, TransformationContext ctx )
   {
      try
      {
         // the transformer method takes a context
         if( this.needsContext )
         {
            return this.transformerMethod.invoke(transformerInstance, src, ctx);
         }
         // no context necessary
         else
         {
            return this.transformerMethod.invoke(transformerInstance, src);
         }
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
