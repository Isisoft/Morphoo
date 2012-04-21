package org.isisoft.morphoo.test.unit;

import org.isisoft.morphoo.core.TransformerGraph;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Carlos Munoz
 */
public class TransformerGraphTests
{
   @Test
   public void shortestPath()
   {
      TransformerGraph graph = new TransformerGraph();

      graph.addTransformerRoute(String.class, Integer.class);
      graph.addTransformerRoute(Integer.class, Long.class);
      graph.addTransformerRoute(Long.class, Date.class);

      List<Class<?>> shortestPath = graph.getFastestRouteToTarget(String.class, Date.class);

      List<Class<?>> expectedPath = new ArrayList<Class<?>>();
      expectedPath.add(Integer.class);
      expectedPath.add(Long.class);
      expectedPath.add(Date.class);

      assertThat(shortestPath, equalTo(expectedPath));
   }

   @Test
   public void realShortestPath()
   {
      TransformerGraph graph = new TransformerGraph();

      // Add a long path
      graph.addTransformerRoute(String.class, Integer.class);
      graph.addTransformerRoute(Integer.class, Long.class);
      graph.addTransformerRoute(Long.class, Date.class);

      // Add a short Path
      graph.addTransformerRoute(String.class, Date.class);

      List<Class<?>> shortestPath = graph.getFastestRouteToTarget(String.class, Date.class);

      List<Class<?>> expectedPath = new ArrayList<Class<?>>();
      expectedPath.add(Date.class);

      assertThat(shortestPath, equalTo(expectedPath));
   }

   @Test
   public void noPathAtAll()
   {
      TransformerGraph graph = new TransformerGraph();

      graph.addTransformerRoute(String.class, Integer.class);
      graph.addTransformerRoute(Integer.class, Long.class);
      graph.addTransformerRoute(Long.class, Date.class);

      List<Class<?>> shortestPath = graph.getFastestRouteToTarget(String.class, Calendar.class);

      List<Class<?>> expectedPath = new ArrayList<Class<?>>();

      assertThat(shortestPath, equalTo(expectedPath));
   }

   @Test
   public void noPathAtAllButClassPresent()
   {
      TransformerGraph graph = new TransformerGraph();

      // Add a long path
      graph.addTransformerRoute(String.class, Integer.class);
      graph.addTransformerRoute(Integer.class, Long.class);
      graph.addTransformerRoute(Long.class, Date.class);
      // Another independent path
      graph.addTransformerRoute(Calendar.class, Double.class);

      List<Class<?>> shortestPath = graph.getFastestRouteToTarget(String.class, Calendar.class);

      List<Class<?>> expectedPath = new ArrayList<Class<?>>();

      assertThat(shortestPath, equalTo(expectedPath));
   }
}
