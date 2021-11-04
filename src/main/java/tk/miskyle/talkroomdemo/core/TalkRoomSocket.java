package tk.miskyle.talkroomdemo.core;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/TalkRoomSocket")
@Component
public class TalkRoomSocket {
  private final static CopyOnWriteArrayList<TalkRoomSocket> preConnection = new CopyOnWriteArrayList<>();
  private final static CopyOnWriteArrayList<TalkRoomSocket> connection = new CopyOnWriteArrayList<>();
  private final static CopyOnWriteArrayList<String> connectionUsers = new CopyOnWriteArrayList<>();
  private String nickname;
  private Session session;

  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    preConnection.add(this);
  }

  @OnClose
  public void onClose() {

  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    throwable.printStackTrace();
  }

  @OnMessage
  public void onMessage(String message, Session session) throws IOException {
    if (preConnection.contains(this)) {
      JSONObject obj;
      try {
        obj = JSONObject.parseObject(message);
      } catch (Exception e) {
        obj = null;
      }
      if (obj == null || !obj.containsKey("join") || !obj.containsKey("nickname")) {
        sendMessage(getErrorMessage(-100, "未进行身份验证."));
        disconnection();
        return;
      }
      if (obj.getBoolean("join")) {
        this.nickname = obj.getString("nickname");
        if (connectionUsers.contains(nickname)) {
          sendMessage(getErrorMessage(-101, "该昵称用户已存在!"));
          disconnection();
          return;
        }
        preConnection.remove(this);
        connection.add(this);
        connectionUsers.add(nickname);
        JSONObject obj2 = new JSONObject();
        obj2.put("user", "System");
        obj2.put("msg", nickname + " 加入了聊天室.");
        obj2.put("icon", "<img alt=\"\" src=\"/blogAvatar/system?s=96&amp;d=mm&amp;r=g\" srcset=\"/blogAvatar/system?s=192&amp;d=mm&amp;r=g 2x\" class=\"avatar avatar-96 photo\" height=\"96\" width=\"96\" loading=\"lazy\">");
        sendAllMessage(obj2.toJSONString());
      }

      // pre connection;
    } else if (connection.contains(this)) {
      JSONObject obj;
      try {
        obj = JSONObject.parseObject(message);
      } catch (Exception e) {
        obj = null;
      }
      if (obj == null || !obj.containsKey("msg")) {
        if (obj != null && obj.containsKey("exit")) {
          disconnection();
        }
        return;
      }
      obj.put("user", nickname);
      obj.put("time", new Date().getTime());
      if (obj.containsKey("split")) {
        obj.put("msg", obj.getString("msg").replace("\n", obj.getString("split")));
      }
      sendAllMessage(obj.toJSONString());
    } else {
      this.session.close();
      session.close();
    }
  }

  private void sendAllMessage(String msg) {
    for (TalkRoomSocket socket : connection) {
      try {
        socket.sendMessage(msg);
      } catch (Exception e) {
        try {
          socket.disconnection();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  private void sendMessage(String msg) throws IOException {
    session.getBasicRemote().sendText(msg);
  }

  private void disconnection() throws IOException {
    preConnection.remove(this);
    connection.remove(this);
    if (nickname != null && connectionUsers.contains(nickname)) {
      JSONObject obj2 = new JSONObject();
      obj2.put("user", "System");
      obj2.put("msg", nickname + " 退出了聊天室.");
      obj2.put("icon", "<img alt=\"\" src=\"/blogAvatar/system?s=96&amp;d=mm&amp;r=g\" srcset=\"/blogAvatar/system?s=192&amp;d=mm&amp;r=g 2x\" class=\"avatar avatar-96 photo\" height=\"96\" width=\"96\" loading=\"lazy\">");
      sendAllMessage(obj2.toJSONString());
      connectionUsers.remove(nickname);
    }
    session.close();
  }

  public static String getErrorMessage(int code, String msg) {
    JSONObject obj = new JSONObject();
    obj.put("code", code);
    obj.put("msg", msg);
    return obj.toJSONString();
  }

}
