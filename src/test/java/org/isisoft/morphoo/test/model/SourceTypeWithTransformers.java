package org.isisoft.morphoo.test.model;

import org.isisoft.morphoo.annotation.Transformers;
import org.isisoft.morphoo.test.model.transformer.SourceTypeDeclaredTransformers;

import java.util.Date;

/**
 * @author Carlos Munoz
 */
@Transformers(SourceTypeDeclaredTransformers.class)
public class SourceTypeWithTransformers
{
   private Long id;

   private Date creationDate;

   private String name;

   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public Date getCreationDate()
   {
      return creationDate;
   }

   public void setCreationDate(Date creationDate)
   {
      this.creationDate = creationDate;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }
}
