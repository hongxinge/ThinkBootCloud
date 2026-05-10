# 还在手写微服务脚手架？开箱即用才是正解！

> **ThinkBootCloud** - 专为 C 端客户端设计的轻量级微服务开发框架

![ThinkBootCloud](https://gitee.com/hongxinge/think-boot-cloud/raw/main/promotion/thinkboot-cloud-promo.png)

---

## 一、框架简介

**ThinkBootCloud** 是一款基于 **Spring Cloud Alibaba + Spring Boot 3** 的轻量级微服务开发框架。

与若依等后台管理系统不同，本框架专注于 **C 端客户端场景**（移动端 App、Web 前端、小程序等），去除了复杂的角色权限体系，仅保留基础的 Token 验证，让开发者能够**开箱即用，快速开发业务逻辑**。

### 设计理念

- **简单**：引入依赖，简单配置即可开始开发
- **安全**：默认所有接口需要认证，明确标记才放行
- **轻量**：按需引入模块，不臃肿
- **高效**：代码一键生成，减少重复劳动

---

## 二、核心优势

| 优势 | 说明 |
|------|------|
| ⚡ **开箱即用** | 引入依赖，简单配置即可开始开发，无需从零搭建脚手架 |
| 🔐 **安全优先** | 默认所有接口需要 Token 认证，使用 `@IgnoreAuth` 标记公开接口 |
| 📦 **模块化设计** | 按需引入模块，不臃肿，轻量灵活 |
| 🚀 **代码生成器** | 基于数据库表一键生成 Entity、Mapper、Service、Controller |
| 🔄 **约定优于配置** | 提供合理默认值，减少配置工作量 |
| 👨‍💻 **开发者友好** | 统一响应格式、全局异常处理、自动装配、全中文文档 |

---

## 三、技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 核心框架 |
| Spring Cloud | 2023.0.1 | 微服务框架 |
| Spring Cloud Alibaba | 2023.0.1.0 | 阿里微服务组件 |
| Nacos | 2.x | 注册中心 + 配置中心 |
| MyBatis-Plus | 3.5.6 | ORM 框架 |
| Redis (Redisson) | 3.27.2 | 缓存 + 分布式锁 |
| JWT (JJWT) | 0.12.5 | Token 认证 |
| OpenFeign | 4.1.x | 服务间通信 |
| Sentinel | 2023.0.1.0 | 限流熔断 |
| Gateway | 4.1.x | API 网关 |
| Druid | 1.2.21 | 数据库连接池 |
| Knife4j | 4.4.0 | API 文档 |

**开发环境要求**：JDK 17+、Maven 3.6+

---

## 四、模块结构

| 模块 | 名称 | 说明 |
|------|------|------|
| `think-boot-common` | 公共基础模块 | 统一响应 R、分页支持、业务异常、全局异常处理 |
| `think-boot-core` | 核心配置模块 | CORS 配置、Jackson 时间序列化、请求日志 |
| `think-boot-auth` | 认证模块 | JWT Token 生成/验证、@IgnoreAuth 注解、拦截器 |
| `think-boot-feign` | 服务通信模块 | OpenFeign 集成、Token 自动传递、全局日志 |
| `think-boot-sentinel` | 限流熔断模块 | Sentinel 集成、自定义限流响应 |
| `think-boot-gateway` | API 网关模块 | 网关路由、Token 验证、CORS、全局错误处理 |
| `think-boot-nacos` | 服务注册模块 | Nacos 服务发现、动态配置 |
| `think-boot-mybatis` | 数据库模块 | MyBatis-Plus、BaseEntity、分页、自动填充 |
| `think-boot-redis` | 缓存模块 | RedisTemplate 封装、Redisson 分布式锁 |
| `think-boot-file` | 文件上传模块 | 本地存储、阿里云 OSS、文件上传下载 |
| `think-boot-codegen` | 代码生成器模块 | 基于数据库表自动生成 CRUD 代码 |
| `think-boot-example` | 示例模块 | 完整演示框架用法 |

---

## 五、快速开始

### 第一步：引入父 POM

```xml
<parent>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-cloud</artifactId>
    <version>1.0.0</version>
</parent>
```

### 第二步：引入依赖

**必选依赖**（所有项目都需要）：

```xml
<!-- 公共基础 -->
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-common</artifactId>
</dependency>

<!-- 核心配置（Web、CORS、JSON序列化） -->
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-core</artifactId>
</dependency>

<!-- JWT 认证 -->
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-auth</artifactId>
</dependency>
```

**按需引入**：

```xml
<!-- 数据库模块 -->
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-mybatis</artifactId>
</dependency>

<!-- 缓存模块 -->
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-redis</artifactId>
</dependency>

<!-- 文件上传模块 -->
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-file</artifactId>
</dependency>

<!-- 代码生成器 -->
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-codegen</artifactId>
</dependency>
```

### 第三步：配置 application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

  data:
    redis:
      host: localhost
      port: 6379

# ThinkBootCloud 配置
thinkboot:
  auth:
    jwt:
      secret: dGhpbmstYm9vdC1qd3Qtc2VjcmV0LWtleS1tdXN0LWJlLWF0LWxlYXN0LTI1Ni1iaXRz
      expiration: 7200000
      # 不需要认证的接口
      skip-paths:
        - /api/auth/login
        - /api/auth/register
        - /api/public/**
```

### 第四步：创建启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### 第五步：运行项目

```bash
mvn clean package -DskipTests
java -jar your-project.jar
```

---

## 六、使用示例

### 6.1 创建 Controller（默认需要登录）

框架采用**"默认需要认证"**策略，所有接口都需要 Token 验证，除非明确标记：

```java
import com.thinkboot.auth.annotation.IgnoreAuth;
import com.thinkboot.common.result.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    // 默认需要登录（无需任何注解）
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id) {
        return R.success(userService.getById(id));
    }
    
    // 不需要登录（使用 @IgnoreAuth 注解）
    @IgnoreAuth
    @GetMapping("/public/info")
    public R<String> publicInfo() {
        return R.success("这是公开信息");
    }
}
```

### 6.2 统一响应格式

所有接口统一使用 `R<T>` 返回：

```java
// 成功响应
R.success(data);
R.success("操作成功");

// 失败响应
R.fail("操作失败");
R.fail(400, "参数错误");
```

### 6.3 分页查询

```java
@GetMapping("/list")
public R<PageResponse<User>> list(
    @RequestParam(defaultValue = "1") Integer pageNum,
    @RequestParam(defaultValue = "10") Integer pageSize
) {
    PageResponse<User> page = userService.page(pageNum, pageSize);
    return R.success(page);
}
```

### 6.4 文件上传

```java
import com.thinkboot.file.service.FileStorageService;
import com.thinkboot.file.model.FileUploadResult;

@PostMapping("/upload")
public R<FileUploadResult> upload(@RequestParam("file") MultipartFile file) {
    FileUploadResult result = fileStorageService.upload(file);
    return R.success(result);
}
```

### 6.5 Redis 缓存

```java
import com.thinkboot.redis.util.RedisUtil;

@GetMapping("/cache")
public R<String> getCache(@RequestParam String key) {
    // 获取缓存
    String value = RedisUtil.get(key);
    if (value != null) {
        return R.success(value);
    }
    
    // 查询数据库并缓存
    value = queryFromDB();
    RedisUtil.set(key, value, 30, TimeUnit.MINUTES);
    return R.success(value);
}
```

### 6.6 分布式锁

```java
import com.thinkboot.redis.util.DistributedLock;

@PostMapping("/order")
public R<String> createOrder() {
    String result = DistributedLock.executeWithLock(
        "lock:order:create",  // 锁名称
        () -> {
            // 执行业务逻辑
            return orderService.createOrder();
        },
        3,   // 等待时间（秒）
        10,  // 锁持有时间（秒）
        TimeUnit.SECONDS
    );
    return R.success(result);
}
```

### 6.7 代码生成器

```java
import com.thinkboot.codegen.ThinkBootCodeGenerator;

public class CodeGenerator {
    public static void main(String[] args) {
        ThinkBootCodeGenerator generator = new ThinkBootCodeGenerator();
        
        generator.url("jdbc:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
                .username("root")
                .password("your_password")
                .tableName("tb_user", "tb_order")  // 要生成的表名
                .moduleName("system")              // 模块名称
                .author("YourName")                // 作者
                .outputPath("D:/project/src/main/java")  // 输出路径
                .parentPackage("com.yourcompany")  // 父包名
                .useBaseEntity(true)               // 使用 BaseEntity
                .useLogicDelete(true)              // 启用逻辑删除
                .ignoreTablePrefix("tb_")          // 忽略表前缀
                .generate();                       // 执行生成
    }
}
```

---

## 七、网关配置

如果你的项目需要 API 网关，可以独立部署 Gateway 服务：

```yaml
server:
  port: 9000

spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    gateway:
      routes:
        - id: your-service
          uri: lb://your-service
          predicates:
            - Path=/api/your-service/**

thinkboot:
  gateway:
    # JWT 密钥（必须与业务服务一致）
    jwt-secret: dGhpbmstYm9vdC1nYXRld2F5LWp3dC1zZWNyZXQta2V5
    # 不需要认证的路径
    skip-auth-paths:
      - /api/auth/login
      - /api/auth/register
    # 跨域配置
    cors-allowed-origins:
      - http://localhost:3000
      - http://localhost:8080
```

---

## 八、常见问题

### Q1：如何跳过 Token 验证？

**方式一**：配置 skip-paths（推荐批量放行）

```yaml
thinkboot:
  auth:
    jwt:
      skip-paths:
        - /api/auth/login
        - /api/auth/register
        - /api/public/**
```

**方式二**：使用 `@IgnoreAuth` 注解（推荐单个接口）

```java
@IgnoreAuth
@GetMapping("/public/info")
public R<String> publicInfo() {
    return R.success("公开信息");
}
```

### Q2：如何更换 JWT 密钥？

```yaml
thinkboot:
  auth:
    jwt:
      secret: your-base64-encoded-secret-key
```

> 注意：密钥必须为 Base64 编码，建议使用至少 256 位的密钥。

### Q3：如何配置多数据源？

框架提供了读写分离和动态数据源的示例配置，位于 `datasource-examples` 目录下，可直接复制使用。

### Q4：文件上传支持哪些存储方式？

支持两种方式，通过配置切换：

```yaml
thinkboot:
  file:
    # local - 本地存储，oss - 阿里云OSS
    storage-type: local
    # 本地存储路径
    local-path: /data/uploads
    # 阿里云OSS配置（storage-type=oss时需要）
    oss:
      endpoint: oss-cn-hangzhou.aliyuncs.com
      access-key-id: your-key
      access-key-secret: your-secret
      bucket-name: your-bucket
```

### Q5：如何自定义全局异常处理？

框架已提供默认的全局异常处理，如需自定义，可继承 `GlobalExceptionHandler` 并添加自己的异常处理方法。

---

## 九、项目地址

- 🔗 **Gitee 仓库**：https://gitee.com/hongxinge/think-boot-cloud
- 📮 **问题反馈**：https://gitee.com/hongxinge/think-boot-cloud/issues

---

## 十、开源协议

本项目基于 **MIT License** 开源协议发布。

✅ **完全免费** - 个人和商业项目均可免费使用  
✅ **可自由商用** - 无需授权即可用于商业项目  
✅ **可修改分发** - 可以修改源码并重新分发  

---

**如果你也喜欢这个框架，欢迎 Star ⭐ 支持一下！**
