package org.isisoft.morphoo.core;

/**
 * Indicates a problem when performing a transformation on an instance.
 *
 * @author Carlos Munoz
 */
public class TransformationException extends RuntimeException
{
   public TransformationException()
   {
   }

   public TransformationException(String s)
   {
      super(s);
   }

   public TransformationException(String s, Throwable throwable)
   {
      super(s, throwable);
   }

   public TransformationException(Throwable throwable)
   {
      super(throwable);
   }
}
