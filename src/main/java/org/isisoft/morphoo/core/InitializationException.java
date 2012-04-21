package org.isisoft.morphoo.core;

/**
 * @author Carlos Munoz
 */
public class InitializationException extends RuntimeException
{
   public InitializationException()
   {
   }

   public InitializationException(String s)
   {
      super(s);
   }

   public InitializationException(String s, Throwable throwable)
   {
      super(s, throwable);
   }

   public InitializationException(Throwable throwable)
   {
      super(throwable);
   }
}
