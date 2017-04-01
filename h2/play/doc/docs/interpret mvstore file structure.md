# interpretation of MVStore file structure 
h2database的存储引擎是mvstore，一般来说，一个数据库不论有几张表，都存储于一个文件。那么文件结构是怎样的呢？详细说明见官方[MVStore](http://www.h2database.com/html/mvstore.html)

### 静态结构
下图解析了mvstore文件结构及其参数意义：
![mvstore](http://ohz440knb.bkt.clouddn.com/h2mvstore.png)

下图是dump一个实际数据库文件：
![mvstore](http://ohz440knb.bkt.clouddn.com/h2mvstore_dump.png)

### 动态变更
那么如果用户修改了数据库，文件如何变化呢？[MVStore文件动态变更](http://www.h2database.com/html/mvstore.html#fileFormat)

###### 源码
```
MVStore s = MVStore.open("file_change_demo");
MVMap<Integer, String> map = s.openMap("data");
for (int i = 0; i < 400; i++) {
	map.put(i, "Hello");
}
s.commit();
for (int i = 0; i < 100; i++) {
	map.put(i, "Hi");
}
s.commit();

for (int i = 0; i < 1; i++) {
	map.put(i, "hahh");
}
s.commit();

s.close();
```

###### dump
dump可以直接使用MVStoreTool.dump函数
```
File file_change_demo, 20480 bytes, 0 MB
0000 fileHeader H:2,blockSize:1000,created:15b238862e4,format:1,fletcher:fe4a711a
1000 fileHeader H:2,blockSize:1000,created:15b238862e4,format:1,fletcher:fe4a711a

2000 chunkHeader chunk:1,block:2,len:1,map:1,max:1260,next:3,pages:4,root:4000039204,time:26,version:1
+0094 node, map 1, 2 entries, 32 bytes, maxLen 20
    140 children @ chunk 1 +00b4
    260 children @ chunk 1 +051a
+00b4 leaf, map 1, 140 entries, 1126 bytes, maxLen 600
+051a leaf, map 1, 260 entries, 2350 bytes, maxLen c00
+0e48 leaf, map 0, 3 entries, 55 bytes, maxLen 40
    map.1 = name:data
    name.data = 1
    root.1 = 4000002501
map 0: 55 bytes, 1%
map 1: 3508 bytes, 98%
+0f80 chunkFooter chunk:1,block:2,version:1,fletcher:84d0d5f6

3000 chunkHeader chunk:2,block:3,len:1,map:1,max:4e0,next:4,pages:3,root:800000fb8a,time:31,version:2
+0094 node, map 1, 2 entries, 32 bytes, maxLen 20
    140 children @ chunk 2 +00b4
    260 children @ chunk 1 +051a
+00b4 leaf, map 1, 140 entries, 826 bytes, maxLen 400
+03ee leaf, map 0, 4 entries, 173 bytes, maxLen c0
    chunk.1 = chunk:1,block:2,len:1,liveMax:c00,livePages:1,map:1,max:1260,next:3,pages:4,root:4000039204,time:26,version:1
    map.1 = name:data
    name.data = 1
    root.1 = 8000002501
map 0: 173 bytes, 16%
map 1: 858 bytes, 83%
+0f80 chunkFooter chunk:2,block:3,version:2,fletcher:95d0d8f6

4000 chunkHeader chunk:3,block:4,len:1,map:1,max:5a0,next:5,pages:3,root:c00000fc0e,time:34,version:3
+0094 node, map 1, 2 entries, 32 bytes, maxLen 20
    140 children @ chunk 3 +00b4
    260 children @ chunk 1 +051a
+00b4 leaf, map 1, 140 entries, 828 bytes, maxLen 400
+03f0 leaf, map 0, 5 entries, 288 bytes, maxLen 180
    chunk.1 = chunk:1,block:2,len:1,liveMax:c00,livePages:1,map:1,max:1260,next:3,pages:4,root:4000039204,time:26,version:1
    chunk.2 = chunk:2,block:3,len:1,liveMax:0,livePages:0,map:1,max:4e0,next:4,pages:3,root:800000fb8a,time:31,version:2
    map.1 = name:data
    name.data = 1
    root.1 = c000002501
map 0: 288 bytes, 25%
map 1: 860 bytes, 74%
+0f80 chunkFooter chunk:3,block:4,version:3,fletcher:a6d0dbf6

5000 eof

page size total: 5742 bytes, page count: 10, average page size: 574 bytes
map 0: 516 bytes, 8%
map 1: 5226 bytes, 91%
```