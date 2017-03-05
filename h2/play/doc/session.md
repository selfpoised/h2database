# Session

session相关的对象，客户端是SessionRemote，服务端是Session，主要讲服务端的session管理。

### in-memory 管理数据库
该数据库是用来进行内部状态管理用的，名称如：jdbc:h2:mem:management_db_5000，其中5000是tcp server 监听端口。

那么，何时初始化management_db_5000数据库呢？
```
org.h2.tools.Server.main->
	org.h2.tools.Server.runTool->
    	org.h2.server.TcpServer.start->
        	org.h2.server.TcpServer.initManagementDb
```
我在initManagementDb添加了一条PreparedStatement： managementDbQuery = conn.prepareStatement("SELECT * FROM SESSIONS")，用以在tcp server 添加或者删除session时，query management_db_5000内尚存的active sessions。

那么何时向management_db_5000添加新的session呢?
```
org.h2.server.TcpServerThread.run->
	server.addConnection(threadId, originalURL, ci.getUserName())
```
tcp server会为每个client链接启动一个TcpServerThread，在run函数中首先和client通信初始化(详见3.1：客户端与服务端tcp通信.md)后，且在确定session id之前，就创建了session，并添加到了management_db_5000。

### 服务端session的创建
```
org.h2.server.TcpServerThread.run->
	transfer.init()...
    ...
    session = Engine.getInstance().createSession(ci)
    	Engine.createSession->
        	createSessionAndValidate->
            	openSession(ci)->
                	openSession(ci, ifExists, cipher)->
                    	database.createSession(user)->
                        	Session session = new Session(this, user, ++nextSessionId)
```
由此可见，每一个client触发一个server 端的TcpServerThread，并新建一个session对象
```
Session(Database database, User user, int id)
```
无论有多少个client请求database A，A对象有且仅有一个；对于多个client请求，如果database和user相同，那么会生成各自独立的session。id由database同步自增。

### h2 console (command line)
使用上述工具可以连接：jdbc:h2:mem:management_db_5000，但是查询sessions表，内容貌似不一致？待进一步研究