package tk.miskyle.talkroomdemo.core.permission;

import tk.miskyle.talkroomdemo.core.setting.MysqlSetting;
import tk.miskyle.talkroomdemo.core.setting.Settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class Permission {
  public static final String ALL_PERMISSION = "*";

  public static boolean hasPermission(int userid, String permission) {
    boolean flag = false;
    try (Connection connection = Settings.getMysqlSetting().getConnection();
         PreparedStatement ps = connection.prepareStatement("SELECT `permission` FROM " + MysqlSetting.PERMISSION_TABLE + " WHERE `user_id` = ?")) {
      ps.setInt(1, userid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        List<String> list = Arrays.asList(rs.getString("permission").split(","));
        flag = list.contains(ALL_PERMISSION) || list.contains(permission);
      }
      MysqlSetting.free(null, rs, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return flag;
  }

  public static void addPermission(int userid, String permission) {
    List<String> list = null;
    String permissions = "";
    try (Connection connection = Settings.getMysqlSetting().getConnection();
         PreparedStatement ps = connection.prepareStatement("SELECT `permission` FROM " + MysqlSetting.PERMISSION_TABLE + " WHERE `user_id` = ?")) {
      ps.setInt(1, userid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        permissions = rs.getString("permission");
        list = Arrays.asList(permissions.split(","));
      }
      MysqlSetting.free(null, rs, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (list != null && (list.contains(ALL_PERMISSION) || list.contains(permission))) {
      return;
    }
    permissions = permission + "," + permission;
    try (Connection connection = Settings.getMysqlSetting().getConnection();
         PreparedStatement ps = connection.prepareStatement("UPDATE " + MysqlSetting.PERMISSION_TABLE + " SET `permission` = ? WHERE `user_id` = ?")) {
      ps.setString(1, permissions);
      ps.setInt(2, userid);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void removePermission(int userid, String permission) {
    List<String> list = null;
    try (Connection connection = Settings.getMysqlSetting().getConnection();
         PreparedStatement ps = connection.prepareStatement("SELECT `permission` FROM " + MysqlSetting.PERMISSION_TABLE + " WHERE `user_id` = ?")) {
      ps.setInt(1, userid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        list = Arrays.asList(rs.getString("permission").split(","));
      }
      MysqlSetting.free(null, rs, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    StringBuilder sb = new StringBuilder();
    if (list != null) {
      list.remove(permission);
      for (String str : list) {
        sb.append(str).append(',');
      }
    }

    try (Connection connection = Settings.getMysqlSetting().getConnection();
         PreparedStatement ps = connection.prepareStatement("UPDATE " + MysqlSetting.PERMISSION_TABLE + " SET `permission` = ? WHERE `user_id` = ?")) {
      ps.setString(1, sb.toString());
      ps.setInt(2, userid);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
