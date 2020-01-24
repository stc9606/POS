package auth.services.pos.plzec;

import java.sql.Connection;
import java.util.ArrayList;

import beans.pos.plzec.EmployeeBean;
import module.pos.plzec.DAOModule;

class DataAccessObject extends DAOModule {

	DataAccessObject(Connection con) {
		super(con);
	}
	
	/* 신규 등록할 직원코드 가져오기 */
	EmployeeBean getNewEmpCode() {
		EmployeeBean bean = new EmployeeBean();
		String query = "SELECT MAX(EMP_CODE) AS EMPCODE FROM EMP";
		
		try {
			pStatement = this.connection.prepareStatement(query);
			
			rs = pStatement.executeQuery();

			while(rs.next()) {
				bean.setEcode(rs.getNString("EMPCODE"));
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	/* 직원 등록 */
	boolean insEmp(EmployeeBean bean) {
		boolean result = false;
		String query = "INSERT INTO EMP(EMP_CODE, EMP_PWD, EMP_NAME, EMP_LEV)"
				+ " VALUES( ?, ?, ?, ?)";
		
		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getEcode());
			pStatement.setNString(2, bean.getAcode());
			pStatement.setNString(3, bean.getName());
			pStatement.setNString(4, bean.getLev());

			result = (pStatement.executeUpdate() == 1)? true : false;
			

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/* 직원리스트 */
	ArrayList<EmployeeBean> getEmpList(boolean check, EmployeeBean b){
		ArrayList<EmployeeBean> empList = new ArrayList<EmployeeBean>();
		String query = "SELECT EMPCODE, EMPNAME, EMPLEVEL, HDATE FROM EMPLIST";

		if(check) { 
			query += " WHERE";
			if(!b.getEcode().equals("")) {
				if(!b.getName().equals("")) {
					query += " (EMPCODE = ?";
					query += " OR EMPNAME LIKE '%' || ? || '%')";
				}else {
					query += " EMPCODE = ?";
				}
				if(!b.getLev().equals("")) {query += " AND EMPLEVEL = ?";}
			}else if(!b.getName().equals("")) {
				query += " EMPNAME LIKE '%' || ? || '%'";
				if(!b.getLev().equals("")) {query += " AND EMPLEVEL = ?";}
			}else {
				if(!b.getLev().equals("")) {query += " EMPLEVEL = ?";}
			}
		}
				
		try {
			int index = 0;
			pStatement = this.connection.prepareStatement(query);
			if(check) { 
				if(!b.getEcode().equals("")) {
					index++;
					if(!b.getName().equals("")) { 
						index++;
						pStatement.setNString(index-1, b.getEcode());
						pStatement.setNString(index, b.getName());
					} else {
						pStatement.setNString(index, b.getEcode());
					}
					if(!b.getLev().equals("")) { 
						index++;
						pStatement.setNString(index, b.getLev());
					}
				}else if(!b.getName().equals("")) {
					index++;
					pStatement.setNString(index, b.getName());
					if(!b.getLev().equals("")) { 
						index++;
						pStatement.setNString(index, b.getLev());
					}
				}else {
					index++;
					pStatement.setNString(index, b.getLev());
				}
			}

			rs = pStatement.executeQuery();

			while(rs.next()) {
				EmployeeBean bean = new EmployeeBean();
				bean.setEcode(rs.getNString("EMPCODE"));
				bean.setName(rs.getNString("EMPNAME"));
				bean.setLev(rs.getNString("EMPLEVEL"));
				bean.setDate(rs.getNString("HDATE"));
				empList.add(bean);
			}

		}catch(Exception e){ e.printStackTrace();}

		return empList;
	}
	
	boolean empUpd(EmployeeBean bean) {
		boolean result = false;
		String query = "UPDATE EMP SET EMP_PWD = ?, EMP_NAME = ?, EMP_LEV = ? WHERE EMP_CODE = ?";
		
		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getAcode());
			pStatement.setNString(2, bean.getName());
			pStatement.setNString(3, bean.getLev());
			pStatement.setNString(4, bean.getEcode());
			
			result = (pStatement.executeUpdate() == 1)?  true : false;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/* history 삭제 */
	boolean historyDel(EmployeeBean bean) {
		boolean result = false;
		String query = "DELETE FROM HI WHERE HI_EMPCODE = ?";
		
		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getEcode());
			
			
			if(pStatement.executeUpdate() == 0) {
				result = true;
			}
			else if(pStatement.executeUpdate() >= 1) {
				result = true;
			}
			else {
				result = false;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/* 직원 삭제 */
	boolean empDel(EmployeeBean bean) {
		boolean result = false;
		String query = "DELETE FROM EMP WHERE EMP_CODE = ?";
		
		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getEcode());
			
			result = (pStatement.executeUpdate() == 1)?  true : false;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/* ecode의 존재유무 */
	boolean isEcode(EmployeeBean bean) {
		boolean result = false;
		String query = 
				"SELECT COUNT(*) AS \"ISEMP\" FROM EMP WHERE EMP_CODE = ?";

		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getEcode());

			rs = pStatement.executeQuery();
			while(rs.next()) {
				result = (rs.getInt("ISEMP") == 0)? false:true;
			}

		}catch(Exception e) {
			e.printStackTrace();
		}	

		return result;
	}

	/* ecode & acode 일치 여부 */
	boolean getAccess(EmployeeBean bean) {
		boolean result = false;
		String query = 
				"SELECT COUNT(*) AS \"ISEMP\" FROM EMP WHERE EMP_CODE = ? "
						+ "AND EMP_PWD = ?";

		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(1, bean.getEcode());
			pStatement.setNString(2, bean.getAcode());

			rs = pStatement.executeQuery();
			while(rs.next()) {
				result = (rs.getInt("ISEMP") == 0)? false:true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	

}
