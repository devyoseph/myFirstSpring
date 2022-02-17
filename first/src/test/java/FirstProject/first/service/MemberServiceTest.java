package FirstProject.first.service;

import FirstProject.first.domain.Member;
import FirstProject.first.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*; //static import 한 모습 = option enter
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    //MemberService memberService = new MemberService();
    //MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach //동작하기 전에 넣어주기: 위 Member리포지로티하고 Member 서비스의 데이터가 일치하지 않는 문제 발생
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach // 끝날 때마다 DB값을 날려주기
    public void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName(("hello"));

        //when
        Long saveId = memberService.join(member);

        //then
        Member result = memberService.findOne(saveId).get(); // cmd+opt+v
        assertThat(member.getName()).isEqualTo(result.getName());
    }

    @Test
    public void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

/*  try-catch가 애매할 때가 있다. 이 때 assertThrows를 사용한다.
        try{
            memberService.join(member2);
            fail(); //예외가 발생해야하는데 발생하지 않는 경우 fail() 구문이 실행된다
        }catch(IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
*/
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        //then
    }


    @Test
    void findOne() {
    }
}