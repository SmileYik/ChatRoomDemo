package tk.miskyle.talkroomdemo.core.util;

import lombok.Getter;

import java.util.HashMap;

public class MapBuilder <K, V> {
  @Getter
  private final HashMap<K, V> map = new HashMap<>();

  public MapBuilder<K, V> append(K k, V v) {
    map.put(k, v);
    return this;
  }
}
