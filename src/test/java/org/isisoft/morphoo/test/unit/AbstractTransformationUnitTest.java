package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.Morphoo;
import org.testng.annotations.BeforeClass;

/**
 */
public abstract class AbstractTransformationUnitTest
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
