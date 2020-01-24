package ctl.pos.plzec;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.services.pos.plzec.EmployeeAuth;
import beans.pos.plzec.Forward;
import goods.services.pos.plzec.GoodsManagements;
import sales.services.pos.plzec.Sales;

@WebServlet({"/MoveMain", "/Access", "/ManagerView", "/SalesView", "/LogOut", "/EmpSearch", "/NewEmpCode", "/EmpReg", "/EmpUpd","/EmpDel","/MoveGoodsMgr", "/GoodsInfo"})
public class FrontCtl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public FrontCtl() {
        super();
    }
    
    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String mapping = request.getRequestURI();
		EmployeeAuth ea = null;
		GoodsManagements gm = null;
		Sales sa = null;
		Forward forward = null;
		
//		System.out.println(request.getParameterValues("emp")[0]);
//		System.out.println(request.getParameter("empLevel"));
		
		switch(mapping) {
		case "/MoveMain":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(0);
			break;
		case "/Access":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(1);
			break;
		case "/ManagerView":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(2);
			break;
		case "/EmpSearch":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(12);
			break;
		case "/SalesView":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(3);
			break;
		case "/EmpReg":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(13);
			break;
		case "/NewEmpCode":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(14);
			break;
		case "/EmpUpd":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(15);
			break;
		case "/EmpDel":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(16);
			break;
		case "/MoveGoodsMgr":
			gm = new GoodsManagements(request);
			forward = gm.serviceCtl(0);
			break;
		case "/GoodsInfo":
			sa = new Sales(request);
			forward = sa.serviceCtl(0);
			break;
		case "/LogOut":
			ea = new EmployeeAuth(request);
			forward = ea.serviceCtl(-1);
			break;
		}
		
		if(forward.isRedirect()) {
			response.sendRedirect(forward.getPage());
		}else {
			RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPage());
			dispatcher.forward(request, response);
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EmployeeAuth ea = new EmployeeAuth(request);
		String page = ea.logOut("올바르지 않은 접근이어서 초기페이지로 이동합니다.");
		response.sendRedirect(page);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doProcess(request, response);
	}

}
