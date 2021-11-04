package tk.miskyle.talkroomdemo.api.event.annotation;

import tk.miskyle.talkroomdemo.api.event.EventPriority;

import java.lang.annotation.*;

/**
 * 用来标记事件的方法.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandle {
  /**
   * 事件的优先级, 优先级越高越先执行.
   * @return 当前事件方法调用的优先级.
   */
  EventPriority priority() default EventPriority.Normal;

  /**
   * 是否在事件被取消的时候不执行此事件方法.
   * @return 是否在事件被取消的时候不执行此事件方法.
   */
  boolean ignoreCanceled() default false;
}

