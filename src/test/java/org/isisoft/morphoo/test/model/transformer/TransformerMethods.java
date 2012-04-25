package org.isisoft.morphoo.test.model.transformer;

import org.isisoft.morphoo.annotation.Transformer;
import org.isisoft.morphoo.test.model.unit.FinalTargetType;
import org.isisoft.morphoo.test.model.unit.IntermediateType;
import org.isisoft.morphoo.test.model.unit.SourceType;

import java.util.Date;

/**
 * @author Carlos Munoz
 */
public class TransformerMethods
{
   @Transformer
   public String toString( SourceType obj )
   {
      return obj.getName();
   }

   @Transformer
   public int toInteger( SourceType obj )
   {
      return obj.getValue();
   }

   @Transformer
   public static Date toDate( SourceType obj )
   {
      return obj.getDate();
   }

   @Transformer
   public IntermediateType toIntermediateType( SourceType obj )
   {
      IntermediateType intermediate = new IntermediateType();
      intermediate.setEncapsulated(obj);
      return intermediate;
   }

   @Transformer
   public FinalTargetType toFileTargetType( IntermediateType intermediate )
   {
      FinalTargetType finalTarget = new FinalTargetType();
      finalTarget.setDate(intermediate.getEncapsulated().getDate());
      return finalTarget;
   }
}
