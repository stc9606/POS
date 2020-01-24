package sales.services.pos.plzec;

import java.sql.Connection;
import java.util.ArrayList;

import beans.pos.plzec.GoodsBean;
import module.pos.plzec.DAOModule;

public class DataAccessObject extends DAOModule {
	
	DataAccessObject(Connection con) {
		super(con);
	}
	
	ArrayList<GoodsBean> getSalesInfo(GoodsBean bean) {
		ArrayList<GoodsBean> list = new ArrayList<GoodsBean>();	
		GoodsBean gb = null;
		String query = "SELECT GCODE, GNAME, GPRICE, SALESPRICE FROM SALESINFO WHERE GCODE IN("+bean.getGoodsCode()+")";
		try {
			pStatement = this.connection.prepareStatement(query);
			
			rs = pStatement.executeQuery();			
			while(rs.next()) {
			gb = new GoodsBean();
			gb.setGoodsCode(rs.getNString("GCODE"));
			gb.setGoodsName(rs.getNString("GNAME"));
			gb.setGoodsPrice(rs.getInt("GPRICE"));
			gb.setSalesPrice(rs.getInt("SALESPRICE"));
			list.add(gb);			
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
