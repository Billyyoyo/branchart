<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{title}}
      <mu-button icon slot="right" @click="createDialog.isOpen=true">
        <mu-icon value="add"></mu-icon>
      </mu-button>
    </mu-appbar>
    <mu-load-more v-if="resultList.length>0" @refresh="refresh" :refreshing="refreshing" :loading="loading" @load="load">
      <mu-list>
        <BookItemTile v-for="book in resultList" :key="book.id" :book="book"></BookItemTile>
      </mu-list>
    </mu-load-more>
    <div v-else class="search_result_empty">
      {{$t('empty_result')}}
    </div>
    <mu-dialog width="360" transition="slide-bottom" :open.sync="createDialog.isOpen">
      <mu-appbar color="primary">
        <mu-button slot="left" icon @click="createDialog.isOpen=false">
          <mu-icon value="close"></mu-icon>
        </mu-button>
        <mu-button flat="" slot="right" @click="addBook">
          {{$t('create')}}
        </mu-button>
      </mu-appbar>
      <div style="padding: 24px;">
        <mu-form ref="form" :model="createForm">
          <mu-form-item :label="$t('title')" prop="input" :rules="titleRules">
            <mu-text-field v-model="createForm.title" prop="title" max-length="60"></mu-text-field>
          </mu-form-item>
        </mu-form>
      </div>
    </mu-dialog>
  </mu-container>
</template>

<script>
import BookItemTile from '../views/BookItemTile'
import {api} from '../service/api'

export default {
  name: 'BookList',
  components: {BookItemTile},
  data: function () {
    return {
      refreshing: false,
      loading: false,
      title: this.$route.query.title,
      titleRules: [
        {validate: (val) => !!this.createForm.title, message: this.$t('tip_need_title')}
      ],
      resultList: [],
      form: {
        pageNumber: 1,
        pageSize: 16
      },
      createDialog: {
        isOpen: false
      },
      createForm: {
        title: ''
      }
    }
  },
  created: function () {
    Object.assign(this.form, this.$route.query)
    api.getBookList(this.form)
      .then((result) => {
        this.resultList = result.data
      })
  },
  methods: {
    refresh () {
      this.refreshing = true
      this.$refs.container.scrollTop = 0
      this.form.pageNumber = 1
      api.getBookList(this.form)
        .then((result) => {
          this.refreshing = false
          this.resultList = result.data
        })
    },
    load () {
      this.loading = true
      this.form.pageNumber++
      api.getBookList(this.form)
        .then((result) => {
          this.loading = false
          this.resultList = this.resultList.concat(result.data)
        })
    },
    addBook () {
      this.$refs.form.validate().then((result) => {
        if (result) {
          api.addBook(this.createForm)
            .then(result => {
              this.resultList.push(result.data)
              this.createDialog.isOpen = false
            })
            .catch(error => {
              this.createDialog.isOpen = false
              this.$toast.error(error.data)
            })
        }
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
