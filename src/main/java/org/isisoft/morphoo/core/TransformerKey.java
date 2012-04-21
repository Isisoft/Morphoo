package org.isisoft.morphoo.core;

import java.io.Serializable;

/**
 * @author Carlos Munoz
 */
public class TransformerKey implements Serializable
{
   private Class<?> sourceType;

   private Class<?> targetType;


   public TransformerKey(Class<?> sourceType, Class<?> targetType)
   {
      this.sourceType = sourceType;
      this.targetType = targetType;
   }

   public Class<?> getTargetType()
   {
      return targetType;
   }

   public Class<?> getSourceType()
   {
      return sourceType;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TransformerKey that = (TransformerKey) o;

      if (sourceType != null ? !sourceType.equals(that.sourceType) : that.sourceType != null) return false;
      if (targetType != null ? !targetType.equals(that.targetType) : that.targetType != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = sourceType != null ? sourceType.hashCode() : 0;
      result = 31 * result + (targetType != null ? targetType.hashCode() : 0);
      return result;
   }
}
