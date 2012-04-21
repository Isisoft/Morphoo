package org.isisoft.morphoo.test.model.transformer;

import org.isisoft.morphoo.annotation.Transformer;
import org.isisoft.morphoo.core.TransformationContext;

/**
 * @author Carlos Munoz
 */
public class TransformerMethodsWithContext
{
   @Transformer
   public String toAnotherStringWithContext(String obj, TransformationContext ctx)
   {
      return new StringBuilder(obj).append(ctx.get("additional")).toString();
   }
}
