import model.entities.User;
import model.sql.SqlDriver;

class TestDriver {
    public static void main(String [] args) {
        SqlDriver driver = null;

        try {
            driver = SqlDriver.getInstance();
            User user = new User("m", "borisov", "borisov", "pass");

            driver.registerUser(user);

            user = new User("a", "borisov", "borisov", "pass");

            driver.registerUser(user);
        }
        catch (Exception ex) {
            System.out.println("Ошибка");
            System.out.println(ex.getMessage());
            System.out.println(ex.getLocalizedMessage());
            driver.close();
        }
    }
}