package com.example.swithme;
import com.example.swithme.entity.User;
import com.example.swithme.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
@SpringBootTest
public class SaveTest {

    //    실제 데이터베이스에 데이터를 저장하는 JPA 리포지토리입니다.
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    PostRepository postRepository;
//    @Autowired
//    CategoryRepository categoryRepository;
//    @Autowired
//    PasswordEncoder passwordEncoder;
//    @Autowired
//    ChatRoomRepository chatRoomRepository;
//    @Autowired
//    ChatGroupRepository chatGroupRepository;
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Test
//    void saveTest() {
//        //관리자 로그인
//        User admin = new User("admin@naver.com", passwordEncoder.encode("1234"), "관리자_채팅개설",1);
//        userRepository.save(admin);
//        //일반 로그인
//        User user = new User("spring@naver.com", passwordEncoder.encode("1234"), "스프링",1);
//        userRepository.save(user);
//
//    }
//
//    @Test
//    @Transactional
//    @Rollback(value = false)
//    public void insertDataIntoCategoryTable() {
//        jdbcTemplate.execute("insert into category values (1, '프로그래밍')");
//        jdbcTemplate.execute("insert into category values (2, '수능')");
//        jdbcTemplate.execute("insert into category values (3, '공무원시험')");
//        jdbcTemplate.execute("insert into category values (4, '어학 자격증')");
//    }

}