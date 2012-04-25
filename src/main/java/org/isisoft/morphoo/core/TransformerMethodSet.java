package org.isisoft.morphoo.core;

import java.util.HashSet;

/**
 * Implements a set of transformer method abstractions and adds utility methods to operate on its contents.
 *
 * @author Carlos Munoz
 */
public class TransformerMethodSet extends HashSet<TransformerMethod>
{
   public TransformerMethod getDefault()
   {
      for( TransformerMethod tm : this )
      {
         if( tm.isDefault() )
         {
            return tm;
         }
      }
      return null;
   }

   public TransformerMethod getByName( String name )
   {
      for( TransformerMethod tm : this )
      {
         if( tm.getName().equals(name) )
         {
            return tm;
         }
      }
      return null;
   }
}
