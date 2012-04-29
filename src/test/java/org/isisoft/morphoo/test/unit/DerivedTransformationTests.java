package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transformation;
import org.isisoft.morphoo.core.TransformationException;
import org.isisoft.morphoo.test.AbstractTransformationTest;
import org.isisoft.morphoo.test.model.transformer.TransformerMethods;
import org.isisoft.morphoo.test.model.unit.FinalTargetType;
import org.isisoft.morphoo.test.model.unit.IntermediateType;
import org.isisoft.morphoo.test.model.unit.SourceType;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Carlos Munoz
 */
public class DerivedTransformationTests extends AbstractTransformationTest
{
   @Override
   protected void prepareTransformationFramework()
   {
      Morphoo.registerClasses(TransformerMethods.class);
   }

   @Test
   public void simpleDerivation()
   {
      SourceType src = new SourceType();
      src.setDate(new Date());
      src.setValue(11);
      src.setName("My name is Morphoo!");

      FinalTargetType finalDerivedType = Transformation.into(FinalTargetType.class).deriving().performOn(src);
      FinalTargetType finalNonDerivedType = Transformation.into(FinalTargetType.class).through(IntermediateType.class).performOn(src);

      assertThat(finalDerivedType, equalTo(finalNonDerivedType));
   }

   @Test(expectedExceptions = TransformationException.class)
   public void derivationNotFound()
   {
      Transformation.into(Calendar.class).deriving().performOn("I am the source");
   }

}
