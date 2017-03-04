# TCP 通信

### 客户端tcp调用过程
![客户端tcp](http://ohz440knb.bkt.clouddn.com/h2-tcp%E5%AE%A2%E6%88%B7%E7%AB%AFtcp%E8%BF%9E%E6%8E%A5%E8%BF%87%E7%A8%8B.png)

### 服务端tcp调用过程
![客户端tcp](http://ohz440knb.bkt.clouddn.com/%E6%9C%8D%E5%8A%A1%E7%AB%AFtcp%E4%BE%A6%E5%90%AC%E8%BF%87%E7%A8%8B.png)

### 通信过程
1. 客户端初始化
client:
```
int minTcpVersion; //6
int maxTcpVersion; //16
String db;
String url;
String userName;
String userpasswordhash;
String filepasswordhash;
keys.length 个
{
	String key;
    String value;
}
```
server响应：
```
STATUS_ERROR
		String sqlstate
    	String message
        String sql
        int errorCode
        String stackTrace
        STATUS_CLOSED
STATUS_OK_STATE_CHANGED
        int clientVersion
STATUS_OK
	    int clientVersion
```

2. session id
client:
```
int session_set_id
String sessionId
```
server响应：
```
STATUS_ERROR
		String sqlstate
    	String message
        String sql
        int errorCode
        String stackTrace
STATUS_CLOSED
STATUS_OK_STATE_CHANGED
        boolean autoCommit
STATUS_OK
	    boolean autoCommit
```

3. prepare阶段
query 和 update具体调用之前会先prepare
client:
```
int command // SESSION_PREPARE_READ_PARAMS2 or SESSION_PREPARE_READ_PARAMS
int id
String sql
```
server响应：
```
boolean isQuery
boolean readonly
int paramCount
Metadata[] params
```

4. executeQuery
client:
```
int command
int id
int objectId
int maxRows
int fetch
```
server响应：
```
STATUS_ERROR
		String sqlstate
    	String message
        String sql
        int errorCode
        String stackTrace
STATUS_CLOSED
STATUS_OK_STATE_CHANGED
        int columnCount
        int rowCount
STATUS_OK
	    int columnCount
        int rowCount
```

5. RESULT_FETCH_ROWS
该命令是紧接着executeQuery成功后，由客户端自动发出的，查询成功后，自然要求结果
client:
```
int command
int id
int fetch
```
server响应：
```
fetch 个
{
boolean row
Value[]    values // a row of columns.length
}
```

6. executeUpdate
client:
```
int command //COMMAND_EXECUTE_UPDATE
int id
```
server响应：
```
int updateCount
boolean autoCommit
```

