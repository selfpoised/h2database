# prepared statement
h2中是怎样实现prepared statement的呢？

在客户端调用stmt.executeQuery时会执行以下步骤：
1）SessionRemote.SESSION_PREPARE
该步骤关键Command command = session.prepareLocal(sql)

Session有一个SmallLRUCache<String, Command> queryCache成员，其作用就是以sql为键值，缓存解析后的Command。
如果在缓存中找到，并且在此期间数据没有被修改，那么使用缓存中既有Command，否则重新解析sql生成command并加入缓存。

SmallLRUCache<K, V> extends LinkedHashMap<K, V>，是一个非常有意思的数据结构，LinkedHashMap可以说是天然实现了LRU算法。
2）SessionRemote.COMMAND_EXECUTE_QUERY
在该阶段才真正调用前面生成的command