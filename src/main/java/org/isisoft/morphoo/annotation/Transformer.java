package org.isisoft.morphoo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as being a transformer method.
 * Transformer methods must have at least one argument. The first argument must be the source object, and all others may
 * be either context variables (annotated with {@link ContextParam}), or the TransformationContext itself, in which case
 * it doesn't need to be annotated.
 *
 * A transformer can be named, which is useful to qualify a specific transformer to use when multiple are available.
 * It can also be marked as the default transformer for a source and target type. Two transformer methods for the
 * same source and target cannot be marked as default.
 *
 * @author Carlos Munoz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transformer
{
   /** Indicates if this is the default transformer to use when there are resolution conflicts */
   boolean isDefault() default false;

   /** Transformer name. Only necessary when there are multiple transformers for the same source and target
    * types */
   String name() default "";
}
