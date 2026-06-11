package by.psu.first;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Properties PROPERTIES = new Properties();


    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream inputStream = ConnectionManager.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("Файл db.properties не найден в resources");
            }
            PROPERTIES.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении db.properties", e);
        }
    }


    public static Connection open() {
        try {

            String envUrl = System.getenv("SPRING_DATASOURCE_URL");


            String finalUrl = (envUrl != null) ? envUrl : PROPERTIES.getProperty("db.url");


            String envUser = System.getenv("SPRING_DATASOURCE_USERNAME");
            String finalUser = (envUser != null) ? envUser : PROPERTIES.getProperty("db.username");

            String envPassword = System.getenv("SPRING_DATASOURCE_PASSWORD");
            String finalPassword = (envPassword != null) ? envPassword : PROPERTIES.getProperty("db.password");


            return DriverManager.getConnection(finalUrl, finalUser, finalPassword);

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось установить соединение с БД", e);
        }
    }

}