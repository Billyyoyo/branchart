<template>
  <mu-container ref="container" style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{$t('favorite')}}
    </mu-appbar>
    <mu-list>
      <template v-if="articleList&&articleList.length>0">
        <mu-sub-header style="text-align: left;">{{$t('article')}}</mu-sub-header>
        <FavItemTile v-for="article in articleList" :key="article.id" :item="article"></FavItemTile>
      </template>
      <mu-divider v-if="bookList&&articleList&&bookList.length>0&&articleList.length>0"></mu-divider>
      <template v-if="bookList&&bookList.length>0">
        <mu-sub-header style="text-align: left;">{{$t('book')}}</mu-sub-header>
        <FavItemTile v-for="book in bookList" :key="book.id" :item="book"></FavItemTile>
      </template>
    </mu-list>
  </mu-container>
</template>

<script>
import {api} from '../service/api'
import FavItemTile from '../views/FavItemTile'

export default {
  name: 'FavoriteList',
  components: {FavItemTile},
  data: function () {
    return {
      articleList: [],
      bookList: []
    }
  },
  computed: {
    loginUser () {
      return this.$store.getters.getLoginUser
    },
    loginUserExtra () {
      return this.$store.getters.getLoginUserExtra
    }
  },
  created: function () {
    api.getFavoriteList()
      .then(result => {
        result.data.forEach(item => {
          if (item.type === 0) {
            this.articleList.push(item)
            console.log('++++++++++++++++++++++++++++++++')
          } else {
            this.bookList.push(item)
            console.log('--------------------------------')
          }
        })
        console.log('articleList : ' + JSON.stringify(this.articleList))
        console.log('bookList : ' + JSON.stringify(this.bookList))
      })
  }
}
</script>

<style scoped>

</style>
