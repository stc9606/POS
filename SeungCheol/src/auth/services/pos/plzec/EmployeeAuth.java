package auth.services.pos.plzec;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import beans.pos.plzec.EmployeeBean;
import beans.pos.plzec.Forward;
import module.pos.plzec.DBConnection;
import module.pos.plzec.SessionManagement;

public class EmployeeAuth extends SessionManagement {
	/* Instance Variable Zone*/
	private Connection con;
	private DataAccessObject dao;

	/* Constructor Zone */
	public EmployeeAuth(HttpServletRequest request){
		super(request);		
	}

	/* Service Controller */
	public Forward serviceCtl(int serviceCode) {

		switch(serviceCode) {
		case 0:
			if(isAccess()){moveMain();}
			else {
				forward.setRedirect(false);
				forward.setPage("/jsp/main.jsp");
			}
			break;
		case 1:
			access();
			break;
		case 2:
			if(isAccess()){if(isManager()) {moveManager(null, null);}}
			break;
		case 3:
			if(isAccess()){moveSales();}
			break;
		case 12:
			if(isAccess()){if(isManager()) {empSearch();}}
			break;
		case 13:
			if(isAccess()){if(isManager()) {empReg();}}
			break;
		case 14:
			if(isAccess()){if(isManager()) {getNewEmpCode();}}
			break;
		case 15:
			if(isAccess()){if(isManager()) {empUpd();}}
			break;
		case 16:
			if(isAccess()){if(isManager()) {empDel();}}
			break;
		case -1:
			if(isAccess()){logOut();}
			break;
		}
		return forward;
	}

	/*  CASE 0 */
	private void moveMain() {

		EmployeeBean bean = new EmployeeBean();
		bean.setEcode(this.getSessionObject("empCode").toString());
		bean.setIp(this.request.getRemoteAddr());

		/* Linkage : DAO ~ Connection */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);

		setRequestData(dao.getAccessInfo(bean),"상점관리");
		DBConnection.dbClose();

		page = "/jsp/sales.jsp";
		isRedirect = false;

		forward.setRedirect(isRedirect);
		forward.setPage(page);
	}

	/*  CASE 1 */
	private void access() {
		/* bean에 Request 데이터 저장 */
		EmployeeBean bean = new EmployeeBean();
		bean.setEcode(this.request.getParameter("empCode"));
		bean.setAcode(this.request.getParameter("pwdCode"));
		bean.setStatus(1);
		bean.setIp(request.getRemoteAddr());

		/* Linkage : DAO ~ Connection */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);

		/* 1. ECODE Check */
		if(dao.isEcode(bean)) {
			/* 2. ECODE & ACODE Check */
			if(dao.getAccess(bean)) {
				/* 3. DB Login  : History insert */
				if(dao.insHistory(bean)) {
					setRequestData(dao.getAccessInfo(bean),"상점관리");
					dao.setEndTransaction(true);
					/* 4. Session LogIn */
					this.setSessoinObject("sId", this.session.getId());
					this.setSessoinObject("empCode", bean.getEcode());
					/* 5. Forward Set */
					page = "/jsp/sales.jsp";
					isRedirect = false;
				}
			}else {
				bean.setStatus(0);
				dao.insHistory(bean);
				dao.setEndTransaction(true);
			}
		}

		/* Disconnect */
		DBConnection.dbClose();

		forward.setPage(page);
		forward.setRedirect(isRedirect);
	}

	/* CASE 2 */
	private void moveManager(String message, String empCode) {

		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);

		/* 관리자 여부 판단 후 페이지 상단 정보값 추출 */
		EmployeeBean bean = new EmployeeBean();
		bean.setEcode(this.getSessionObject("empCode"));
		bean.setIp(this.request.getRemoteAddr());

		setRequestData(dao.getAccessInfo(bean),"상품판매");

		/* 직원리스트 가져오기 */
		this.request.setAttribute("empList", makeTable(dao.getEmpList(false, bean)));
		
		/* message 저장 */
		if(message != null) {
			this.request.setAttribute("message", message);
		}
		/* 신규 EMPCODE 코드 저장 */
		if(empCode != null) {
			this.request.setAttribute("newEmpCode", empCode);
		}
		
		DBConnection.dbClose();

		page = "/jsp/empMgr.jsp";
		isRedirect = false;

		forward.setPage(page);
		forward.setRedirect(isRedirect);	
	}

	/* CASE 12 */
	private void empSearch() {
		/* 변수 선언 */
		EmployeeBean bean = new EmployeeBean();
		String[] empInfo = null;
		String lev = null;

		/* 리퀘스트 객체로 부터 빈에 데이터 저장 */
		empInfo = this.request.getParameterValues("empInfo");
		lev = this.request.getParameter("empLev");

		/* 커넥션 할당 후 dao 전달 : 데이터 전달 받기 */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);

		/* 클라이언트에 보낼 데이터 리퀘스트에 담기 */
		bean.setEcode(this.getSessionObject("empCode"));
		bean.setIp(this.request.getRemoteAddr());
		setRequestData(dao.getAccessInfo(bean),"판매관리");

		/* 직원리스트 가져오기 */
		bean.setEcode((empInfo[0] == null)? "": empInfo[0]);		
		bean.setName((empInfo[1] == null)? "": empInfo[1]);
		bean.setLev((lev == null)? "":lev);
		this.request.setAttribute("empList", makeTable(dao.getEmpList(true, bean)));

		/* 커넥션 닫기 */
		DBConnection.dbClose();

		/* 이동할 페이지와 이동 방식 지정 후 포워드에 담기 */
		page = "/jsp/empMgr.jsp";
		isRedirect = false;

		forward.setPage(page);
		forward.setRedirect(isRedirect);
	}
	
	/* CASE 13 : 직원등록 */
	private void empReg() {
		/* 변수 선언 */
		EmployeeBean bean = new EmployeeBean();
		boolean check = false;
		String message = null;
		
		/* 리퀘스트 객체로 부터 빈에 데이터 저장 */
		bean.setEcode(this.request.getParameterValues("emp")[0]);
		bean.setAcode(this.request.getParameterValues("emp")[1]);
		bean.setName(this.request.getParameterValues("emp")[2]);		
		bean.setLev(this.request.getParameter("empLevel"));
		
		/* 커넥션 할당 후 dao 전달 : 데이터 전달 받기 */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);
		
		check = dao.insEmp(bean);
		
		/* 트랜젝션 & 커넥션 닫기 */
		dao.setEndTransaction(true);
		DBConnection.dbClose();
		
		message = check? "새로운 직원이 등록되었습니다." : "직원 등록이 실패하였습니다."; 	
		moveManager(message, null);
		
			
	}

	/* CASE 14 : 직원등록시 자동으로 신규 직원코드 생성해서 클라이언트 전달 */
	private void getNewEmpCode() {
		/* 변수 선언 */
		EmployeeBean bean = null;
		String empCode = null;
		
		/* 커넥션 할당 후 dao 전달 : 데이터 전달 받기 */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);
		
		bean = dao.getNewEmpCode();
		/* 커넥션 닫기 */
		DBConnection.dbClose();
		
		empCode = (Integer.parseInt(bean.getEcode()) + 1) + "";
		moveManager(null,  empCode);
	}
	
	/* CASE 15 */
	private void empUpd() {
		/* 변수 선언 */
		EmployeeBean bean = new EmployeeBean();
		boolean check = false;
		String message = null;

		bean.setEcode(this.request.getParameterValues("emp")[0]);
		bean.setAcode(this.request.getParameterValues("emp")[1]);
		bean.setName(this.request.getParameterValues("emp")[2]);
		bean.setLev(this.request.getParameter("empLevel"));
		
		/* 커넥션 할당 후 dao 전달 : 데이터 전달 받기 */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);
		
		check = dao.empUpd(bean);
		
		/* 트랜젝션 & 커넥션 닫기 */
		dao.setEndTransaction(true);
		DBConnection.dbClose();
		
		message = check? "직원 정보가 수정되었습니다." : "직원 정보 수정에 실패하셨습니다."; 	
		moveManager(message, null);
	}
	
	/* CASE 16 */
	private void empDel() {
		/* 변수 선언 */
		EmployeeBean bean = new EmployeeBean();
		boolean check = false;
		String message = null;
		
		bean.setEcode(this.request.getParameterValues("emp")[0]);
		
		/* 커넥션 할당 후 dao 전달 : 데이터 전달 받기 */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);
		
		
		if(dao.historyDel(bean)) {
			check = dao.empDel(bean);
		}
		
		/* 트랜젝션 & 커넥션 닫기 */
		dao.setEndTransaction(true);
		DBConnection.dbClose();
		
		message = check? "직원이 삭제되었습니다." : "직원 삭제에 실패하셨습니다."; 	
		moveManager(message, null);

	}
	
	/* 직원리스트 --> Making Table*/
	private String makeTable(ArrayList<EmployeeBean> empList) {
		StringBuffer sb = new StringBuffer();
		String oddEven = null;
		String color = null;

		sb.append("<TABLE id=\"lists\">");
		sb.append("<TR>");
		sb.append("<TH>직원코드</TH>");
		sb.append("<TH>직원성명</TH>");
		sb.append("<TH>직원등급</TH>");
		sb.append("<TH>최근접속기록</TH>");
		sb.append("</TR>");

		for(int i=0; i<empList.size(); i++) {
			/* 홀수번째 레코드와 짝수번째 레코드의 CSS 값 분기*/
			if(i%2 == 0) {
				oddEven = "odd";
				color = "\'rgba(246, 246, 246, 0.8)\'"; 	
			}else {
				oddEven =  "even";
				color = "\'rgba(217, 229, 255, 0.8)\'";
			}

			sb.append("<TR class=\""+ oddEven +"\" onClick=\"ctlLightsBox(\'lightBoxCanvas" + "\', \'lightBoxView\', \'emp\', 1, [\'" 
						+ empList.get(i).getEcode() + "\', \'" + empList.get(i).getName() + "\', " + (empList.get(i).getLev().equals("M")?1:2) + "])\" onMouseOver=\"selectedRow" + "(this, \'rgba(255, 228, 0, 0.8)\')\" onMouseOut=\"selectedRow"	+ "(this, " + color + ")\">");
			sb.append("<TD>" + empList.get(i).getEcode() + "</TD>");
			sb.append("<TD>" + empList.get(i).getName() + "</TD>");
			sb.append("<TD>" + ((empList.get(i).getLev().equals("M"))? "매니저":"알바") + "</TD>");
			sb.append("<TD>" + empList.get(i).getDate() + "</TD>");
			sb.append("</TR>");
		}

		sb.append("</TABLE>");

		return sb.toString();
	}
}



