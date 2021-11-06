package tk.miskyle.talkroomdemo.api.message;

import java.util.AbstractSequentialList;
import java.util.LinkedList;

public class MessageChan extends LinkedList<IMessage> {
  public MessageChan append(IMessage msg) {
    add(msg);
    return this;
  }
}
