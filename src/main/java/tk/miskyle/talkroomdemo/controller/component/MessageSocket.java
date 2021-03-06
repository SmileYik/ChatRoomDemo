package tk.miskyle.talkroomdemo.controller.component;

import org.springframework.stereotype.Component;
import tk.miskyle.talkroomdemo.core.message.MessageHandler;
import tk.miskyle.talkroomdemo.core.message.MessagePacket;
import tk.miskyle.talkroomdemo.core.token.Account;
import tk.miskyle.talkroomdemo.core.token.TokenManager;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint("/MessageSocket")
public class MessageSocket {
  public static final String CLOSE_REASON_NO_TOKEN = "no token.";

  private String userToken;
  private Account account;
  private Session session;

  @OnOpen
  public void onOpen(Session session) throws IOException {
    if (!session.getRequestParameterMap().containsKey("token")) {
      session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, CLOSE_REASON_NO_TOKEN));
      return;
    }
    userToken = session.getRequestParameterMap().get("token").stream().findFirst().orElse("");
    if (userToken.isEmpty() || !TokenManager.containsKey(userToken)) {
      session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, CLOSE_REASON_NO_TOKEN));
      return;
    }
    account = TokenManager.getAccount(userToken);
    this.session = session;
    account.setSocket(this);
  }

  @OnMessage
  public void onMessage(String message) {
    MessagePacket packet = MessagePacket.decrypt(message, userToken);
    packet.setSystem(false);
    packet.setTime(System.currentTimeMillis());
    packet.setSender(account.getNickname());
    MessageHandler.doMessage(packet, account);
  }

  @OnError
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @OnClose
  public void onClose() {

  }

  public void sendMessage(String encryptMessage) throws Exception {
    session.getAsyncRemote().sendText(encryptMessage);
  }

}
