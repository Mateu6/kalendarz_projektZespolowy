package studia.projektzespolowy.kalendarz;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    /**Method for establishing a database connection.
     * Configures the database name, user, and password.
     * Constructs the URL for the database connection.
     * Attempts to establish a connection using the MySQL driver.
     * Returns the database connection object.*/
    public Connection getConnection(){
        String databaseName = "login-user";
        String databaseUser = "root";
        String databasePassword = "root";
        String url = "jdbc: mysql://localhost/ " + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword );

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}

