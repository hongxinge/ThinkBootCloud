# ThinkBootCloud

> 🚀 轻量级微服务开发框架，专为C端客户端场景设计

**技术栈**: Spring Boot 3.2.5 | Spring Cloud 2023.0.1 | Spring Cloud Alibaba 2023.0.1.0 | Java 17 | RabbitMQ

**开源协议**: [MIT](LICENSE) | 完全免费 | 可自由商用

---

## 📖 项目简介

ThinkBootCloud 是一款基于 Spring Cloud Alibaba + Spring Boot 3 的轻量级微服务开发框架。

与若依等后台管理系统不同，本框架**专注于C端客户端场景**（移动端App、Web前端、小程序等），去除了复杂的角色权限体系，仅保留基础的 Token 验证，让开发者能够**开箱即用，快速开发业务逻辑**。

### 设计理念

- **开箱即用**：引入依赖，简单配置即可开始开发
- **轻量灵活**：模块化设计，按需引入，不臃肿
- **约定优于配置**：提供合理默认值，减少配置工作量
- **开发者友好**：统一响应格式、全局异常处理、自动装配
- **安全优先**：默认所有接口需要认证，明确标记才放行

---

## 🏗️ 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 核心框架 |
| Spring Cloud | 2023.0.1 | 微服务框架 |
| Spring Cloud Alibaba | 2023.0.1.0 | 阿里微服务组件 |
| Nacos | 2.x | 注册中心 + 配置中心 |
| MyBatis-Plus | 3.5.6 | ORM框架 |
| Redis (Redisson) | 3.27.2 | 缓存 + 分布式锁 |
| RabbitMQ | 3.12.x | 消息队列 |
| JWT (JJWT) | 0.12.5 | Token认证 |
| OpenFeign | 4.1.x | 服务间通信 |
| Sentinel | 2023.0.1.0 | 限流熔断 |
| Gateway | 4.1.x | API网关 |
| Druid | 1.2.21 | 数据库连接池 |
| Knife4j | 4.4.0 | API文档 |

---

## 📦 模块结构

```
think-boot-cloud/
├── pom.xml                                    # 父POM（统一版本管理）
├── LICENSE                                    # MIT 开源协议
├── README.md                                  # 使用文档
├── templates/                                 # 部署模板目录
├── think-boot-common/                         # 公共模块
├── think-boot-core/                           # 核心模块（Web配置）
├── think-boot-auth/                           # JWT认证模块
├── think-boot-feign/                          # OpenFeign集成模块
├── think-boot-sentinel/                       # Sentinel限流熔断模块
├── think-boot-file/                           # 文件上传模块
├── think-boot-codegen/                        # 代码生成器模块
├── think-boot-mq-rabbitmq/                    # RabbitMQ消息队列模块
├── think-boot-gateway/                        # API网关模块
├── think-boot-nacos/                          # Nacos注册配置模块
├── think-boot-mybatis/                        # MyBatis-Plus集成模块
├── think-boot-redis/                          # Redis缓存模块
└── think-boot-example/                        # 示例模块（演示用法）
```

### 模块说明

| 模块 | 说明 | 核心功能 |
|------|------|----------|
| **think-boot-common** | 公共基础模块 | 统一响应R、分页支持、业务异常、全局异常处理 |
| **think-boot-core** | 核心配置模块 | CORS配置、Jackson时间序列化、请求日志 |
| **think-boot-auth** | 认证模块 | JWT Token生成/验证、@IgnoreAuth注解、拦截器、UserContext |
| **think-boot-feign** | 服务通信模块 | OpenFeign集成、Token自动传递、全局日志 |
| **think-boot-sentinel** | 限流熔断模块 | Sentinel集成、自定义限流响应、Nacos规则源 |
| **think-boot-gateway** | API网关模块 | 网关路由、Token验证、CORS、全局错误处理 |
| **think-boot-nacos** | 服务注册模块 | Nacos服务发现、动态配置 |
| **think-boot-mybatis** | 数据库模块 | MyBatis-Plus、BaseEntity、分页、自动填充 |
| **think-boot-redis** | 缓存模块 | RedisTemplate封装、Redisson分布式锁 |
| **think-boot-file** | 文件上传模块 | 本地存储、阿里云OSS、文件上传下载 |
| **think-boot-codegen** | 代码生成器模块 | 基于数据库表自动生成CRUD代码 |
| **think-boot-mq-rabbitmq** | 消息队列模块 | RabbitMQ集成、消息发送消费、延迟消息 |
| **think-boot-example** | 示例模块 | 完整演示框架用法 |

---

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Nacos 2.x（可选，微服务场景需要）

### 第一步：编译安装框架

```bash
# 克隆项目
git clone https://gitee.com/hongxinge/think-boot-cloud.git
cd think-boot-cloud

# 编译并安装到本地Maven仓库
mvn clean install -DskipTests
```

### 第二步：创建你的业务项目

在你的项目中引入框架作为父POM：

```xml
<parent>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-cloud</artifactId>
    <version>1.0.0</version>
</parent>
```

引入需要的模块依赖：

```xml
<dependencies>
    <!-- 必选：公共模块 + 核心配置 -->
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-common</artifactId>
    </dependency>
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-core</artifactId>
    </dependency>
    
    <!-- 必选：认证模块 -->
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-auth</artifactId>
    </dependency>
    
    <!-- 按需引入：数据库 -->
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-mybatis</artifactId>
    </dependency>
    
    <!-- 按需引入：缓存 -->
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-redis</artifactId>
    </dependency>
    
    <!-- 按需引入：服务间通信 -->
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-feign</artifactId>
    </dependency>
    
    <!-- 按需引入：限流熔断 -->
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-sentinel</artifactId>
    </dependency>
    
    <!-- 按需引入：文件上传 -->
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-file</artifactId>
    </dependency>

    <!-- 按需引入：RabbitMQ消息队列 -->
    <dependency>
        <groupId>com.thinkboot</groupId>
        <artifactId>think-boot-mq-rabbitmq</artifactId>
    </dependency>
</dependencies>
```

### 第三步：配置 application.yml

在 `src/main/resources/application.yml` 中添加配置：

```yaml
server:
  port: 8080

spring:
  application:
    name: your-service-name

  # 数据库配置（如果使用数据库）
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

  # Redis配置（如果使用缓存）
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0

# JWT认证配置（必须配置）
thinkboot:
  auth:
    jwt:
      # Base64编码的密钥（至少32位）
      # 可用在线工具生成：https://www.base64encode.org/
      secret: dGhpbmstYm9vdC1qd3Qtc2VjcmV0LWtleS1tdXN0LWJlLWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc=
      # Token有效期（默认2小时）
      expiration: 7200000
      # 刷新Token有效期（默认7天）
      refresh-expiration: 604800000
      # 不需要Token验证的路径（白名单）
      skip-paths:
        - /api/auth/login
        - /api/auth/register
        - /doc.html
        - /swagger-resources/**
        - /v3/api-docs/**
```

### 第四步：编写业务代码

#### 4.1 创建启动类

```java
package com.yourcompany.yourproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YourProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourProjectApplication.class, args);
    }
}
```

#### 4.2 创建实体类

继承 `BaseEntity`，自动包含 `id`、`createTime`、`updateTime`、`deleted` 等字段：

```java
package com.yourcompany.yourproject.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thinkboot.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Integer status;
}
```

#### 4.3 创建 Mapper 和 Service

```java
// Mapper - 继承 BaseMapper，自动拥有 CRUD 方法
package com.yourcompany.yourproject.mapper;

import com.thinkboot.mybatis.base.BaseMapper;
import com.yourcompany.yourproject.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```

```java
// Service 接口 - 继承 BaseService
package com.yourcompany.yourproject.service;

import com.thinkboot.mybatis.base.BaseService;
import com.yourcompany.yourproject.model.entity.User;

public interface UserService extends BaseService<User> {
    User getByUsername(String username);
}
```

```java
// Service 实现 - 继承 BaseServiceImpl
package com.yourcompany.yourproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinkboot.mybatis.base.BaseServiceImpl;
import com.yourcompany.yourproject.mapper.UserMapper;
import com.yourcompany.yourproject.model.entity.User;
import com.yourcompany.yourproject.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
    
    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }
}
```

#### 4.4 创建 Controller

```java
package com.yourcompany.yourproject.controller;

import com.thinkboot.auth.annotation.IgnoreAuth;
import com.thinkboot.auth.context.UserContext;
import com.thinkboot.common.result.R;
import com.yourcompany.yourproject.model.entity.User;
import com.yourcompany.yourproject.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 默认需要登录（无需添加任何注解）
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return R.error(404, "用户不存在");
        }
        return R.success(user);
    }

    // 不需要登录（使用 @IgnoreAuth 注解）
    @IgnoreAuth
    @GetMapping("/public/info")
    public R<String> publicInfo() {
        return R.success("这是公开信息");
    }

    // 获取当前登录用户（默认需要登录）
    @GetMapping("/me")
    public R<String> getCurrentUser() {
        String userId = UserContext.getCurrentUserId();
        return R.success("当前用户ID: " + userId);
    }
}
```

### 第五步：启动项目

```bash
# 编译打包
mvn clean package -DskipTests

# 启动
java -jar target/your-project-1.0.0.jar
```

启动成功后，访问以下地址：

- API文档：http://localhost:8080/doc.html
- 接口测试：使用 Postman 或 curl

---

## 📖 使用指南

### 统一响应格式

所有接口自动返回统一格式，前端无需特殊处理：

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": 1700000000000
}
```

常用方法：

```java
// 成功响应（带数据）
return R.success(user);

// 成功响应（无数据）
return R.success();

// 错误响应（自定义状态码）
return R.error(404, "资源不存在");

// 错误响应（默认500）
return R.error("服务器异常");
```

### 分页查询

```java
@GetMapping("/list")
public PageResponse<User> list(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "10") int pageSize) {
    // 创建分页对象
    Page<User> page = PageUtils.toPage(pageNo, pageSize);
    // 执行分页查询
    Page<User> result = userService.page(page);
    // 返回分页结果
    return PageUtils.toPageResponse(result);
}
```

前端请求：`GET /api/users/list?pageNo=1&pageSize=10`

返回结果：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [...],
    "total": 100,
    "pages": 10,
    "pageNo": 1,
    "pageSize": 10,
    "hasPrevious": false,
    "hasNext": true
  },
  "timestamp": 1700000000000
}
```

### Token 认证

**重要：框架默认所有接口都需要 Token 验证**，只有以下两种情况会放行：

#### 方式一：配置免认证路径（推荐批量管理）

在 `application.yml` 中配置不需要 Token 验证的路径：

```yaml
thinkboot:
  auth:
    jwt:
      skip-paths:
        - /api/auth/login          # 登录接口
        - /api/auth/register       # 注册接口
        - /api/auth/refresh-token  # 刷新Token接口
        - /api/public/**           # 所有公开接口
        - /doc.html
        - /swagger-resources/**
        - /v3/api-docs/**
```

#### 方式二：使用 @IgnoreAuth 注解（推荐单个接口）

在方法或类上添加 `@IgnoreAuth` 注解，标记该接口不需要认证：

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // 免认证 - 单个接口
    @IgnoreAuth
    @PostMapping("/login")
    public R<LoginResponse> login(@RequestBody LoginRequest request) {
        // 验证用户名密码
        User user = userService.getByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return R.error(401, "用户名或密码错误");
        }

        // 生成 Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        
        String token = jwtUtils.generateToken(user.getId().toString(), claims);
        String refreshToken = jwtUtils.generateRefreshToken(user.getId().toString(), claims);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId().toString());
        response.setExpireTime(7200L);

        return R.success(response);
    }

    // 免认证 - 整个类所有接口
    @IgnoreAuth
    @PostMapping("/register")
    public R<Void> register(@RequestBody LoginRequest request) { ... }
}
```

#### 认证接口（默认行为）

**不需要添加任何注解**，框架默认所有接口都需要认证：

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 默认需要认证 - 无需添加任何注解
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id) {
        return R.success(userService.getById(id));
    }

    // 默认需要认证
    @PostMapping
    public R<Void> create(@RequestBody UserDTO dto) { ... }
}
```

#### 前端携带 Token

```
GET /api/users/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### 获取当前登录用户

```java
// 获取当前登录用户（默认需要认证，无需添加注解）
@GetMapping("/me")
public R<String> getCurrentUser() {
    // 获取当前用户ID
    String userId = UserContext.getCurrentUserId();
    
    // 根据userId查询完整用户信息
    User user = userService.getById(Long.valueOf(userId));
    return R.success(user);
}
```

### OpenFeign 服务间调用

#### 1. 定义 Feign 接口

```java
package com.yourcompany.orderservice.feign;

import com.thinkboot.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserFeignClient {
    
    @GetMapping("/{id}")
    R<User> getUserById(@PathVariable Long id);
}
```

#### 2. 使用 Feign 接口

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final UserFeignClient userFeignClient;

    public OrderController(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }
    
    @GetMapping("/{id}")
    public R<Order> getOrder(@PathVariable Long id) {
        // Token 会自动传递到 user-service
        R<User> userResult = userFeignClient.getUserById(1L);
        
        Order order = orderService.getById(id);
        return R.success(order);
    }
}
```

> **注意**：Token 传递是自动的，无需手动设置 Header。当 A 服务调用 B 服务时，当前请求的 Token 会自动传递到 B 服务。

### Sentinel 限流熔断

#### 1. 配置文件

```yaml
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8080  # Sentinel 控制台地址
      eager: true                  # 应用启动时立即初始化

thinkboot:
  sentinel:
    enabled: true
    dashboard: localhost:8080
```

#### 2. 使用限流注解

```java
@SentinelResource(value = "getUserById", blockHandler = "handleBlock")
@GetMapping("/{id}")
public R<User> getById(@PathVariable Long id) {
    return R.success(userService.getById(id));
}

// 限流后的降级方法
public R<User> handleBlock(Long id, BlockException ex) {
    return R.error(429, "请求过于频繁，请稍后再试");
}
```

### Redis 缓存使用

#### 基本操作

```java
// 设置缓存（30分钟过期）
RedisUtils.set("user:1", userObject, 30, TimeUnit.MINUTES);

// 获取缓存
User user = (User) RedisUtils.get("user:1");

// 删除缓存
RedisUtils.delete("user:1");

// 判断是否存在
boolean exists = RedisUtils.hasKey("user:1");

// 设置过期时间
RedisUtils.expire("user:1", 1, TimeUnit.HOURS);
```

#### 分布式锁

```java
// 方式1：手动加锁解锁
RLock lock = DistributedLock.tryLock("lock:order:create", 3, 10, TimeUnit.SECONDS);
try {
    if (lock != null) {
        // 执行业务逻辑
    }
} finally {
    DistributedLock.unlock("lock:order:create");
}

// 方式2：函数式API（推荐）
String result = DistributedLock.executeWithLock(
    "lock:order:create",
    () -> {
        // 执行业务逻辑
        return "success";
    },
    3,   // 等待时间（秒）
    10,  // 锁持有时间（秒）
    TimeUnit.SECONDS
);
```

### RabbitMQ 消息队列

框架集成了 RabbitMQ 消息队列，**自动配置，开箱即用**。开发者只需关注业务逻辑（发送消息和消费消息），其他全部由框架自动处理。

#### 1. 引入依赖

在你的项目中添加 RabbitMQ 模块依赖：

```xml
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-mq-rabbitmq</artifactId>
</dependency>
```

#### 2. 配置 application.yml

```yaml
spring:
  rabbitmq:
    host: localhost          # RabbitMQ 地址
    port: 5672               # RabbitMQ 端口
    username: guest          # 用户名
    password: guest          # 密码
    virtual-host: /          # 虚拟主机

thinkboot:
  mq:
    rabbitmq:
      enable: true                              # 启用 RabbitMQ
      exchange: thinkboot.default.exchange      # 默认交换机名称
      queue-prefix: thinkboot.                  # 队列前缀（自动创建的队列会使用此前缀）
```

#### 3. 自动配置说明

**框架自动完成以下配置，开发者无需关心**：

- ✅ 自动创建 DirectExchange（默认交换机）
- ✅ 自动创建默认队列（带死信队列配置）
- ✅ 自动创建队列绑定关系
- ✅ 自动配置 JSON 序列化器
- ✅ 自动配置 RabbitTemplate

**开发者只需要做两件事**：
1. 注入 `RabbitMessageSender` 发送消息
2. 使用 `@RabbitListener` 消费消息

#### 4. 发送消息（生产者）

注入 `RabbitMessageSender` 即可发送消息，**消息对象会自动序列化为 JSON**：

```java
import com.thinkboot.mq.rabbitmq.core.RabbitMessageSender;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    @Autowired
    private RabbitMessageSender messageSender;
    
    // 发送普通消息
    public void createOrder(Order order) {
        // 1. 创建订单（你的业务逻辑）
        saveOrder(order);
        
        // 2. 发送消息（框架自动序列化）
        messageSender.send("order.created", order);
    }
    
    // 发送延迟消息（30分钟后超时取消）
    public void sendTimeoutMessage(Order order) {
        messageSender.sendDelay(
            "order.timeout", 
            order, 
            30 * 60 * 1000  // 30分钟，单位毫秒
        );
    }
    
    // 发送到自定义交换机
    public void sendToCustom(String exchange, String routingKey, Object data) {
        messageSender.send(exchange, routingKey, data);
    }
}
```

#### 5. 消费消息（消费者）

使用 Spring 原生 `@RabbitListener` 注解，**框架自动反序列化为 Java 对象**：

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderMessageConsumer {
    
    // 消费订单创建消息
    @RabbitListener(queues = "thinkboot.order.created")
    public void handleOrderCreated(Order order) {
        log.info("收到订单创建消息: {}", order);
        // 处理订单创建后的业务逻辑（如发送通知、更新库存等）
    }
    
    // 消费订单超时消息
    @RabbitListener(queues = "thinkboot.order.timeout")
    public void handleOrderTimeout(Order order) {
        log.info("收到订单超时消息: {}", order);
        // 处理订单超时取消业务逻辑
    }
}
```

#### 6. 配置项说明

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `thinkboot.mq.rabbitmq.enable` | false | 是否启用 RabbitMQ |
| `thinkboot.mq.rabbitmq.exchange` | thinkboot.default.exchange | 默认交换机名称 |
| `thinkboot.mq.rabbitmq.queue-prefix` | thinkboot. | 队列前缀 |

#### 7. 完整使用流程

```
1. 引入依赖 → think-boot-mq-rabbitmq
2. 配置连接 → spring.rabbitmq.*
3. 启用功能 → thinkboot.mq.rabbitmq.enable: true
4. 发送消息 → @Autowired + messageSender.send()
5. 消费消息 → @RabbitListener + 处理方法
```

**总结：开发者只需关注第 4 步和第 5 步的业务逻辑，其他全部自动配置！**

### 幂等性机制

框架提供 `@Idempotent` 注解，基于 Redis + Token 机制自动防止接口重复提交。

#### 1. 引入依赖

在你的项目中添加公共模块依赖（已包含幂等性功能）：

```xml
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-common</artifactId>
</dependency>
```

#### 2. 前端获取 Token

前端在提交表单前，先调用接口获取幂等 Token：

```bash
GET /api/idempotent/token
```

响应示例：
```json
{
  "code": 200,
  "message": "success",
  "data": "550e8400e29b41d4a716446655440000",
  "timestamp": 1700000000000
}
```

#### 3. 后端使用注解

在需要防重复提交的接口上添加 `@Idempotent` 注解，并在请求头中携带 Token：

```java
import com.thinkboot.common.idempotent.Idempotent;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // 防止订单重复提交
    @Idempotent(key = "create_order", expire = 5, message = "订单正在处理中，请勿重复提交")
    @PostMapping
    public R<Order> createOrder(@RequestBody OrderDTO dto) {
        Order order = orderService.createOrder(dto);
        return R.success(order);
    }
}
```

前端请求示例：
```bash
POST /api/orders
X-Idempotent-Token: 550e8400e29b41d4a716446655440000
Content-Type: application/json

{"productId": 1, "quantity": 2}
```

#### 4. 注解参数说明

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| key | String | "" | 业务键前缀，用于区分不同场景 |
| expire | long | 5 | Token 过期时间（秒） |
| message | String | "请勿重复提交" | 重复提交时的错误提示 |

#### 5. 工作原理

1. 前端调用 `GET /api/idempotent/token` 获取 Token（框架自动在 Redis 中创建，默认 5 秒过期）
2. 前端携带 `X-Idempotent-Token` 请求头提交请求
3. 拦截器检查 Redis 中该 Token 是否存在：
   - 存在：删除 Token，放行请求
   - 不存在：拦截请求，返回 "请勿重复提交"
4. 重复提交时 Token 已被删除，自动拦截

#### 6. 配置项

```yaml
thinkboot:
  core:
    idempotent-enabled: true  # 是否启用幂等性机制（默认 true）
```

### Feign Fallback 防级联失败

框架提供 Fallback 示例，当服务调用失败时自动降级，防止级联雪崩。

#### 1. 使用 FallbackFactory（推荐）

```java
import com.thinkboot.feign.fallback.ExampleFeignFallbackFactory;

@FeignClient(
    name = "user-service",
    path = "/api/users",
    fallbackFactory = UserFeignFallbackFactory.class
)
public interface UserFeignClient {
    @GetMapping("/{id}")
    R<User> getUserById(@PathVariable Long id);
}

@Component
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignClient> {
    private static final Logger log = LoggerFactory.getLogger(UserFeignFallbackFactory.class);

    @Override
    public UserFeignClient create(Throwable cause) {
        return new UserFeignClient() {
            @Override
            public R<User> getUserById(Long id) {
                log.error("getUserById fallback, id={}, error={}", id, cause.getMessage());
                return R.error(503, "用户服务暂时不可用");
            }
        };
    }
}
```

#### 2. 启用 Fallback 配置

在 `application.yml` 中启用 Fallback：

```yaml
spring:
  cloud:
    openfeign:
      circuitbreaker:
        enabled: true
```

#### 3. TraceId 自动传递

框架已配置 Feign 请求拦截器，自动传递以下请求头：
- `Authorization` - Token 认证
- `X-User-Id` - 用户 ID
- `X-Trace-Id` - 链路追踪 ID

### 响应缓存

框架基于 Spring Cache + Redis 提供声明式缓存。

#### 1. 基本使用

```java
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

@Service
public class ProductService {

    // 查询时缓存结果
    @Cacheable(value = "product", key = "#id", unless = "#result == null")
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    // 更新时刷新缓存
    @CachePut(value = "product", key = "#product.id")
    public Product update(Product product) {
        productMapper.updateById(product);
        return product;
    }

    // 删除时清除缓存
    @CacheEvict(value = "product", key = "#id")
    public void delete(Long id) {
        productMapper.deleteById(id);
    }

    // 批量清除缓存
    @CacheEvict(value = "product", allEntries = true)
    public void clearCache() {
        // 清除 product 缓存下所有条目
    }
}
```

#### 2. 缓存注解说明

| 注解 | 作用 | 适用场景 |
|------|------|----------|
| @Cacheable | 先查缓存，没有则执行方法并缓存 | 查询接口 |
| @CachePut | 执行方法并更新缓存 | 更新接口 |
| @CacheEvict | 执行方法并清除缓存 | 删除接口 |

#### 3. 完整示例

参考 `think-boot-redis` 模块中的 [CacheExampleService](file:///G:/ThinkBootCloud/think-boot-redis/src/main/java/com/thinkboot/redis/example/CacheExampleService.java) 文件。

---

### 代码生成器

框架提供了独立的代码生成器模块 `think-boot-codegen`，可以基于数据库表自动生成 Entity、Mapper、Service、ServiceImpl、Controller 等 CRUD 代码。

#### 1. 引入依赖

在你的项目中添加代码生成器模块依赖：

```xml
<dependency>
    <groupId>com.thinkboot</groupId>
    <artifactId>think-boot-codegen</artifactId>
</dependency>
```

#### 2. 使用代码生成器

创建代码生成器类并运行：

```java
package com.yourcompany.yourproject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.thinkboot.codegen.ThinkBootCodeGenerator;

public class CodeGenerator {

    public static void main(String[] args) {
        ThinkBootCodeGenerator generator = new ThinkBootCodeGenerator();
        
        generator.url("jdbc:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
                .username("root")
                .password("your_password")
                .tableName("tb_user", "tb_order")
                .moduleName("system")
                .author("YourName")
                .outputPath("D:/project/your-project/src/main/java")
                .parentPackage("com.yourcompany")
                .useBaseEntity(true)
                .useLogicDelete(true)
                .logicDeleteField("deleted")
                .idType(IdType.ASSIGN_ID)
                .ignoreTablePrefix("tb_")
                .generate();
    }
}
```

#### 3. 生成结果

运行后会自动生成以下文件：

```
src/main/java/com/yourcompany/system/
├── domain/entity/
│   ├── User.java
│   └── Order.java
├── mapper/
│   ├── UserMapper.java
│   ├── OrderMapper.java
│   └── xml/
│       ├── UserMapper.xml
│       └── OrderMapper.xml
├── service/
│   ├── UserService.java
│   └── OrderService.java
├── service/impl/
│   ├── UserServiceImpl.java
│   └── OrderServiceImpl.java
└── controller/
    ├── UserController.java
    └── OrderController.java
```

#### 4. 常用配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| url | 数据库连接URL | jdbc:mysql://localhost:3306/thinkboot |
| username | 数据库用户名 | root |
| password | 数据库密码 | root |
| tableName | 要生成的表名（支持多个） | 必填 |
| moduleName | 模块名称（如system、order） | 空 |
| author | 作者名 | thinkboot |
| outputPath | 代码输出路径 | 当前目录/src/main/java |
| parentPackage | 父包名 | com.thinkboot |
| useBaseEntity | 是否继承BaseEntity | true |
| useLogicDelete | 是否启用逻辑删除 | true |
| ignoreTablePrefix | 忽略的表前缀 | tb_ |
| idType | 主键类型 | ASSIGN_ID |

#### 5. 选择性生成

可以通过 `disable*` 方法控制生成哪些文件：

```java
generator.tableName("tb_user")
         .disableController()
         .disableService()
         .disableServiceImpl()
         .generate();
```

> **提示：** 生成代码后，请根据实际业务需求调整生成的代码，尤其是Controller中的接口逻辑。

---

## 🔧 完整配置参考

```yaml
server:
  port: 8080

spring:
  application:
    name: your-service-name

  # 数据库
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000

  # Redis
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0
      timeout: 10000ms

  # Nacos（微服务场景）
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: dev
      config:
        server-addr: localhost:8848
        namespace: dev
        file-extension: yml

# MyBatis-Plus
mybatis-plus:
  mapper-locations: classpath*:mapper/**/*.xml
  type-aliases-package: com.yourcompany.yourproject.model.entity
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
  global-config:
    db-config:
      id-type: ASSIGN_ID
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

# Knife4j API文档
knife4j:
  enable: true
  openapi:
    title: Your Project API
    description: API 文档
    version: 1.0.0

# ThinkBoot 框架配置
thinkboot:
  # 核心配置
  core:
    enable-cors: true
    cors-allowed-origins: ["*"]
    max-upload-size: 10

  # JWT认证
  auth:
    jwt:
      secret: dGhpbmstYm9vdC1qd3Qtc2VjcmV0LWtleS1tdXN0LWJlLWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc=
      expiration: 7200000
      refresh-expiration: 604800000
      skip-paths:
        - /api/auth/login
        - /api/auth/register
        - /doc.html
        - /swagger-resources/**
        - /v3/api-docs/**

  # Redis/Redisson
  redis:
    redisson:
      mode: single                    # single/cluster/sentinel
      address: redis://localhost:6379
      password:
      database: 0
      timeout: 3000
      connection-pool-size: 64

  # Sentinel
  sentinel:
    enabled: false
    dashboard: localhost:8080
    eager: true
```

---

## 💾 数据源配置

框架默认使用**单数据源**配置（满足 90%+ 的项目需求）。如需使用多数据源（读写分离、分库），框架提供了完整的示例配置。

### 单数据源（默认）

在 `application.yml` 中配置即可：

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_db
    username: root
    password: your_password
```

### 多数据源（读写分离）

框架提供了现成的示例配置文件，位于：

```
think-boot-example/src/main/resources/datasource-examples/
├── single-datasource-example.yml      # 单数据源示例
├── multi-datasource-example.yml       # 读写分离示例
└── dynamic-datasource-example.yml     # 分库示例
```

**使用步骤：**

1. 在 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>dynamic-datasource-spring-boot3-starter</artifactId>
    <version>4.3.0</version>
</dependency>
```

2. 将 `multi-datasource-example.yml` 中的配置复制到 `application.yml`

3. 在代码中使用 `@DS` 注解切换数据源：

```java
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> {
    
    // 默认使用主库（写操作）
    public void createUser(User user) {
        this.save(user);
    }
    
    // 使用从库（读操作）
    @DS("slave_1")
    public User getUserById(Long id) {
        return this.getById(id);
    }
}
```

### 分库配置（按业务分离）

参考 `dynamic-datasource-example.yml`，适用于用户库、订单库、日志库分离的场景：

```java
@Service
@DS("order_db")                    // 整个类使用订单库
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> {
    public Order createOrder(Order order) {
        this.save(order);          // 使用 order_db
        return order;
    }
}
```

> **注意事项：**
> - 同一事务中只能使用一个数据源
> - 跨数据源无法使用本地事务，需要使用分布式事务（如 Seata）
> - 建议避免跨库事务，使用消息队列等方式异步处理

---

## 📋 部署模板

框架提供开箱即用的部署模板，位于 `templates/` 目录，开发者可按需复制到项目中使用。

### Docker Compose 一键部署

```bash
cd templates/docker
cp .env.example .env  # 修改环境变量
docker-compose up -d
```

包含服务：Nacos、MySQL、Redis、RabbitMQ、Gateway

### CI/CD 配置

- **GitHub Actions**: 复制 `templates/ci-cd/.github-workflows.yml` 到 `.github/workflows/`
- **GitLab CI**: 复制 `templates/ci-cd/gitlab-ci.yml` 到项目根目录

### Nginx 反向代理

复制 `templates/nginx/gateway.conf` 到 Nginx 配置目录，修改域名和 SSL 证书路径。

---

## 📝 项目结构建议

```
your-project/
├── src/main/java/com/yourcompany/yourproject/
│   ├── YourProjectApplication.java    # 启动类
│   ├── controller/                    # 控制器
│   │   ├── UserController.java
│   │   └── AuthController.java
│   ├── service/                       # 服务接口
│   │   ├── UserService.java
│   │   └── impl/                      # 服务实现
│   │       └── UserServiceImpl.java
│   ├── mapper/                        # 数据访问层
│   │   └── UserMapper.java
│   ├── model/
│   │   ├── entity/                    # 数据库实体
│   │   │   └── User.java
│   │   └── dto/                       # 数据传输对象
│   │       ├── LoginRequest.java
│   │       └── UserDTO.java
│   └── config/                        # 项目特定配置
│       └── YourConfig.java
└── src/main/resources/
    ├── application.yml                # 配置文件
    └── mapper/                        # XML Mapper（可选）
        └── UserMapper.xml
```

---

## 🐛 常见问题

### Q1: JWT密钥怎么生成？

**方式1：在线生成**
访问 https://www.base64encode.org/ ，输入任意字符串（至少32位），点击编码。

**方式2：代码生成**
```java
import cn.hutool.crypto.SecureUtil;
import cn.hutool.codec.Base64;

String key = SecureUtil.generateKey("HmacSHA256").toString();
String base64Key = Base64.encode(key.getBytes());
System.out.println(base64Key);
```

### Q2: 如何关闭某个接口的Token验证？

有两种方式：

**方式1**：在配置文件的 `skip-paths` 中添加路径：

```yaml
thinkboot:
  auth:
    jwt:
      skip-paths:
        - /api/users/public/**
```

**方式2**：在方法或类上添加 `@IgnoreAuth` 注解：

```java
@IgnoreAuth
@GetMapping("/public/info")
public R<String> publicInfo() {
    return R.success("这是公开信息");
}
```

### Q3: 如何自定义分页大小限制？

在请求中传递 `pageNo` 和 `pageSize` 参数，框架默认限制 pageSize 最大为 100：

```java
// 修改 PageRequest 中的默认限制
public class CustomPageRequest extends PageRequest {
    @Override
    public int getPageSize() {
        return pageSize < 1 ? 10 : (pageSize > 500 ? 500 : pageSize);
    }
}
```

### Q4: Gateway网关如何配置路由？

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
```

### Q5: 如何获取当前登录用户信息？

```java
// 获取当前登录用户（默认需要认证，无需添加注解）
@GetMapping("/me")
public R<String> getCurrentUser() {
    // 获取当前用户ID
    String userId = UserContext.getCurrentUserId();
    
    // 根据userId查询完整用户信息
    User user = userService.getById(Long.valueOf(userId));
    return R.success(user);
}
```

### Q6: 微服务之间如何通信？

推荐使用 **OpenFeign**（已内置）：

1. 在启动类添加 `@EnableFeignClients`
2. 定义 `@FeignClient` 接口
3. 注入接口直接调用
4. **Token自动传递**，无需手动处理

---

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

**MIT 协议是最宽松的开源协议之一，意味着：**

- ✅ 完全免费使用
- ✅ 可自由修改源码
- ✅ 可自由分发
- ✅ 可自由商用
- ✅ 无需支付任何费用
- ✅ 无强制开源要求

**唯一要求：在软件副本中包含原始版权声明和许可声明即可。**

---

## 🤝 参与贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的改动 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

---

## 📮 联系方式

- 项目地址：https://gitee.com/hongxinge/think-boot-cloud
- 问题反馈：https://gitee.com/hongxinge/think-boot-cloud/issues
