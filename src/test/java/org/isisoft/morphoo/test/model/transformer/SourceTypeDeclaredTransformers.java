package org.isisoft.morphoo.test.model.transformer;

import org.isisoft.morphoo.annotation.Transformer;
import org.isisoft.morphoo.test.model.SourceTypeWithTransformers;

import java.util.Date;

/**
 * @author Carlos Munoz
 */
public class SourceTypeDeclaredTransformers
{
   @Transformer
   public String toString( SourceTypeWithTransformers src )
   {
      return src.getName();
   }

   @Transformer
   public Long toLong( SourceTypeWithTransformers src )
   {
      return src.getId();
   }

   @Transformer
   public Date toDate( SourceTypeWithTransformers src )
   {
      return src.getCreationDate();
   }

   @Transformer
   public SourceTypeWithTransformers fromDate( Date d )
   {
      SourceTypeWithTransformers target = new SourceTypeWithTransformers();
      target.setCreationDate(d);
      return target;
   }

}
