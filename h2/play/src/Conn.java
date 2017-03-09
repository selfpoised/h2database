/**
 * Created by wanghao on 2017/3/3.
 */
import java.sql.*;
public class Conn {
    public static void main(String[] a)
            throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.
                getConnection("jdbc:h2:~/test", "sa", "");
        // add application code here
        conn.close();
    }
}