package org.isisoft.morphoo.test.unit;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transformation;
import org.isisoft.morphoo.test.model.transformer.InjectionTransformers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Carlos Munoz
 */
public class InjectionTransformerTests extends AbstractTransformationUnitTest
{
   @Override
   protected void prepareTransformationFramework()
   {
      Morphoo.registerClasses(InjectionTransformers.class);
   }

   @Test
   protected void simpleInjectionTransformer()
   {
      String src = "transformed";
      String pre = "I was ";
      String pos = " today!";

      String result = Transformation.into(String.class).withContext("pre", pre).withContext("pos", pos).performOn(src);

      assertThat(result, is(pre + src + pos));
   }

   @Test
   protected void namedInjectionTransformer()
   {
      String src = "transformed";
      String pre = "I was ";
      String pos = " today!";

      String result = Transformation.into(String.class).withContext("pre", pre).withContext("pos", pos)
            .using("appendAndPrependWithName").performOn(src);

      assertThat(result, is(pre + src + pos + " with name!"));
   }
}
