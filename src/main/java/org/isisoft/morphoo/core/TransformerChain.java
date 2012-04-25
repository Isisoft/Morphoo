package org.isisoft.morphoo.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A transformer chain is an ordered sequence of transformers in which the output of each transformer is the input to
 * the next one. The output from the last transformer in the sequence is then the result of the transformation.
 *
 * @author Carlos Munoz
 */
public class TransformerChain implements Transformer
{
   List<Transformer> transformerSeq;

   public TransformerChain()
   {
      this.transformerSeq = new ArrayList<Transformer>();
   }

   /**
    * Adds a transformer to the end of the chain.
    *
    * @param transformer The transformer to add.
    */
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
