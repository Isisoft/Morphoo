package org.isisoft.morphoo.test.model.transformer;

import org.isisoft.morphoo.annotation.Transformer;

/**
 * @author Carlos Munoz
 */
public class NamedTransformers
{
   @Transformer(name = "STRING-TO-STRING-1")
   public String toStringPlus1( String str )
   {
      return str + "+1";
   }

   @Transformer(name = "STRING-TO-STRING-2")
   public String toStringPlus2( String str )
   {
      return str + "+2";
   }

   @Transformer(name = "STRING-TO-STRING-3")
   public String toStringPlus3( String str )
   {
      return str + "+3";
   }
}
