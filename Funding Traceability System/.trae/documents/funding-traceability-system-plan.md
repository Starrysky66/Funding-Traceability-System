# 助学帮扶管理系统 - 实施方案

## Context（背景）
用户需要一套可直接运行、适合课程设计/Demo展示的助学帮扶管理简易系统。采用 SpringBoot + Vue2 + MySQL 技术栈，实现管理员与资助人双角色权限严格隔离，覆盖受助学生档案、资助资金流水、下乡回访、助学成效加权打分、首页可视化、Excel导出等完整功能。当前工作目录为空，为全新项目。

## 技术栈
- 后端：Spring Boot 2.7.18 + Java 8 + MyBatis-Plus 3.5.x + MySQL 8 + JWT(jjwt 0.11.x) + Apache POI 5.x(Excel) + Lombok
- 前端：Vue 2.6 + Element UI 2.15 + Vue Router 3 + Vuex 3 + Axios + ECharts 5
- 构建：后端 Maven，前端 Vue CLI 4

## 项目结构
```
d:\Funding Traceability System\Funding Traceability System\
├── backend\                          # Spring Boot 后端
│   ├── pom.xml
│   └── src\main\
│       ├── java\com\funding\
│       │   ├── FundingApplication.java
│       │   ├── common\            # 通用：Result 统一响应、JwtUtils、拦截器、全局异常、CORS配置
│       │   ├── entity\             # 实体：SysUser, Student, Fund, Visit, ScoreItem, Score, ScoreDetail
│       │   ├── mapper\             # MyBatis-Plus Mapper 接口
│       │   ├── service\            # Service 接口 + Impl
│       │   ├── controller\         # Controller
│       │   └── config\            # MyBatis-Plus 配置、WebMvc 配置
│       └── resources\
│           ├── application.yml
│           └── sql\schema.sql     # 建表 + 初始账号数据
└── frontend\                         # Vue2 前端
    ├── package.json, vue.config.js
    └── src\
        ├── api\          # axios 请求模块（auth/student/fund/visit/score/dashboard）
        ├── utils\        # request.js(token拦截) + permission.js
        ├── router\       # 路由 + 守卫(角色动态路由)
        ├── store\        # Vuex(user/role)
        ├── directive\     # v-permission 按钮权限指令
        ├── layout\        # Layout.vue(侧边栏+顶栏)
        └── views\
            ├── Login.vue, Dashboard.vue
            ├── student\Student.vue, fund\Fund.vue, visit\Visit.vue
            ├── score\Score.vue, score\ScoreItem.vue
            └── user\User.vue(管理员账号管理)
```

## 数据库设计（6 张核心表）
1. `sys_user`：用户(id, username, password, real_name, role[ADMIN/SPONSOR], phone, create_time)
2. `student`：受助学生档案(id, name, gender, id_card, school, grade, family_address, family_situation, phone, sponsor_id→sys_user.id, create_time, update_time)
3. `fund`：资助资金流水(id, student_id, sponsor_id, amount, fund_date, purpose, remark, create_time)
4. `visit`：回访记录(id, student_id, sponsor_id, visit_date, content, situation, problem, visitor, create_time)
5. `score_item`：评分指标(id, name, weight[权重百分比], description) —— 预置：学习情况、生活改善、心理状态、家庭帮扶成效
6. `score`：助学评分单(id, student_id, sponsor_id, score_date, total_score[自动加权], remark, create_time)
7. `score_detail`：评分明细(id, score_id, item_id, item_score)

**数据隔离核心**：`student/fund/visit/score` 均含 `sponsor_id`，资助人查询自动按当前登录 userId 过滤，管理员不过滤。

## 权限与安全
- 登录返回 JWT（含 userId、username、role），前端存 localStorage，axios 携带 `Authorization: Bearer`
- 后端 `JwtInterceptor` 校验 token；`@RoleCheck` 或路径维度区分：写操作（POST/PUT/DELETE/export）仅 ADMIN
- 前端 `v-permission="['ADMIN']"` 指令隐藏资助人页面的新增/编辑/删除/导出按钮
- 路由守卫：登录后按 role 生成动态路由并 `addRoutes`

## 助学评分自动计算（核心）
- 管理员在评分页录入各指标单项分数（0-100）
- 后端保存 `score_detail` 时，按 `score_item.weight` 加权计算：`总分 = Σ(item_score × weight%)`
- `total_score` 由后端计算写入，前端只读展示，接口拒绝直接传入 total_score
- 指标权重管理员可调整，调整后新评分按新权重计算

## Excel 导出
- 后端用 Apache POI 在 `/api/xxx/export` 生成 .xlsx，以流形式返回
- 学生/资金/回访/评分四个模块各一个导出接口，仅 ADMIN 可调用

## 首页可视化（ECharts）
- 统计接口 `/api/dashboard/stats`：学生总数、资助总金额、资助次数、回访次数、各指标评分均值、月度资金趋势
- 4 个简易图表：数据卡片 + 柱状图(各模块次数) + 折线图(月度资金) + 饼图(资金用途分布)

## 关键接口清单
| 模块 | 方法 | 路径 | 权限 |
|------|------|------|------|
| 登录 | POST | /api/auth/login | 公开 |
| 当前用户 | GET | /api/auth/info | 登录 |
| 学生 | GET/POST/PUT/DELETE/GET export | /api/students... | 查看ALL；写/export仅ADMIN |
| 资金 | 同上 | /api/funds... | 同上 |
| 回访 | 同上 | /api/visits... | 同上 |
| 评分指标 | GET/POST/PUT/DELETE | /api/score-items | GET登录；写仅ADMIN |
| 评分 | GET/POST/PUT/DELETE/export | /api/scores... | 查看ALL；写/export仅ADMIN |
| 统计 | GET | /api/dashboard/stats | 登录 |

## 初始账号（schema.sql 预置）
- admin / 123456（管理员）
- sponsor / 123456（资助人）—— 并预置该资助人名下若干学生、资金、回访、评分样例数据，便于验证只读隔离

## 实施步骤
1. 创建 `backend/` Maven 工程：pom.xml、application.yml、FundingApplication、schema.sql
2. 实体 + Mapper + MyBatis-Plus 配置
3. 通用层：Result、JwtUtils、JwtInterceptor、GlobalExceptionHandler、CORS、WebMvcConfig
4. 业务 Service + Controller（学生/资金/回访/指标/评分/统计/用户），含权限校验与数据隔离
5. Excel 导出工具类 + 各导出接口
6. 创建 `frontend/` Vue 工程：package.json、vue.config.js、main.js
7. utils/request.js、api 模块、router 守卫、Vuex store、v-permission 指令
8. Layout 组件 + Login + Dashboard(ECharts)
9. 各业务页面（学生/资金/回访/评分/指标/用户），区分 ADMIN/SPONSOR 视图
10. 编写运行说明 README，验证后端启动与前端启动

## 验证方式
- 后端：`mvn spring-boot:run`（需先在 MySQL 执行 schema.sql），访问 http://localhost:8080
- 前端：`npm install` 后 `npm run serve`，访问 http://localhost:8081，代理到后端
- 验证点：
  1. admin 登录后全量 CRUD + 导出可用
  2. sponsor 登录后仅看到自己名下数据，且无新增/编辑/删除/导出按钮
  3. 直接调用写接口（sponsor token）被后端拦截返回 403
  4. 评分保存后 total_score 由后端自动加权计算，前端不可手改
  5. 首页 ECharts 图表正常渲染统计数据
