package model.sql;

import model.entities.*;

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

        String query = "INSERT INTO USERS (first_name, last_name, login, pass) VALUES " + user + " returning user_id;";
        rs = st.executeQuery(query);
        rs.next();
        user.id = rs.getInt("user_id");
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


    public List<Drugstore> findDrugstoreByName(String drugstoreName) throws Exception {
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

    /*
     * 1. Запрос на цены на лекарство по имени
     */
    public List<Drug> findDrugByName(String drugName) throws Exception{
        String findDrugQuery = String.format("SELECT drug_id, name FROM drug where name='%s'", drugName);
        rs = st.executeQuery(findDrugQuery);
        List<Drug> result = new ArrayList<>();
        while(rs.next()) {
            String name = rs.getString("name");
            int drugstore_id = rs.getInt("drug_id");
            result.add(new Drug(drugstore_id, name));
        }
        return result;
    }

    /*
    * 4. Запрос на интересующие пользователя лекарства.
    */
    public List<Drug> findFavouriteDrugs(User user) throws Exception {
        String query = "SELECT * FROM drug WHERE drug_id IN " +
                "(SELECT drug_id FROM interested_drug WHERE user_id=" + user.getStringId() + ");";

        System.out.println(query);
        rs = st.executeQuery(query);

        List<Drug> result = new ArrayList<>();
        while(rs.next()) {
            String name = rs.getString("name");
            int drugstore_id = rs.getInt("drug_id");
            result.add(new Drug(drugstore_id, name));
        }
        return result;
    }

    /*
    * 3. Запрос на изменения цен на лекарство
    */
    public List<PriceChange> findPriceChanges(Drug drug) throws Exception {
        String sql = "SELECT * FROM price_change WHERE drug_id=%s";
        String query = String.format(sql, drug.getId());
        rs = st.executeQuery(query);

        List<PriceChange> result = new ArrayList<>();
        while(rs.next()) {
            String time = rs.getString("change_time");
            String price = rs.getString("price");
            result.add(new PriceChange(time, price));
        }

        return result;
    }

    public boolean isDrugInterestingForUser(String drugId, String userId) throws Exception {
        String query = String.format("select * from interested_drug where drug_id=%s and user_id=%s;", drugId, userId);
        rs = st.executeQuery(query);
        return rs.next();
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

    /////////////////// INSERTS ////////////////////////////////

    public void addDrug(String name) throws SQLException {

        String query = "";
        query += "INSERT INTO DRUG (name)";
        query += "VALUES ('" + name + "')";
        query += ";";

        st.execute(query);

        return;
    }

    public void addUser(String first_name, String second_name, String login, String password) throws SQLException {

        String query = "";
        query += "INSERT INTO USERS (first_name, last_name, login, pass)";
        query += "VALUES ('" + first_name + "', '" + second_name + "', '" + login + "', '" + password + "')";
        query += ";";

        st.execute(query);

        return;
    }

    public void addDrugstore(String name) throws SQLException {

        String query = "";
        query += "INSERT INTO DRUGSTORE (name)";
        query += "VALUES ('" + name + "')";
        query += ";";

        st.execute(query);

        return;
    }

    public void addInterestedDrug(int drug_id, int user_id) throws SQLException {

        String query = "";
        query += "INSERT INTO INTERESTED_DRUG (drug_id, user_id)";
        query += "VALUES ('" + drug_id + "', '" + user_id + "')";
        query += ";";

        st.execute(query);

        return;
    }

    public void addPriceChange(int drug_id, String timestamp, double price/*, int drugstore_id*/) throws SQLException {

        String query = "";
        query += "INSERT INTO PRICE_CHANGE (drug_id, change_time, price)"; //, drugstore_id)";
        query += "VALUES ('" + drug_id + "', '" + timestamp + "', '" + price + /*"', '" + drugstore_id +*/ "')";
        query += ";";

        st.execute(query);

        return;
    }

    public void addDrugInStore(int drug_id, double price, int drugstore_id) throws SQLException {

        String query = "";
        query += "INSERT INTO DRUG_IN_STORE (drug_id, price, drugstore_id)";
        query += "VALUES ('" + drug_id + "', '" + price + "', '" + drugstore_id + "')";
        query += ";";

        st.execute(query);

        return;
    }

    /////////////////// CREATES ////////////////////////////////

    public void createDrug() throws SQLException {

        String query = "";
        query += "DROP TABLE IF EXISTS DRUG CASCADE;";

        query += "CREATE TABLE IF NOT EXISTS DRUG";
        query += "(";
        query += "	drug_id SERIAL PRIMARY KEY,";
        query += "	name VARCHAR (50)";
        query += ");";

        st.execute(query);

        return;
    }

    public void createUsers() throws SQLException {

        String query = "";
        query += "DROP TABLE IF EXISTS USERS CASCADE;";

        query += "CREATE TABLE IF NOT EXISTS USERS";
        query += "(";
        query += "	user_id SERIAL PRIMARY KEY,";
        query += "	first_name VARCHAR (50),";
        query += "	last_name VARCHAR (50),";
        query += "	login VARCHAR (50),";
        query += "	pass VARCHAR (50)";
        query += ");";

        st.execute(query);

        return;
    }

    public void createDrugstore() throws SQLException {

        String query = "";
        query += "DROP TABLE IF EXISTS DRUGSTORE CASCADE;";

        query += "CREATE TABLE IF NOT EXISTS DRUGSTORE";
        query += "(";
        query += "	drugstore_id SERIAL PRIMARY KEY,";
        query += "	name VARCHAR (100)";
        query += ");";

        st.execute(query);

        return;
    }

    public void createInterestedDrug() throws SQLException {

        String query = "";
        query += "DROP TABLE IF EXISTS INTeRESTED_DRUG CASCADE;";

        query += "CREATE TABLE IF NOT EXISTS INTeRESTED_DRUG";
        query += "(";
        query += "	interested_drug_id SERIAL PRIMARY KEY,";
        query += "	drug_id INTEGER references drug(drug_id),";
        query += "	user_id INTEGER references users(user_id)";
        query += ");";

        st.execute(query);

        return;
    }

    public void createPriceChange() throws SQLException {

        String query = "";
        query += "DROP TABLE IF EXISTS PRICE_CHANGE CASCADE;";

        query += "CREATE TABLE IF NOT EXISTS PRICE_CHANGE";
        query += "(";
        query += "	price_change_id SERIAL PRIMARY KEY,";
        query += "	drug_id INTEGER references drug(drug_id),";
        query += "	change_time TIMESTAMP,";
        query += "	price REAL";
        //query += "	,drugstore_id INTEGER references drugstore(drugstore_id)";
        query += ");";

        st.execute(query);

        return;
    }

    public void createDrugInStore() throws SQLException {

        String query = "";
        query += "DROP TABLE IF EXISTS DRUG_IN_STORE CASCADE;";

        query += "CREATE TABLE IF NOT EXISTS DRUG_IN_STORE";
        query += "(";
        query += "	drug_in_store_id SERIAL PRIMARY KEY,";
        query += "	drug_id INTEGER references drug(drug_id),";
        query += "	price REAL,";
        query += "	drugstore_id INTEGER references drugstore(drugstore_id)";
        query += ");";

        st.execute(query);

        return;
    }

    public void dropAll() throws SQLException {

        String query = "";

        query = "DROP TABLE IF EXISTS DRUG CASCADE;";
        st.execute(query);

        query = "DROP TABLE IF EXISTS USERS CASCADE;";
        st.execute(query);

        query = "DROP TABLE IF EXISTS DRUGSTORE CASCADE;";
        st.execute(query);

        query = "DROP TABLE IF EXISTS INTERESTED_DRUG CASCADE;";
        st.execute(query);

        query = "DROP TABLE IF EXISTS PRICE_CHANGE CASCADE;";
        st.execute(query);

        query = "DROP TABLE IF EXISTS DRUG_IN_STORE CASCADE;";
        st.execute(query);

        return;

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
