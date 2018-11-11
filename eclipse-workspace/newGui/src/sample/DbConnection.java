package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {

    protected static String DATABASE_NAME = "newUni";
    protected static String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";

    public Connection con;
    public Statement statement;

    public DbConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(CONNECTION_STRING + DATABASE_NAME, "root", "");
            statement = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
