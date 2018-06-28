## crashaccumulator
Android Crash Monitor 是一款日常开发android 过程中的异常日志记录软件包
 
### 特征
   - 简单
   - 实用
   - 方便
   
#### 使用Gradle构建时添加一下依赖即可:
```
debugImplementation 'com.dvsnier:crash:0.0.4'
releaseImplementation 'com.dvsnier:crash-no:0.0.4'
```

#### 使用前配置
##### 需要的权限
```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

##### 初始化
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
#### FAQ
1.默认异常日志文件保存目录为： `/mnt/sdcard/Android/data/package_your_name/files/crash/`

----
### 关于作者
* Email： <3086722095@qq.com>
* 有任何建议或者使用中遇到问题都可以给我发邮件，欢迎技术交流QQ:578562841
