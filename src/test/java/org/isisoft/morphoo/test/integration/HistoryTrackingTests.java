package org.isisoft.morphoo.test.integration;

import org.isisoft.morphoo.core.Morphoo;
import org.isisoft.morphoo.core.Transformation;
import org.isisoft.morphoo.core.TransformationException;
import org.isisoft.morphoo.test.AbstractTransformationTest;
import org.isisoft.morphoo.test.model.integration.TransformationHierarchy;
import org.isisoft.morphoo.test.model.integration.TransformationHierarchy.ClassA;
import org.isisoft.morphoo.test.model.integration.TransformationHierarchy.ClassB;
import org.isisoft.morphoo.test.model.integration.TransformationHierarchy.ClassD;
import org.isisoft.morphoo.test.model.integration.TransformationHierarchy.ClassE;
import org.isisoft.morphoo.test.model.integration.TransformationHierarchy.ClassF;
import org.isisoft.morphoo.test.model.integration.TransformationHierarchy.HistoryTracker;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Carlos Munoz
 */
public class HistoryTrackingTests extends AbstractTransformationTest
{
   @Override
   protected void prepareTransformationFramework()
   {
      Morphoo.registerClasses(TransformationHierarchy.class);
   }

   protected static void assertHistory(HistoryTracker ht, String ... expectedHistory)
   {
      assertThat(ht.getHistory(), equalTo(Arrays.asList(expectedHistory)));
   }

   @Test()
   public void directDerivedPath()
   {
      ClassD target = Transformation.into(ClassD.class).deriving().performOn(new ClassA());

      assertHistory(target, "classAtoB", "classBtoC", "classCtoD");
   }

   @Test
   public void specifiedPath()
   {
      ClassD target = Transformation.into(ClassD.class).through(ClassB.class).through(ClassE.class).performOn(new ClassA());

      assertHistory(target, "classAtoB", "classBtoE", "classEtoD");
   }

   @Test(expectedExceptions = TransformationException.class)
   public void throughAndDeriveSimultaneously()
   {
      Transformation.into(ClassD.class).deriving().through(ClassB.class).performOn(new ClassA());
   }

   @Test(expectedExceptions = TransformationException.class)
   public void nonExistingPath()
   {
      Transformation.into(ClassD.class).performOn(new ClassA());
   }

   @Test(expectedExceptions = TransformationException.class)
   public void nonExistingGraphVertex()
   {
      Transformation.into(ClassE.class).performOn("");
   }

   @Test(expectedExceptions = TransformationException.class)
   public void nonExistingDerivedPath()
   {
      Transformation.into(ClassF.class).deriving().performOn(new ClassA());
   }

   @Test
   public void usingNamedAndDeriving()
   {
      ClassD target = Transformation.into(ClassD.class).deriving().using("classBtoE").performOn(new ClassA());
      System.out.println(target.getHistory());
      // TODO Currently this derives the shortest path without taking the named transformers into account. Determine if
      // this is the desired behaviour
   }
}
