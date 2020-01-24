package sales.services.pos.plzec;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import beans.pos.plzec.Forward;
import beans.pos.plzec.GoodsBean;
import module.pos.plzec.DBConnection;
import module.pos.plzec.SessionManagement;
import sales.services.pos.plzec.DataAccessObject;

public class Sales extends SessionManagement{

	/* Instance Variable Zone*/
	private Connection con;
	private DataAccessObject dao;
	
	/* Constructor Zone */
	public Sales(HttpServletRequest request) {
		super(request);
	}
	
	public Forward serviceCtl(int serviceCode) {

		switch(serviceCode) {
		case 0:
			getGoodsInfo();
			break;
		
		}
		
		return forward;
	}
	
	private void getGoodsInfo() {
		String temp = null;
		ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
		ArrayList<GoodsBean> search = null;
		/* 변수선언 */
		GoodsBean bean = new GoodsBean();
		
		
		/* 커넥션 연결 */
		con = DBConnection.dbcpConnect();
		dao = new DataAccessObject(con);
		bean.setGoodsCode(this.request.getParameterValues("goodsCode")[0]);
		bean.setGoodsStock(1);
		gList.add(bean);
		temp = "'"+gList.get(0).getGoodsCode()+"'";
		for(int i=1; i<this.request.getParameterValues("goodsCode").length; i++) {
			bean = new GoodsBean();
			bean.setGoodsCode(this.request.getParameterValues("goodsCode")[i]);
			temp += ",'"+bean.getGoodsCode()+"'";
			bean.setGoodsStock(Integer.parseInt(this.request.getParameterValues("ea")[i-1]));
			gList.add(bean);
			if(bean.getGoodsCode().equals(gList.get(0).getGoodsCode())) {
				gList.remove(0);
				bean.setGoodsStock(bean.getGoodsStock()+1);
			}
		}
		bean = new GoodsBean();
		bean.setGoodsCode(temp);

		
		/* DAO 호출 */
		search = dao.getSalesInfo(bean);
		for(int i = 0 ; i < search.size() ; i++) {
			for(int j = 0 ; j < search.size() ; j++) {
				if(gList.get(j).getGoodsCode().equals(search.get(i).getGoodsCode())) {
					gList.get(j).setGoodsName(search.get(i).getGoodsName());
					gList.get(j).setGoodsPrice(search.get(i).getGoodsPrice());
					gList.get(j).setSalesPrice(search.get(i).getSalesPrice());
					break;
				}
			}
		}
		this.request.setAttribute("salesList", makeTable(gList));
		
		/* 커넥션 닫기  및 트랜젝션 처리 */
		DBConnection.dbClose();
		
		/* 클라이언트로 전송할 Forward 작성 */
		this.moveSales();
		
	}
	
	private String makeTable(ArrayList<GoodsBean> list) {
		StringBuffer sb = new StringBuffer();
		String oddEven = null;
		String color = null;
		
		
		sb.append("<TABLE id=\"lists\">");
		sb.append("<TR> <TH>상품코드</TH> <TH>상품이름</TH> <TH>상품가격</TH> <TH>판매가격</TH> <TH>수량</TH> <TH>판매합계</TH> </TR>");
		
		for(int i=0; i<list.size(); i++) {
			/* 홀수번째 레코드와 짝수번째 레코드의 css 값 분기 */
			if(i%2 == 0) {
				oddEven = "odd";
				color = "\'rgba(246, 246, 246, 0.8)\'";
			}else {
				oddEven = "even";
				color = "\'rgba(217, 229, 255, 0.8)\'";
			}
			
			sb.append("<TR class=\""+ oddEven + "\" onMouseOver=\"selectedRow" + "(this, \'rgba(255, 228, 0, 0.8)\')\" onMouseOut=\"selectedRow" + "(this, " + color + ")\">");
			sb.append("<TD name=\"goodsCode\">"+list.get(i).getGoodsCode()+"</TD>");
			sb.append("<TD>");
			sb.append(list.get(i).getGoodsName());
			sb.append("</TD>");
			sb.append("<TD>");
			sb.append(list.get(i).getGoodsPrice());
			sb.append("</TD>");			
			sb.append("<TD name=\"money\">");
			sb.append(list.get(i).getSalesPrice());
			sb.append("</TD>");
			sb.append("<TD>");
			sb.append("<input type=\"number\" name=\"ea\" value=\""+list.get(i).getGoodsStock()+"\" min=\"1\" max=\"100\" step=\"1\" onchange=\"salesSum(this,"+i+")\" /");
			sb.append("</TD>");
			sb.append("<TD>");
			sb.append("<input type=\"text\" name=\"amount\" value="+list.get(i).getSalesPrice()*list.get(i).getGoodsStock()+" /> ");
			sb.append("</TD>");			
			sb.append("</TR>");
			
		}	
		sb.append("</TABLE>");
		
		return sb.toString();		
	}
	
}
