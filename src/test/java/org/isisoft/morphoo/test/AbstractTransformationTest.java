package org.isisoft.morphoo.test;

import org.isisoft.morphoo.core.Morphoo;
import org.testng.annotations.BeforeClass;

/**
 */
public abstract class AbstractTransformationTest
{
   @BeforeClass
   public void configure()
   {
      // Reset the Morphoo framework
      Morphoo.reset();

      this.prepareTransformationFramework();
   }

   protected abstract void prepareTransformationFramework();
}
