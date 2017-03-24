/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */

import java.sql.*;

/**
 * This sample application shows how to define and use
 * custom (user defined) functions in this database.
 */
public class EmployeesFunctions {

    /**
     * This method is called when executing this sample application from the
     * command line.
     *
     * @param args the command line parameters
     */
    public static void main(String... args) throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost:5000/~/employees;TRACE_LEVEL_SYSTEM_OUT=3;", "sa", "");
        Statement stat = conn.createStatement();

        // Using a custom Java function
        stat.execute("CREATE ALIAS EMP_DEPT_ID " +
                "FOR \"EmployeesFunctions.emp_dept_id\" ");

        stat.close();
        conn.close();
    }

    public static String emp_dept_id(Connection conn, int employee_id) throws SQLException {
        Statement stmt = conn.createStatement();

        String query = "select max(from_date) from dept_emp where emp_no = " + employee_id;
        ResultSet rs = stmt.executeQuery(query);
        String maxDate = "";
        while(rs.next()){
            maxDate = rs.getDate(1).toString();
            break;
        }

        if(!maxDate.isEmpty()){
            query = "select dept_no from dept_emp where emp_no = " + employee_id + " and from_date = '" + maxDate + "' limit 1";
            rs = stmt.executeQuery(query);
            while(rs.next()){
                return rs.getString(1);
            }
        }

        return "";
    }

    public static ResultSet emp_dept_id2(Connection conn, int employee_id) throws SQLException {
        Statement stmt = conn.createStatement();

        String query = "select max(from_date) from dept_emp where emp_no = " + employee_id;
        ResultSet rs = stmt.executeQuery(query);
        String maxDate = null;
        while(rs.next()){
            maxDate = rs.getDate(1).toString();
            break;
        }

        if(!maxDate.isEmpty()){
            String query0 = "select dept_no from dept_emp where emp_no = " + employee_id + " and from_date = '" + maxDate + "' limit 1";
            ResultSet rs0 = stmt.executeQuery(query0);
            return rs0;
        }

        return null;
    }

    public static String emp_dept_name(Connection conn, int employee_id) throws SQLException {
        String query = "select dept_name from departments where dept_no = emp_dept_id(" + employee_id + ")";
        ResultSet rs = conn.createStatement().executeQuery(query);
        while(rs.next()){
            return rs.getString(1);
        }

        return "";
    }

    public static String emp_name(Connection conn, int employee_id) throws SQLException {
        String query = "select concat(first_name, ' ', last_name) as name from employees where emp_no = " + employee_id;
        ResultSet rs = conn.createStatement().executeQuery(query);
        while(rs.next()){
            return rs.getString(1);
        }

        return "";
    }

    public static String current_manager(Connection conn, String dept_id) throws SQLException {
        Statement stmt = conn.createStatement();

        String query = "select max(from_date) from  dept_manager  where  dept_no = '" + dept_id + "'";
        ResultSet rs = stmt.executeQuery(query);
        String maxDate = "";
        while(rs.next()){
            maxDate = rs.getDate(1).toString();
            break;
        }

        if(!maxDate.isEmpty()){
            query = String.format("select emp_name(emp_no) from  dept_manager where  dept_no = '%s' and from_date = '%s' limit 1",dept_id,maxDate);
            rs = stmt.executeQuery(query);
            while(rs.next()){
                return rs.getString(1);
            }
        }

        return "";
    }

    // this is a stored procedure
    public static void show_departments(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.executeUpdate("DROP TABLE IF EXISTS department_max_date;");
        stmt.executeUpdate("DROP TABLE IF EXISTS department_people;");
        stmt.executeUpdate("CREATE TEMPORARY TABLE department_max_date\n" +
                "    (\n" +
                "        emp_no int not null primary key,\n" +
                "        dept_from_date date not null,\n" +
                "        dept_to_date  date not null,\n" +
                "        KEY (dept_from_date, dept_to_date)\n" +
                "    );");
        stmt.executeUpdate("INSERT INTO department_max_date\n" +
                "    SELECT\n" +
                "        emp_no, max(from_date), max(to_date) \n" +
                "    FROM\n" +
                "        dept_emp\n" +
                "    GROUP BY\n" +
                "        emp_no;");
        stmt.executeUpdate("CREATE TEMPORARY TABLE department_people \n" +
                "    (\n" +
                "        emp_no int not null,\n" +
                "        dept_no char(4) not null,\n" +
                "        primary key (emp_no, dept_no)\n" +
                "    );");
        stmt.executeUpdate("insert into department_people \n" +
                "    select dmd.emp_no, dept_no\n" +
                "    from \n" +
                "        department_max_date dmd \n" +
                "        inner join dept_emp de \n" +
                "            on dmd.dept_from_date=de.from_date \n" +
                "            and dmd.dept_to_date=de.to_date\n" +
                "            and dmd.emp_no=de.emp_no;");

        stmt.executeQuery("SELECT  v.dept_no,dept_name,manager, count(*)\n" +
                "              from v_full_departments  as v\n" +
                "                   inner join department_people on department_people.dept_no=v.dept_no\n" +
                "              group by v.dept_no");

        stmt.executeUpdate("DROP TABLE department_max_date;");
        stmt.executeUpdate("DROP TABLE department_people;");
    }
}
