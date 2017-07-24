package test;

import java.sql.Connection;
import java.sql.DriverManager;


public class Test {
	public static void main(String[] args) throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test"
				
				,"postgres","123456");
		System.out.println(conn);
		
		
	}
}
