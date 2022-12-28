package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DBreader {
	Connection conn = null;
	Statement stmt = null;
	ResultSet resultSet = null;
	ResultSetMetaData rsMetaData;

	public ResultSet dbreader(String query) throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/orcl", "hr", "oracle");
		stmt = conn.createStatement();
		String q = "\"" + query + "\"";

		resultSet = stmt.executeQuery(query);
		rsMetaData = resultSet.getMetaData();
		return resultSet;
	}
}
