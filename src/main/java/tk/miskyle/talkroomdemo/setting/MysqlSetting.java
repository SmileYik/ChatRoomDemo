package tk.miskyle.talkroomdemo.setting;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.sql.*;

@AllArgsConstructor
public class MysqlSetting {
  public static final String PERMISSION_TABLE = "TalkRoom_Permission";

  private final String url;
  private final String user;
  private final String password;

  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  @SneakyThrows
  public Connection getConnection() {
    return DriverManager.getConnection(url,user,password);
  }

  public void createTables() {
    String temple = "CREATE TABLE IF NOT EXISTS %s ( %s ) DEFAULT CHARSET=UTF8";
    try (Connection connection = getConnection()) {
      // permission_table;
      String sql = String.format(temple, PERMISSION_TABLE,
              "user_id int not null primary key, permission longtext not null");
      connection.prepareStatement(sql).executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void free(Connection connection, ResultSet rs, PreparedStatement ps) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
