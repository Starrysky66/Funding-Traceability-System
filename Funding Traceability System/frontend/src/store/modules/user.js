import { login, getInfo, logout } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

const getDefaultState = () => ({
  token: getToken(),
  id: null,
  username: '',
  realName: '',
  role: '',
  phone: '',
  accessRoutes: []
})

const state = getDefaultState()

const mutations = {
  RESET_STATE(state) {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN(state, token) {
    state.token = token
  },
  SET_USER_INFO(state, userInfo) {
    state.id = userInfo.id || null
    state.username = userInfo.username || ''
    state.realName = userInfo.realName || ''
    state.role = userInfo.role || ''
    state.phone = userInfo.phone || ''
  },
  SET_ACCESS_ROUTES(state, routes) {
    state.accessRoutes = routes || []
  }
}

const actions = {
  // 登录
  login({ commit }, { username, password }) {
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password }).then(data => {
        const token = (data && data.token) || ''
        commit('SET_TOKEN', token)
        setToken(token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取当前用户信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      if (!state.token) {
        reject(new Error('无 token，请重新登录'))
        return
      }
      getInfo().then(data => {
        if (!data) {
          reject(new Error('用户信息获取失败，请重新登录'))
          return
        }
        commit('SET_USER_INFO', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 退出登录
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        commit('RESET_STATE')
        removeToken()
        resolve()
      }).catch(error => {
        // 即使退出接口失败，也清除本地状态
        commit('RESET_STATE')
        removeToken()
        reject(error)
      })
    })
  },

  // 仅清除前端 token / 状态（用于 401 失效兜底）
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('RESET_STATE')
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
