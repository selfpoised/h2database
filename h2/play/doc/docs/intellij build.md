# build h2database source code under intellij idea

### pom.xml
h2代码并未直接使用mvn编译，而是使用了mvnw。并且其目标码为java1.6，而其mvn又依赖1.7。为了解决这个问题其使用了maven-toolchains-plugin：
```
<!-- Maven requires at least JRE 1.7 but we want to build with JDK 1.6 -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-toolchains-plugin</artifactId>
        <version>1.1</version>
        <executions>
          <execution>
            <goals>
              <goal>toolchain</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <toolchains>
            <jdk>
              <version>1.6</version>
            </jdk>
          </toolchains>
        </configuration>
      </plugin>
```
此外，为了检查代码中没有使用jdk1.6之外的东西，又用了如下插件：
```
<!-- Make sure we are not using anything outside JDK 1.6 -->
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>animal-sniffer-maven-plugin</artifactId>
        <version>1.15</version>
        <executions>
          <execution>
            <id>check-java-api</id>
            <phase>test</phase>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <signature>
            <groupId>org.codehaus.mojo.signature</groupId>
            <artifactId>java16</artifactId>
            <version>1.1</version>
          </signature>
        </configuration>
      </plugin>
```
仅为学习和调试源码的情况下，可以将上述两个插件去掉，避免编译错误。

### idea 配置
1.添加依赖库
![h2依赖库](http://ohz440knb.bkt.clouddn.com/h2-buildQQ%E5%9B%BE%E7%89%8720170303165203.png)

2.设置文件路径
![路径设置](http://ohz440knb.bkt.clouddn.com/h2-buildQQ%E5%9B%BE%E7%89%8720170303165232.png)

特别注意：h2/src/tools要设置在"Test Source Folders"下面，否则会有文件找不到import