package tk.miskyle.talkroomdemo.core.group;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import tk.miskyle.talkroomdemo.core.message.MessagePacket;
import tk.miskyle.talkroomdemo.core.token.Account;
import tk.miskyle.talkroomdemo.core.token.TokenManager;
import tk.miskyle.talkroomdemo.core.util.MapUtil;

import java.util.HashSet;
import java.util.Map;

public class Group implements tk.miskyle.talkroomdemo.api.group.Group {
  @Getter
  private final int maxChatter;
  @Getter
  private final String groupId;
  @Setter
  private String displayName;
  @Setter
  @Getter
  private boolean permission;
  private final HashSet<String> chatters;

  public Group(int maxChatter, String groupId, boolean permission) {
    chatters = new HashSet<>();
    this.maxChatter = maxChatter;
    this.groupId = groupId;
    this.permission = permission;
  }

  public String getDisplayName() {
    return displayName == null ? groupId : displayName;
  }

  public Map<String, Object> connect(String token) {
    if (!TokenManager.containsKey(token)) {
      return MapUtil.toMap(
              String.class, Object.class,
              "result", CODE_CONNECT_NOT_REGISTER
      );
    } else if (chatters.contains(token)) {
      return MapUtil.toMap(
              String.class, Object.class,
              "result", CODE_CONNECT_JOINED
      );
    } else if (chatters.size() == maxChatter) {
      return MapUtil.toMap(
              String.class, Object.class,
              "result", CODE_CONNECT_FULL
      );
    }
    Account account = TokenManager.getAccount(token);
    if (permission && !account.hasPermission(PERMISSION_PREFIX + groupId, true)) {
      return MapUtil.toMap(
              String.class, Object.class,
              "result", CODE_CONNECT_NO_PERMISSION
      );
    }
    join(token);
    account.setGroup(this);
    return MapUtil.toMap(
            String.class, Object.class,
            "result", CODE_CONNECTED
    );
  }

  @Synchronized
  private void join(String token) {
    chatters.add(token);
  }

  @Synchronized
  public void quit(String token) {
    chatters.remove(token);
  }

  public void chat(MessagePacket packet) {
    String rawMessage = packet.pack();
    new HashSet<>(chatters).forEach(token -> {
      Account account = TokenManager.getAccount(token);
      if (account == null) {
        quit(token);
        return;
      } else if (account.getId() == packet.getSenderId()) {
        return;
      }
      String encode = MessagePacket.encrypt(rawMessage, token);
      try {
        account.sendMessage(encode);
      } catch (Exception e) {
        quit(token);
        e.printStackTrace();
      }
    });
  }
}
