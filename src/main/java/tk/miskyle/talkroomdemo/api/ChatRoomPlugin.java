package tk.miskyle.talkroomdemo.api;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tk.miskyle.talkroomdemo.core.setting.Settings;

import java.io.File;

public abstract class ChatRoomPlugin {
  @Getter(AccessLevel.PROTECTED)
  private String name;
  @Getter(AccessLevel.PROTECTED)
  private String version;
  @Getter(AccessLevel.PROTECTED)
  private String author;

  protected abstract void onLoad();
  protected abstract void onEnable();

  protected File getDataFolder() {
    return new File(Settings.FOLDER_PLUGIN, name);
  }

  private void initialization(@NotNull String name,
                              @NotNull String version,
                              @NotNull String author) {
    if (!name.matches("[a-zA-Z0-9 ]")) {
      throw new RuntimeException("This plugin name does not match the regular expression [a-zA-Z0-9 ]");
    }
    this.name = name;
    this.version = version;
    this.author = author;
  }
}
