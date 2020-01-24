package module.pos.plzec;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.pos.plzec.EmployeeBean;
import beans.pos.plzec.Forward;


/**
 * @author 	HoonZzang
 * @name	SessionManagement
 * @explanation
 *  Plzec 프로젝트의 모든 서비스 클래스에서 공통으로 사용되어질 JOB Processing Method를 모듈화
 *  	
 * @field	HttpServletRequest request 	Constructor := @param request
 * 			HttpSession session 		Constructor := @field request.getSession
 * 			Forward forward 			Constructor := new
 * 			String page 				Constructor	:= "/index.jsp"
 * 			boolean isRedirect			Constructor := true
 * @param	HttpServletRequest request
 * 
 */
public class SessionManagement {
	protected HttpSession session;
	protected HttpServletRequest request;
	protected Forward forward;
	protected String page;
	protected boolean isRedirect;
	
	protected SessionManagement(HttpServletRequest request) {
		this.request = request;
		this.session = this.request.getSession();
		
		forward = new Forward();
		page = "/index.jsp";
		isRedirect = true;
	}
	
	/**
	 * @name	isAccess
	 * @explanation
	 * 	현재 로그인 여부 확인 : 현재 세션에 로그인 기록이 있는지 여부를 판단하여 결과값 반환
	 * @access		protected
	 * @param		null
	 * @return		boolean
	 * @variable	result := false
	 * @reference	session.getAttribute(String), session.getId(), 
	 * 				getSessionObject(String)
	 * @forward		Required
	 */
	protected boolean isAccess() {
		boolean result = false;

		if(this.session.getAttribute("sId") != null) {
			if(this.session.getId().equals(this.getSessionObject("sId"))) {
				if(this.getSessionObject("empCode") != null) {
					result = true;
				}
			}
		}
		forward.setPage("/index.jsp");
		forward.setRedirect(true);
		
		return result;
	}

	/**
	 * @name	isManager
	 * @explanation
	 * 	로그인 계정의 매니저 등급 여부 확인 : 세션에 저장되어 있는 empCode가 매니저 등급 인지 여부 확인
	 * @access		protected
	 * @param		null
	 * @return		boolean
	 * @variable	boolean result := false
	 * 				Connection con := null
	 * 				DAOModule daoModule := null	
	 * 				EmployeeBean bean := null
	 * @reference	DBConnection.dbcpConnect(), DBConnection.dbClose();
	 * 				daoModule.isManager(EmployeeBean),
	 * 				session.getAttribute(String), request.getRemoteAddr()
	 * @forward		None
	 */
	protected boolean isManager() {
		boolean result = false;
		Connection con = null;
		DAOModule daoModule = null;
		EmployeeBean bean = null;
		
		/* 로그인 계정이 관리자 여부 판단 */
		bean = new EmployeeBean();
		bean.setEcode(this.session.getAttribute("empCode").toString());
		bean.setIp(request.getRemoteAddr());
		
		con = DBConnection.dbcpConnect();
		daoModule = new DAOModule(con);
		
		result = daoModule.isManager(bean);
		
		DBConnection.dbClose();
		
		return result; 
	}
	
	/**
	 * @name	logOut
	 * @explanation
	 * 	로그아웃 요청시 데이터베이스와 세션의 로그아웃처리를 위한 Job Controller
	 * @access		protected
	 * @param		null
	 * @return		void
	 * @variable	
	 * @reference	logoutProcessing(), setInit()
	 * @forward		Required
	 */
	protected void logOut() {
		this.logOutProcessing();

		/* Session LogOut */
		this.setInit();

		/* Forward Set */
		forward.setPage(page);
		forward.setRedirect(isRedirect);
	}

	/**
	 * @name	Overloading logOut(String)
	 * @explanation
	 * 	서버요청방식이 GET방식인 경우 강제 로그아웃 후 사이트 첫 페이지 이동 Job Controller
	 * @access		public
	 * @param		String message
	 * @return		String
	 * @variable	String page := "/index.jsp?message="
	 * @reference	getSessionObject(String), logOutProcessing(), setInit()
	 * @forward		None
	 */
	public String logOut(String message) throws IOException {
		String page = "/index.jsp?message=";
		page += java.net.URLEncoder.encode(message, "UTF-8");

		if(this.getSessionObject("empCode") != null && this.getSessionObject("empCode") != "") {
			logOutProcessing();
			/* Session LogOut */
			this.setInit();
		}

		return page;
	}

	/**
	 * @name	logOutProcessing()
	 * @explanation
	 * 	로그아웃 요청시 데이터베이스에 로그아웃 기록을 하기위한 Job Method
	 * @access		proteced
	 * @param		
	 * @return		void
	 * @variable	Connection con := null, DAOModule daoModule := null,
	 * 				EmployeeBean bean := null
	 * @reference	getSessionObject(String), request.getRemoteAddr()
	 * 				DBConnection.dbcpConnect(), DBConnection.dbClose();
	 * 				daoModule.insHistory(EmployeeBean), 
	 * 				daoModule.setEndTransaction(boolean)
	 * @forward		None
	 */
	protected void logOutProcessing() {
		Connection con = null;
		DAOModule daoModule = null;
		EmployeeBean bean = null;
		
		/* bean에 Request 데이터 저장 */
		bean = new EmployeeBean();
		bean.setEcode(this.getSessionObject("empCode"));
		bean.setStatus(-1);
		bean.setIp(this.request.getRemoteAddr());

		/* Linkage : DAO ~ Connection */
		con = DBConnection.dbcpConnect();
		daoModule = new DAOModule(con);

		/* DB LogOut */
		daoModule.insHistory(bean);
		daoModule.setEndTransaction(true);

		/* Disconnect */
		DBConnection.dbClose();
	}
	
	/**
	 * @name	setRequestData(ArrayList<EmployeeBean>, String) 
	 * @explanation
	 * 	클라이언트의 로그인 후 모든 페이지의 Navigation영역에 표시할 정보를 리퀘스트에 담는 Job Method
	 * @access		proteced
	 * @param		ArrayList<EmployeeBean> list, String value
	 * @return		void
	 * @variable	
	 * @reference	makeMoveJob(String, String, boolean, String)
	 * @forward		None
	 */
	/* 로그인 후 페이지 상단 로그인 정보 작성 */
	protected void setRequestData(ArrayList<EmployeeBean> list, String value) {
		for(int i=0; i<list.size();i++) {
			/* 정보를 Request객체에 담기 */
			this.request.setAttribute("lev", (list.get(i).getLev().equals("M"))? "MANAGER": "ALBA");
			this.request.setAttribute("name", list.get(i).getName());
			this.request.setAttribute("ecode", list.get(i).getEcode());
			this.request.setAttribute("hdate", list.get(i).getDate());
			this.request.setAttribute("hip", " Connect IP : " + list.get(i).getIp());
			this.request.setAttribute("moveJob", makeMoveJob(list.get(i).getEcode(), list.get(i).getIp(), (list.get(i).getLev().equals("M"))? true: false, value));					
		}
	}

	/**
	 * @name		makeMoveJob(String, String, boolean, String) 
	 * @explanation
	 * 	클라이언트의 로그인 후 모든 페이지의 Navigation영역에 로그인 정보에 따라 나타낼
	 *  HTML개체 생성 TAG를 작성 후 리턴하는 Job Method
	 * @access		proteced
	 * @param		String ecode, String ip, boolean type, String value
	 * @return		String
	 * @variable	StringBuffer sb := new 
	 * @reference	
	 * @forward		None
	 */
	protected String makeMoveJob(String ecode, String ip, boolean type, String value) {
		StringBuffer sb = new StringBuffer();

		String function = (value.equals("상점관리"))? "moveManagerJob":"moveSalesJob";

		sb.append((type)? "<BUTTON id=\"manager\" class=\"btn\" onClick=\"" + function + "()\" onMouseOver=\"changeButton(this, 1, 'btnOver')\" onMouseOut=\"changeButton(this, 0, 'btn')\"> " + value + " </BUTTON>" : "");		
		sb.append(" <BUTTON id=\"log\" class=\"btn\" onClick=\"logOut()\" onMouseOver=\"changeButton(this, 1, 'btnOver')\" onMouseOut=\"changeButton(this, 0, 'btn')\"> 로그아웃 </BUTTON>");

		return sb.toString();
	}
	
	/**
	 * @name		moveSales() 
	 * @explanation
	 * 	상품 판매 페이지로 이동하는  Job Method
	 * @access		proteced
	 * @param		
	 * @return		void
	 * @variable	Connection con := null, DAOModule daoModule := null,
	 * 				EmployeeBean bean := null
	 * @reference	DBConnection.dbcpConnect(), DBConnection.dbClose()
	 * 				getSessionObject(String), request.getRemoteAddr(),
	 * 				daoModule.getAccessInfo(EmployeeBean)
	 * @forward		Required
	 *
	 **/
	protected void moveSales() {
		Connection con = null;
		DAOModule daoModule = null;
		EmployeeBean bean = null;
		
		/* bean에 Request 데이터 저장 */
		bean = new EmployeeBean();

		/* Linkage : DAO ~ Connection */
		con = DBConnection.dbcpConnect();
		daoModule = new DAOModule(con);
		bean.setEcode(this.getSessionObject("empCode"));
		bean.setIp(this.request.getRemoteAddr());
		setRequestData(daoModule.getAccessInfo(bean),"상점관리");

		/* Disconnect */
		DBConnection.dbClose();

		page = "/jsp/sales.jsp";
		isRedirect = false;

		forward.setPage(page);
		forward.setRedirect(isRedirect);
	}
	
	/**
	 * @name		setSessoinObject(String, String)
	 * @explanation
	 * 	HttpSession에 필요데이터를 저장하는 Job Method
	 * @access		proteced
	 * @param		String name, String value
	 * @return		void
	 * @variable	
	 * @reference	
	 * @forward		None
	 **/
	protected void setSessoinObject(String name, String value) {
		this.session.setAttribute(name, value);
	}
	
	/**
	 * @name		getSessionObject(String)
	 * @explanation
	 * 	HttpSession에 저장되어있는 Attribute중 파라미터로 넘겨받은 이름의 값을 리턴하는 Job Method
	 * @access		proteced
	 * @param		String name
	 * @return		String
	 * @variable	
	 * @reference	
	 * @forward		None
	 **/
	protected String getSessionObject(String name) {
		return this.session.getAttribute(name).toString();
	}
	
	/**
	 * @name		removeParam(String)
	 * @explanation
	 * 	HttpSession에 저장되어 있는 Attribute중 파라미터로 넘겨 받은 이름의 Attributr를 삭제하는  Job Method
	 * @access		proteced
	 * @param		String name
	 * @return		void
	 * @variable	
	 * @reference	
	 * @forward		None
	 **/
	protected void removeParam(String name) {
		this.session.removeAttribute(name);
	}
	
	/**
	 * @name		setInit()
	 * @explanation
	 * 	HttpSession을 초기화하는 Job Method
	 * @access		proteced
	 * @param		
	 * @return		void
	 * @variable	
	 * @reference	
	 * @forward		None
	 **/
	protected void setInit() {
		this.session.invalidate();
	}
}
