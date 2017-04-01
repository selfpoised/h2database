import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MYDB {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/mydb;TRACE_LEVEL_SYSTEM_OUT=3;", "SA", "");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS my_table");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS my_table(name varchar(20), first varchar(10),id int)");
        String v = String.format("VALUES('wh','hello',1)");
        stmt.executeUpdate("INSERT INTO my_table(name,first,id) " + v);

        ResultSet rs = stmt.executeQuery("SELECT * FROM my_table");
        while(rs.next()){
            System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3));
        }

        stmt.close();
        conn.close();
    }
}
