package tk.miskyle.talkroomdemo.core.token;

import lombok.Synchronized;
import tk.miskyle.talkroomdemo.api.event.chatter.ChatterLoginEvent;
import tk.miskyle.talkroomdemo.core.plugin.event.EventManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TokenManager {
  private static final HashMap<String, Account> accounts = new HashMap<>();
  private static final HashMap<String, String> keys = new HashMap<>();

  @Synchronized
  public static String put(Account account) {
    String[] keys = randomToken();
    ChatterLoginEvent event = new ChatterLoginEvent(account, keys[0], keys[1]);
    EventManager.getManager().callEvent(event);
    if (!event.isCanceled()) {
      accounts.put(keys[0], account);
      TokenManager.keys.put(keys[0], keys[1]);
      return keys[0];
    }
    return null;
  }

  @Synchronized
  public static Account remove(String token) {
    keys.remove(token);
    return accounts.remove(token);
  }

  public static Account getAccount(String token) {
    return accounts.get(token);
  }

  public static String getPrivateKey(String token) {
    return keys.get(token);
  }

  public static boolean containsKey(String token) {
    return accounts.containsKey(token);
  }

  public static boolean containsValue(int id) {
    return new HashSet<>(accounts.values()).stream().anyMatch(account -> account.getId() == id);
  }

  public static Account getOnlineAccountById(int id) {
    return new HashSet<>(accounts.values())
            .stream()
            .filter(account -> account.getId() == id)
            .findFirst()
            .orElse(null);
  }

  public static String[] randomToken() {
    String[] keys;
    String token = null;
    do {
      keys = RSAEncrypt.getNewKey();
      try {
        token = RSAEncrypt.x509ToPKCS8(keys[0]);
      } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
        e.printStackTrace();
      }
    } while (token == null || accounts.containsKey(token));
    return new String[]{token, keys[1]};
  }
}
