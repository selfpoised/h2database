package src;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.FileUtils;

// copy from: https://github.com/codefollower/H2-Research/blob/master/my-docs/%E6%95%B4%E7%90%86%E5%90%8E%E7%9A%84%E6%96%87%E6%A1%A3/H2%E6%BA%90%E4%BB%A3%E7%A0%81%E8%B0%83%E8%AF%95%E8%BF%90%E8%A1%8C%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.java
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
