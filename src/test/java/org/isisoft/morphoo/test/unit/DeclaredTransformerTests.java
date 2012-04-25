package org.isisoft.morphoo.test.unit;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.isisoft.morphoo.core.Transformation;
import org.isisoft.morphoo.test.model.SourceTypeWithTransformers;
import org.testng.annotations.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Carlos Munoz
 */
public class DeclaredTransformerTests extends AbstractTransformationUnitTest
{
   @Override
   protected void prepareTransformationFramework()
   {
      // all transformers should be declared on the class itself
   }

   @Test
   public void declaredSourceTransformers()
   {
      SourceTypeWithTransformers src = new SourceTypeWithTransformers();
      src.setCreationDate( new Date() );
      src.setName( "I am a declared transformer" );
      src.setId(34L);

      String s = Transformation.into(String.class).performOn( src );
      Date d = Transformation.into(Date.class).performOn( src );
      Long l = Transformation.into(Long.class).performOn( src );

      assertThat(s, is(src.getName()));
      assertThat(d, is(src.getCreationDate()));
      assertThat(l, is(src.getId()));
   }

   @Test
   public void declaredTargetTransformers()
   {
      Date d = new Date();

      SourceTypeWithTransformers target = Transformation.into(SourceTypeWithTransformers.class).performOn(d);

      assertThat(target.getCreationDate(), is(d));
   }
}
