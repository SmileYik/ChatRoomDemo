package tk.miskyle.talkroomdemo.core.token;

import lombok.*;
import tk.miskyle.talkroomdemo.api.group.chatter.Chatter;
import tk.miskyle.talkroomdemo.api.group.chatter.Gender;

import java.util.HashMap;

@RequiredArgsConstructor
public class Account implements Chatter {
  @Getter
  private final int id;
  @Getter
  private final String email;
  @Getter
  private final String nickname;
  @Getter
  private final Gender gender;
  @Getter
  @Setter
  private boolean op;
  private final HashMap<Object, Object> properties = new HashMap<>();

  @Override
  public boolean isOnline() {
    // TODO
    return false;
  }

  @Override
  public boolean hasPermission(String permission) {
    // TODO
    return false;
  }

  @Override
  public void addPermission(String permission) {
    // TODO
  }

  @Override
  public boolean isBaned() {
    // TODO
    return false;
  }

  @Override
  public boolean containsProperty(Object key) {
    return properties.containsKey(key);
  }

  @Override
  public Object getProperty(Object key) {
    return properties.get(key);
  }

  @Override
  public <T> T getProperty(Object key, Class<T> clazz) {
    return properties.containsKey(key) ?
            clazz.cast(properties.get(key)) : null;
  }

  @Override
  public void putProperty(Object key, Object value) {
    properties.put(key, value);
  }

  @Override
  public HashMap<Object, Object> getProperties() {
    return new HashMap<>(properties);
  }
}
