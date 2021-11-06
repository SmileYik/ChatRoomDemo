package tk.miskyle.talkroomdemo.api.message.sub;

import lombok.Getter;
import tk.miskyle.talkroomdemo.api.message.MessageType;
import tk.miskyle.talkroomdemo.core.message.AMessage;

import java.util.HashMap;

public class ImageMessage extends AMessage {
  public static final String IMAGE_TYPE = "img_t";
  @Getter
  private final String imageType;

  public ImageMessage(String imageType, String imageBase64) {
    super(MessageType.Image);
    this.imageType = imageType;
    setRawMessage(imageBase64);
  }

  /**
   * 将需要发送的键值对进行装箱.<br/>
   * 需要注意的是, 需要使用相应消息类中的常量来进行装箱.
   */
  @Override
  protected HashMap<String, Object> box() {
    HashMap<String, Object> map = new HashMap<>();
    map.put(IMAGE_TYPE, imageType);
    map.put(AMessage.RAW_MESSAGE, getRawMessage());
    return map;
  }
}
