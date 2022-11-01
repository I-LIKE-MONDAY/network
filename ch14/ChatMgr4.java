package ch14;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChatMgr4 {

	private DBConnectionMgr pool;
	
	public ChatMgr4() {
		pool = DBConnectionMgr.getInstance();
	}
	
	// 로그인
	public boolean loginChk(String id, String pwd) {
		// java.sql 붙어있는지 확인하고 Connection import 하기
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection(); // Connection 빌려옴
			sql = "select id from tblRegister where id = ? and pwd = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id); // 첫번째 ?에 대입, id = 'aaa' // 쿼리문의 문자열은 " " 아니고 ' ' 사용
			pstmt.setString(2, pwd); // 첫번째 ?에 대입, id = '1234' - setString이니까 1234 문자열 ' ' 으로 받음
			rs = pstmt.executeQuery(); // 실행문
			flag = rs.next(); // 조건에 맞는 결과값이 있으면 true, 없으면 false
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// con은 반납 | pstmt, rs는 둘 다 close
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}
	
}
