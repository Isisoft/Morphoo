package org.isisoft.morphoo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
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
