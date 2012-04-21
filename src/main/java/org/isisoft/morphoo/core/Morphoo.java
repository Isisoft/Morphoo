package org.isisoft.morphoo.core;

/**
 * @author Carlos Munoz
 */
public class Morphoo
{
   private Morphoo()
   {
   }

   public static final void registerPackages(String ... packages)
   {
      TransformerRegistry.getInstance().addScannablePackages(packages);
   }

   public static final void registerClasses(Class<?> ... classes)
   {
      TransformerRegistry.getInstance().addScannableClasses(classes);
   }

   public static final void registerClasses(String ... classes)
   {
      TransformerRegistry.getInstance().addScannableClasses(classes);
   }
}
