# H2 database structure

### table与mvstore映射
一般而言，用户创建的表比如table0，那么实际上在mvstore里面，它实际上存储的map名字叫table.3.

这个3是怎么来的呢？每个数据库有一个objectid的分配对象(BitField)，先给内部管理表分配序号，然后给用户创建的表分配序号。如果用户接着创建一个表，那么表的序号为4.

使用dump工具打印数据库(mvstore)详细信息的时候，是看不到表名的，只能看到table.3之类，实际上这些都保存在数据库内部的管理表里面，具体如何实现尚待研究，数据库内部管理表定义在MetaTable。

可以使用h2database commandline: jdbc:h2:tcp://localhost:9092/~/mydb1，查看相应数据库内部信息

##### 临时表
数据库初始化后创建，顾名思义，包含实时动态数据，非持久化
```
name.openTransactions = 1
name.undoLog = 2
name.lobData = 6
name.lobMap = 4
name.lobRef = 5
```
##### 非临时表
为用户创建的表，以及索引。
```
name.index.0 = 8
name.table.0 = 3 
name.table.3 = 9
name.table.4 = 11
```
table.0是"PUBLIC.SYS"表，数据库启动时创建，一般映射关系是table.0->map.3，而MVStore的meta数据映射为map.0，所以二者不同。如果说map.0包含了数据的meta data，那么public.sys中包含的应该是创建和修改数据库表schema的命令，比如建表，或者创建索引之类数据，这些数据是在Database类初始化时建立的，dump数据库文件可以发现，map.3数据变化较少，这与schema改变较少是相一致的。


比如，table.3 映射为用户数据表"mytable",table.4 映射为用户数据表"mytable2"等等。
此处table后缀3和4在用户表创建时，即固定，存储在information_schema.tables列id项



### maxMemoryRows
是干什么的？

### sql where conditions
对于如下查询串：
```
SELECT * FROM my_table where id < 10
```
那么在Select.java 544行，condition即为"id < 10"， "Boolean.TRUE.equals(condition.getBooleanValue(session))"会对当前行是否符合条件进行判断。由于此处id列并未创建index，实际结果不到两万行，但是却要遍历全部90万数据。对于建立了index的列应该会好一些？