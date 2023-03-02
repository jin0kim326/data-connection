package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {
    MemberRepositoryV1 repository;

    /**
     * DI
     * DriverManagerDataSource -> HikariDataSource로 변경해도 MemberRepositoryV1 코드는 전혀 변경하지 않아도됨
     * MemberRepositoryV1는 DataSource 인터페이스에만 의존!! (DI + OCP)
     */
    @BeforeEach
    void beforeEach() {
        //기본 DriverManager - 항상 새로운 커넥션 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setSchema(SCHEMA);
        dataSource.setPassword(PASSWORD);

        repository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("jinyoung15", 100000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        log.info("member == findMember {}", member == findMember );           // false : 다른 인스턴스 이기 때문에
        log.info("member equals findMember {}", member.equals(findMember));     //true : 클래스의 내부 필드가 같은 값을 가지고 있기때문에 @EqualsAndHashcode
        assertThat(findMember).isEqualTo(member);
        
        //update: money: 10000 -> 20000
        repository.update(member.getMemberId(), 200000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(200000);

        //delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
//        Member byId = repository.findById(member.getMemberId());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}