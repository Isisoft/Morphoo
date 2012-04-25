package org.isisoft.morphoo.core;

import java.lang.reflect.Method;

/**
 * Internal abstraction of a transformer method.
 *
 * @author Carlos Munoz
 */
public class TransformerMethod
{
   private Transformer executor;

   private Class<?> sourceType;

   private Class<?> targetType;

   private Method javaMethod;

   private boolean isStatic;

   private boolean isDefault;

   private String name;

   private TransformerMethodArgument[] arguments;


   public Transformer getExecutor()
   {
      if( executor == null )
      {
         executor = new SimpleTransformer(this);
      }
      return executor;
   }

   public Class<?> getSourceType()
   {
      return sourceType;
   }

   public void setSourceType(Class<?> sourceType)
   {
      this.sourceType = sourceType;
   }

   public Class<?> getTargetType()
   {
      return targetType;
   }

   public void setTargetType(Class<?> targetType)
   {
      this.targetType = targetType;
   }

   public Method getJavaMethod()
   {
      return javaMethod;
   }

   public void setJavaMethod(Method javaMethod)
   {
      this.javaMethod = javaMethod;
   }

   public boolean isStatic()
   {
      return isStatic;
   }

   public void setStatic(boolean aStatic)
   {
      isStatic = aStatic;
   }

   public boolean isDefault()
   {
      return isDefault;
   }

   public void setDefault(boolean aDefault)
   {
      isDefault = aDefault;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public TransformerMethodArgument[] getArguments()
   {
      return arguments;
   }

   public void setArguments(TransformerMethodArgument[] arguments)
   {
      this.arguments = arguments;
   }

   public static class TransformerMethodArgument
   {
      private String ctxVarName;

      private boolean isTransformationContext;

      private boolean isSourceInstance;

      private boolean isNullable;


      public String getCtxVarName()
      {
         return ctxVarName;
      }

      public void setCtxVarName(String ctxVarName)
      {
         this.ctxVarName = ctxVarName;
      }

      public boolean isTransformationContext()
      {
         return isTransformationContext;
      }

      public void setTransformationContext(boolean transformationContext)
      {
         isTransformationContext = transformationContext;
      }

      public boolean isSourceInstance()
      {
         return isSourceInstance;
      }

      public void setSourceInstance(boolean sourceInstance)
      {
         isSourceInstance = sourceInstance;
      }

      public boolean isNullable()
      {
         return isNullable;
      }

      public void setNullable(boolean nullable)
      {
         isNullable = nullable;
      }
   }
}
