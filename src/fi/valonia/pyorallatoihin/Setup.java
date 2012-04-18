package fi.valonia.pyorallatoihin;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

public class Setup {
    public static final String url = "url";
    public static final String user = "user";
    public static final String password = "password";
    public static final Properties properties = new Properties();

    static {
        try {
            InputStream in = Setup.class.getClassLoader().getResourceAsStream(
                    "Setup.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getSetting(String key) {
        try {
            String setting = properties.getProperty(key);
            System.out.println("returning key: " + key + ", value: " + setting);
            return setting;
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
