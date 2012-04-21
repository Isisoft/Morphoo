package org.isisoft.morphoo.core;

/**
 * Framework setup class. It has all the functionality offered for the framework's global setup.
 *
 * @author Carlos Munoz
 */
public class Morphoo
{
   private Morphoo()
   {
   }

   /**
    * Registers multiple packages to be scanned for transformer methods.
    * Registered packages will be scanned recursively.
    *
    * @param packages The package names to scan for transformer methods.
    */
   public static final void registerPackages(String ... packages)
   {
      TransformerRegistry.getInstance().addScannablePackages(packages);
   }

   /**
    * Registers multiple classes to be scanned for transformer methods.
    *
    * @param classes The individual classes to scan for transformer methods.
    */
   public static final void registerClasses(Class<?> ... classes)
   {
      TransformerRegistry.getInstance().addScannableClasses(classes);
   }

   /**
    * Registers multiple class names to be scanned for transformer methods.
    *
    * @param classes The individual class names to be scanned for transformer methods.
    */
   public static final void registerClasses(String ... classes)
   {
      TransformerRegistry.getInstance().addScannableClasses(classes);
   }

   /**
    * Resets the framework clearing out all detected transformer methods and all scanned
    * packages and classes.
    */
   public static final void reset()
   {
      TransformerRegistry.getInstance().reset();
   }
}
