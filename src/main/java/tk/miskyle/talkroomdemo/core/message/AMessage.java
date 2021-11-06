package tk.miskyle.talkroomdemo.core.message;

import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import tk.miskyle.talkroomdemo.api.message.IMessage;
import tk.miskyle.talkroomdemo.api.message.MessageType;
import tk.miskyle.talkroomdemo.api.message.sub.ImageMessage;
import tk.miskyle.talkroomdemo.api.message.sub.TextMessage;

import java.util.HashMap;

public abstract class AMessage implements IMessage {
  public static final String MESSAGE_TYPE = "type";
  public static final String RAW_MESSAGE = "raw";

  @Getter
  private final MessageType messageType;
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private String rawMessage;

  protected AMessage(MessageType type) {
    messageType = type;
  }

  /**
   * 将需要发送的键值对进行装箱.<br/>
   * 需要注意的是, 需要使用相应消息类中的常量来进行装箱.
   */
  protected abstract HashMap<String, Object> box();

  /**
   * 打包至json.
   * @return 打包成的JSONObject.
   */
  public JSONObject toJSON() {
    HashMap<String, Object> items = box();
    items.put(MESSAGE_TYPE, messageType.getId());
    JSONObject obj = new JSONObject();
    obj.putAll(items);
    return obj;
  }

  public static IMessage unpack(JSONObject obj) {
    MessageType type = MessageType.getById(obj.getIntValue(MESSAGE_TYPE));
    if (type == null) {
      return null;
    }
    switch (type) {
      case Text:
        return new TextMessage(obj.getString(RAW_MESSAGE));
      case Image:
        return new ImageMessage(
                obj.getString(ImageMessage.IMAGE_TYPE),
                obj.getString(AMessage.RAW_MESSAGE)
        );
    }
    return null;
  }

}
