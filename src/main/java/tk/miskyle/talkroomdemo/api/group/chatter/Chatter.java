package tk.miskyle.talkroomdemo.api.group.chatter;

import java.util.HashMap;

public interface Chatter {
  /**
   * 获取用户id.
   * @return 用户id号.
   */
  int getId();

  /**
   * 获取用户邮箱.
   * @return 用户的邮箱.
   */
  String getEmail();

  /**
   * 获取用户的昵称.
   * @return 用户的昵称.
   */
  String getNickname();

  /**
   * 获取用户的性别.
   * @return 用户的性别.
   */
  Gender getGender();

  /**
   * 判断用户是否在线.
   * @return 如果用户在线则返回true, 其他情况返回false.
   */
  boolean isOnline();

  /**
   * 检查用户是否拥有某种权限.
   * @param permission 需要检查的权限.
   * @return 如果拥有该权限则返回true. 否之则返回false.
   */
  boolean hasPermission(String permission);

  /**
   * 为此用户增加权限.
   * @param permission 要增加的权限名.
   */
  void addPermission(String permission);

  /**
   * 判断用户是否被封禁.
   * @return 如果被封禁返回true, 否则返回false.
   */
  boolean isBaned();

  /**
   * 判断该用户是否为op.
   * @return 如果是op则返回true, 否则返回false.
   */
  boolean isOp();

  /**
   * 设置当前用户是否为op.
   * @param flag 是否为op.
   */
  void setOp(boolean flag);

  /**
   * 获取当前用户设置中是否包含键key.
   * @param key 键.
   * @return 如果含有指定键则返回true.
   */
  boolean containsProperty(Object key);

  /**
   * 获取用户设置.
   * @param key 键.
   * @return 键对应的用户设置, 如果键不存在则返回null.
   */
  Object getProperty(Object key);

  /**
   * 获取用户设置.
   * @param key 键.
   * @param clazz 要返回的类型.
   * @param <T> 要返回的类型.
   * @return 返回对应类型的用户设置, 如果键不存在则返回null.
   */
  <T> T getProperty(Object key, Class<T> clazz);

  /**
   * 根据键, 来添加(覆盖)一个用户配置.
   * @param key 键.
   * @param value 值.
   */
  void putProperty(Object key, Object value);

  /**
   * 获取当前用户配置副本.
   * @return 当前用户配置的副本.
   */
  HashMap<Object, Object> getProperties();
}
