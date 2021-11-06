package tk.miskyle.talkroomdemo.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import tk.miskyle.talkroomdemo.api.ChatRoomPlugin;
import tk.miskyle.talkroomdemo.api.event.Event;
import tk.miskyle.talkroomdemo.api.event.Listener;
import tk.miskyle.talkroomdemo.api.event.annotation.EventHandle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Data
@AllArgsConstructor
public class EventListener {
  private final Listener ownerListener;
  private final ChatRoomPlugin ownerPlugin;
  private final Method method;
  private final Type eventType;
  private final EventHandle eventHandle;

  public void invoke(Event event) {
    try {
      method.invoke(ownerListener, event);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
