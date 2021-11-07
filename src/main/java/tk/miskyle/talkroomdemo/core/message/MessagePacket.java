package tk.miskyle.talkroomdemo.core.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tk.miskyle.talkroomdemo.api.message.IMessage;
import tk.miskyle.talkroomdemo.api.message.MessageChan;
import tk.miskyle.talkroomdemo.core.token.RSAEncrypt;
import tk.miskyle.talkroomdemo.core.token.TokenManager;

import java.util.HashMap;

/**
 * 消息类. 收发消息用.
 */
@Data
@RequiredArgsConstructor
public class MessagePacket {
  @NonNull private MessageChan message;
  /**
   * 发送者的id.
   */
  private int senderId;
  /**
   * 发送者的昵称.
   */
  private String sender;
  /**
   * 服务器接收到的时间.
   */
  private long time;
  /**
   * 是否为系统消息.
   */
  private boolean system;

  /**
   * 其余信息.
   */
  private HashMap<Object, Object> opinions = new HashMap<>();

  /**
   * 添加新的消息选项.
   * @param key key
   * @param value value
   */
  public void putOpinion(Object key, Object value) {
    opinions.put(key, value);
  }

  /**
   * 获取指定key对应的选项.
   * @param key key.
   * @return 对应的选项, 如果不存在key则返回null.
   */
  public Object getOpinion(Object key) {
    return opinions.get(key);
  }

  /**
   * 获取指定key对应的选项.
   * @param key key.
   * @param clazz 选项的类型.
   * @param <T> 指定选项的类型.
   * @return 对应的选项, 如果不存在key则返回null.
   */
  public <T> T getOpinion(Object key, Class<T> clazz) {
    return opinions.containsKey(key) ? clazz.cast(opinions.get(key)) : null;
  }

  /**
   * 是否包含指定选项.
   * @param key key
   * @return 包含返回true, 否则返回false.
   */
  public boolean containsOpinion(Object key) {
    return opinions.containsKey(key);
  }

  /**
   * 将消息打包.
   * @return 打包后的消息.
   */
  public String pack() {
    JSONObject obj = new JSONObject();
    JSONArray array = new JSONArray();
    for (IMessage msg : message) {
      array.add(((AMessage) msg).toJSON());
    }
    obj.put("message", array);
    obj.put("senderId", senderId);
    obj.put("sender", sender);
    obj.put("system", system);
    obj.put("opinions", opinions);
    return obj.toJSONString();
  }

  public static MessagePacket unpack(String json) {
    JSONObject obj = JSONObject.parseObject(json);
    MessageChan messageChan = new MessageChan();
    JSONArray array = obj.getJSONArray("message");
    for (int i = 0; i < array.size(); ++i) {
      messageChan.append(AMessage.unpack(array.getJSONObject(i)));
    }
    MessagePacket packet = new MessagePacket(messageChan);
    if (obj.containsKey("sender")) {
      packet.setSender(obj.getString("sender"));
    }
    if (obj.containsKey("senderId")) {
      packet.setSenderId(obj.getIntValue("senderId"));
    }
    if (obj.containsKey("time")) {
      packet.setTime(obj.getLongValue("time"));
    }
    if (obj.containsKey("system")) {
      packet.setSystem(obj.getBoolean("system"));
    }
    if (obj.containsKey("opinions")) {
      packet.setOpinions(obj.getObject("opinions", HashMap.class));
    }
    return packet;
  }

  /**
   * 加密信息.
   * @param message 要加密的消息.
   * @param msgToken 用户公钥.
   * @return 加密过后的信息. 如果加密失败则返回空串.
   */
  public static String encrypt(String message, String msgToken) {
    try {
      return RSAEncrypt.privateEncrypt(message, TokenManager.getPrivateKey(msgToken));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 解密消息.
   * @param input 加密后的消息.
   * @param msgToken 加密后的信息对应的公钥.
   * @return 解密后的信息, 如果解密失败则返回null.
   */
  @SneakyThrows
  public static MessagePacket decrypt(String input, String msgToken) {
    return unpack(RSAEncrypt.privateDecrypt(input, TokenManager.getPrivateKey(msgToken)));
  }
}
