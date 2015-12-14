#OPENDJ技术分享
* OPENDJ是什么
* OPENDJ启动过程分析
* OPENDJ处理LDAP请求过程分析

##OPENDJ是什么?
OPENDJ是基于OPENDS的开源LDAP项目，因为License原因，改名为OPENDJ。

##OPENDJ启动过程分析
* 启动命令 
```
sh start-ds
```
* 进程示例 
```
/home/opendj/jdk1.6.0_45/jre/bin/java -server -Dorg.opends.server.scriptName=start-ds org.opends.server.core.DirectoryServer --configClass org.opends.server.extensions.ConfigFileHandler --configFile /home/opendj/asinst_ins/upds/config/config.ldif
```

##启动脚本分析

* 启动相关文件
    * CONFIG_FILE=${INSTANCE_ROOT}/config/config.ldif
    * PID_FILE=${INSTANCE_ROOT}/logs/server.pid
    * LOG_FILE=${INSTANCE_ROOT}/logs/server.out
    * STARTING_FILE=${INSTANCE_ROOT}/logs/server.starting
    
* 执行DirectoryServer  main方法，传入相关参数进行LDAP服务器启动前检查，检查是否能正常启动，返回相应错误码。
* 错误码是99，则说明LDAP服务器启动前已完成，可以再次执行DirectoryServer的main方法，启动LDAP服务器。
* 错误码是98，则说明LDAP服务器已启动，无需再次启动。
* 其他错误码，不启动。

## 检查LDAP服务器是否可正常启动
* 检查时执行的Java进程
```
/home/opendj/jdk1.6.0_45/jre/bin/java -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y -client -Dorg.opends.server.scriptName=start-ds org.opends.server.core.DirectoryServer --configClass org.opends.server.extensions.ConfigFileHandler --configFile /home/opendj/asinst_ins/upds/config/config.ldif --checkStartability
```
* checkStartability(), 获取lock文件（upds/./locks/server.lock）的排它锁，如果成功说明LDAP服务器未启动，否则LDAP服务器已启动
* 返回结果，进行执行完毕。
* 具体代码DirectoryServer.main(String[] args), DirectoryServer.checkStartability(ArgumentParser argParser)
* 获取文件排它锁逻辑
    * 根据文件创建可随机访问可读写文件  raf = new RandomAccessFile(lockFile, "rw");
    * 从可随机访问可读写文件获取FileChannel raf.getChannel()
    * 从FileChannel获取排他文件锁  fileLock = channel.tryLock(0L, Long.MAX_VALUE, false);
    * 

## LDAP服务器正常启动
* 设置Directory Server Environment, 构建DirectoryEnvironmentConfig
```
[java.vm.version=20.45-b01
 sun.jnu.encoding=UTF-8
 java.vendor.url=http://java.sun.com/
 org.opends.server.scriptName=start-ds
 java.vm.info=mixed mode
 user.dir=/home/opendj/asinst_ins/upds/bin
 sun.cpu.isalist=
 java.awt.graphicsenv=sun.awt.X11GraphicsEnvironment
 sun.os.patch.level=unknown
 java.io.tmpdir=/tmp
 user.home=/home/opendj
 java.awt.printerjob=sun.print.PSPrinterJob
 java.version=1.6.0_45
 file.encoding.pkg=sun.io
 java.vendor.url.bug=http://java.sun.com/cgi-bin/bugreport.cgi
 file.encoding=UTF-8
 line.separator=

 sun.java.command=org.opends.server.core.DirectoryServer --configClass org.opends.server.extensions.ConfigFileHandler --configFile /home/opendj/asinst_ins/upds/config/config.ldif
 java.vm.specification.vendor=Sun Microsystems Inc.
 java.vm.vendor=Sun Microsystems Inc.
 org.opends.server.UseLastKnownGoodConfiguration=false
 java.class.path=/home/opendj/asinst_ins/upds/classes:/home/opendj/asinst_ins/upds/resources/*.jar:/home/opendj/asinst_ins/upds/lib/bootstrap.jar
 sun.io.unicode.encoding=UnicodeLittle
 os.arch=amd64
 user.name=opendj
 user.language=en
 java.runtime.version=1.6.0_45-b06
 sun.boot.class.path=/home/opendj/jdk1.6.0_45/jre/lib/resources.jar:/home/opendj/jdk1.6.0_45/jre/lib/rt.jar:/home/opendj/jdk1.6.0_45/jre/lib/sunrsasign.jar:/home/opendj/jdk1.6.0_45/jre/lib/jsse.jar:/home/opendj/jdk1.6.0_45/jre/lib/jce.jar:/home/opendj/jdk1.6.0_45/jre/lib/charsets.jar:/home/opendj/jdk1.6.0_45/jre/lib/modules/jdk.boot.jar:/home/opendj/jdk1.6.0_45/jre/classes
 sun.cpu.endian=little
 org.opends.server.ConfigClass=org.opends.server.extensions.ConfigFileHandler
 sun.boot.library.path=/home/opendj/jdk1.6.0_45/jre/lib/amd64
 java.vm.name=Java HotSpot(TM) 64-Bit Server VM
 java.home=/home/opendj/jdk1.6.0_45/jre
 java.endorsed.dirs=/home/opendj/jdk1.6.0_45/jre/lib/endorsed
 sun.management.compiler=HotSpot 64-Bit Tiered Compilers
 java.runtime.name=Java(TM) SE Runtime Environment
 java.library.path=/home/opendj/jdk1.6.0_45/jre/lib/amd64/server:/home/opendj/jdk1.6.0_45/jre/lib/amd64:/home/opendj/jdk1.6.0_45/jre/../lib/amd64:/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
 file.separator=/
 java.specification.vendor=Sun Microsystems Inc.
 java.vm.specification.version=1.0
 sun.java.launcher=SUN_STANDARD
 user.timezone=
 os.name=Linux
 path.separator=:
 java.ext.dirs=/home/opendj/jdk1.6.0_45/jre/lib/ext:/usr/java/packages/lib/ext
 org.opends.server.ConfigFile=/home/opendj/asinst_ins/upds/config/config.ldif
 sun.arch.data.model=64
 java.specification.name=Java Platform API Specification
 os.version=3.0.13-0.27-default
 java.class.version=50.0
 user.country=US
 java.vendor=Sun Microsystems Inc.
 java.vm.specification.name=Java Virtual Machine Specification
 java.specification.version=1.6]
 ```

* bootstrap directory server
    * 增加DirectoryServerShutdownHook，JVM退出时可通知Directory Server
    * 初始化jmx subsystem, 实例化MBean Server.
    * bootstrapMatchingRules, 注册基本的matching rules，这些matching rules和配置无关。如CaseIgnoreEqualityMatchingRuleFactory
    * bootstrapAttributeSyntaxes， 注册基本的attribute syntaxes,这些attribute syntaxes和配置无关。如BooleanSyntaxw
    * 标记isBootstrapped=true
    


* initializeConfiguration 
    * 设置Config Class, org.opends.server.extensions.ConfigFileHandler
    * 设置config file, /home/opendj/asinst_ins/upds/config/config.ldif
    * initializeConfiguration， 
        * 实际调用configHandler.initializeConfigHandler(path, false),不检查schema
        *  判断是否使用最后一次成功配置，默认为false，如果配置是则使用config.ldif.startok，否则使用config.ldif
        *  (默认不存在）如果存在config-changes.ldif，则应用其中的更改。修改后config.ldif文件保存为config.ldif.prechanges，变更文件保存为config-changes.ldif.applied
        *  解析config.ldif文件，获取root config DN, 注册config DN。

* start server
    * 检查binary version和instance version是否一致，如果不一致，则初始化异常，启动失败。
    * 标记当前时间为启动时间
    * 判断是否需要启动connection handler, 默认为true
    * 初始化schema, 
        * 包括matching rules, attribute syntaxes, 初始化schema目录下的xml文件。
        * 为cn=config注册add, delete, change listener.
        * 初始化config handler，并检查schema,
    * 初始化plugin config manager， pluginConfigManager.initializePluginConfigManager();
    * 初始化virtual attributes, DirectoryServer.initializeVirtualAttributes()
    * 初始化core directory server configuration, coreConfigManager.initializeCoreConfig();
    * 初始化crypto manager, DirectoryServer.initializeCryptoManager
    * 初始化log ratation policy, rotationPolicyConfigManager.initializeLogRotationPolicyConfig();
    * 初始化log retention policy,  retentionPolicyConfigManager.initializeLogRetentionPolicyConfig();
    * 初始化logger config,  loggerConfigManager.initializeLoggerConfig();
    * 输出日志，记录RuntimeInformation
    * 初始化alert handlers, DirectoryServer.initializeAlertHandlers()
    * 初始化default entry cache, entryCacheConfigManager.initializeDefaultEntryCache()
    * 根据需要创建自签证书
    * 初始化key manager provider
    * 初始化trust manager provider
    * 初始化certificate mapper
    * 初始化identity mapper
    * `初始化root dn`
    * `初始化subentry manager`
    * `初始化group manager`
    * `初始化access control`
    * `初始化backend`
    *  Creates the missing workflows, one for the config backend and one for the rootDSE backend.
    * `初始化entry cache`
    * 初始化supported control
    * 初始化supoorted features
    * 初始化extended operation
    * 初始化SASL mechanisms
    * `初始化connection handler`
        * 初始化Administration Connector,cn=config=Administration Connector 0.0.0.0 port 7444  
        * 在rootCfg中增加connection handler add, delete listener,如果connection handler增加或删除，
        * 遍历rootCfg中所有connection handler, 并获取connection handler的配置，为配置增加listener
        * rootCfg中配置的connection hanlder列表,其中默认启用的Connection Handler有LDAP Connection Handler
            * LDAP Connection Handler
            * LDIF Connection Handler
            * JMX Connection Handler
            * HTTP Connection Handler
            * LDAPS Connection Handler
        * DirectoryServer中注册已启用connection handlers
    * 初始化monitor provider
    * 初始化authentication policy component
    * 初始化user plugins
    * 初始化extension
    * 初始化synchronization provider
    * `初始化work queue`
    * 出发startup plugin，默认无插件
    * 初始化完成，通知listeners
    * `启动connection handler`
        * 默认的connection handlers包括 Administration Connector handler， LDAP connection handler。
        * 调用Connection Handler的start方法。
    * 启动成功记录成功启动的配置文件，拷贝config.ldif为config.ldif.startok  
    * 获取internal connection (InternalClientConnection(connID=-1, authDN="cn=Internal Client,cn=Root DNs,cn=config"))
    * 如果没有禁用admin data同步，则同步admin data, 默认没有禁用。
    * 删除starting文件 upds/logs/server.starting文件
    * 删除hostname文件 upds/config/hostname文件
