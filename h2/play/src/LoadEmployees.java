import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class LoadEmployees {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/employees;mode=mysql;", "SA", "");
        Statement stmt = conn.createStatement();

        try{
            File f = new File("D:\\work\\repository\\h2database\\datasets\\employees_db-full-1.0.6\\load_salaries.dump");
            List<String> lines = FileUtils.readLines(f);
            for(String line: lines){
                stmt.executeUpdate(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        stmt.close();
        conn.close();
    }
}
