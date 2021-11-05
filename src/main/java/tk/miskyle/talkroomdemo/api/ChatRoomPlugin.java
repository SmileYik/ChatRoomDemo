package tk.miskyle.talkroomdemo.api;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.miskyle.talkroomdemo.core.setting.Settings;

import java.io.File;

/**
 * 继承这个类, 以制作ChatRoom的插件. <br />
 * 在打包成jar包后, jar包中根目录需含有一个"plugin.properties"文件,<br />
 * 其中包含3个必要字段"name", "main", "version", 分别代表
 * 插件名字, 继承此类的包名加类名(如test.Plugin), 及此插件版本.
 */
public abstract class ChatRoomPlugin {
  /**
   * 获取插件名.
   */
  @Getter
  private String name;
  /**
   * 获取该插件版本号.
   */
  @Getter
  private String version;
  /**
   * 获取该插件的作者.
   */
  @Getter
  private String author;
  @Getter
  private Logger logger;

  /**
   * 当插件从文件加载完后将会调用.
   */
  public abstract void onLoad();

  /**
   * 当插件被启用时将会被调用.
   */
  public abstract void onEnable();

  /**
   * 当插件被禁用时将会被调用.
   */
  public abstract void onDisable();

  protected File getDataFolder() {
    return new File(Settings.FOLDER_PLUGIN, name);
  }

  private void initialization(@NotNull String name,
                              @NotNull String version,
                              @NotNull String author) {
    if (!name.replaceAll("[a-zA-Z0-9 ]", "").isEmpty()) {
      throw new RuntimeException("This plugin name does not match the regular expression [a-zA-Z0-9 ]");
    }
    this.name = name;
    this.version = version;
    this.author = author;
    this.logger = LoggerFactory.getLogger(name);
  }
}
