package tk.miskyle.talkroomdemo.core.message;

import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import tk.miskyle.talkroomdemo.api.message.MessageType;

import java.util.HashMap;

public abstract class AMessage {
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

}
