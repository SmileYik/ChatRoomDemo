package tk.miskyle.talkroomdemo.api.message.sub;

import tk.miskyle.talkroomdemo.api.message.MessageType;
import tk.miskyle.talkroomdemo.core.message.AMessage;

import java.util.HashMap;

public class ImageMessage extends AMessage {
  private String imageBase64;

  public ImageMessage() {
    super(MessageType.Image);
    setRawMessage("[Image]");

  }

  /**
   * 将需要发送的键值对进行装箱.<br/>
   * 需要注意的是, 需要使用相应消息类中的常量来进行装箱.
   */
  @Override
  protected HashMap<String, Object> box() {
    return null;
  }
}
