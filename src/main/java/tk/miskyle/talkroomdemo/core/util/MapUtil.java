package tk.miskyle.talkroomdemo.core.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
  public static <K, V> Map<K, V> toMap(Class<K> kClass, Class<V> vClass, Object ... objs) {
    if (objs.length % 2 == 1) {
      throw new RuntimeException("args cannot be an odd number.");
    }
    HashMap<K, V> map = new HashMap<>();
    K key;
    V value;
    for (int i = 0; i < objs.length; i += 2) {
      key = kClass.cast(objs[i]);
      value = vClass.cast(objs[i + 1]);
      map.put(key, value);
    }
    return map;
  }
}
