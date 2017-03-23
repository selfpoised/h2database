package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// copy from: https://github.com/codefollower/H2-Research/blob/master/my-docs/%E6%95%B4%E7%90%86%E5%90%8E%E7%9A%84%E6%96%87%E6%A1%A3/H2%E6%BA%90%E4%BB%A3%E7%A0%81%E8%B0%83%E8%AF%95%E8%BF%90%E8%A1%8C%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.java
public class JDBCTest {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        // Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/mydb", "DBA", "");
        // JdbcConnection
        // add 'MV_STORE=FALSE' to enable old pagestore
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/mydb1;TRACE_LEVEL_SYSTEM_OUT=3;", "SA", "");
        // JdbcStatement
        Statement stmt = conn.createStatement();
//        stmt.executeUpdate("DROP TABLE IF EXISTS my_table");
        //stmt.executeUpdate("CREATE TABLE IF NOT EXISTS my_table(name varchar(20), first varchar(10),id int)");
//        int j = 1;
//        for(int i=0;i<900000;i++){
//            if(i % 1000 == 0){
//                j++;
//            }
//            String v = String.format("VALUES('wh%d','hello%d',%d)",j,j,j);
//            stmt.executeUpdate("INSERT INTO my_table(name,first,id) " + v);
//        }

        int i=0;
        ResultSet rs = stmt.executeQuery("SELECT * FROM my_table where id < 10");
        //ResultSet rs = stmt.executeQuery("SELECT * FROM PUBLIC.SYS");
        while(rs.next()){
            //System.out.println(rs.getString(1));
            i++;
            System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getInt(3));
        }

        System.out.println(i + " records");

        stmt.close();
        conn.close();
    }
}
