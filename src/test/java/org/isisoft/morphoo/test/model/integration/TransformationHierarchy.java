package org.isisoft.morphoo.test.model.integration;

import org.isisoft.morphoo.annotation.Transformer;

import java.util.ArrayList;
import java.util.List;

import static org.isisoft.morphoo.test.model.integration.TransformationHierarchy.HistoryTracker.copyAndAddHistory;

/**
 * Test transformation hierarchy. The following transformations should be available:
 * A -> B -> C -> D <-|
 *      |             |
 *      |-----------> E
 *
 * F -> G
 *
 * @author Carlos Munoz
 */
public class TransformationHierarchy
{
   /**
    * Base class to keep a history of the transformers used.
    */
   public static abstract class HistoryTracker
   {
      protected List<String> history = new ArrayList<String>();

      public static <T extends HistoryTracker> T copyAndAddHistory(HistoryTracker from, T to, String h)
      {
         to.history = new ArrayList<String>( from.getHistory() );
         to.history.add(h);
         return to;
      }

      public List<String> getHistory()
      {
         return history;
      }
   }


   public static class ClassA extends HistoryTracker
   {
   }

   public static class ClassB extends HistoryTracker
   {
   }

   public static class ClassC extends HistoryTracker
   {
   }

   public static class ClassD extends HistoryTracker
   {
   }

   public static class ClassE extends HistoryTracker
   {
   }

   public static class ClassF extends HistoryTracker
   {
   }

   public static class ClassG extends HistoryTracker
   {
   }

   @Transformer
   public ClassB classAtoB(ClassA src)
   {
      return copyAndAddHistory( src, new ClassB(), "classAtoB" );
   }

   @Transformer
   public ClassC classBtoC(ClassB src)
   {
      return copyAndAddHistory( src, new ClassC(), "classBtoC" );
   }

   @Transformer
   public ClassD classCtoD( ClassC src )
   {
      return copyAndAddHistory( src, new ClassD(), "classCtoD" );
   }

   @Transformer
   public ClassE classBtoE(ClassB src)
   {
      return copyAndAddHistory( src, new ClassE(), "classBtoE" );
   }

   @Transformer
   public ClassD classEtoD(ClassE src)
   {
      return copyAndAddHistory( src, new ClassD(), "classEtoD" );
   }

   @Transformer
   public ClassG classFtoG(ClassF src)
   {
      return copyAndAddHistory( src, new ClassG(), "classFtoG" );
   }
}
