package org.isisoft.morphoo.core;

import java.lang.reflect.InvocationTargetException;

import static org.isisoft.morphoo.core.TransformerMethod.TransformerMethodArgument;

/**
 * @author Carlos Munoz
 */
public class SimpleTransformer implements Transformer
{
   private Object transformerInstance;

   private TransformerMethod transformerMethod;


   public SimpleTransformer(TransformerMethod transformerMethod)
   {
      this.transformerMethod = transformerMethod;
      this.initTransformerInstance();
   }

   private void initTransformerInstance()
   {
      if( transformerMethod.isStatic() )
      {
         try
         {
            this.transformerInstance = transformerMethod.getJavaMethod().getDeclaringClass().newInstance();
         }
         catch (InstantiationException e)
         {
            throw new InitializationException("Error creating new Transformer instance of class "
                  + transformerMethod.getJavaMethod().getDeclaringClass().getName(), e);
         }
         catch (IllegalAccessException e)
         {
            throw new InitializationException("Error creating new Transformer instance of class "
                  + transformerMethod.getJavaMethod().getDeclaringClass().getName(), e);
         }
      }
      else
      {
         this.transformerInstance = null;
      }
   }

   private Object[] buildInvocationParameters(Object src, TransformationContext ctx)
   {
      Object[] params = new Object[ transformerMethod.getArguments().length ];

      for( int i=0; i<transformerMethod.getArguments().length; i++ )
      {
         TransformerMethodArgument argument = transformerMethod.getArguments()[i];

         // source object
         if( argument.isSourceInstance() )
         {
            params[i] = src;
         }
         // Transformation context
         else if( argument.isTransformationContext() )
         {
            params[i] = ctx;
         }
         // It's a context variable
         else
         {
            Object argValue = ctx.get( argument.getCtxVarName() );

            if( !argument.isNullable() && argValue == null )
            {
               throw new TransformationException("Context variable " + argument.getCtxVarName() + " cannot be null " +
                     "in Transformer method " + transformerMethod.getJavaMethod().getDeclaringClass().getName() + "." +
                     transformerMethod.getJavaMethod().getName());
            }
            else
            {
               params[i] = argValue;
            }
         }
      }

      return params;
   }

   @Override
   public Object transform(Object src, TransformationContext ctx)
   {
      try
      {
         Object[] params = this.buildInvocationParameters(src, ctx);
         return this.transformerMethod.getJavaMethod().invoke(transformerInstance, params);
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
