package ch14;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class MsgMgr4 {
	
	private DBConnectionMgr pool;
	
	public MsgMgr4() {
		pool = DBConnectionMgr.getInstance();
	}
	
	// insert 기능
	public void insertMsg(MessageBean4 bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			con = pool.getConnection();//Connection 빌려옴
			sql = "insert tblMessage(fid, tid, Msg)values(?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getFid());
			pstmt.setString(2, bean.getTid());
			pstmt.setString(3, bean.getMsg());
			pstmt.executeUpdate(); // insert로 받아오면 int 형으로 받아서 들어옴(반드시는 아님)
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
	}
	
	// list
	public Vector<MessageBean4> getMsgList(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		Vector<MessageBean4> vlist = new Vector<MessageBean4>();
		try {
			con = pool.getConnection();//Connection 빌려옴
			sql = "select * from tblMessage where fid = ? or tid = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MessageBean4 bean = new MessageBean4();
				bean.setNo(rs.getInt("no"));
				bean.setFid(rs.getString("fid"));
				bean.setTid(rs.getString("tid"));
				bean.setMsg(rs.getString("msg"));
				// 날짜는 문자형태로 가져와도 됨
				bean.setMdate(rs.getString("mdata")); // 날짜타입도 문자타입으로 리턴 가능
				vlist.addElement(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	// get : select * from tblMessage where no = ?
	public MessageBean4 getMsg(int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		MessageBean4 bean = new MessageBean4();

		try {
			con = pool.getConnection();//Connection 빌려옴
			sql = "select * from tblMessage where no = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				bean.setNo(rs.getInt("no"));
				bean.setFid(rs.getString("fid"));
				bean.setTid(rs.getString("tid"));
				bean.setMsg(rs.getString("msg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}

}
