package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transform;
import org.isisoft.morphoo.test.model.transformer.NamedTransformers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Carlos Munoz
 */
public class NamedTransformationTest extends AbstractTransformationUnitTest
{
   @Override
   protected void prepareTransformationFramework()
   {
      Morphoo.registerClasses(NamedTransformers.class);
   }

   @Test
   public void namedTransformers()
   {
      String original = "10";

      String plus1 = Transform.from(original).using("STRING-TO-STRING-1").to(String.class);
      String plus2 = Transform.from(original).using("STRING-TO-STRING-2").to(String.class);
      String plus3 = Transform.from(original).using("STRING-TO-STRING-3").to(String.class);

      assertThat(plus1, is("10+1"));
      assertThat(plus2, is("10+2"));
      assertThat(plus3, is("10+3"));
   }
}
