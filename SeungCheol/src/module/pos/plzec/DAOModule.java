package module.pos.plzec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import beans.pos.plzec.EmployeeBean;

public class DAOModule {
	protected Connection connection;
	protected PreparedStatement pStatement;
	protected ResultSet rs;
	
	protected DAOModule(Connection con) {
		this.connection = con;
	}
	
	/* Client에 전송할 정보 가공 */
	public ArrayList<EmployeeBean> getAccessInfo(EmployeeBean bean) {
		ArrayList<EmployeeBean> list = new ArrayList<EmployeeBean>();
		String query = "SELECT ECODE, ENAME, LEV, MAX(HDATE) AS HDATE, HIP "
				+ "FROM ACCESSINFO "
				+ "WHERE ECODE = ? AND HIP = ? AND HSTATUS = 1 "
				+ "GROUP BY ECODE, ENAME, LEV, HIP";

		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getEcode());
			pStatement.setNString(2, bean.getIp());

			rs = pStatement.executeQuery();
			while(rs.next()) {
				EmployeeBean eb = new EmployeeBean();
				eb.setEcode(rs.getNString("ECODE"));
				eb.setName(rs.getNString("ENAME"));
				eb.setLev(rs.getNString("LEV"));
				eb.setDate(rs.getNString("HDATE"));
				eb.setIp(rs.getNString("HIP"));

				list.add(eb);
			}
		}catch(Exception e) {e.printStackTrace();}
		
		return list;
	}
	
	/* Manager 판별 */
	public boolean isManager(EmployeeBean bean) {
		boolean result = false;
		String query = 
				"SELECT EMP_LEV AS \"LEV\" FROM EMP WHERE EMP_CODE = ?";
		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getEcode());

			rs = pStatement.executeQuery();
			while(rs.next()) {
				result = (rs.getNString("LEV").equals("M"))? true:false;
			}

		}catch(Exception e) {e.printStackTrace();}	
		return result;
	}
	
	/* HISTORY 테이블에 레코드 삽입 */
	public boolean insHistory(EmployeeBean bean) {
		boolean result = false;
		int record = 0;
		String query = "INSERT INTO HI(HI_EMPCODE, HI_DATE, HI_STATUS, HI_IP) "
				+ "VALUES(?, DEFAULT, ?, ?)";
		
		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getEcode());
			pStatement.setInt(2, bean.getStatus());
			pStatement.setNString(3, bean.getIp());

			record = pStatement.executeUpdate();
			result = (record == 0)? false: true;

		}catch(Exception e) {e.printStackTrace();}

		return result;
	}
	
	/* Transaction End */
	public void setEndTransaction(boolean tran) {
		try {
			if(this.connection != null) {
				if(tran) {this.connection.commit();}
				else {	this.connection.rollback(); }
			}
		} catch(Exception e) { e.printStackTrace();}
	}
}
