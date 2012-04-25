package org.isisoft.morphoo.core;

/**
 * Interface for classes that are able to perform transformations.
 *
 * @author Carlos Munoz
 */
public interface Transformer
{
   /**
    * Transform an object.
    *
    * @param src The object to transform.
    * @param ctx The transformation context to use in the transformation.
    * @return The transformed object.
    */
   Object transform(Object src, TransformationContext ctx);
}
