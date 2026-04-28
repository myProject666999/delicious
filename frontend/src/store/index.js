import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    userInfo: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : null,
    role: localStorage.getItem('role') || ''
  },
  getters: {
    isLoggedIn: state => !!state.token,
    isAdmin: state => state.role === 'admin',
    isUser: state => state.role === 'user'
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    SET_ROLE(state, role) {
      state.role = role
      localStorage.setItem('role', role)
    },
    LOGOUT(state) {
      state.token = ''
      state.userInfo = null
      state.role = ''
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('role')
    }
  },
  actions: {
    login({ commit }, { token, userInfo, role }) {
      commit('SET_TOKEN', token)
      commit('SET_USER_INFO', userInfo)
      commit('SET_ROLE', role)
    },
    logout({ commit }) {
      commit('LOGOUT')
    }
  },
  modules: {
  }
})
