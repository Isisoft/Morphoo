package org.isisoft.morphoo.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Munoz
 */
public class TransformerChain implements Transformer
{
   List<Transformer> transformerSeq;

   public TransformerChain()
   {
      this.transformerSeq = new ArrayList<Transformer>();
   }

   public void addTransformer( Transformer transformer )
   {
      this.transformerSeq.add(transformer);
   }

   @Override
   public Object transform( Object src, TransformationContext ctx )
   {
      Object from = src;
      Object result = null;

      for( int i=0; i<this.transformerSeq.size(); i++ )
      {
         result = this.transformerSeq.get(i).transform(from, ctx);
         from = result;
      }

      return result;
   }
}
