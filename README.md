## crashaccumulator

Android Crash Monitor 是一款日常开发android 过程中的异常日志记录软件包
 
### 一. 特征

   - 简单
   - 实用
   - 方便
   
### 二. Gradle 依赖 

使用Gradle构建时添加一下依赖即可:

```
debugImplementation 'com.dvsnier:crash:0.0.6'
releaseImplementation 'com.dvsnier:crash-no:0.0.6'
```

### 三. 权限配置

```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### 四. 初始化
```
    // 在application的onCreate中初始化
    @Override
    public void onCreate() {
        super.onCreate();
//        Crash.initialize(this);
        Crash.initialize(this, true);
//        Crash.initialize(this, true, "测试崩溃提示语...");
        ...
    }

```

### 五. 自定义配置

#### 5.1 debug 配置

```
// res/values/bools.xml

<bool name="debug_monitor_server">true</bool>
```

#### 5.2 MIME Type 配置(可选项)

```
// res/values/strings.xml

// mime xxx.txt
<string name="mime_type">TYPE_TEXT</string>

// mime xxx.log
<string name="mime_type">TYPE_LOG</string>
```

### 六. FAQ

1.默认异常日志文件保存目录为： `/mnt/sdcard/Android/data/package_your_name/files/crash/`

----

### 七. 关于作者

* Email： <dovsnier@qq.com>
* 有任何建议或者使用中遇到问题都可以给我发邮件，欢迎技术交流QQ:578562841
