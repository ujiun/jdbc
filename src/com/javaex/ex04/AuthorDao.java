package com.javaex.ex04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/************************
 * Dao(Data Access Object)
 * DataBase(오라클) 관련된 일을 하는 클래스
 ************************/

public class AuthorDao {
	//필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	//생성자
	
	//메소드-gs
	
	//메소드-일반
	
	//--DB연결 메소드
	private void getConnection() {	//메인이나 다른곳에서 쓰지 못하도록 private으로 설정
		
		try {
			 // 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

		    // 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			
		}catch (ClassNotFoundException e) {
		    System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		
		
	}
	
	
	private void close() {
		// 5. 자원정리
	    try {
	    	
	        if (rs != null) {
	            rs.close();
	        }   
	                    
	        if (pstmt != null) {
	            pstmt.close();
	        }
	        if (conn != null) {
	            conn.close();
	        }
	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    }
	    
	}
	
	
	//--작가 등록 메소드
	public int authorInsert(AuthorVo authorVo) {// 리턴값 int
		int count = -1; // -1이 나오면 try catch에 걸린것! 0으로 초기화해놓으면 알수없어서 -1로
		
		getConnection(); //this.getConnection();
		
		try {
		    
		    // 3. SQL문 준비 / 바인딩 / 실행
		    //SQL문 준비
			String query = "";
			query += " insert into author ";
			query += " values (seq_author_id.nextval, ? , ?) ";
			System.out.println(query);
			
			//바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, authorVo.getAuthorName());
			pstmt.setString(2, authorVo.getAuthorDesc());
			
			//실행
			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println(count + "건이 등록 되었습니다.");
			
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		} 
		
		close(); //this.close();
		
		return count;
		
	}
	
	
	//--작가 삭제 메소드
	public int authorDelete(int authorId) {
		int count = -1;
		
		getConnection();

		try {
		    
		    // 3. SQL문 준비 / 바인딩 / 실행
		    //SQL문 준비
			String query = "";
			query += " delete from author ";
			query += " where author_id = ? ";
			System.out.println(query);
			
			//바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, authorId);
			
			//실행
			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println(count + "건 삭제 되었습니다.");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);	
		} 
		
		close();
		
		return count;
	}
	
	//--작가 수정 메소드
	public int authorUpdate(AuthorVo authorVo) {
		int count = -1;
		
		getConnection();

		try {

		    // 3. SQL문 준비 / 바인딩 / 실행
		    //SQL문 준비
			String query = "";
			query += " update author ";
			query += " set author_name = ?, ";
			query += "     author_desc = ? ";
			query += " where author_id = ? ";
			
			//바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, authorVo.getAuthorName());
			pstmt.setString(2, authorVo.getAuthorDesc());
			pstmt.setInt(3, authorVo.getAuthorId());
			
			//실행
			count = pstmt.executeUpdate();
			
		    // 4.결과처리
			System.out.println(count + "건이 수정 되었습니다.");
			
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		
		close();
		
		return count;
	}
	
	//--작가 전체리스트 가져오기 메소드
	public List<AuthorVo> authorSelect() {
		
		List<AuthorVo> authorList = new ArrayList<AuthorVo>();
		
		getConnection();

		try {

		    // 3. SQL문 준비 / 바인딩 / 실행
		    //SQL문 준비
			String query = "";
			query += " select  author_id, ";
			query += "         author_name, ";
			query += "         author_desc ";
			query += " from author ";
			System.out.println(query);
			
			//바인딩
			pstmt = conn.prepareStatement(query);
			
			//실행
			//ResultSet 가져요기
			rs = pstmt.executeQuery();
			
		    // 4.결과처리
			//리스트로 만들기
			//List<AuthorVo> authorList = new ArrayList<AuthorVo>(); //return 값 때문에 위에다가 선언
			
			//반복문으로 Vo 만들기 List에 추가하기
			while(rs.next()) {
				int authorId = rs.getInt("author_id");
				String authorName = rs.getString("author_name");
				String authorDesc = rs.getString("author_desc");
				
			    AuthorVo authorVo = new AuthorVo(authorId, authorName, authorDesc);
			    
			    authorList.add(authorVo);
			}
			
			//리스트를 출력해보기(개발용)
			//System.out.println(authorList.toString());
			
		
		} catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		
		close();
		
		return authorList;
	}
	
}
