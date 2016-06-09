package Model.sql;

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
        String checkLoginQuery = "SELECT * FROM users WHERE login='" + user.login + "'";

        rs = st.executeQuery(checkLoginQuery);
        if (rs.next()) {
            throw new RegistrationException();
        }

        String query = "INSERT INTO users (first_name, last_name, login, pass) values " + user;
        st.execute(query);
    }

    public User authorize(String login, String password) {
        try {
            String sql = "SELECT * FROM users WHERE login='" + login + "' AND pass='" + password + "'";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                User user = new User(firstName, lastName, login, password);
                user.id = id;
                return user;
            }

            return null;
        }
        catch (Exception ex) {
            return null;
        }

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
