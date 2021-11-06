package tk.miskyle.talkroomdemo.api.event;

import tk.miskyle.talkroomdemo.api.ChatRoomPlugin;

public interface EventManager {
  /**
   * 注册事件.
   * @param plugin 要注册事件的插件.
   * @param listener 要注册的事件.
   */
  void registerEvents(ChatRoomPlugin plugin, Listener listener);

  /**
   * 取消注册事件.
   * @param listener 要取消的事件.
   */
  void unregisterEvents(Listener listener);

  /**
   * 取消注册某个插件的所有事件.
   * @param plugin 要取消所有事件的插件.
   */
  void unregisterAllEvents(ChatRoomPlugin plugin);

  /**
   * 广播事件.
   * @param event 要广播的事件实例.
   */
  void callEvent(Event event);
}
