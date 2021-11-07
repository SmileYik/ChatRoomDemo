package tk.miskyle.talkroomdemo.core.message;

import tk.miskyle.talkroomdemo.core.group.Group;
import tk.miskyle.talkroomdemo.core.token.Account;

public class MessageHandler {
  public static void doMessage(MessagePacket packet, Account account) {
    Group group = account.getGroup();
    if (group == null) {
      return;
    }
    group.chat(packet);
  }
}
