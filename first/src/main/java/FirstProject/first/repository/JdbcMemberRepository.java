package hello.hellospring.repository;
    import hello.hellospring.domain.Member;
    import org.springframework.jdbc.datasource.DataSourceUtils;
    import javax.sql.DataSource;
    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {
  private final DataSource dataSource;
  public JdbcMemberRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  //데이터 베이스 리소는 연결하고 사용을 다 했다면 꼭 끊어주어야한다.
	@Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; // 결과를 받아줌

      	// exeption을 매우 많이 던지기 때문에 try - catch로 받음
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
          	// Statement.RETURN_GENERATED_KEYS : 옵션값 - id값을 받아옴
          	// 반환은 ResultSet으로 받아준다.

            pstmt.setString(1, member.getName()); // ? 랑 매칭됨 (setString)

            pstmt.executeUpdate(); //실제 쿼리로 날라감

            rs = pstmt.getGeneratedKeys();
          	// 위 옵셥값에서 던져준 Statement.RETURN_GENERATED_KEYS를 받아줌

            if (rs.next()) { // rs가 null이 아니면
              member.setId(rs.getLong(1)); // 그 값에 id부여(이전에 만든 메서드)
            } else {
            	throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
          	// 리소스들을 끊어주는 부분 : 이것도 구현해야함 = 아래 Close 메서드 참고
          	// 내용: conn, pstmt, rs가 null이 아니면 하나씩 .close() 로 닫아준다
          	// 주의: try catch로 각각 실행해야한다.
        }
		}

  @Override
    public Optional<Member> findById(Long id) { //조회 쿼리
        String sql = "select * from member where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
      	ResultSet rs = null;

      try {
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, id);

        rs = pstmt.executeQuery();
        //조회는 executeUpdate() 가 아닌 executeQuery() 이다.

        if(rs.next()) { // 만약 값이 있으면
            Member member = new Member(); //멤버 클래스 형식으로 만들어주고
            member.setId(rs.getLong("id")); // DB에서 불러온 값으로 세팅
            member.setName(rs.getString("name"));
            return Optional.of(member); //반환
        } else {
            return Optional.empty();
        }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            } finally {
                close(conn, pstmt, rs);
        } }

  @Override
  public List<Member> findAll() {
      String sql = "select * from member"; //전체조회라 더 단순
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          conn = getConnection();
          pstmt = conn.prepareStatement(sql);

        	rs = pstmt.executeQuery(); //StringTokenizer 느낌

        	List<Member> members = new ArrayList<>();

        	while(rs.next()) { //while을 사용해 계속 받아줌
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            members.add(member);
      }
              return members;
          } catch (Exception e) {
              throw new IllegalStateException(e);
          } finally {
              close(conn, pstmt, rs);
          }
      }
  @Override
  public Optional<Member> findByName(String name) {
      String sql = "select * from member where name = ?";
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          conn = getConnection();
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, name);
          rs = pstmt.executeQuery();
          if(rs.next()) {
              Member member = new Member();
              member.setId(rs.getLong("id"));
              member.setName(rs.getString("name"));
              return Optional.of(member);
  }

        return Optional.empty();
 } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
}
  // Connection을 인스턴스로 생성하기보다 DataSourceUtils 를 이용해 가져오기
  // database 트랜젝션에 걸릴 수 있는데 연결을 계속 유지해주는 역할을 한다.
  // 가져올 때 꼭 이렇게 가져온다. 닫을 때 역시 DataSourceUtils로 닫는다.
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
		}

  	//close 메서드 시작부
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
			} try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
		} }

  // Connection을 닫을 때 또한 DataSourceUtils 이용
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }}