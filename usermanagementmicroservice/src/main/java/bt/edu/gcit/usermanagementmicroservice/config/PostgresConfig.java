package bt.edu.gcit.usermanagementmicroservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class PostgresConfig {

    private static final Logger logger = LoggerFactory.getLogger(PostgresConfig.class);

    // 🔹 External PostgreSQL credentials
    private static final String DB_URL = "jdbc:postgresql://dpg-d6u2do9j16oc73fma5r0-a.singapore-postgres.render.com:5432/ams_db_qrv5";
    private static final String DB_USERNAME = "ams_admin";
    private static final String DB_PASSWORD = "swsKsQXsPpmXPRUzwIT7fffBCqbLUqVN";

    // Optional SSL: Render requires SSL
    private static final String DB_SSL_PARAMS = "?sslmode=require";

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        // Full URL with SSL
        config.setJdbcUrl(DB_URL + DB_SSL_PARAMS);
        config.setUsername(DB_USERNAME);
        config.setPassword(DB_PASSWORD);
        config.setDriverClassName("org.postgresql.Driver");

        // Connection pool settings (optional tuning)
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10000); // 10s
        config.setIdleTimeout(30000); // 30s
        config.setMaxLifetime(1800000); // 30 mins

        HikariDataSource dataSource = new HikariDataSource(config);

        // 🔹 Test the connection with error handling
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(5)) {
                logger.info("Successfully connected to PostgreSQL at Render!");
            } else {
                logger.error("Connection obtained but not valid!");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("SSL")) {
                logger.error("SSL handshake failed. Check your connection settings and SSL mode.", e);
            } else {
                logger.error("Failed to connect to PostgreSQL", e);
            }
            // Optionally: exit app if DB connection is critical
            // System.exit(1);
        }

        return dataSource;
    }
}