package tk.miskyle.talkroomdemo.controller.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tk.miskyle.talkroomdemo.core.message.MessageHandler;
import tk.miskyle.talkroomdemo.core.message.MessagePacket;
import tk.miskyle.talkroomdemo.core.token.Account;
import tk.miskyle.talkroomdemo.core.token.RSAEncrypt;
import tk.miskyle.talkroomdemo.core.token.TokenManager;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Component
@ServerEndpoint("/MessageEchoSocket")
public class MessageEchoSocket {
  public static final String CLOSE_REASON_NO_TOKEN = "no token.";

  private String userToken;
  private Account account;
  private Session session;

  @OnOpen
  public void onOpen(Session session) throws IOException {
    System.out.println("connecting");
    if (!session.getRequestParameterMap().containsKey("token")) {
      System.out.println("no token");
      session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, CLOSE_REASON_NO_TOKEN));
      return;
    }
    userToken = session.getRequestParameterMap().get("token").stream().findFirst().orElse("");
    if (userToken.isEmpty() || !TokenManager.containsKey(userToken)) {
      System.out.println(userToken);
      session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, CLOSE_REASON_NO_TOKEN));
      return;
    }
    account = TokenManager.getAccount(userToken);
    this.session = session;
    System.out.println("connect");
  }

  @OnMessage
  public void onMessage(String message) throws Exception {
    message = RSAEncrypt.privateDecrypt(message, TokenManager.getPrivateKey(userToken));
    log.info(message);
    sendMessage(RSAEncrypt.privateEncrypt(message, TokenManager.getPrivateKey(userToken)));
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
