package school.hei.exam.agriculturalfederation.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DataSource {
    private final String DB_URL = System.getenv("DB_URL");
    private final String DB_USER = System.getenv("DB_USER");
    private final String DB_PWD = System.getenv("DB_PWD");

    @Bean
    public Connection getConnection(){
        try{
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
