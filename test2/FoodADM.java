package test2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class FoodADM {
	
	Scanner in = new Scanner(System.in);
	Connection conn = null;
	String num = null;
	String jong = null;
	String f_name = null;
	Connect con = new Connect();
	String url = con.getUrl();
	String id = con.getId();
	String pwd = con.getPwd();
	private ResultSet rs = null;
	private Statement stmt = null;
	
	FoodADM(){
		while(true) {
			System.out.println("원하는 항목을 선택하세요.");
			System.out.println("1. 음식 추가하기");
			System.out.println("2. 음식 삭제하기");
			System.out.println("3. 음식 수정하기");
			System.out.println("4. 음식 전체보기");
			System.out.println("5. 종료");
			
			int selNum = in.nextInt();
			in.nextLine();
			
			if(selNum == 1) {
				insert();			
			}else if(selNum==2) {
				delete();
			}else if(selNum==3) {
				mod();
			}else if(selNum==4) {
				all();
			}else if(selNum==5) {
				break;
			}			
		}
	}

	private void insert() {
		// TODO Auto-generated method stub
		FoodDTO food = new FoodDTO();
		System.out.println("추가할 음식의 종류를 입력하시오. - (한식/양식/중식/분식/일식)");
		jong = in.nextLine();
		System.out.println("음식의 이름을 입력하시오. - (메뉴명, 중복불가)");
		f_name = in.nextLine();
		food.setJong(jong);
		food.setFood(f_name);
		
		try {
			conn = DriverManager.getConnection(url,id,pwd);
//			System.out.println("커넥션을 가져옴**");
			
			String sql = "insert into foodone values(?,?)";
			// 입력하고 싶은 값을 쿼리문으로 작성
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, food.getJong());
			pstmt.setString(2, food.getFood());
			
			int result = pstmt.executeUpdate();
			if(result == 0) {
				conn.rollback();
			}else {
				conn.commit();
				System.out.println("해당 음식의 추가가 완료되었습니다.");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void delete() {
		// TODO Auto-generated method stub
		System.out.println("삭제할 음식명을 입력하세요.");
		String d_food = in.nextLine();
		
		try {
			conn = DriverManager.getConnection(url,id,pwd);
			String sql = "DELETE from foodone WHERE food = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, d_food);
			
			int result = pstmt.executeUpdate();
			if(result == 0) {
				conn.rollback();
				System.err.println("해당 음식은 존재하지않습니다.(삭제실패)");
			}else {
				conn.commit();
				System.out.println(f_name+" 이름의 음식내역이 삭제되었습니다 -*");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}

	private void mod() {
		// TODO Auto-generated method stub
		System.out.println("수정할 음식명을 입력하세요.");
		String food = in.nextLine();
		System.out.println("변경하고 싶은 음식명을 입력하세요.");
		String m_food = in.nextLine();
		
		try {
			conn = DriverManager.getConnection(url,id,pwd);
			String sql = "update foodone set food = ? where food = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m_food);
			pstmt.setString(2, food);
			
			int result = pstmt.executeUpdate();
			if(result == 0) {
				conn.rollback();
				System.err.println("해당 음식은 존재하지않습니다.(수정실패)");
			}else {
				conn.commit();
				System.out.println(food+" 이름의 음식내역이 "+m_food+" 로 변경되었습니다 -*");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void all() {
		// TODO Auto-generated method stub
//		FoodDTO food = new FoodDTO();
		int cnt = 1;
		try {
			conn = DriverManager.getConnection(url,id,pwd);		// 드라이버 로드
			String sql = "select * from foodone";

			stmt = conn.createStatement();
			// Statement 객체 생성
			rs = stmt.executeQuery(sql);
			// 조회 결과 ResultSet 자료형, rs 변수에 저장
			// 이때, Statement 객체 통하여 메소드 executeQuery사용
			System.out.println("-----현재 저장된 항목입니다.-----");
			System.out.println("   "+"<내가 좋아하는 음식>"+"   ");
			while(rs.next()) {		// 다음행이 존재한다면 rs.next()는 true반환 + 한칸 내려감 / 없으면 false 반환
				System.out.println(cnt+"번 :  "+rs.getString(1)+"\t"+rs.getString(2));
				cnt++;
			}
			System.out.println("----------------------------");
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(conn!=null) {
				try {
					conn.close();
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


}
