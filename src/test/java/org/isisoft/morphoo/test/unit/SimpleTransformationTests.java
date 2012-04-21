package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transform;
import org.isisoft.morphoo.test.model.SourceType;
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
      Morphoo.registerPackages("org.isisoft.morphoo.test");
   }

   @Test
   public void transformSimple()
   {
      SourceType s = new SourceType();
      s.setName("My name is Morphoo!");

      String strVal = Transform.from(s).to(String.class);

      assertThat(strVal, is(s.getName()));
   }

   @Test
   public void transformSimple2()
   {
      SourceType s = new SourceType();
      s.setName("My name is Morphoo!");

      int intVal = Transform.from(s).to(int.class);

      assertThat(intVal, is(s.getValue()));
   }

   @Test
   public void transformUsingStatic()
   {
      SourceType s = new SourceType();
      s.setName("My name is Morphoo!");
      s.setDate(new Date());

      Date dateVal = Transform.from(s).to(Date.class);

      assertThat(dateVal, is(s.getDate()));
   }

   @Test
   public void transformSimpleStringWithContext()
   {
      String toTransform = "Hello ";
      String result = Transform.from(toTransform).withContext("additional", "World!").to(String.class);

      assertThat(result, is("Hello World!"));
   }

}
