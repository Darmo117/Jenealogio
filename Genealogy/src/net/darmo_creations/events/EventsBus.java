/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Jenealogio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the main events bus.
 *
 * @author Damien Vergnet
 */
public class EventsBus {
  /** Event bus instance */
  public static final EventsBus EVENT_BUS = new EventsBus();

  private Map<Object, List<Method>> listeners;

  private EventsBus() {
    this.listeners = new HashMap<>();
  }

  /**
   * Registers all methods of the given object annotated by the SubscribeEvent annotation.
   * 
   * @param o the object
   * @throws RuntimeException if an annotated method does not fulfill the requirements
   * @see SubsribeEvent
   */
  public void register(Object o) {
    List<Method> methods = new ArrayList<>();
    Method[] publicMethods = o.getClass().getMethods();

    for (Method m : publicMethods) {
      if (m.isAnnotationPresent(SubsribeEvent.class)) {
        Class<?>[] c = m.getParameterTypes();

        if (c.length == 1 && AbstractEvent.class.isAssignableFrom(c[0])) {
          methods.add(m);
        }
        else {
          String msg = String.format("annotated method '%s' argument does not extend AbstractEvent or has more than 1 argument.",
              m.getName());
          throw new RuntimeException(msg);
        }
      }
    }

    this.listeners.put(o, methods);
  }

  /**
   * Sends an event on the bus.
   * 
   * @param e the event
   */
  public void dispatchEvent(AbstractEvent e) {
    for (Map.Entry<Object, List<Method>> l : this.listeners.entrySet()) {
      Object callee = l.getKey();
      List<Method> methods = l.getValue();

      methods.stream().filter(m -> methodHasRequiredParameterType(m, e.getClass())).forEach(m -> {
        try {
          if (!m.isAccessible())
            m.setAccessible(true);
          m.invoke(callee, e);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
          throw new Error(ex);
        }
      });
    }
  }

  private boolean methodHasRequiredParameterType(Method m, Class<? extends AbstractEvent> arg) {
    Class<?>[] c = m.getParameterTypes();
    return c.length == 1 && c[0].isAssignableFrom(arg);
  }
}
