// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'
import router from './router'
import MuseUI from 'muse-ui'
import Toast from 'muse-ui-toast'
import Loading from 'muse-ui-loading'
import Message from 'muse-ui-message'
import 'muse-ui/dist/muse-ui.css'
import 'muse-ui-loading/dist/muse-ui-loading.css'
import 'muse-ui-message/dist/muse-ui-message.css'
import {store} from './service/store'
import {i18n} from './service/i18n'
import {api} from './service/api'

Vue.config.productionTip = false
Vue.use(Vuex)
Vue.use(MuseUI)
Vue.use(Toast)
Vue.use(Loading)
Vue.use(Message)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store: store,
  i18n: i18n,
  router: router,
  components: {App},
  template: '<App/>',
  created: function () {
    if (localStorage.getItem('ba_accept_language')) {
    } else {
      localStorage.setItem('ba_accept_language', (navigator.language || navigator.browserLanguage).toLowerCase())
    }
    if (localStorage.getItem('ba_user_id')) {
      api.getUserInfo(localStorage.getItem('ba_user_id'))
        .then((result) => {
          this.$store.commit('setLoginUser', result.data)
        })
        .catch(error => {
          this.$toast.error(error.data.msg)
        })
    }
  }
})
