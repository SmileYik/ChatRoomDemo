package tk.miskyle.talkroomdemo.core.setting;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import tk.miskyle.talkroomdemo.core.group.GroupManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Settings {
  public static final File FOLDER_ROOT;
  public static final File FOLDER_PLUGIN;
  public static final File FOLDER_LOG;

  public static final File FILE_SETTING;

  @Getter
  private static MysqlSetting mysqlSetting;

  // initialization path
  static {
    FOLDER_ROOT   = new File("./");
    FOLDER_PLUGIN = new File(FOLDER_ROOT, "plugins");
    FOLDER_LOG    = new File(FOLDER_ROOT, "log");

    FILE_SETTING  = new File(FOLDER_ROOT, "setting.json");

    createFolder(FOLDER_ROOT);
    createFolder(FOLDER_PLUGIN);
    createFolder(FOLDER_LOG);

    loadSetting();
  }

  private static void createFolder(File folder) {
    if (!folder.exists() && !folder.mkdirs()) {
      throw new RuntimeException("Cannot create directories in this path: " + folder);
    }
  }

  private static void loadSetting() {
    if (FILE_SETTING.exists()) {
      try {
        JSONObject settings = JSONObject.parseObject(new FileInputStream(FILE_SETTING), JSONObject.class);
        mysqlSetting = settings.getObject("mysql", MysqlSetting.class);
        mysqlSetting.createTables();
        new GroupManager(settings.getJSONArray("groups"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
