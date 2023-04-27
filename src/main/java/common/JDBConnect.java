package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;

public class JDBConnect {
	public Connection con; 			// 데이터베이스와의 연결 담당
	public Statement stmt;			// 정적 쿼리문 실행할 때 사용(파라미터 없음)
	public PreparedStatement psmt;	// 동적 쿼리문 실행할 때 사용(파라미터 있음)
	public ResultSet rs;			// select 쿼리문 결과 저장 시 사용
	
	// 기본 생성자
	public JDBConnect() {
		try {
			// JDBC 드라이버 로드
			Class.forName("oracle.jdbc.OracleDriver");
			
			// DB에 연결
			String url = "jdbc:oracle:thin:@localhost:1521:xe"; // 오라클프로토콜@IP주소:포트번호:sid(오라클 인스턴스의 식별자)
			String id = "musthave";
			String pw = "1234";
			con = DriverManager.getConnection(url, id, pw);
			
			System.out.println("DB 연결 성공(기본 생성자)");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 두 번째 생성자
	public JDBConnect(String driver, String url, String id, String pwd) {
		try {
			// JDBC 드라이버 로드
			Class.forName(driver);
			
			// DB에 연결
			con = DriverManager.getConnection(url, id, pwd);
			
			System.out.println("DB 연결 성공(인수 생성자 1)");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 세 번째 생성자
	public JDBConnect(ServletContext application) {
		try {
			// JDBC 드라이버 로드
			String driver = application.getInitParameter("OracleDriver");
			Class.forName(driver);
			
			// DB에 연결
			String url = application.getInitParameter("OracleURL");
			String id = application.getInitParameter("OracleId");
			String pwd = application.getInitParameter("OraclePwd");
			
			con = DriverManager.getConnection(url, id, pwd);
			
			System.out.println("DB 연결 성공(인수 생성자 2)");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 연결 해제(자원 반납)
	public void close() {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(psmt != null) psmt.close();
			if(con != null) con.close();
			
			System.out.println("JDBC 자원 해제");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
