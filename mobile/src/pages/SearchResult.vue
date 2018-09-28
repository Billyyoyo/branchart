<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{keyword}}
    </mu-appbar>
    <mu-load-more v-if="resultList.length>0" @refresh="refresh" :refreshing="refreshing" :loading="loading" @load="load">
      <mu-list>
        <ArticleItemTile v-for="article in resultList" :key="article.id" :article="article"></ArticleItemTile>
      </mu-list>
    </mu-load-more>
    <div v-else class="search_result_empty">
      {{$t('empty_result')}}
    </div>
  </mu-container>
</template>

<script>
import ArticleItemTile from '../views/ArticleItemTile'
import {api} from '../service/api'

export default {
  name: 'SearchResult',
  components: {ArticleItemTile},
  data: function () {
    return {
      refreshing: false,
      loading: false,
      title: this.$route.query.title,
      resultList: [],
      form: {
        tag: '',
        pageNumber: 1,
        pageSize: 16
      }
    }
  },
  created: function () {
    this.form.tag = this.title
    api.getArticleList(this.form)
      .then((result) => {
        this.resultList = result.data
      })
  },
  methods: {
    refresh () {
      this.refreshing = true
      this.$refs.container.scrollTop = 0
      this.form.pageNumber = 1
      api.getArticleList(this.form)
        .then((result) => {
          this.refreshing = false
          this.articleList = result.data
        })
    },
    load () {
      this.loading = true
      this.form.pageNumber++
      api.getArticleList(this.form)
        .then((result) => {
          this.loading = false
          this.articleList = this.articleList.concat(result.data)
        })
    }
  }
}
</script>

<style scoped>
  .search_result_empty {
    font-size: 20px;
    font-weight: bold;
    color: lightslategray;
    height: 600px;
    margin-top: 280px;
    vertical-align: middle;
  }
</style>
