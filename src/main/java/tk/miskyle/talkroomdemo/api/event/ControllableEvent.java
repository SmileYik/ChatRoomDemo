package tk.miskyle.talkroomdemo.api.event;

import lombok.Getter;
import lombok.Setter;

public class ControllableEvent extends Event {
  @Getter
  @Setter
  private boolean canceled = false;
}
