package tk.miskyle.talkroomdemo.api.message;

public interface IMessage {
  /**
   * 获取原消息数据.
   * @return 返回原消息数据.
   */
  String getRawMessage();

  /**
   * 获取消息类型.
   * @return 消息类型.
   */
  MessageType getMessageType();
}
