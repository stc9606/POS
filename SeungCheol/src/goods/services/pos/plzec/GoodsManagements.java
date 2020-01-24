package goods.services.pos.plzec;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import beans.pos.plzec.EmployeeBean;
import beans.pos.plzec.Forward;
import beans.pos.plzec.GoodsBean;
import module.pos.plzec.DBConnection;
import module.pos.plzec.SessionManagement;

public class GoodsManagements extends SessionManagement{
	/* Instance Variable Zone*/
	private Connection con;
	private DataAccessObject dao;
	
	/* Constructor Zone */
	public GoodsManagements(HttpServletRequest request) {
		super(request);
	}
	
	/* Service Controller */
	public Forward serviceCtl(int serviceCode) {
		/* *
		 *    JOB		SCode	 CtlMethod  			RefMethod
		 * 	mapping				 Destination			isRedirect
		 * ---------------------------------------------------------------------------------   
		 * 상품관리페이지	  0		 moveGoods()			ArrayList<goodsBean> searchGoods(bean)
		 * 						 req.param("JobCode");	I void setRequestData(list, value)
		 * 											
		 * /MoveGoodsMgr		 goodsMgr.jsp			false
		 * ---------------------------------------------------------------------------------
		 * 
		 * 상품판매페이지	  1		 I moveSales()
		 * 
		 * 상품조회(AG, SG)	 	 searchGoods(bean)	String makeTable(ArrayList<GoodsBean> bean)
		 * 
		 * 상품등록(RE)			 regGoods(bean)		 
		 * 		  
		 * 상품수정(UP)			 updGoods(bean)
		 * 		  
		 * 상품삭제(DE)			 delGoods(bean)
		 * 		  
		 * 로그아웃		  -1	 moveSales()	 
		 * /LogOut	  		 	 /index.jsp			 true
		 *  		
		 * */
		
		switch(serviceCode) {
		case 0:
			moveGoods();
			break;
		}
		return forward;
	}
	
	private void moveGoods() {
		/* 변수 선언 */
		EmployeeBean eb = new EmployeeBean();
		GoodsBean gb = null;
		ArrayList<GoodsBean> list = new ArrayList<GoodsBean>();
		String jobCode = null;
		
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);
		
		/* 페이지 상단 로그인 정보 추출을 위한 EmployeeBean 정보 저장 */
		eb.setEcode(getSessionObject("empCode"));
		eb.setIp(this.request.getRemoteAddr());
		setRequestData(dao.getAccessInfo(eb),"상품판매");
		
		DBConnection.dbClose();
		
		/* 리퀘스트로부터 JobCode  추출 */
		jobCode = this.request.getParameter("JobCode");
		
		System.out.println(jobCode);
		switch(jobCode) {
		case "AG":
			list = searchGoods(gb);
			break;
		case "SG":
			gb = new GoodsBean();
			/* 검색조건을 리퀘스트 파라미터로부터 추출하여 상품빈에 저장*/
			gb.setGoodsCode(this.request.getParameterValues("goodsInfo")[0]);
			gb.setGoodsName(this.request.getParameterValues("goodsInfo")[1]);
			gb.setMinPrice((this.request.getParameterValues("goodsInfo")[2].equals(""))? -1 : Integer.parseInt(this.request.getParameterValues("goodsInfo")[2]));
			gb.setMaxPrice((this.request.getParameterValues("goodsInfo")[3].equals(""))? -1 : Integer.parseInt(this.request.getParameterValues("goodsInfo")[3]));
		
			list = searchGoods(gb);	
			break;
		case "UG":
			gb = new GoodsBean();
			gb.setGoodsCode(this.request.getParameterValues("goods")[0]);
			gb.setGoodsName(this.request.getParameterValues("goods")[1]);
			gb.setGoodsPrice(Integer.parseInt(this.request.getParameterValues("goods")[2]));
			gb.setGoodsStock(Integer.parseInt(this.request.getParameterValues("goods")[3]));
			updateGoods(gb);
			list = searchGoods(null);
			break;
		}
		
		/* Make HTML Table */
		request.setAttribute("goodsList", makeTable(list));
		
		page = "/jsp/goodsMgr.jsp";
		isRedirect = false;
		
		forward.setPage(page);
		forward.setRedirect(isRedirect);
		
		
		
	}
	
	private void updateGoods(GoodsBean gb) {
		/* 커넥션 할당 후 dao 전달 : 데이터 전달 받기 */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);
		
		dao.isUpdGoods(gb);
		
		/* 커넥션 닫기 */
		dao.setEndTransaction(true);
		DBConnection.dbClose();		
		
		
	}
	
	private ArrayList<GoodsBean> searchGoods(GoodsBean bean){
		/* DAO로부터 전달받을 상품리스트를 저장할 ArrayList 선언 
		 *  DAO에서 이미 ArrayList는 생성되어 있으므로 선언만 하고 참조만 연결. 
		 * */
		ArrayList<GoodsBean> list = null;
		
		/* 커넥션 할당 후 dao 전달 : 데이터 전달 받기 */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);
		
		if(bean == null) {
			/* 전체상품조회 */
			list = dao.getGoodsList();
		}else {
			/* 조건상품조회 */
			list = dao.getGoodsList(bean);
		}
		
		/* 커넥션 닫기 */
		DBConnection.dbClose();
		
		return list;
	}
	
	/* 상품리스트 --> Making Table */
	private String makeTable(ArrayList<GoodsBean> goodsList) {
		StringBuffer sb = new StringBuffer();
		String oddEven = null;
		String color = null;

		sb.append("<TABLE id=\"lists\">");
		sb.append("<TR>");
		sb.append("<TH>상품코드</TH>");
		sb.append("<TH> 상품명 </TH>");
		sb.append("<TH>상품가격</TH>");
		sb.append("<TH>현재재고</TH>");
		sb.append("</TR>");

		for(int i=0; i<goodsList.size(); i++) {
			/* 홀수번째 레코드와 짝수번째 레코드의 CSS 값 분기*/
			if(i%2 == 0) {
				oddEven = "odd";
				color = "\'rgba(246, 246, 246, 0.8)\'"; 	
			}else {
				oddEven =  "even";
				color = "\'rgba(217, 229, 255, 0.8)\'";
			}

			sb.append("<TR class=\""+ oddEven +"\" onClick=\"ctlLightsBox(\'lightBoxCanvas" + "\', \'lightBoxView\', \'goods\', 1, [\'" 
						+ goodsList.get(i).getGoodsCode() + "\', \'" + goodsList.get(i).getGoodsName() + "\', " + goodsList.get(i).getGoodsPrice() + ", \'" + goodsList.get(i).getGoodsStock() + "\'])\" onMouseOver=\"selectedRow" + "(this, \'rgba(255, 228, 0, 0.8)\')\" onMouseOut=\"selectedRow"	+ "(this, " + color + ")\">");
			sb.append("<TD>" + goodsList.get(i).getGoodsCode() + "</TD>");
			sb.append("<TD>" + goodsList.get(i).getGoodsName() + "</TD>");
			sb.append("<TD>" + goodsList.get(i).getGoodsPrice() + "</TD>");
			sb.append("<TD>" + goodsList.get(i).getGoodsStock() + "</TD>");
			sb.append("</TR>");
		}

		sb.append("</TABLE>");

		return sb.toString();
	}	
}
