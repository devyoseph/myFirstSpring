package FirstProject.first;
import org.springframework.stereotype.Controller;

@Controller // 자바스크립트에서 컨트롤러와 비슷: 스프링 시작과 동시에 올라옴
public class MemberController { //기능은 없지만 스프링 컨테이너가 처음 뜰 때 그 안에 컨트롤러를 넣어둠

    private final MemberService memberService = new MemberService();

    @Autowired // 멤버서비스에 있는 스프링이랑 연결시켜줌: 그러나 memberService는 순수 클래스기 때문에
    public MemberController(MemberService memberService) { //스프링이 바로 인식을 못함
        this.memberService = memberService; // 인식을 위해 MemberService에 @Service 입력
        //MemberRepository는 가서 @Repository를 써주면 된다
    }

    @GetMapping("/members/new")
    public String createForm(){
        retrun "members/createMemberForm";
    }

    @PostMapping("members/new") // 같은 단이지만 뒷 단에서 이루어지는 작업
    public String create(MemberForm form){
        Member member = new Member(); // 객체 인스턴스가 만들어지고
        member.setName(form.getName()); // 입력값으로 멤버의 이름 설정

        memberService.join(member); // 멤버 서비스의 회원가입 메소드 실행을 위해 멤버를 넘김

        return "redirect:/"; // redirect를 사용해 다시 보냄:  /  = 홈의 위치
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Memeber> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}