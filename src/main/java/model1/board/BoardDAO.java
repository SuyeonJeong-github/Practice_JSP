package model1.board;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import common.JDBConnect;

public class BoardDAO extends JDBConnect{
	public BoardDAO(ServletContext application) {
		super(application);
	}
	
	// 검색 조건에 맞는 게시물의 개수를 반환합니다.
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0; // 결과(게시물 수)를 담을 변수
		
		// 게시물 수를 얻어오는 쿼리문 작성
		String query = "select count(*) from board";
		if(map.get("searchWord") != null) {
			query += " where " + map.get("searchField") + " "
					+ " like '%" + map.get("searchWord") + "%'";
		}
		
		try {
			stmt = con.createStatement(); // 정적 쿼리문을 실행하기 위해 Statement 객체 생성
			rs = stmt.executeQuery(query); // select 쿼리 실행. 결과는 ResultSet 객체로 반환
			rs.next(); // 커서를 첫 번째 행으로 이동
			totalCount = rs.getInt(1); // 첫 번째 컬럼 값을 가져옴(DB에서 인덱스는 1부터 시작함)
		}
		catch(Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생");
			e.printStackTrace();
		}
		
		return totalCount;
	}
	
	// 검색 조건에 맞는 게시물 목록을 반환합니다.
	public List<BoardDTO> selectList(Map<String, Object> map){
		// 결과(게시물 목록)를 담을 변수
		List<BoardDTO> bbs = new Vector<BoardDTO>(); // Vector 대신에 ArrayList, LinkedList 등 List 계열의 컬렉션이라면 가능
		
		String query = "select * from board ";
		if(map.get("searchWord") != null) {
			query += " where " + map.get("searchField") + " "
					+ " like '%" + map.get("searchWord") + "%' ";
		}
		
		query += " order by num desc ";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			System.out.println(query);

			while(rs.next()) { // 결과를 순화하며
				// 한 행(게시물 하나)의 내용을 DTO에 저장
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getString("num"));				// 일련번호
				dto.setTitle(rs.getString("title"));			// 제목
				dto.setContent(rs.getString("content"));		// 내용
				dto.setPostdate(rs.getDate("postdate"));		// 작성일
				dto.setId(rs.getString("id"));					// 작성자 아이디
				dto.setVisitcount(rs.getString("visitcount"));	// 조회수
				
				bbs.add(dto); // 결과 목록에 저장
			}
		}
		catch(Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		
		return bbs;
	}
}
