package tk.miskyle.talkroomdemo.core.message;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tk.miskyle.talkroomdemo.core.token.RSAEncrypt;
import tk.miskyle.talkroomdemo.core.token.TokenManager;

import java.util.HashMap;

@RequiredArgsConstructor
@Data
public class Message {
  @NonNull private String message;
  private int senderId;
  private String sender;
  private long time;
  private boolean system;
  private HashMap<Object, Object> opinions = new HashMap<>();

  public void putOpinion(Object key, Object value) {
    opinions.put(key, value);
  }

  public Object getOpinion(Object key) {
    return opinions.get(key);
  }

  public <T> T getOpinion(Object key, Class<T> clazz) {
    return opinions.containsKey(key) ? clazz.cast(opinions.get(key)) : null;
  }

  public static String encrypt(Message message, String msgToken) {
    String json = JSONObject.toJSONString(message);
    try {
      return RSAEncrypt.privateEncrypt(json, TokenManager.getPrivateKey(msgToken));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  @SneakyThrows
  public static JSONObject decrypt(String input, String msgToken) {
    return JSONObject.parseObject(
           RSAEncrypt.privateDecrypt(input, msgToken)
    );
  }
}
