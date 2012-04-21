package org.isisoft.morphoo.core;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Carlos Munoz
 */
public class TransformerGraph
{
   private DirectedGraph<Class<?>, DefaultEdge> transformerGraph;

   public TransformerGraph()
   {
      this.transformerGraph = new DefaultDirectedGraph<Class<?>, DefaultEdge>(DefaultEdge.class);
   }

   public void addTransformerRoute( Class<?> from, Class<?> to )
   {
      this.transformerGraph.addVertex(from);
      this.transformerGraph.addVertex(to);
      this.transformerGraph.addEdge(from, to);
   }

   public List<Class<?>> getFastestRouteToTarget(Class<?> from, Class<?> to)
   {
      // Make sure the graph contains both vertexes. If not, return an empty list
      if( !this.transformerGraph.containsVertex(from) || !this.transformerGraph.containsVertex(to) )
      {
         return Collections.emptyList();
      }

      List<DefaultEdge> route = DijkstraShortestPath.findPathBetween(this.transformerGraph, from, to);

      // If there is no route, return an empty list
      if( route == null )
      {
         return Collections.emptyList();
      }

      // Convert to a list of intermediate classes
      List<Class<?>> classRoute = new ArrayList<Class<?>>(route.size());
      for( DefaultEdge edge : route )
      {
         classRoute.add( this.transformerGraph.getEdgeTarget(edge) );
      }

      return classRoute;
   }
}
