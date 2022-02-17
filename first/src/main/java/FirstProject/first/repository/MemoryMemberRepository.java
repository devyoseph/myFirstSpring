package FirstProject.first.repository;

import FirstProject.first.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L; // 실무에서는 동시성 문제로 어텀 롱을 해야함

    @Override
    public Member save(Member member) {
        member.setId(++sequence); // save 할 때마다 squence 증가시킨 후 넣어서 id 저장
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // Optional을 이용하면 null이어도 감쌀 수 있다.
        //return store.get(id); // Map에서 찾아서 반환하는데 없으면 null 이 반환된다.
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()  // values() 를 통해 키의 value 들 순회하면서
                 .filter(member -> member.getName().equals(name)) // 람다식
                    /// 멤버를 넣고 멤버의 이름을 반환해 같은지 확인 (equals 는 true, false)
                 .findAny(); //하나라도 찾는다
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // 실무에서는 ArrayList로 주로 반환
    }

    public void clearStore(){
        store.clear();
    }
}
