import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import { getToken } from '@/utils/auth'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user
  },
  getters: {
    token: state => state.user.token,
    role: state => state.user.role,
    realName: state => state.user.realName,
    username: state => state.user.username,
    userId: state => state.user.id,
    phone: state => state.user.phone,
    accessRoutes: state => state.user.accessRoutes,
    hasToken: state => !!state.user.token || !!getToken()
  }
})

export default store
