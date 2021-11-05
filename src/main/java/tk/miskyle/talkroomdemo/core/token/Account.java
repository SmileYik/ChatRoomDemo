package tk.miskyle.talkroomdemo.core.token;

import lombok.*;
import tk.miskyle.talkroomdemo.api.group.chatter.Chatter;
import tk.miskyle.talkroomdemo.api.group.chatter.Gender;
import tk.miskyle.talkroomdemo.core.setting.MysqlSetting;
import tk.miskyle.talkroomdemo.core.setting.Settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@RequiredArgsConstructor
public class Account implements Chatter {
  @Getter
  private final int id;
  @Getter
  private final String email;
  @Getter
  private final String nickname;
  @Getter
  private final Gender gender;
  private final HashMap<Object, Object> properties = new HashMap<>();

  @Override
  public boolean isOnline() {
    return TokenManager.containsValue(id);
  }

  @Override
  public boolean hasPermission(String permission) {
    return hasPermission(permission, true);
  }

  public boolean hasPermission(String permission, boolean checkRoot) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    try (Connection connection = Settings.getMysqlSetting().getConnection()) {
      ps = connection.prepareStatement(
              "SELECT * FROM " +
                      MysqlSetting.PERMISSION_TABLE +
                      " where user_id = ? and permission = ?;"
      );
      ps.setInt(1, id);
      ps.setString(2, permission);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      } else if (!checkRoot || !permission.contains(".")) {
        // if not check root permission or have not then return false.
        return false;
      }
      permission = permission.substring(0, permission.lastIndexOf(".")) + ".*";
      MysqlSetting.free(null, rs, ps);
      ps = connection.prepareStatement(
              "SELECT * FROM " +
                      MysqlSetting.PERMISSION_TABLE +
                      " where user_id = ? and permission = ?;"
      );
      ps.setInt(1, id);
      ps.setString(2, permission);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (SQLException throwable) {
      throwable.printStackTrace();
    } finally {
      MysqlSetting.free(null, rs, ps);
    }
    return false;
  }

  @Override
  public void addPermission(String permission) {
    PreparedStatement ps = null;
    try (Connection connection = Settings.getMysqlSetting().getConnection()) {
      ps = connection.prepareStatement(
              "UPDATE " + MysqlSetting.PERMISSION_TABLE +
                      " set user_id = ?, permission = ?;"
      );
      ps.setInt(1, id);
      ps.setString(2, permission);
      ps.executeUpdate();
    } catch (SQLException throwable) {
      throwable.printStackTrace();
    } finally {
      MysqlSetting.free(null, null, ps);
    }
  }

  @Override
  public void removePermission(String permission) {
    PreparedStatement ps = null;
    try (Connection connection = Settings.getMysqlSetting().getConnection()) {
      ps = connection.prepareStatement(
              "DELETE FROM " +
                      MysqlSetting.PERMISSION_TABLE +
                      " where user_id = ? and permission = ?;"
      );
      ps.setInt(1, id);
      ps.setString(2, permission);
      ps.executeUpdate();
    } catch (SQLException throwable) {
      throwable.printStackTrace();
    } finally {
      MysqlSetting.free(null, null, ps);
    }
  }

  @Override
  public boolean isBaned() {
    return hasPermission("system.baned", false);
  }

  @Override
  public boolean isOp() {
    return hasPermission("system.op", false);
  }

  @Override
  public void setOp(boolean flag) {
    addPermission("system.op");
  }

  @Override
  public boolean containsProperty(Object key) {
    return properties.containsKey(key);
  }

  @Override
  public Object getProperty(Object key) {
     return properties.get(key);
  }

  @Override
  public <T> T getProperty(Object key, Class<T> clazz) {
    return properties.containsKey(key) ?
            clazz.cast(properties.get(key)) : null;
  }

  @Override
  public void putProperty(Object key, Object value) {
    properties.put(key, value);
  }

  @Override
  public HashMap<Object, Object> getProperties() {
    return new HashMap<>(properties);
  }
}
