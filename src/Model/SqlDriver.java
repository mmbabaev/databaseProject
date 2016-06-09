package Model;

import Model.Entities.User;

import java.sql.*;

public class SqlDriver {
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;


    String url = "jdbc:postgresql://localhost:5432/test";
    String userSql = "postgres";
    String password = "1234";

    private static SqlDriver driver = null;

    public static SqlDriver getInstance() {
        try {
            if (driver == null) {
                driver = new SqlDriver();
            }

            return driver;
        }
        catch (Exception ex) {
            System.out.println("Ошибка\n" + ex.getLocalizedMessage());
        }

        return null;
    }

    private SqlDriver() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");

        con = DriverManager.getConnection(url, this.userSql, password);
        st = con.createStatement();
    }

    public void registerUser(User user) throws Exception {
        String query = "INSERT INTO users (first_name, last_name, login, password) values " + user;
        st.execute(query);
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
