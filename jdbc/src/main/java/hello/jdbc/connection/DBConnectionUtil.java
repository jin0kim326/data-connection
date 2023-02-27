package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;


/**
 * JDBC는 java.sql.Connection 표준 커넥션 인터페이스를 정의
 * -> postgresql 데이터베이스 드라이버는 JDBC Connection 인터페이스를 구현한 org.postgresql.jdbc.PgConnection 구현체 제공
 */
@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setSchema("jdbc-connection");
            log.info("get connection={}, class={}", connection, connection.getClass());
            //출력결과 : get connection=org.postgresql.jdbc.PgConnection@609db546, class=class org.postgresql.jdbc.PgConnection
            //PgConnection : postgresql 데이터베이스가 제공하는 드라이버 커넥션

            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

    }
}
