import axios from 'axios'
import { Message } from 'element-ui'
import { getToken, removeToken } from './auth'

// 使用 lazy 引入 router，避免与 store/router 之间的循环依赖问题
let router
const getRouter = () => {
  if (!router) {
    router = require('../router').default
  }
  return router
}

const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 请求拦截器：附加 Token
service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器：统一处理后端返回结构 {code, message, data}
service.interceptors.response.use(
  response => {
    const config = response.config || {}
    const contentType = response.headers['content-type'] || ''
    // 文件下载（blob / excel / 流）直接返回原始数据，不走统一解构
    if (
      config.responseType === 'blob' ||
      contentType.includes('spreadsheet') ||
      contentType.includes('octet-stream') ||
      contentType.includes('excel') ||
      contentType.includes('application/vnd.ms-excel')
    ) {
      return response.data
    }
    const res = response.data
    // 非 JSON 结构直接返回
    if (typeof res !== 'object' || res === null) {
      return res
    }
    if (res.code === 200) {
      return res.data
    }
    if (res.code === 401) {
      removeToken()
      Message.error(res.message || '登录已失效，请重新登录')
      getRouter().push('/login')
      return Promise.reject(res)
    }
    if (res.code === 403) {
      Message.error(res.message || '权限不足，无法执行此操作')
      return Promise.reject(res)
    }
    Message.error(res.message || '请求失败')
    return Promise.reject(res)
  },
  error => {
    const status = error.response && error.response.status
    if (status === 401) {
      removeToken()
      Message.error('登录已失效，请重新登录')
      getRouter().push('/login')
    } else if (status === 403) {
      Message.error((error.response && error.response.data && error.response.data.message) || '权限不足，无法执行此操作')
    } else {
      const msg = (error.response && error.response.data && (error.response.data.message || error.response.data.error)) || error.message || '网络错误'
      Message.error(msg)
    }
    return Promise.reject(error)
  }
)

export default service
