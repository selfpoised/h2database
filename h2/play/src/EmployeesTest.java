import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmployeesTest {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        // Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/mydb", "DBA", "");
        // JdbcConnection
        // add 'MV_STORE=FALSE' to enable old pagestore
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/employees;TRACE_LEVEL_SYSTEM_OUT=3;", "SA", "");
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
        ResultSet rs = stmt.executeQuery("SELECT * FROM employees limit 10");
        //ResultSet rs = stmt.executeQuery("SELECT * FROM PUBLIC.SYS");
        while(rs.next()){
            //System.out.println(rs.getString(1));
            i++;
            System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3)+ "," + rs.getString(4));
        }

        System.out.println(i + " records");

        rs = stmt.executeQuery("SELECT * FROM employees limit 5");

        stmt.close();
        conn.close();
    }
}
