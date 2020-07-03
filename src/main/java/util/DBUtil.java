package util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/cash?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static volatile DataSource DATASOURCE;

    //数据库连接池
    private static DataSource getDATASOURCE() {
        if (DATASOURCE == null) {
            synchronized (DBUtil.class) {
                if (DATASOURCE == null) {
                    DATASOURCE = new MysqlDataSource();
                    ((MysqlDataSource)DATASOURCE).setUrl(URL);
                    ((MysqlDataSource)DATASOURCE).setUser(USERNAME);
                    ((MysqlDataSource)DATASOURCE).setPassword(PASSWORD);

                }
            }
        }
        return DATASOURCE;
    }

    //获取 Connection 链接
    public static Connection getConnection(boolean autocommit) throws SQLException {
        Connection connection = getDATASOURCE().getConnection();
        connection.setAutoCommit(autocommit);
        return connection;
    }

    public static void close(Connection con, PreparedStatement ps, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    }

}
