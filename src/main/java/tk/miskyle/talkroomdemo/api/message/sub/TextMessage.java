package tk.miskyle.talkroomdemo.api.message.sub;

import tk.miskyle.talkroomdemo.api.message.MessageType;
import tk.miskyle.talkroomdemo.core.message.AMessage;

import java.util.HashMap;

/**
 * 普通文本类型消息.
 */
public class TextMessage extends AMessage {

  /**
   * 创建一个普通文本类型的消息.
   * @param msg 消息内容.
   */
  public TextMessage(String msg) {
    super(MessageType.Text);
    setRawMessage(msg);
  }

  /**
   * 获取消息内容.
   * @return 返回消息的内容.
   */
  public String getContent() {
    return getRawMessage();
  }

  @Override
  protected HashMap<String, Object> box() {
    HashMap<String, Object> map = new HashMap<>();
    map.put(AMessage.RAW_MESSAGE, getRawMessage());
    return map;
  }
}
