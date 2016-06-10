package model.sql;

import model.entities.Drug;
import model.entities.DrugInStore;
import model.entities.Drugstore;
import model.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String checkLoginQuery = "SELECT user_id, first_name, last_name, login, pass FROM users WHERE login='" + user.login + "'";
        rs = st.executeQuery(checkLoginQuery);
        if (rs.next()) {
            throw new RegistrationException();
        }

        String query = "INSERT INTO USERS (first_name, last_name, login, pass) VALUES " + user + ";";
        System.out.println(query);
        st.execute(query);
    }

    public User authorize(String login, String password) {
        try {
            String sql = "SELECT * FROM USERS WHERE login='" + login + "' AND pass='" + password + "'";
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

    public List<Drugstore> findDrugstoreByName(String drugstoreName) throws Exception{
        String findDrugQuery = "SELECT drugstore_id, name FROM drugstore where name='" + drugstoreName + "'";
        rs = st.executeQuery(findDrugQuery);
        List<Drugstore> result = new ArrayList<>();
        while(rs.next()){
            String name = rs.getString("name");
            int drugstore_id = rs.getInt("drugstore_id");
            result.add(new Drugstore(drugstore_id, name));

        }
        return result;
    }

    public List<Drug> findDrugByName(String drugName) throws Exception{
        String findDrugQuery = "SELECT drug_id, name FROM drug where name='" + drugName + "'";
        rs = st.executeQuery(findDrugQuery);
        List<Drug> result = new ArrayList<>();
        while(rs.next()){
            String name = rs.getString("name");
            int drugstore_id = rs.getInt("drug_id");
            result.add(new Drug(drugstore_id, name));

        }
        return result;
    }

    public List<DrugInStore> findDrugPrices(String drugName) throws Exception {
        String findDrugPricesQuery = "SELECT ds.name, dis.price\n" +
                "FROM DRUG_IN_STORE dis \n" +
                "\t\tjoin DRUGSTORE ds\n" +
                "\t\t\ton dis.drugstore_id = ds.drugstore_id\n" +
                "\t\tjoin DRUG d\n" +
                "\t\t\ton dis.drug_id = d.drug_id\n" +
                "WHERE d.name = '" + drugName + "'\n" +
                ";";

        rs = st.executeQuery(findDrugPricesQuery);

        List<DrugInStore> result = new ArrayList<>();
        while(rs.next()) {
            String store = rs.getString("name");
            Double price = rs.getDouble("price");
            result.add(new DrugInStore(store, price.toString()));
        }

        return result;
    }

    public void addDrugInterest(String userId, String drugId) throws Exception {
        String query = "INSERT INTO INTERESTED_DRUG (user_id, drug_id) VALUES (" + userId + ", " + drugId + ");";
        st.execute(query);
    }

    public void deleteFromInterest(String userId, String drugId) throws Exception {
        String query = "delete from interested_drug where drug_id=" + drugId + " and user_id=" + userId + ";";
        st.execute(query);
    }

    public String getUserIdByLogin(String login) throws Exception {
        String query = "select user_id from users where login='" + login + "';";
        rs = st.executeQuery(query);
        rs.next();
        int id = rs.getInt("user_id");
        return Integer.toString(id);
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
