package studia.projektzespolowy.kalendarz;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

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

