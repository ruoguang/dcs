

## 项目名称

### **文档转换(Document Conversion Server)** by CC（v1.0.0）


## 目录

- [功能演示](#project_show)
- [项目简介](#introduction)
- [功能简述](#function_brief)
- [项目架构](#project_structure)
- [目录结构](#directory_structure)
- [使用效果](#use_effect)
- [安装说明](#installation_notes)
- [使用说明](#instructions_for_use)
- [开发人员](#developers)
- [版权信息](#copyright_information)
- [更新日志](#update_log)
- [特别说明](#tip)

## <span id="project_show" >功能演示</span>


![image](https://pdsapi.aliyundrive.com/v2/redirect?id=40caa973900b44a1a14c3a63aa5162101680857411200631573)

## <span id="introduction" >项目简介</span>

文档转换服务项目（以下简称dcs项目），是一个提供在线各类文档转换服务的非商用项目。

## <span id="function_brief" >功能简述</span>

此版本功能包含了提供的线上服务和项目的以下特色：

### 线上服务：

- 格式转换：word2pdf、html2pdf、pdf2img、pdf2file还支持html2img、html2md等一系列格式转换服务；
- 异步任务：pdf异步解析；
- 水印去除：去除水印页数的限制输出；
- 云文档合并：支持开饭云pdf合并（通常是发票收据pdf合并展示如百旺）；

### 项目特色

- 双系统支持：支持window和linux作为服务器,对应插件位置放对就行；
- 定时任务：支持多维度系统定时任务；
- 自定义业务异常：自定义业务异常；
- 全局异常：全局异常处理器；
- 全局响应格式：全局响应格式返回；
- knife4j：knife4j在线文档；
- pdf高清和缩略图的高效解决方案：缓存访问的解决方案,分片异步解析图片；
- 线程池的最佳实践：核心数自动是cpu核数，无需配置；
- redis中央缓存服务：第一次解析的pdf图片集都在redis中，过期时间可配置；

### 可扩展的

- 全局异常日志查询接口（唯一异常暂未开启，无法实现）

## <span id="project_structure" >项目架构 </span>

springboot+redis+knife4j+scheduTask+pdfbox+aspose+plugs+businessAsync

## <span id="directory_structure" >目录结构 </span>

```
├── Readme.md                                                                                                           // readme文档
├── pom                                                                                                                 // 依赖pom
├── src                                                                                                                 // src
│    └──main                                                                                                            // main  
│        ├── java                                                                                                       // java
│        │       └── com.ruoguang.dcs                                                                           			// 代码主目录
│        │                       ├── advice                                                                             // 通知
│        │                       │       ├── GlobalExceptionAdvice                                                      // 全局异常通知
│        │                       │       └── RestReturnValueHandlerAdvice                                               // 全局响应通知
│        │                       ├── async                                                                              // 异步
│        │                       │     └── BusinessAsync                                                                // 业务异步
│        │                       ├── config                                                                             // 配置
│        │                       │      ├── configEntity                                                                // 配置实体类
│        │                       │      ├── CustomParamResolverConfigurer                                               // CustomParamResolverConfigurer
│        │                       │      ├── FrameworkConfig                                                             // 自定义鉴权配置
│        │                       │      ├── HttpSessionConfig                                                           // HttpSessionConfig
│        │                       │      ├── InterceptorConfig                                                           // InterceptorConfig
│        │                       │      ├── MvcConfigurer                                                               // MvcConfigurer
│        │                       │      ├── CommandExecutor                                                          	// CommandExecutor
│        │                       │      ├── RestReturnValueHandlerConfigurer                                            // 全局响应程序配置类
│        │                       │      ├── ScheduleConfig                                                              // ScheduleConfig
│        │                       │      ├── Swagger2Config                                                              // swagger2ui信息配置
│        │                       │      └── WebAppConfig                                                                // WebAppConfig
│        │                       ├── entity                                                                             // 实体类
│        │                       ├── enums                                                                              // 枚举
│        │                       ├── exception                                                                          // 异常
│        │                       │        └── BusinessException                                                         // 业务异常
│        │                       ├── interceptor                                                                        // 拦截器
│        │                       │       └── AllInterceptor                                                             // 总拦截器
│        │                       ├── listener                                                                           // 监听器
│        │                       │        └── ApplicationStartup                                                        // 服务启动监控 
│        │                       ├── model                                                                              // 模型
│        │                       │      ├── AjaxResult                                                                  // ajax返回模型
│        │                       │      └── ReturnInfo                                                                  // 全局响应模型
│        │                       ├── open                                                                               // 开放接口层
│        │                       ├── pool                                                                               // 池化
│        │                       │     └── TaskExecutor                                                                 // 本地任务池
│        │                       ├── resolver                                                                           // 整理
│        │                       ├── service                                                                            // 业务层
│        │                       ├── task                                                                               // 任务
│        │                       │    └── ScheduTask                                                                    // 计划任务/定时任务
│        │                       ├── util                                                                               // 工具
│        │                       └── DcsApp                                                                             // Application
│        └── resources                                                                                                  // 资源集   
│                ├── application.yml                                                                                    // 主配置文件
│                ├── *.properties                                                                                       // 各类环境配置文件
│                ├── *.yml                                                                                              // 各类环境配置文件
│                ├── license.xml                                                                                        // aspose服务验签的配置文件
│                └── logback-spring.xml                                                                                 // 系统slf4j日志配置文件                
└──test                                                                                                                 // test
```

## <span id="use_effect" >使用效果 </span>

## <span id="installation_notes" >安装说明</span>

- 1.环境：推荐(open)jdk 8.0+，我用的jdk11
- 2.JVM：在jvm启动的时候可能需要添加：--add-opens java.base/java.lang=ALL-UNNAMED --illegal-access=deny
- 3.wkhtmltopdf插件：html2pdf,html2img功能使用的插件完成的，插件分为环境win和linux，也需要这些来配合功能完成（下载地址https://wkhtmltopdf.org/downloads.html）
- 4.字体：在linux环境下要添加必须的相关字体库，比如重要的宋体simsum.tff
- 5.license：这个商用需要付费的，可以看贴别说明这块免费获取破解版jar
- 6.aspose：doc和pdf相关功能的支持和水印去除的依赖，给出其一的pdf的jar包下载地址（https://releases.aspose.com/pdf/java/21-8/#packageexplorer-tab）

## <span id="instructions_for_use" >使用说明</span>

安装好相关环境组件和项目依赖后（最低JVM堆大小4G+），正常启动DcsApp.main即可；



## <span id="developers" >开发人员（欢迎参与开发）</span>

- 廖启安


## <span id="copyright_information" >版权信息</span>

### 当前版本 v.1.0.0

### 当前版本内容

- 格式转换：word2pdf、html2pdf、pdf2img、pdf2file还支持html2img、html2md等一系列

## <span id="update_log" >更新日志</span>

| 版本    | 时间     | 内容                                                         |
| ------- | -------- | ------------------------------------------------------------ |
| v.1.0.0 | 2023.4.x | 基本word pdf文档格式转换服务             |

## <span id="tip" >特别说明</span>
- 破解：如果你想用免费的破解jar包（请支持付费商用），请star这个项目，加我v13195530002，备注dcs就行了
- 参与：如果你也想参与开发或者有更好的想法请及时找我联系，我也会把你加入到开发或感谢列表中