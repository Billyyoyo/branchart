import axios from 'axios'
import md5 from 'js-md5'

const SIGN_SALT = '7146442KDH'

axios.defaults.timeout = 5000
axios.defaults.baseURL = 'http://localhost:8889'

axios.interceptors.request.use(
  (config) => {
    let uuidCode
    if (localStorage.getItem('ba_uuid_code')) {
      uuidCode = localStorage.getItem('ba_uuid_code')
    } else {
      let uuidv1 = require('uuid/v1')
      let uuid = uuidv1()
      localStorage.setItem('ba_uuid_code', uuid)
      uuidCode = uuid
    }
    config.headers['Accept-Language'] = getLanguage()
    config.headers['ba_uuid_code'] = uuidCode
    return config
  }, (error) => {
    // console.log(JSON.stringify(error))
    return Promise.reject(error)
  })

axios.interceptors.response.use(
  (res) => {
    // console.log(JSON.stringify(res))
    if (res.data.code === 0) {
      return res
    }
    return Promise.reject(res)
  }, (error) => {
    // console.log(JSON.stringify(error))
    return Promise.reject(error)
  })

function getLanguage () {
  if (localStorage.getItem('ba_accept_language')) {
    return localStorage.getItem('ba_accept_language')
  } else {
    return (navigator.language || navigator.browserLanguage).toLowerCase().substr(0, 2)
  }
}

export function get (url, params) {
  let sign = url + '|' + localStorage.getItem('ba_uuid_code') + '|' + SIGN_SALT
  sign = md5(sign)
  // console.log('uuid: ' + localStorage.getItem('ba_uuid_code'))
  // console.log('sign: ' + sign)
  return new Promise((resolve, reject) => {
    axios.get(url,
      {
        headers: {
          'ba_token_id': localStorage.getItem('ba_token_code'),
          'ba_user_id': localStorage.getItem('ba_user_id'),
          'ba_signiture_code': sign
        },
        params: params
      })
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
      .catch((error) => {
        reject(error)
      })
  })
}

export function post (url, params) {
  let sign = url + '|' + localStorage.getItem('ba_uuid_code') + '|' + SIGN_SALT
  sign = md5(sign)
  return new Promise((resolve, reject) => {
    axios.post(url,
      params,
      {
        headers: {
          'ba_token_id': localStorage.getItem('ba_token_code'),
          'ba_user_id': localStorage.getItem('ba_user_id'),
          'ba_signiture_code': sign
        }
      }
    ).then(response => {
      resolve(response.data)
    }, err => {
      reject(err)
    })
      .catch((error) => {
        reject(error)
      })
  })
}

export const api = {
  login: function (loginName, password) {
    let params = {
      loginName: loginName,
      password: password
    }
    return post('/auth/login', params)
  },
  register: function (loginName, password) {
    let params = {
      loginName: loginName,
      password: password
    }
    return post('/auth/register', params)
  },
  getUserInfo: function (userId) {
    return get('/user/' + userId)
  },
  editUserInfo: function (form) {
    return post('/user/edit', form)
  },
  changePassword: function (form) {
    return post('/user/changepwd', form)
  },
  logout: function () {
    return get('/auth/logout')
  },
  getArticleList: function (form) {
    return get('/article/list', form)
  },
  getBookList: function (form) {
    if (form.ownerId) {
      return get('/book/list/' + form.ownerId, form)
    } else {
      return get('/book/list', form)
    }
  },
  getTagList: function (form) {
    return get('/article/tags', form)
  },
  getUserList: function (form) {
    return get('/user/list/' + form.sort, form)
  },
  getArticle: function (articleId) {
    return get('/article/' + articleId)
  },
  getCommentList: function (target, type, targetId, form) {
    return get('/comment/list/' + target + '/' + type + '/' + targetId, form)
  },
  sendComment: function (form) {
    return post('/comment/send', form)
  },
  createArticle: function (form) {
    return post('/article/create', form)
  },
  checkSocial: function (idolId) {
    return get('/social/check/' + idolId)
  },
  getSocial: function (flag, type, userId, form) {
    return get('/social/relationlist/' + flag + '/' + type + '/' + userId, form)
  },
  follow: function (idolId, flag, type) {
    return get('/social/follow/' + flag + '/' + type + '/' + idolId)
  },
  addBook: function (form) {
    return post('/book/create', form)
  },
  getBook: function (bookId, form) {
    return get('/book/' + bookId, form)
  },
  addArticleInBook: function (bookId, articleId) {
    return post('/book/addarticle/' + bookId + '/' + articleId)
  },
  addFavorite: function (type, itemId) {
    return post('/fav/add/' + type + '/' + itemId)
  },
  getFavoriteList: function () {
    return get('/fav/list')
  }
}
