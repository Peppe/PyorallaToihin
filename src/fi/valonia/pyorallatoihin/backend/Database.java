package fi.valonia.pyorallatoihin.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.h2.tools.Server;

import fi.valonia.pyorallatoihin.Setup;

public class Database implements ServletContextListener {

    private static Connection conn;
    private static Server server;
    private static String url;
    private static String user;
    private static String password;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            org.h2.Driver.load();
            url = Setup.getSetting(Setup.url);
            user = Setup.getSetting(Setup.user);
            password = Setup.getSetting(Setup.password);
            // To access the database in server mode, use the database URL:
            // jdbc:h2:tcp://localhost/~/test
            org.h2.Driver.load();
            // Start the server if configured to do so
            server = Server.createTcpServer("-tcpAllowOthers");
            server.start();
            DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            if (conn != null) {
                Statement stat = conn.createStatement();
                stat.execute("SHUTDOWN");
                stat.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (server != null) {
            server.stop();
            server = null;
        }
    }
}
