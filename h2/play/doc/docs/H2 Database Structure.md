# H2 database structure

### table与mvstore映射
一般而言，用户创建的表比如table0，那么实际上在mvstore里面，它实际上存储的map名字叫table.3.

这个3是怎么来的呢？每个数据库有一个objectid的分配对象(BitField)，先给内部管理表分配序号，然后给用户创建的表分配序号。如果用户接着创建一个表，那么表的序号为4.

使用dump工具打印数据库(mvstore)详细信息的时候，是看不到表名的，只能看到table.3之类，实际上这些都保存在数据库内部的管理表里面，具体如何实现尚待研究，数据库内部管理表定义在MetaTable。

可以使用h2database commandline: jdbc:h2:tcp://localhost:9092/~/mydb1，查看相应数据库内部信息
### maxMemoryRows
是干什么的？

### sql where conditions
对于如下查询串：
```
SELECT * FROM my_table where id < 10
```
那么在Select.java 544行，condition即为"id < 10"， "Boolean.TRUE.equals(condition.getBooleanValue(session))"会对当前行是否符合条件进行判断。由于此处id列并未创建index，实际结果不到两万行，但是却要遍历全部90万数据。对于建立了index的列应该会好一些？