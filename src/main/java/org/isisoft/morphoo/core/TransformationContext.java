package org.isisoft.morphoo.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Carlos Munoz
 */
public class TransformationContext
{
   private Map<String, Object> contextMap = new HashMap<String, Object>();

   public Object put(String name, Object o)
   {
      return contextMap.put(name, o);
   }

   public Object remove(Object o)
   {
      return contextMap.remove(o);
   }

   public Object get(String name)
   {
      return contextMap.get(name);
   }

   public <T> T get(String name, Class<T> asClass)
   {
      return (T)this.get(name);
   }

   public int size()
   {
      return contextMap.size();
   }

   public boolean containsKey(String name)
   {
      return contextMap.containsKey(name);
   }
}
