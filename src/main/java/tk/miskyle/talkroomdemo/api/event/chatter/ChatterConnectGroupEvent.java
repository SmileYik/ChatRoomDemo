package tk.miskyle.talkroomdemo.api.event.chatter;

import tk.miskyle.talkroomdemo.api.group.chatter.Chatter;

public class ChatterConnectGroupEvent extends ChatterEvent {

  protected ChatterConnectGroupEvent(Chatter chatter) {
    super(chatter);
  }
}
