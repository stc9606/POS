package goods.services.pos.plzec;

import java.sql.Connection;
import java.util.ArrayList;

import beans.pos.plzec.GoodsBean;
import module.pos.plzec.DAOModule;

class DataAccessObject extends DAOModule {

	DataAccessObject(Connection con) {
		super(con);
	}

	/* 상품 수정 */
	boolean isUpdGoods(GoodsBean gb) {
		boolean result = false;
		String query = "UPDATE GO SET GO_NAME = ?, GO_PRICE = ?, GO_STOCK = ? WHERE GO_CODE = ?";
		
		try {
			pStatement = this.connection.prepareStatement(query);
			pStatement.setNString(4, gb.getGoodsCode());
			pStatement.setNString(1, gb.getGoodsName());
			pStatement.setInt(2, gb.getGoodsPrice());
			pStatement.setInt(3, gb.getGoodsStock());
			
			result = (pStatement.executeUpdate() == 1)?  true : false;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
					
		return result;
	}
	
	/* 전체 상품 조회 */
	ArrayList<GoodsBean> getGoodsList(){
		/* ArrayList 선언 및 할당*/
		ArrayList<GoodsBean> list = new ArrayList<GoodsBean>();
		GoodsBean bean = null;
		String query = "SELECT GO_CODE AS GOODSCODE, GO_NAME AS GOODSNAME, "
				+ "GO_PRICE AS GOODSPRICE, GO_STOCK AS GOODSSTOCK FROM GO";
		
		try {
			pStatement = this.connection.prepareStatement(query);

			rs = pStatement.executeQuery();
			while(rs.next()) {
				bean = new GoodsBean();
				bean.setGoodsCode(rs.getNString("GOODSCODE"));
				bean.setGoodsName(rs.getNString("GOODSNAME"));
				bean.setGoodsPrice(rs.getInt("GOODSPRICE"));
				bean.setGoodsStock(rs.getInt("GOODSSTOCK"));
				list.add(bean);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	/* 조건 상품 조회 */
	ArrayList<GoodsBean> getGoodsList(GoodsBean gb){
		/* ArrayList 선언 및 할당*/
		ArrayList<GoodsBean> list = new ArrayList<GoodsBean>();
		GoodsBean bean = null;
		StringBuffer query = new StringBuffer();
		String price = null;
		boolean check = false;
		int index = 0;
		
		query.append("SELECT GO_CODE AS GOODSCODE, GO_NAME AS GOODSNAME, "
				+ "GO_PRICE AS GOODSPRICE, GO_STOCK AS GOODSSTOCK FROM GO WHERE");
	
		if(gb.getMinPrice() != -1 && gb.getMaxPrice() != -1) {
			price = " GO_PRICE >= ? AND GO_PRICE <= ?";
		}else if(gb.getMinPrice() != -1) {	price = " GO_PRICE >= ?";}
		else if(gb.getMaxPrice() != -1) { price = " GO_PRICE <= ?";}
		
		if(!gb.getGoodsCode().equals("")) {
			check = true;
			if(!gb.getGoodsName().equals("")) {
				query.append(" (GO_CODE = ?");
				query.append(" OR GO_NAME LIKE '%' || ? || '%')");
			}else { query.append(" GO_CODE = ?");}
		}else if(!gb.getGoodsName().equals("")) {
			check = true;
			query.append(" GO_NAME LIKE '%' || ? || '%'");
		}
		query.append((price != null)? ((check)? " AND": "") + price : "");
	
		try {
			pStatement = this.connection.prepareStatement(query.toString());
			
			if(!gb.getGoodsCode().equals("")) {
				index++;
				if(!gb.getGoodsName().equals("")) {
					index++;
				pStatement.setNString(index-1, gb.getGoodsCode());
				pStatement.setNString(index, gb.getGoodsName());
				}else { pStatement.setNString(index, gb.getGoodsCode());}
			}else if(!gb.getGoodsName().equals("")) {
				index++;
				pStatement.setNString(index, gb.getGoodsName());
			}
			
			if(gb.getMinPrice() != -1 && gb.getMaxPrice() != -1) {
				index++;
				pStatement.setInt(index, gb.getMinPrice());
				pStatement.setInt(index+1, gb.getMaxPrice());
			}else if(gb.getMinPrice() != -1) {index++;	pStatement.setInt(index, gb.getMinPrice());}
			else if(gb.getMaxPrice() != -1) {index++; pStatement.setInt(index, gb.getMaxPrice());}
			
			
			rs = pStatement.executeQuery();
			while(rs.next()) {
				bean = new GoodsBean();
				bean.setGoodsCode(rs.getNString("GOODSCODE"));
				bean.setGoodsName(rs.getNString("GOODSNAME"));
				bean.setGoodsPrice(rs.getInt("GOODSPRICE"));
				bean.setGoodsStock(rs.getInt("GOODSSTOCK"));
				list.add(bean);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
