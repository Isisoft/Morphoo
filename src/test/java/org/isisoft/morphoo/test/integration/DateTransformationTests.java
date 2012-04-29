package org.isisoft.morphoo.test.integration;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transformation;
import org.isisoft.morphoo.test.AbstractTransformationTest;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * @author Carlos Munoz
 */
public class DateTransformationTests extends AbstractTransformationTest
{

   @Override
   protected void prepareTransformationFramework()
   {
      Morphoo.registerPackages("org.isisoft.morphoo.test.model.integration");
   }

   @Test
   public void dateTransformations()
   {
      Date d = new Date();

      String longDate = Transformation.into(String.class).using("toLongString").performOn(d);
      String mediumDate = Transformation.into(String.class).using("toMediumString").performOn(d);
      String shortDate = Transformation.into(String.class).using("toShortString").performOn(d);

      String defaultDate = Transformation.into(String.class).performOn(d);

      System.out.println(longDate);
      System.out.println(mediumDate);
      System.out.println(shortDate);

      String formattedDate = Transformation.into(String.class).using("toDateWithFormat").withContext("format", "dd-MM-yyy").performOn(d);
      String nonFormattedDate = Transformation.into(String.class).using("toDateWithFormat").performOn(d);

      System.out.println(formattedDate);
      System.out.println(nonFormattedDate);
   }
}
