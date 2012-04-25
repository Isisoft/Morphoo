package org.isisoft.morphoo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enumerates a list of classes where transformers may be found for this class. The classes may contain transformer
 * methods only for the given type, meaning that these transformer methods should only transform from and into the
 * annotated class; and they eventually might conflict with other transformer methods.
 *
 * This annotation provides a way to use transformers without scanning for transformer methods, but rather detecting
 * them at runtime.
 *
 * @author Carlos Munoz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Transformers
{
   /** Array of classes that hold transformer methods from / into he annotated class */
   Class<?>[] value();
}
