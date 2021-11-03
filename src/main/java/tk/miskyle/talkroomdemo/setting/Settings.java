package tk.miskyle.talkroomdemo.setting;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Settings {
  @Getter
  private static MysqlSetting mysqlSetting;

  static {
    File file = new File("./setting.json");
    if (file.exists()) {
      try {
        JSONObject settings = JSONObject.parseObject(new FileInputStream(file), JSONObject.class);
        mysqlSetting = settings.getObject("mysql", MysqlSetting.class);
        mysqlSetting.createTables();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
