import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/pages/Main'
import SignIn from '@/pages/SignIn'
import SignUp from '@/pages/SignUp'
import EditProfile from '@/pages/EditProfile'
import Settings from '@/pages/Settings'
import ChangePwd from '@/pages/ChangePwd'
import CreateArticle from '@/pages/CreateArticle'
import Search from '@/pages/Search'
import SearchResult from '@/pages/SearchResult'
import ArticleList from '@/pages/ArticleList'
import BookList from '@/pages/BookList'
import Article from '@/pages/Article'
import Book from '@/pages/Book'
import User from '@/pages/User'
import Social from '@/pages/Social'
import FavoriteList from '@/pages/FavoriteList'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Main',
      component: Main
    },
    {
      path: '/signin',
      name: 'SignIn',
      component: SignIn
    },
    {
      path: '/signup',
      name: 'SignUp',
      component: SignUp
    },
    {
      path: '/editprofile',
      name: 'EditProfile',
      component: EditProfile
    },
    {
      path: '/settings',
      name: 'Settings',
      component: Settings
    },
    {
      path: '/changepwd',
      name: 'ChangePwd',
      component: ChangePwd
    },
    {
      path: '/createarticle',
      name: 'CreateArticle',
      component: CreateArticle
    },
    {
      path: '/search',
      name: 'Search',
      component: Search
    },
    {
      path: '/searchresult',
      name: 'SearchResult',
      component: SearchResult
    },
    {
      path: '/articlelist',
      name: 'ArticleList',
      component: ArticleList
    },
    {
      path: '/booklist',
      name: 'BookList',
      component: BookList
    },
    {
      path: '/article/:id',
      name: 'Article',
      component: Article
    },
    {
      path: '/book/:id',
      name: 'Book',
      component: Book
    },
    {
      path: '/favoritelist',
      name: 'FavoriteList',
      component: FavoriteList
    },
    {
      path: '/user/:id',
      name: 'User',
      component: User
    },
    {
      path: '/social/:type/:userId',
      name: 'Social',
      component: Social
    }
  ]
})
