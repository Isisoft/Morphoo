package org.isisoft.morphoo.core;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanner;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The Singleton registry where all transformer methods get stored. It is the framework's main storage mechanism.
 * This class is for the framework's internal use only.
 *
 * @author Carlos Munoz
 */
class TransformerRegistry
{

   private static final Object[] DEFAULT_SCAN_HINTS = new Object[]{
         org.isisoft.morphoo.annotation.Transformer.class
   };

   private static TransformerRegistry instance;

   private TransformerGraph transformerGraph;

   private boolean initialized;

   private Collection<String> registeredPackages;

   private Collection<Class<?>> registeredClasses;

   private Collection<Class<?>> runtimeCollectedClasses;


   protected TransformerRegistry()
   {
      this.reset();
   }

   /**
    * Clears out all the registered information pertaining to transformer methods from the framework.
    */
   public void reset()
   {
      this.registeredPackages = new HashSet<String>();
      this.registeredClasses = new HashSet<Class<?>>();
      this.transformerGraph = new TransformerGraph();
      this.runtimeCollectedClasses = new HashSet<Class<?>>();
      this.initialized = false;
   }

   /**
    * @return The singleton instance for the registry.
    */
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

   public Transformer getTransformer(Class<?> sourceType, Class<?> targetType, Collection<String> names, boolean derive)
   {
      this.initializeIfNotReady();
      this.processClassDeclaredTransformers(sourceType);
      this.processClassDeclaredTransformers(targetType);

      return this.transformerGraph.getTransformer(sourceType, targetType, names, derive);
   }

   public Transformer getTransformer( Class<?> ... steps )
   {
      this.initializeIfNotReady();
      for( Class<?> c : steps )
      {
         this.processClassDeclaredTransformers(c);
      }

      return this.transformerGraph.getTransformer( steps );
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
         Reflections reflections =
            new Reflections(
                  this.getReflectionScanHints(),
                  new Scanner[]{ new MethodAnnotationsScanner()}
            );

         Set<Method> transformerMethods =
               reflections.getMethodsAnnotatedWith(org.isisoft.morphoo.annotation.Transformer.class);

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
      this.transformerGraph.addTransformer(method);
   }

   private void processClassDeclaredTransformers( Class<?> cls )
   {
      if( !this.runtimeCollectedClasses.contains( cls ) )
      {
         Method[] candidateTransMethods =
               TransformerMethodAnalyzer.extractClassDeclaredTransformerCandidates( cls );

         for( Method candidate : candidateTransMethods )
         {
            // only process those methods which either transform into or from the object class
            if( candidate.getReturnType() == cls ||
                (candidate.getParameterTypes().length > 0 && candidate.getParameterTypes()[0] == cls) )
            {
               this.registerTransformerMethod(candidate);
            }
         }

         // cache the processed class so it's not processed again
         this.runtimeCollectedClasses.add( cls );
      }
   }


}
