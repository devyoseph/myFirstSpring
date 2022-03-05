package FirstProject.first.repository;

import FirstProject.first.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemoryMemberRepositoryTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository repository;

/*    @AfterEach : 각 테스트 메소드가 실행된 이후 실행하는 메소드
    public void afterEach(){
        repository.clearStore();
    }  @Transactional의 존재로 인해 매번 비워줄 필요가 없다
*/
    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);
        Member result = repository.findById(member.getId()).get();
        // System.out.println("result = "+ (result == member)); // 계속 볼 수는 없으므로
        //Assertions.assertEquals(member,result); //Assertion을 이용해 비교
        // Assertions.assertEquals(member,null); //Assertion 만약 다른 값이 튀어나오면 오류
        assertEquals(member,result); // Assertions 를 드래그한 상태로 opt+Enter 한다음 static import
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertEquals(result,member1);

        List<Member> result2 = repository.findAll(); //이 메소드도 검사
        assertEquals(result2.size(),2); // 총 2개가 나와야함
    }

}
