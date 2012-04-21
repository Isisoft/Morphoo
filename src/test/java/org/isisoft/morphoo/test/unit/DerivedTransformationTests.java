package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transformation;
import org.isisoft.morphoo.core.TransformationException;
import org.isisoft.morphoo.test.model.FinalTargetType;
import org.isisoft.morphoo.test.model.IntermediateType;
import org.isisoft.morphoo.test.model.SourceType;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Carlos Munoz
 */
public class DerivedTransformationTests extends AbstractTransformationUnitTest
{
   @Override
   protected void prepareTransformationFramework()
   {
      Morphoo.registerPackages("org.isisoft.morphoo.test");
   }

   @Test
   public void simpleDerivation()
   {
      SourceType src = new SourceType();
      src.setDate(new Date());
      src.setValue(11);
      src.setName("My name is Morphoo!");

      FinalTargetType finalDerivedType = Transformation.from(src).deriving().to(FinalTargetType.class);
      FinalTargetType finalNonDerivedType = Transformation.from(src).through(IntermediateType.class).to(FinalTargetType.class);

      assertThat(finalDerivedType, equalTo(finalNonDerivedType));
   }

   @Test(expectedExceptions = TransformationException.class,
         expectedExceptionsMessageRegExp = "Unable to derive transformation chain .*")
   public void derivationNotFound()
   {
      Transformation.from("I am the source").deriving().to(Calendar.class);
   }

}
