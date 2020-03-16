/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student1
 */
public class ConnectionFactory {

    private String driver;
    private String url;
    private String user;
    private String password;

    private Connection connection;

    private static ConnectionFactory instance;

    private ConnectionFactory() throws Exception {

        try {
            driver = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://localhost:3306/prosoftjul19";
            user = "root";
            password = "";

            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            System.out.println("Uspesno uspostavljena konekcija sa bazom!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Greska prilikom uspostavljanja konekcije sa bazom!\n" + ex.getMessage());
        }

    }

    public static ConnectionFactory getInstance() throws Exception {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
