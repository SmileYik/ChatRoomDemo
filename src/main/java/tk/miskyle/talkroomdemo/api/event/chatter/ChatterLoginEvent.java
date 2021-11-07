package tk.miskyle.talkroomdemo.api.event.chatter;

import lombok.Getter;
import tk.miskyle.talkroomdemo.api.group.chatter.Chatter;

public class ChatterLoginEvent extends ChatterEvent {
  @Getter
  private String publicKey;
  @Getter
  private String privateKey;
  public ChatterLoginEvent(Chatter chatter, String publicKey, String privateKey) {
    super(chatter);
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }
}
