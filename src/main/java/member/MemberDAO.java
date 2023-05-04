package member;

import common.JDBConnect;

public class MemberDAO extends JDBConnect{
	// 명시한 데이터베이스의 연결이 완료된 MemberDAO 객체를 생성합니다.
	public MemberDAO(String drv, String url, String id, String pw) {
		super(drv, url, id, pw);
	}
	
	// 명시한 아이디/패스워드와 일치하는 회원 정보를 반환합니다.
	public MemberDTO getMemberDTO(String uid, String upass) {
		MemberDTO dto = new MemberDTO(); // 회원 정보 DTO 객체 생성
		String query = "select * from member where id=? and pass=?"; // 쿼리문 템플릿
		
		try { // JDBC 프로그래밍은 기본적으로 예외처리를 해야함(try-catch문)
			// 쿼리 실행
			psmt = con.prepareStatement(query); // 동적 쿼리문 준비
			psmt.setString(1, uid);
			psmt.setString(2, upass);
			rs = psmt.executeQuery(); // 쿼리문 실행
			
			// 결과 처리
			if(rs.next()) {
				// 쿼리 결과로 얻은 회원 정보를 DTO 객체에 저장
				dto.setId(rs.getString("id"));
				dto.setPass(rs.getString("pass"));
				dto.setName(rs.getString(3));
				dto.setRegidate(rs.getString(4));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

}
