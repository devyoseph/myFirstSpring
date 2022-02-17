package FirstProject.first.repository;

import FirstProject.first.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member); // 인터페이스이므로 { } 블럭 X
    Optional<Member> findById(Long id); // java 8의 기능
    Optional<Member> findByName(String name); // 찾았을 때 null이면 Option으로 감싸서 반환
    List<Member> findAll();
}
