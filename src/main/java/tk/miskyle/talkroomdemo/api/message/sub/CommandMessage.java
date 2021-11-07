package tk.miskyle.talkroomdemo.api.message.sub;

import lombok.Getter;
import lombok.Setter;
import tk.miskyle.talkroomdemo.api.message.MessageType;
import tk.miskyle.talkroomdemo.core.message.AMessage;
import tk.miskyle.talkroomdemo.core.util.MapBuilder;

import java.util.HashMap;

public class CommandMessage extends AMessage {
  public static final String TARGET_ID = "td";
  public static final String COMMAND = "cmd";

  @Setter
  @Getter
  private int targetId;
  @Setter
  @Getter
  private int command;

  public CommandMessage() {
    super(MessageType.Command);
  }

  public CommandMessage setResultMessage(String result) {
    setRawMessage(result);
    return this;
  }

  /**
   * 将需要发送的键值对进行装箱.<br/>
   * 需要注意的是, 需要使用相应消息类中的常量来进行装箱.
   */
  @Override
  protected HashMap<String, Object> box() {
    return new MapBuilder<String, Object>()
            .append(RAW_MESSAGE, getRawMessage())
            .append(TARGET_ID, targetId)
            .append(COMMAND, command).getMap();
  }
}
