public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    //생성자 주입식(인젝션:Injection)으로 제공할 수 없음 = DataSource 사용

    @Autowired //생략 가능: 생성자가 딱 하나 있어서 자동 인식
    public JdbcTemplateMemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource); // 디자인패턴 중에 Template 메서드 패턴이 존재
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<Sting , Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name); //리스트로 나오기에 리스트로 받는다
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name){
        return Optional.empty();
    }

    @Override
    public List<Member> findAll(){
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper(){
        return new RowMapper<Member>(){ // lambda 스타일로 변경 가능
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException{
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return member;
            }
        }
    }

}