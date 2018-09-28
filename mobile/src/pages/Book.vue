<template>
  <mu-container ref="container" style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="transparent" text-color="grey800" z-depth="0" >
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      <template v-if="book">{{book.title}}</template>
      <mu-button icon slot="right" @click="addFav">
        <mu-icon value="bookmark"></mu-icon>
      </mu-button>
    </mu-appbar>
    <mu-load-more v-if="resultList.length>0" @refresh="refresh" :refreshing="refreshing" :loading="loading" @load="load">
      <div v-if="book" style="text-align: left;margin: 16px;">
        <div style="cursor: pointer;" @click="$router.push('/user/' + book.ownerId)">
          <mu-avatar :size="profileSize" style="vertical-align: middle;">
            <img src="../assets/profile/user_profile.png">
          </mu-avatar>
          <span style="margin-left: 5px;font-size:16px; font-weight: bold;color: #999999;">{{book.ownerName}}</span>
        </div>
      </div>
      <mu-divider/>
      <mu-list>
        <ChapterItemTile v-for="(chapter, i) in resultList" :key="chapter.id" :index="i" :chapter="chapter"></ChapterItemTile>
      </mu-list>
    </mu-load-more>
    <div v-else class="search_result_empty">
      {{$t('empty_result')}}
    </div>
  </mu-container>
</template>

<script>
import ChapterItemTile from '../views/ChapterItemTile'
import {api} from '../service/api'

export default {
  name: 'Book',
  components: {ChapterItemTile},
  data: function () {
    return {
      refreshing: false,
      loading: false,
      book: null,
      resultList: [],
      form: {
        pageNumber: 1,
        pageSize: 16
      }
    }
  },
  created: function () {
    this.refresh()
  },
  methods: {
    refresh () {
      this.refreshing = true
      // this.$refs.container.scrollTop = 0
      this.form.pageNumber = 1
      api.getBook(this.$route.params.id, this.form)
        .then((result) => {
          this.refreshing = false
          this.book = result.data.book
          this.resultList = result.data.chapters
        })
    },
    load () {
      this.loading = true
      this.form.pageNumber++
      api.getBook(this.$route.params.id, this.form)
        .then((result) => {
          this.loading = false
          this.resultList = this.resultList.concat(result.data.chapters)
        })
    },
    addFav () {
      api.addFavorite(1, this.book.id)
        .then(result => {
          this.$toast.success(this.$t('success'))
        })
        .catch(error => {
          this.$toast.success(error.data)
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
