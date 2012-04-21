package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transform;
import org.isisoft.morphoo.test.model.FinalTargetType;
import org.isisoft.morphoo.test.model.IntermediateType;
import org.isisoft.morphoo.test.model.SourceType;
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
      Morphoo.registerPackages("org.isisoft.morphoo.test");
   }

   @Test
   public void simpleChainedTransformation()
   {
      SourceType src = new SourceType();
      src.setName("Source Name");
      src.setDate(new Date());
      src.setValue(10);

      FinalTargetType target =
         Transform.from(src).through(IntermediateType.class).to(FinalTargetType.class);

      assertThat(target.getDate(), equalTo(src.getDate()));
   }
}
