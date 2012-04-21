package org.isisoft.morphoo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as being a transformer method.
 * Transformer methods may have either 1 or 2 arguments. The first argument must be the source object, and the
 * second one (if present) must be of type {@link org.isisoft.morphoo.core.TransformationContext}, for transformations
 * that require more information.
 *
 * A transformer can be named, which is useful to specify a specific transformer to use when multiple are available.
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
