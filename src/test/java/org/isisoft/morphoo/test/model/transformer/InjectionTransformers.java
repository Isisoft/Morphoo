package org.isisoft.morphoo.test.model.transformer;

import org.isisoft.morphoo.annotation.ContextParam;
import org.isisoft.morphoo.annotation.Transformer;

/**
 * @author Carlos Munoz
 */
public class InjectionTransformers
{
   @Transformer(isDefault = true)
   public String appendAndPrepend( String src, @ContextParam(name = "pre")String pre, @ContextParam(name = "pos")String pos )
   {
      return pre + src + pos;
   }

   @Transformer(name = "appendAndPrependWithName")
   public String appendAndPrependWithName(String src, @ContextParam(name = "pre")String pre, @ContextParam(name = "pos")String pos)
   {
      return pre + src + pos + " with name!";
   }
}
