package tk.miskyle.talkroomdemo.controller.component;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/MessageSocket")
public class MessageSocket {
  private String groupToken;
  private String userToken;
  private Session session;

  @OnOpen
  public void onOpen(Session session) {

  }

  @OnMessage
  public void onMessage(String message) {

  }

  @OnError
  public void onError(Throwable throwable) {

  }

  @OnClose
  public void onClose() {

  }

  public void sendMessage(String encryptMessage) {
    session.getAsyncRemote().sendText(encryptMessage);
  }

}
