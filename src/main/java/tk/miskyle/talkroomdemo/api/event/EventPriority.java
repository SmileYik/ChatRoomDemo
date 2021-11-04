package tk.miskyle.talkroomdemo.api.event;

import lombok.Getter;

/**
 * 事件的优先级, 优先级越高就越先执行.
 */
public enum EventPriority {
  Highest((short) 12),
  Higher((short) 10),
  High((short) 8),
  Normal((short) 6),
  Low((short) 4),
  Lower((short) 2),
  Lowest((short) 1);


  /**
   * 返回当前的优先级.
   */
  @Getter
  private final short priority;
  EventPriority(short priority) {
    this.priority = priority;
  }
}
