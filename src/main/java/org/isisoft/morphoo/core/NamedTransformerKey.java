package org.isisoft.morphoo.core;

/**
 * @author Carlos Munoz
 */
public class NamedTransformerKey extends TransformerKey
{
   private String transformerName;


   public NamedTransformerKey(Class<?> sourceType, Class<?> targetType, String transformerName)
   {
      super(sourceType, targetType);
      this.transformerName = transformerName;
   }

   public String getTransformerName()
   {
      return transformerName;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      NamedTransformerKey that = (NamedTransformerKey) o;

      if (transformerName != null ? !transformerName.equals(that.transformerName) : that.transformerName != null)
         return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (transformerName != null ? transformerName.hashCode() : 0);
      return result;
   }
}
