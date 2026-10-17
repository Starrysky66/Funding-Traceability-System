# 助学帮扶管理系统 - 后端

## 技术栈
- Spring Boot 2.7.18 / Java 8
- MyBatis-Plus 3.5.3.1（分页插件）
- MySQL 8（mysql-connector-java 8.0.33）
- jjwt 0.11.5（JWT 认证）
- Apache POI 5.2.3（Excel 导出）
- Lombok 1.18.24

## 运行步骤

### 1. 初始化数据库
执行 `src/main/resources/sql/schema.sql`：
```bash
mysql -u root -p < src/main/resources/sql/schema.sql
```

### 2. 修改数据源配置
编辑 `src/main/resources/application.yml`，按本机 MySQL 配置修改 `username` 和 `password`。

### 3. 编译运行
```bash
cd backend
mvn spring-boot:run
```
服务启动后监听 `http://localhost:8080`。

## 预置账号
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | ADMIN（管理员，全部增删改查+导出）|
| sponsor | 123456 | SPONSOR（资助人，只读自己名下数据）|

## 接口概览
- 认证：`POST /api/auth/login`、`GET /api/auth/info`、`POST /api/auth/logout`
- 用户管理（ADMIN）：`/api/users`
- 学生：`/api/students`（含 `/options`、`/export`）
- 资金：`/api/funds`（含 `/export`）
- 回访：`/api/visits`（含 `/export`）
- 评分指标：`/api/score-items`
- 评分：`/api/scores`（含 `/export`，后端按权重加权自动计算总分）
- 统计：`GET /api/dashboard/stats`

## 认证
请求头携带 `Authorization: Bearer <token>`，登录返回 `{token}`，有效期 24 小时。
