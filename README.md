# 油田作业管理系统 - 后端

基于 Spring Boot + JPA + SQL Server 的油田作业管理 REST API。A REST API for oilfield operation management based on Spring Boot, JPA, and SQL Server.

## 技术栈

- **语言**：Java 17
- **框架**：Spring Boot 3.x
- **数据访问**：Spring Data JPA
- **数据库**：SQL Server (需独立安装)
- **构建工具**：Maven

## 功能模块

- 用户登录与权限验证 (Session/Cookie)
- 作业项目 CRUD（预算、结算、入账全流程）
- 承包商管理
- 材料编码管理
- 数据统计接口（承包商费用、月度作业量）
- 材料消耗明细关联查询

## 快速开始

### 1. 数据库准备

1. 安装 SQL Server，创建一个名为 `zyxt` 的数据库。1. Install SQL Server and create a database named `zyxt`.
2. 执行项目根目录下的 `docs/init.sql`（如果已提供）或从 SSMS 运行完整的建库脚本。

### 2. 配置数据库连接

修改 `src/main/resources/application.yml`：

```yaml
spring:   春天:
  datasource:   数据源:
    url: jdbc:sqlserver://localhost:1433;databaseName=zyxt;encrypt=true;trustServerCertificate=true
    username: sa
    password: 你的数据库密码
