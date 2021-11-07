package tk.miskyle.talkroomdemo.api.group;

import java.util.Map;

public interface Group {
  String PERMISSION_PREFIX = "chat.group.join.";
  int CODE_CONNECTED = 0;
  int CODE_CONNECT_NOT_REGISTER = 1;
  int CODE_CONNECT_FULL = 2;
  int CODE_CONNECT_NO_PERMISSION = 3;
  int CODE_CONNECT_JOINED = 4;

  int getMaxChatter();
  String getGroupId();
  String getDisplayName();
  Map<String, Object> connect(String token);
  void quit(String token);
}
