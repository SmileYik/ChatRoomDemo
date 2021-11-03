package tk.miskyle.talkroomdemo.token;

import lombok.Synchronized;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class TokenManager {
  private static final HashMap<String, Account> accounts = new HashMap<>();
  private static final HashMap<String, String> keys = new HashMap<>();

  @Synchronized
  public static String put(Account account) {
    String token = randomToken();
    accounts.put(token, account);
    return token;
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

  public static boolean containsValue(int id) {
    return new HashSet<>(accounts.values()).stream().anyMatch(account -> account.getId() == id);
  }

  private static String randomToken() {
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
    TokenManager.keys.put(token, keys[1]);
    return token;
  }
}
