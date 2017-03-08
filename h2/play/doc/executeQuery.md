当客户端执行executeQuery时，究竟干了什么呢？

```
// JdbcConnection
Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:5000/~/mydb", "SA", "");
// JdbcStatement
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT name FROM my_table");

JdbcStatement.executeQuery->
	CommandInterface command = conn.prepareCommand // CommandRemote
    command.executeQuery->
    	transfer.writeInt(SessionRemote.COMMAND_EXECUTE_QUERY)->
        result = new ResultRemote(session, transfer, objectId, columnCount, fetch)->
        	fetchRows(false)->
            	1.大部分情况下可以直接读取数据了
            	2.数据量大时，transfer.writeInt(SessionRemote.RESULT_FETCH_ROWS)
```

