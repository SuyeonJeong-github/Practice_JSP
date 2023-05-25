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
	
	// 검색 조건에 맞는 게시물 목록을 반환합니다(페이징 기능 지원).
	public List<BoardDTO> selectListPage(Map<String, Object> map){
		List<BoardDTO> bbs = new Vector<BoardDTO>(); // 결과(게시물 목록)을 담을 변수
		
		// 쿼리문 템플릿
		String query = "select * from ( "
					+ "		select tb.*, rownum rNum from ( "
					+ "			select * from board ";
		
		// 검색 조건 추가
		if(map.get("searchWord") != null) {
			query += " where " + map.get("searchField")
					+ " like '%" + map.get("searchWord") + "%' ";
		}
		
		query += " order by num desc "
				+ " ) tb "
				+ " ) "
				+ " where rNum between ? and ?";
		
		try {
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, map.get("start").toString());
			psmt.setString(2, map.get("end").toString());
			
			// 쿼리문 실행
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				// 한 행(게시물 하나)의 데이터를 DTO에 저장
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString("visitcount"));
				
				// 반환할 결과 목록에 게시물 추가
				bbs.add(dto);
			}
		}
		catch(Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		
		// 목록 반환
		return bbs;
	}
	
	// 게시글 데이터를 받아 DB에 추가합니다.
	public int insertWrite(BoardDTO dto) {
		int result = 0;
		
		try {
			// insert 쿼리문 작성
			String query = "insert into board ( "
						+ " num, title, content, id, visitcount) "
						+ " values ( "
						+ " seq_board_num.nextval, ?, ?, ?, 0)";
			
			psmt = con.prepareStatement(query); // 동적 쿼리
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getId());
			
			result = psmt.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		
		return result; // 추가한 행의 개수 반환
	}
	
	// 지정한 게시물을 찾아 내용을 반환합니다.
	public BoardDTO selectView(String num) {
		BoardDTO dto = new BoardDTO();
		
		// 쿼리문 준비
		String query = "select b.*, m.name "
					+ " from member m inner join board b "
					+ " on m.id = b.id "
					+ " where num=?";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			rs = psmt.executeQuery();
			
			// 결과 처리
			if(rs.next()) {
				dto.setNum(rs.getString(1));
				dto.setTitle(rs.getString(2));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString(6));
				dto.setName(rs.getString("name"));
			}
		}
		catch(Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		
		return dto;
	}
	
	// 지정한 게시물의 조회수를 1 증가시킵니다.
	public void updateVisitCount(String num) {
		// 쿼리문 준비
		String query = "update board set "
					+ " visitcount=visitcount+1 "
					+ " where num=?";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			psmt.executeQuery();
		}
		catch(Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	// 지정한 게시물을 수정합니다.
	public int updateEdit(BoardDTO dto) {
		int result = 0;
		
		try {
			// 쿼리문 템플릿
			String query = "update board set "
						+ " title=?, content=? "
						+ " where num=?";
			
			// 쿼리문 완성
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getNum());
			
			// 쿼리문 실행
			result = psmt.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("게시물 수정 중 예외 발생");
			e.printStackTrace();
		}
		
		return result; // 결과 반환
	}
	
	// 지정한 게시물을 삭제합니다.
	public int deletePost(BoardDTO dto) {
		int result = 0;
		
		try {
			String query = "delete from board where num=?";
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getNum());
			
			result = psmt.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("게시물 삭제 중 예외 발생");
			e.printStackTrace();
		}
		
		return result;
	}
}
