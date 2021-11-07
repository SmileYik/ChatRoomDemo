package tk.miskyle.talkroomdemo.core.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GroupManager {
  private static final HashMap<String, Group> groups = new HashMap<>();

  public GroupManager(JSONArray arr) {
    JSONObject obj;
    for (int i = 0; i < arr.size(); ++i) {
      obj = arr.getJSONObject(i);
      Group group = new Group(
              obj.getIntValue("maxChatter"),
              obj.getString("groupId"),
              obj.getBoolean("permission")
      );
      if (obj.containsKey("display")) {
        group.setDisplayName(obj.getString("display"));
      }
      log.info(group.getDisplayName() + "(" + group.getGroupId() + ") 房间开启.");
      groups.put(group.getGroupId(), group);
    }
  }

  public static Collection<Group> getGroups() {
    return new LinkedList<>(groups.values());
  }

  public static boolean containsGroup(String id) {
    return groups.containsKey(Objects.requireNonNull(id));
  }

  public static Group getGroup(String id) {
    return groups.get(id);
  }
}
