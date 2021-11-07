package tk.miskyle.talkroomdemo.core.plugin.event;

import lombok.Getter;
import tk.miskyle.talkroomdemo.api.ChatRoomPlugin;
import tk.miskyle.talkroomdemo.api.event.Event;
import tk.miskyle.talkroomdemo.api.event.Listener;
import tk.miskyle.talkroomdemo.api.event.annotation.EventHandle;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class EventManager implements tk.miskyle.talkroomdemo.api.event.EventManager {
  @Getter
  private static EventManager manager;
  private final HashMap<Type, PriorityQueue<EventListener>> listeners = new HashMap<>();

  public EventManager() {
    manager = this;
  }

  @Override
  public void registerEvents(ChatRoomPlugin plugin, Listener listener) {
    for (Method method : listener.getClass().getDeclaredMethods()) {
      if (method.isAccessible()
              && method.isAnnotationPresent(EventHandle.class)
              && method.getParameterCount() == 1) {
        EventHandle handle = method.getDeclaredAnnotation(EventHandle.class);
        Type type = method.getParameterTypes()[0];
        EventListener eventListener =
                new EventListener(listener, plugin, method, type, handle);
        if (!listeners.containsKey(type)) {
          listeners.put(type, new PriorityQueue<>((o1, o2) ->
                  (int) Math.signum(o2.getEventHandle().priority().getPriority() -
                          o1.getEventHandle().priority().getPriority())));
        }
        listeners.get(type).add(eventListener);
      }
    }
  }

  @Override
  public void unregisterEvents(Listener listener) {
    new HashMap<>(listeners).forEach((type, queue) -> {
      new LinkedList<>(queue).forEach(eventListener -> {
        if (eventListener.getOwnerListener() == listener) {
          listeners.get(type).remove(eventListener);
        }
      });
    });
  }

  @Override
  public void unregisterAllEvents(ChatRoomPlugin plugin) {
    new HashMap<>(listeners).forEach((type, queue) -> {
      new LinkedList<>(queue).forEach(eventListener -> {
        if (eventListener.getOwnerPlugin() == plugin) {
          listeners.get(type).remove(eventListener);
        }
      });
    });
  }

  @Override
  public void callEvent(Event event) {
    if (listeners.containsKey(event.getClass())) {
      PriorityQueue<EventListener> queue = new PriorityQueue<>(listeners.get(event.getClass()));
      while (!queue.isEmpty()) {
        queue.poll().invoke(event);
      }
    }
  }
}
