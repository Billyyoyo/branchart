import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
export const store = new Vuex.Store({
  state: {
    user: null,
    userExtra: null
  },
  getters: {
    getLoginUser: state => {
      return state.user
    },
    getLoginUserExtra: state => {
      return state.userExtra
    }
  },
  mutations: {
    setLoginUser: (state, userData) => {
      state.user = userData.user
      state.userExtra = userData.extra
      localStorage.setItem('ba_user_id', userData.user.id)
      localStorage.setItem('ba_user', JSON.stringify(userData.user))
      localStorage.setItem('ba_user_extra', JSON.stringify(userData.userExtra))
    },
    removeLoginUser: (state) => {
      state.user = null
      state.userExtra = null
      localStorage.removeItem('ba_user_id')
      localStorage.removeItem('ba_token_code')
      localStorage.removeItem('ba_user')
      localStorage.removeItem('ba_user_extra')
    }
  }
})
