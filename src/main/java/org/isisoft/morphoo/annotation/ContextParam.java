package org.isisoft.morphoo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a transformer argument as a Context parameter. If the parameter is not nullable and a null is encountered
 * when performing the transformation, a {@link org.isisoft.morphoo.core.TransformationException} will be thrown
 * explicitly.
 *
 * A parameter's name must be unique when there are multiple parameters with the same or inheriting types. It is
 * recommended to always specify the name to avoid resolution conflicts. If a parameter is not annotated, it will be
 * named after the method's parameter name.
 *
 * @author Carlos Munoz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ContextParam
{
   /** Name for this context parameter */
   String name();

   /** Indicates whether this parameter is nullable or not. An exception will be thrown if the parameter is non-nullable
    * and a null is encountered during transformation */
   boolean nullable() default true;
}
