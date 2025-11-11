package com.ust.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	public static  Connection getDBConnection()
	{	
		Connection con=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); //register  and load driver
			 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/tripease","root","pass@word1"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	

}
