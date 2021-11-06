package tk.miskyle.talkroomdemo.api;

import tk.miskyle.talkroomdemo.api.event.EventManager;
import tk.miskyle.talkroomdemo.api.group.chatter.Chatter;
import tk.miskyle.talkroomdemo.core.token.TokenManager;

public class ChatRoomAPI {
  /**
   * 获取当前正在在线的用户.
   * @param id 用户的id.
   * @return 如果找到了该用户则返回该用户, 否则返回 null.
   */
  public static Chatter getOnlineChatterById(int id) {
    return TokenManager.getOnlineAccountById(id);
  }

  /**
   * 获取事件管理器.
   * @return 正在运行的事件管理器.
   */
  public static EventManager getEventManager() {
    return tk.miskyle.talkroomdemo.core.event.EventManager.getManager();
  }
}
