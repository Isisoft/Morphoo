package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transformation;
import org.isisoft.morphoo.test.model.SourceType;
import org.isisoft.morphoo.test.model.transformer.TransformerMethods;
import org.isisoft.morphoo.test.model.transformer.TransformerMethodsWithContext;
import org.testng.annotations.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Carlos Munoz
 */
public class SimpleTransformationTests extends AbstractTransformationUnitTest
{

   @Override
   protected void prepareTransformationFramework()
   {
      Morphoo.registerClasses(TransformerMethods.class,
            TransformerMethodsWithContext.class);
   }

   @Test
   public void transformSimple()
   {
      SourceType s = new SourceType();
      s.setName("My name is Morphoo!");

      String strVal = Transformation.into(String.class).performOn(s);

      assertThat(strVal, is(s.getName()));
   }

   @Test
   public void transformSimple2()
   {
      SourceType s = new SourceType();
      s.setName("My name is Morphoo!");

      int intVal = Transformation.into(int.class).performOn(s);

      assertThat(intVal, is(s.getValue()));
   }

   @Test
   public void transformUsingStatic()
   {
      SourceType s = new SourceType();
      s.setName("My name is Morphoo!");
      s.setDate(new Date());

      Date dateVal = Transformation.into(Date.class).performOn(s);

      assertThat(dateVal, is(s.getDate()));
   }

   @Test
   public void transformSimpleStringWithContext()
   {
      String toTransform = "Hello ";
      String result = Transformation.into(String.class).withContext("additional", "World!").performOn(toTransform);

      assertThat(result, is("Hello World!"));
   }

}
