<template>
  <mu-container ref="container" style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-drawer :open.sync="drawer_open" :docked="drawer_docked" :right="drawer_position">
      <Profile></Profile>
    </mu-drawer>
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="drawer_open=!drawer_open">
        <mu-icon value="menu"></mu-icon>
      </mu-button>
      {{$t('appname')}}
      <mu-button icon slot="right">
        <mu-icon value="chat"></mu-icon>
      </mu-button>
    </mu-appbar>
    <mu-load-more @refresh="refresh" :refreshing="refreshing" :loading="loading" @load="load">
      <mu-list>
        <div class="main_search_box" @click="$router.push('/search')">
          <mu-icon value="search" style="vertical-align: middle"/>
          {{$t('search')}}
        </div>
        <SubTitleBar :sub-title="$t('top_like_auther')" :action="$t('more')" :go="goTopAutherList"></SubTitleBar>
        <mu-row gutter>
          <mu-col v-for="user in userList" :key="user.id" span="2">
            <UserSquare :user="user"></UserSquare>
          </mu-col>
        </mu-row>
        <mu-divider></mu-divider>
        <SubTitleBar :sub-title="$t('top_modify_book')" :action="$t('more')" :go="goTopBookList"></SubTitleBar>
        <BookItemTile v-for="book in bookList" :key="book.id" :book="book"></BookItemTile>
        <mu-divider></mu-divider>
        <SubTitleBar :sub-title="$t('top_latest_article')"></SubTitleBar>
        <ArticleItemTile v-for="article in articleList" :key="article.id" :article="article"></ArticleItemTile>
      </mu-list>
    </mu-load-more>
  </mu-container>
</template>

<script>
import Profile from './Profile'
import SubTitleBar from '../views/SubTitleBar'
import UserSquare from '../views/UserSquare'
import BookItemTile from '../views/BookItemTile'
import ArticleItemTile from '../views/ArticleItemTile'
import {api} from '../service/api'

export default {
  name: 'Main',
  components: {Profile, SubTitleBar, UserSquare, BookItemTile, ArticleItemTile},
  data () {
    return {
      drawer_open: false,
      drawer_docked: false,
      drawer_position: false,
      refreshing: false,
      loading: false,
      userList: [],
      bookList: [],
      articleList: [],
      pagerForm: {
        pageNumber: 1,
        pageSize: 20
      },
      goTopAutherList () {
        alert('goTopAuther')
      },
      goTopBookList () {
        alert('goTopAuther')
      }
    }
  },
  created: function () {
    api.getUserList({pageNumber: 1, pageSize: 6, sort: 3})
      .then((result) => {
        this.userList = result.data
      })
    api.getBookList({pageNumber: 1, pageSize: 5})
      .then((result) => {
        this.bookList = result.data
      })
    api.getArticleList(this.pagerForm)
      .then((result) => {
        this.articleList = result.data
      })
  },
  methods: {
    refresh () {
      this.refreshing = true
      this.$refs.container.scrollTop = 0
      this.pagerForm.pageNumber = 1
      api.getArticleList(this.pagerForm)
        .then((result) => {
          this.refreshing = false
          this.articleList = result.data
        })
    },
    load () {
      this.loading = true
      this.pagerForm.pageNumber++
      api.getArticleList(this.pagerForm)
        .then((result) => {
          this.loading = false
          this.articleList = this.articleList.concat(result.data)
        })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .main_search_box{
    cursor: pointer;
    padding: 6px;
    margin-left: 16px;
    margin-right: 16px;
    border-radius: 5px;
    border: 1px solid #bdbdbd;
    color: #bdbdbd;
  }
  .mu-item-action{
    min-width: 32px;
  }
  .mu-tab > div{
    min-height: 64px;
  }
</style>
