package Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberADM {
	// ojdbc6으로 개발하는 순서
	// 1. 드라이버로드 1번만 하면된다.
	// 2. CRUD작업이 있을 때마다 다음 과정을 생각한다.
	//  2-1. 커넥션 가져오기		// DB가 자바에게 연결을 위해서 빌려주는 것
								// 오라클에서 작업하기 전에 커넥션 자원을 획득해야 한다.
								// 커넥션 자원은 한정적이기에 쓰고나면 반납하는 것이 좋다. (2-7)
								// 오라클은 커넥션 자원을 유한으로 만들어놓고 공유해서 사용하도록 한다.
	//  2-2. 쿼리문 만들기
	//  2-3. 쿼리문 완성하기(Mapping)
	//  2-4. 쿼리문 전송하여 오라클에서 실행
	//  2-5. 오라클에서 리턴값 전송
	//  2-6. 자바에서 2-5에서 받은 리턴값 처리
	//  2-7. 커넥션 자원 반납(중요***)		// 커넥션을 무한대로 주게되면, DB의 성능이 낮아질 것이므로 반납과정이 중요함
										// idle time이 길어진다면, 성능이 저하됨(어딘가에는 있는데, 쓰지 않는 경우)
										// 즉 2-1과 2-7은 자원을 효과적으로 사용하기 위한 개발자 코드의 영역.
	
	MemberADM(){
		init();		// 드라이버 로드는 1번만 하면 되므로 생성자에서 작업  -> 생성자가 제일 좋은 위치(메소드 나누는거 추천, init)
		insert();
	}
	private void init() {	// 오라클 드라이버 로드 코딩
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("오라클 드라이버 로드 성공");	// 빌드가 정확하게 됐을 때 이 문구가 출력될 것임.
			// 이 문구가 제대로 출력된다면, 오라클사에서 배포한 라이브러리를 사용할 준비가 완료된것을 의미함.
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void insert() {
		MemberDTO m = new MemberDTO();
		m.setId("a");
		m.setName("kim");
		m.setAge(32);
		// DTO 객체를 만들었음.. 오라클에 저장해보자.
		// 2-1. 커넥션 자원 가져오기
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl"/*포트넘버 1521*/,"system"/*아이디*/,
					"11111111" /*비밀번호*/);
			System.out.println("커넥션 자원 획득 성공");
			String sql = "insert into memberone values(?,?,?,default)";
			// 무슨값이 들어가는지 모르기에 ?로 값을 먼저 입력 -> Mapping을 통하여 최종 값을 넣을건데, 이때 들어가야하는 정보를 DTO가 알려줌
			// 쿼리문을 커넥션을 통해서 연결해라...
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getId());	// 물음표 1번째
			pstmt.setString(2, m.getName());	// 물음표 2번째
			pstmt.setInt(3, m.getAge());	// 물음표 3번째
			// 실행 후 리턴값 가져오기
			int result = pstmt.executeUpdate();
			if(result == 0) {
				conn.rollback();		// 쿼리문 취소시켜라
			}else {	
				conn.commit();		// 커밋실행시켜라
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close(); 	// 자원반납
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
}
