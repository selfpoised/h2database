package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// copy from: https://github.com/codefollower/H2-Research/blob/master/my-docs/%E6%95%B4%E7%90%86%E5%90%8E%E7%9A%84%E6%96%87%E6%A1%A3/H2%E6%BA%90%E4%BB%A3%E7%A0%81%E8%B0%83%E8%AF%95%E8%BF%90%E8%A1%8C%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.java
public class JDBCTest1 {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        // Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/mydb", "DBA", "");
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/mydb1", "SA", "");
        Statement stmt = conn.createStatement();
        //stmt.executeUpdate("DROP TABLE IF EXISTS my_table");
        //stmt.executeUpdate("CREATE TABLE IF NOT EXISTS my_table(name varchar(20))");
        //stmt.executeUpdate("INSERT INTO my_table(name) VALUES('wh')");

        ResultSet rs = stmt.executeQuery("SELECT name FROM my_table");
        while(rs.next()){
            System.out.println(rs.getString(1));
        }

        stmt.close();
        conn.close();
    }
}
