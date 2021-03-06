package tk.miskyle.talkroomdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.miskyle.talkroomdemo.core.plugin.event.EventManager;
import tk.miskyle.talkroomdemo.core.plugin.PluginManager;

@SpringBootApplication
public class TalkRoomDemoApplication {
  public static void main(String[] args) {
    new EventManager();
    new PluginManager();
    SpringApplication.run(TalkRoomDemoApplication.class, args);
  }
}
