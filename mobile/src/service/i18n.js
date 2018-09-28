import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)

export const i18n = new VueI18n({
  locale: getCurrentLanguage(),
  fallbackLocale: 'en-us',
  messages: {
    'en-us': require('../lang/en-us.json'),
    'zh-cn': require('../lang/zh-cn.json')
  }
})

function getCurrentLanguage () {
  if (localStorage.getItem('ba_accept_language')) {
    return localStorage.getItem('ba_accept_language')
  } else {
    return (navigator.language || navigator.browserLanguage).toLowerCase().substr(0, 2)
  }
}
