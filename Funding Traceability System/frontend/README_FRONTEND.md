# 助学帮扶管理系统 - 前端工程

## 技术栈

- Vue 2.6.14
- Element UI 2.15.14
- vue-router 3.5.4
- vuex 3.6.2
- axios 0.27.2
- echarts 5.4.3
- Vue CLI 4.5 (@vue/cli-service 4.5.19)

## 运行方式

在 `frontend` 目录下执行：

```bash
npm install
npm run serve
```

启动成功后访问：http://localhost:8081

## 后端契约

- 后端地址：http://localhost:8080
- 开发代理：`devServer.port = 8081`，`/api` → `http://localhost:8080`（changeOrigin: true）
- axios 实例 baseURL：`/api`
- 统一响应：`{ code: 200, message: "success", data: T }`
- 认证头：`Authorization: Bearer <token>`，token 存 localStorage key `funding_token`
- 角色：`ADMIN`（全部增删改查 + 导出）、`SPONSOR`（纯只读自己名下数据，无任何新增/编辑/删除/导出按钮）

## 目录结构

```
frontend/
├─ package.json
├─ vue.config.js
├─ babel.config.js
├─ .gitignore
├─ public/index.html
├─ src/
│  ├─ main.js
│  ├─ App.vue
│  ├─ api/            # auth, student, fund, visit, score, scoreItem, user, dashboard, index
│  ├─ layout/Layout.vue
│  ├─ router/index.js
│  ├─ store/          # index.js, modules/user.js
│  ├─ utils/          # request.js, auth.js, permission.js, download.js
│  └─ views/          # Login, Dashboard, 404, student/Student, fund/Fund, visit/Visit, score/Score, score/ScoreItem, user/User
└─ README_FRONTEND.md
```

## 构建发布

```bash
npm run build
```

产物输出至 `dist/` 目录，部署时由后端或 Nginx 反向代理 `/api` 至后端服务即可。
