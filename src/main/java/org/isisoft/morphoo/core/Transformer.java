package org.isisoft.morphoo.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Carlos Munoz
 */
public class Transformer
{
   private Method transformerMethod;

   private Object transformerInstance;


   public Transformer(Method transformerMethod)
   {
      this.transformerMethod = transformerMethod;
   }

   Method getTransformerMethod()
   {
      return transformerMethod;
   }

   private Object getTransformerInstance()
   {
      if( this.transformerInstance == null )
      {
         try
         {
            this.transformerInstance = this.transformerMethod.getDeclaringClass().newInstance();
         }
         catch (InstantiationException e)
         {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
         catch (IllegalAccessException e)
         {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
      }
      return this.transformerInstance;
   }

   public Object transform( Object src, TransformationContext ctx )
   {
      Object concreteTransformer = this.getTransformerInstance();

      try
      {
         // the transformer method takes a context
         if( this.transformerMethod.getParameterTypes().length > 1 )
         {
            return this.transformerMethod.invoke(concreteTransformer, src, ctx);
         }
         // no context necessary
         else
         {
            return this.transformerMethod.invoke(concreteTransformer, src);
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
