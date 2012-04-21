package org.isisoft.morphoo.core;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Carlos Munoz
 */
class TransformerRegistry
{

   private static TransformerRegistry instance;

   private Reflections reflections;

   private Map<TransformerKey, Transformer> transformerMap;

   private Map<TransformerKey, Transformer> defaultTransformerMap;

   private static final Object[] DEFAULT_SCAN_HINTS = new Object[]{
         org.isisoft.morphoo.annotation.Transformer.class
   };

   private Collection<String> registeredPackages;

   private Collection<Class<?>> registeredClasses;

   private TransformerGraph transformerGraph;

   private boolean initialized;


   protected TransformerRegistry()
   {
      this.reset();
   }

   public void reset()
   {
      this.transformerMap = new HashMap<TransformerKey, Transformer>();
      this.defaultTransformerMap = new HashMap<TransformerKey, Transformer>();
      this.registeredPackages = new HashSet<String>();
      this.registeredClasses = new HashSet<Class<?>>();
      this.transformerGraph = new TransformerGraph();
      this.initialized = false;
   }

   public static final TransformerRegistry getInstance()
   {
      if( instance == null )
      {
         instance = new TransformerRegistry();
      }
      return instance;
   }

   public void addScannablePackages(String ... packages)
   {
      for( String p : packages )
      {
         this.registeredPackages.add(p);
      }
   }

   public void addScannableClasses(Class<?> ... classes)
   {
      for(Class<?> c : classes)
      {
         this.registeredClasses.add(c);
      }
   }

   public void addScannableClasses(String ... classNames)
   {
      for(String s : classNames)
      {
         try
         {
            this.registeredClasses.add(Class.forName(s));
         }
         catch (ClassNotFoundException e)
         {
            throw new InitializationException("Attempt to register unknown class: " + s, e);
         }
      }
   }

   public Transformer getTransformer(Class<?> sourceType, Class<?> targetType)
   {
      this.initializeIfNotReady();

      TransformerKey key = new TransformerKey(sourceType, targetType);

      return this.transformerMap.get(key);
   }

   public Transformer getTransformer(Class<?> sourceType, Class<?> targetType, Collection<String> names)
   {
      this.initializeIfNotReady();

      for(String name : names)
      {
         TransformerKey key = new NamedTransformerKey(sourceType, targetType, name);

         if( this.transformerMap.containsKey(key) )
         {
            return this.transformerMap.get(key);
         }
      }

      return null; // did not find a transformer
   }

   public List<Class<?>> deriveClassTransformationChain(Class<?> from, Class<?> to)
   {
      this.initializeIfNotReady();

      return this.transformerGraph.getFastestRouteToTarget(from, to);
   }

   private Object[] getReflectionScanHints()
   {
      Object[] scanHints =
            new Object[ DEFAULT_SCAN_HINTS.length + this.registeredClasses.size() + this.registeredPackages.size() ];

      int pos = 0;
      for(Object o : DEFAULT_SCAN_HINTS)
      {
         scanHints[pos++] = o;
      }
      for(Class c : this.registeredClasses )
      {
         scanHints[pos++] = c;
      }
      for(String p : this.registeredPackages)
      {
         scanHints[pos++] = p;
      }

      return scanHints;
   }

   private void initializeIfNotReady()
   {
      if( !this.initialized )
      {
         this.reflections =
            new Reflections(
                  this.getReflectionScanHints(),
                  new Scanner[]{ new MethodAnnotationsScanner()}
            );

         Set<Method> transformerMethods =
               this.reflections.getMethodsAnnotatedWith(org.isisoft.morphoo.annotation.Transformer.class);

         this.removeNonParticipatingMethods(transformerMethods);

         for( Method m : transformerMethods )
         {
            this.registerTransformerMethod(m);
         }

         this.initialized = true;
      }
   }

   private void removeNonParticipatingMethods(final Collection<Method> candidateMethods)
   {
      Iterator<Method> it = candidateMethods.iterator();

      while(it.hasNext())
      {
         Method candidate = it.next();

         boolean packageIsRegistered = false;
         for( String packageName : this.registeredPackages )
         {
            if( candidate.getDeclaringClass().getPackage().getName().startsWith(packageName) )
            {
               packageIsRegistered = true;
               break;
            }
         }

         boolean classNameIsRegistered  = this.registeredClasses.contains(candidate.getDeclaringClass());

         if( !classNameIsRegistered && !packageIsRegistered )
         {
            it.remove();
         }
      }
   }

   private void registerTransformerMethod( Method method )
   {
      org.isisoft.morphoo.annotation.Transformer transformerAnn =
            method.getAnnotation(org.isisoft.morphoo.annotation.Transformer.class);

      if( transformerAnn == null )
      {
         throw new InitializationException("Method " + method.getName() + " on class "
               + method.getDeclaringClass().getName() + " is not a transformer method but was registered as one");
      }

      // Build the transformer key
      Class<?> sourceType;
      Class<?> targetType = method.getReturnType();

      // No parameters found on the transformer method
      if( method.getParameterTypes().length == 0 )
      {
         throw new InitializationException("Transformer method " + method.getName() + " on class "
               + method.getDeclaringClass().getName() + " must have at least one argument" );
      }
      // One parameter, first parameter is the source, no context allowed
      else if( method.getParameterTypes().length == 1 )
      {
         sourceType = method.getParameterTypes()[0];
      }
      // Two parameters found, the second one must be the Context
      else if( method.getParameterTypes().length == 2 )
      {
         sourceType = method.getParameterTypes()[0];
         if( method.getParameterTypes()[1] != TransformationContext.class )
         {
            throw new InitializationException("Transformer method " + method.getName() + " on class "
                  + method.getDeclaringClass().getName() + " has a second parameter which is not of type "
                  + TransformationContext.class.getName() );
         }
      }
      // All other methods cannot be transformers
      else
      {
         throw new InitializationException("Transformer method " + method.getName() + " on class "
               + method.getDeclaringClass().getName() + " has an incorrect signature for a Transformer method" );
      }

      TransformerKey transformerKey;

      if( transformerAnn.name().trim().isEmpty() )
      {
         transformerKey = new TransformerKey(sourceType, targetType);
      }
      else
      {
         transformerKey = new NamedTransformerKey(sourceType, targetType, transformerAnn.name().trim());
      }

      // If another transformer is already registered
      if( this.transformerMap.containsKey( transformerKey ) )
      {
         Transformer conflictingTransformer = this.transformerMap.get(transformerKey);

         StringBuilder exMssg = new StringBuilder("Two transformer methods with equivalent signatures");
         if( transformerKey instanceof NamedTransformerKey)
         {
            exMssg.append(" and the same name '" + ((NamedTransformerKey)transformerKey).getTransformerName() + '\'');
         }
         exMssg.append(" were found:");
         exMssg.append(method.getDeclaringClass().getName() + "." + method.getName());
         exMssg.append(" and ");
         exMssg.append(conflictingTransformer.getTransformerMethod().getDeclaringClass().getName() + "." );
         exMssg.append(conflictingTransformer.getTransformerMethod().getName());

         throw new InitializationException(exMssg.toString());
      }

      // Store in the transformer map
      final Transformer transformer = new Transformer(method);
      this.transformerMap.put(transformerKey, transformer);

      // store in the default transformer map if it is marked as such
      if( transformerAnn.isDefault() )
      {
         this.defaultTransformerMap.put(new TransformerKey(sourceType, targetType), transformer);
      }

      // Store in the Transformer Graph
      this.transformerGraph.addTransformerRoute(sourceType, targetType);
   }


}
