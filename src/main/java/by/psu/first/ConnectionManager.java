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
            return DriverManager.getConnection(
                    PROPERTIES.getProperty("db.url"),
                    PROPERTIES.getProperty("db.username"),
                    PROPERTIES.getProperty("db.password")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось установить соединение с БД", e);
        }
    }
}