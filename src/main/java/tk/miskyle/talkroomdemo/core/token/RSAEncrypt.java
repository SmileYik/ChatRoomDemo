package tk.miskyle.talkroomdemo.core.token;

import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.util.Arrays;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncrypt {
  public static final int ENCRYPT_LENGTH = 116;

  public static String[] getNewKey() {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(1024, new SecureRandom());
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      return new String[]{
              new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded())),
              new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()))
      };
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String x509ToPKCS8(String publicKey) throws NoSuchAlgorithmException,
                                                            InvalidKeySpecException,
                                                            IOException {
    // to pkcs1
    RSAPublicKey key = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(
            new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
    );
    SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(key.getEncoded());
    ASN1Primitive primitive = spkInfo.parsePublicKey();
    // to pkcs8
    org.bouncycastle.asn1.pkcs.RSAPublicKey rsaPublicKey =
            org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(primitive.getEncoded());
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey pkcs8 = kf.generatePublic(
            new RSAPublicKeySpec(rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent())
    );
    return new String(Base64.getEncoder().encode(pkcs8.getEncoded()));
  }

  public static String publicEncrypt(String input, String publicKey) throws NoSuchAlgorithmException,
                                                                      InvalidKeySpecException,
                                                                      NoSuchPaddingException,
                                                                      InvalidKeyException,
                                                                      IllegalBlockSizeException,
                                                                      BadPaddingException {
    byte[] decodeKey = Base64.getDecoder().decode(publicKey);
    RSAPublicKey key = (RSAPublicKey) KeyFactory.getInstance("RSA")
                                                .generatePublic(new X509EncodedKeySpec(decodeKey));
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return encrypt(input, cipher);
  }

  public static String publicDecrypt(String input, String publicKey) throws NoSuchAlgorithmException,
                                                                            InvalidKeySpecException,
                                                                            NoSuchPaddingException,
                                                                            InvalidKeyException,
                                                                            IllegalBlockSizeException,
                                                                            BadPaddingException {
    byte[] decodeKey = Base64.getDecoder().decode(publicKey);
    RSAPublicKey key = (RSAPublicKey) KeyFactory.getInstance("RSA")
                                                .generatePublic(new X509EncodedKeySpec(decodeKey));
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, key);
    return decrypt(input, cipher);
  }

  public static String privateDecrypt(String input, String privateKey) throws NoSuchAlgorithmException,
                                                                              InvalidKeySpecException,
                                                                              NoSuchPaddingException,
                                                                              InvalidKeyException,
                                                                              IllegalBlockSizeException,
                                                                              BadPaddingException {
    byte[] decodeKey = Base64.getDecoder().decode(privateKey);
    RSAPrivateKey key = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                                                  .generatePrivate(new PKCS8EncodedKeySpec(decodeKey));
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, key);
    return decrypt(input, cipher);
  }

  public static String privateEncrypt(String input, String privateKey) throws NoSuchAlgorithmException,
                                                                              NoSuchPaddingException,
                                                                              IllegalBlockSizeException,
                                                                              BadPaddingException,
                                                                              InvalidKeyException,
                                                                              InvalidKeySpecException {
    byte[] decodeKey = Base64.getDecoder().decode(privateKey);
    RSAPrivateKey key = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                                                  .generatePrivate(new PKCS8EncodedKeySpec(decodeKey));
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return encrypt(input, cipher);
  }

  @NotNull
  private static String decrypt(String input, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
    String[] lines = input.split("\n");
    byte[][] bytes = new byte[lines.length][];
    for (int i = 0; i < bytes.length; ++i) {
      bytes[i] = cipher.doFinal(Base64.getDecoder().decode(lines[i].getBytes(StandardCharsets.UTF_8)));
    }
    return new String(Arrays.concatenate(bytes));
  }

  private static String encrypt(String input, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
    byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
    int length = 0;
    StringBuilder sb = new StringBuilder();
    while (length < bytes.length) {
      int size = length + ENCRYPT_LENGTH > bytes.length ? bytes.length - length : ENCRYPT_LENGTH;
      sb.append(new String(Base64.getEncoder().encode(cipher.doFinal(bytes, length, size)))).append('\n');
      length += ENCRYPT_LENGTH;
    }
    return sb.toString();
  }
}
