package module.pos.plzec;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnection {
	public static Connection con = null;
		
	public static Connection dbcpConnect() {
		try {
			Context init = new InitialContext();
			DataSource ds =
					(DataSource)init.lookup("java:comp/env/SeungCheol");
			con = ds.getConnection();
		}catch(Exception e) {
			e.printStackTrace();
			try {if(!con.isClosed()) {
				con.close();}}catch(Exception e1) {e1.printStackTrace();}
		}
		return con;
	}
	
	public static void dbClose() {
		try {if(!con.isClosed()) { con.close();}}catch(Exception e) {e.printStackTrace();}
	}
		
}
