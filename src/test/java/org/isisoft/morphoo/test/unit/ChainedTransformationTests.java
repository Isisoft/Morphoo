package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transformation;
import org.isisoft.morphoo.test.AbstractTransformationUnitTest;
import org.isisoft.morphoo.test.model.unit.FinalTargetType;
import org.isisoft.morphoo.test.model.unit.IntermediateType;
import org.isisoft.morphoo.test.model.unit.SourceType;
import org.isisoft.morphoo.test.model.transformer.TransformerMethods;
import org.testng.annotations.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Carlos Munoz
 */
public class ChainedTransformationTests extends AbstractTransformationUnitTest
{
   @Override
   protected void prepareTransformationFramework()
   {
      Morphoo.registerClasses(TransformerMethods.class);
   }

   @Test
   public void simpleChainedTransformation()
   {
      SourceType src = new SourceType();
      src.setName("Source Name");
      src.setDate(new Date());
      src.setValue(10);

      FinalTargetType target =
            Transformation.into(FinalTargetType.class).through(IntermediateType.class).performOn(src);

      assertThat(target.getDate(), equalTo(src.getDate()));
   }
}
