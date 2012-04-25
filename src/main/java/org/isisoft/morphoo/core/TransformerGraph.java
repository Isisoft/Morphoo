package org.isisoft.morphoo.core;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Carlos Munoz
 */
public class TransformerGraph
{
   private DirectedGraph<Class<?>, DefaultEdge> graphImpl;

   private Map<DefaultEdge, TransformerMethodSet> transformerMethodMap;


   public TransformerGraph()
   {
      this.graphImpl = new DefaultDirectedGraph<Class<?>, DefaultEdge>(DefaultEdge.class);
      this.transformerMethodMap = new HashMap<DefaultEdge, TransformerMethodSet>();
   }

   public void addTransformer( Method transformerMethod )
   {
      TransformerMethod newTrans = TransformerMethodAnalyzer.analyze(transformerMethod);
      Class<?> from = newTrans.getSourceType();
      Class<?> to = newTrans.getTargetType();

      this.graphImpl.addVertex(from);
      this.graphImpl.addVertex(to);
      DefaultEdge graphEdge = this.graphImpl.addEdge(from, to);

      // not a new edge, the graph impl will return null
      if( graphEdge == null )
      {
         graphEdge = this.graphImpl.getEdge(from, to);
      }

      // Add / Get a transformer method set
      TransformerMethodSet edgeMethods;
      if( this.transformerMethodMap.containsKey( graphEdge ) )
      {
         edgeMethods = this.transformerMethodMap.get( graphEdge );
      }
      else
      {
         edgeMethods = new TransformerMethodSet();
         this.transformerMethodMap.put(graphEdge, edgeMethods);
      }


      // if there is a default transformer already defined
      TransformerMethod conflictTrans = edgeMethods.getDefault();
      if( newTrans.isDefault() && conflictTrans != null )
      {
         throw new InitializationException("Multiple default Transformers were detected from " +
               from.getName() + " to " + to.getName() +": " +
               conflictTrans.getJavaMethod().getDeclaringClass().getName() + "." + conflictTrans.getJavaMethod().getName()
               + " and " + newTrans.getJavaMethod().getDeclaringClass().getName() + "." + newTrans.getJavaMethod().getName());
      }

      // if there is already another transformer method with the same name
      conflictTrans = edgeMethods.getByName( newTrans.getName() );
      if( conflictTrans != null )
      {
         throw new InitializationException("Multiple transformers are sharing the name '" + newTrans.getName() + "': " +
               conflictTrans.getJavaMethod().getDeclaringClass().getName() + "." + conflictTrans.getJavaMethod().getName()
               + " and " + newTrans.getJavaMethod().getDeclaringClass().getName() + "." + newTrans.getJavaMethod().getName());
      }

      edgeMethods.add(newTrans);
   }

   public Transformer getTransformer(Class<?> from, Class<?> to, Collection<String> usingNames, boolean includeNonDirect)
   {
      // Make sure the graphImpl contains both vertexes. If not, then there is no transformer
      if( !this.graphImpl.containsVertex(from) || !this.graphImpl.containsVertex(to) )
      {
         return null;
      }

      DefaultEdge grapEdge = this.graphImpl.getEdge(from, to);
      // No direct transformer between the two types
      if( grapEdge == null )
      {
         // if non direct transformers are to be used, try to find one that way
         if( includeNonDirect )
         {
            Transformer nonDirectTrans = this.getNonDirectTransformer(from, to, usingNames);
            if( nonDirectTrans != null )
            {
               return nonDirectTrans;
            }
         }
      }

      TransformerMethodSet methodSet = this.transformerMethodMap.get(grapEdge);
      // No direct transformer between the two types
      if( methodSet.isEmpty() )
      {
         // if non direct transformers are to be used, try to find one that way
         if( includeNonDirect )
         {
            return this.getNonDirectTransformer(from, to, usingNames);
         }
         else
         {
            return null;
         }
      }
      // only one, use that one
      else if( methodSet.size() == 1 )
      {
         return methodSet.iterator().next().getExecutor();
      }
      // more than one transformer
      else
      {
         // Try to resolve the conflict using the given names (first one to find is used)
         if( usingNames != null && !usingNames.isEmpty() )
         {
            for( String name : usingNames )
            {
               TransformerMethod foundByName = methodSet.getByName( name );
               if( foundByName != null )
               {
                  return foundByName.getExecutor();
               }
            }
         }

         // resolve the conflict by getting the default one
         TransformerMethod defaultTrans = methodSet.getDefault();
         if( defaultTrans != null )
         {
            return defaultTrans.getExecutor();
         }
         // no default, cannot resolve conflict
         else
         {
            throw new TransformationException("Multiple Transformers found from " + from.getName() + " to " + to.getName() +
                  " but couldn't decide which one to use. Make sure all transformer methods have been named to avoid this");
         }
      }
   }

   public Transformer getTransformer(Class<?> from, Class<?> to, Collection<String> usingNames)
   {
      return this.getTransformer(from, to, usingNames, false);
   }

   public Transformer getTransformer(Class<?> from, Class<?> to)
   {
      return this.getTransformer(from, to, null, false);
   }

   public Transformer getTransformer(Class<?> ... steps)
   {
      // don't even look at a request like this
      if( steps.length < 2 )
      {
         throw new TransformationException("getTransformer(Class[]) should be invoked with at least 2 classes");
      }
      else
      {
         int fromIdx = 0;
         int toIdx = 1;
         TransformerChain transChain = new TransformerChain();

         while( toIdx < steps.length )
         {
            Transformer intermediateTrans = this.getTransformer(steps[fromIdx], steps[toIdx]);
            if( intermediateTrans == null )
            {
               throw new TransformationException("Could not find a transformer between " + steps[fromIdx].getName() +
                     " to " + steps[toIdx].getName() + " when building a multi-step transformer");
            }
            transChain.addTransformer( intermediateTrans );
            fromIdx++;
            toIdx++;
         }

         return transChain;
      }
   }

   private Transformer getNonDirectTransformer(Class<?> from, Class<?> to, Collection<String> usingNames)
   {
      // Make sure the graphImpl contains both vertexes. If not, return an empty list
      if( !this.graphImpl.containsVertex(from) || !this.graphImpl.containsVertex(to) )
      {
         return null;
      }

      List<DefaultEdge> route = DijkstraShortestPath.findPathBetween(this.graphImpl, from, to);

      TransformerChain transChain = null;
      // if there is a route between the two classes
      if( route != null )
      {
         transChain = new TransformerChain();
         // Get a sequence of direct-only transformers
         for( DefaultEdge edge : route )
         {
            transChain.addTransformer(
                  this.getTransformer( this.graphImpl.getEdgeSource(edge), this.graphImpl.getEdgeTarget(edge), usingNames ) );
         }
      }

      return transChain;
   }
}
