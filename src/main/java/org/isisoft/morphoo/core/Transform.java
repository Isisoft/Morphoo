package org.isisoft.morphoo.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * One of the framework's most important class. It offers ways to customize and perform transformations.
 *
 * To begin a transformation the {@link Transform#from(Object)} method is called. It will initialize a
 * transformation for the given object.
 *
 * A simple transformation may be of the form
 * <code>Transform.from(myObj).to(MyOtherClass.class)</code>
 *
 * @author Carlos Munoz
 */
public class Transform<T>
{

   private T srcObject;

   private TransformationContext context;

   private Queue<Class<?>> transformerChain;

   private Set<String> transformerNames;

   private boolean deriveTransformation;

   private Transform()
   {
      this.context = new TransformationContext();
      this.transformerChain = new LinkedList<Class<?>>();
      this.transformerNames = new HashSet<String>(0);
      this.deriveTransformation = false;
   }

   public static final <R> Transform<R> from( R src )
   {
      Transform<R> transform = new Transform<R>();
      transform.srcObject = src;
      return transform;
   }

   public final <R> R to( Class<R> targetClass )
   {
      Object src = this.srcObject;
      Class<?> toClass;

      // If deriving is enabled, build a transformer chain right now
      if(this.deriveTransformation)
      {
         this.transformerChain.clear();
         List<Class<?>> chain = TransformerRegistry.getInstance().deriveClassTransformationChain(src.getClass(), targetClass);

         if( chain.isEmpty() )
         {
            throw new TransformationException("Unable to derive transformation chain from " + src.getClass().getName()
                  + " to " + targetClass );
         }

         this.transformerChain.addAll(chain.subList(0, chain.size()-1)); // Ignore the last element as it will be dealt with below
      }

      // perform any chained transformations
      while( !this.transformerChain.isEmpty() )
      {
         toClass = this.transformerChain.poll();
         src = this.transform(src, toClass);
      }

      // Last transformation
      toClass = targetClass;
      return (R)this.transform(src, toClass);
   }

   public Transform<T> through( Class<?> intermediateClass )
   {
      // through cannot be used with derive at the same time
      if( this.deriveTransformation )
      {
         throw new TransformationException("A Transformation path cannot be derived and specified simultaneously");
      }

      this.transformerChain.offer(intermediateClass);
      return this;
   }

   public Transform<T> using(String transformerName)
   {
      this.transformerNames.add(transformerName);
      return this;
   }

   public Transform<T> withContext(String name, Object ctxVariable)
   {
      this.context.put(name, ctxVariable);
      return this;
   }

   public Transform<T> deriving()
   {
      // Deriving cannot be used with through at the same time
      if( !this.transformerChain.isEmpty() )
      {
         throw new TransformationException("A Transformation path cannot be derived and specified simultaneously");
      }

      this.deriveTransformation = true;
      return this;
   }

   private Object transform( Object src, Class<?> targetClass )
   {
      Transformer transformer =
            TransformerRegistry.getInstance().getTransformer(src.getClass(), targetClass);

      // Try to find a named transformer if any names were supplied
      if( transformer == null && !this.transformerNames.isEmpty() )
      {
         transformer =
               TransformerRegistry.getInstance().getTransformer(src.getClass(), targetClass, this.transformerNames);
      }

      // If still not found then it is not registered
      if( transformer == null )
      {
         throw new TransformationException("No Transformer method found to transform from " + src.getClass().getName() +
               " to " + targetClass.getName());
      }

      return transformer.transform(src, this.context);
   }

}
