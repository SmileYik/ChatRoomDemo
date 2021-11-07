package tk.miskyle.talkroomdemo.api.event.chatter;

import lombok.Getter;
import tk.miskyle.talkroomdemo.api.event.ControllableEvent;
import tk.miskyle.talkroomdemo.api.group.chatter.Chatter;

public class ChatterEvent extends ControllableEvent {
  @Getter
  protected final Chatter chatter;

  protected ChatterEvent(Chatter chatter) {
    this.chatter = chatter;
  }
}
