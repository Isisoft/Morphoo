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
 * A parameter's name must be unique and all variables that refer to a context variable must be decorated with this
 * annotation.
 *
 * An example is:
 * <code>
 *    public String transformToString( Integer i, @ContextParam(name="prefix") String pre )
 *    {
 *       return pre + i;
 *    }
 * </code>
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
