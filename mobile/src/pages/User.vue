<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="white" z-depth="0" text-color="grey800">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      <mu-button icon slot="right">
        <mu-icon value="more_vert"></mu-icon>
      </mu-button>
    </mu-appbar>
    <mu-container v-if="user">
      <mu-button icon fab class="send_comment_button" @click="showSendSheet">
        <mu-icon value="comment"/>
      </mu-button>
      <mu-avatar :size="profileSize">
        <img src="../assets/profile/user_profile.png">
      </mu-avatar>
      <h3>{{user.nickName}}</h3>
      <p>{{userExtra.bio}}</p>
      <div v-if="relation">
        <mu-button v-if="relation.type===-1" color="primary" @click="doFollow(1)">{{$t('follow')}}</mu-button>
        <mu-button v-else-if="relation.type===0" color="red" @click="doFollow(0)">{{$t('unfollow')}}</mu-button>
      </div>
      <div>
        <div class="profile_social_item" @click="$router.push('/social/follow/' + user.id)">
          <div class="profile_social_number">{{userExtra.followCount}}</div>
          <div class="profile_social_tag">{{this.$t('follow')}}</div>
        </div>
        <div class="profile_social_item" @click="$router.push('/social/fans/' + user.id)">
          <div class="profile_social_number">{{userExtra.fansCount}}</div>
          <div class="profile_social_tag">{{this.$t('fans')}}</div>
        </div>
      </div>
      <mu-tabs :value.sync="tabIndex" inverse color="primary" indicator-color="primary" center style="background-color:white;">
        <mu-tab>{{$t('article')}}({{userExtra.articleCount}})</mu-tab>
        <mu-tab>{{$t('book')}}({{userExtra.bookCount}})</mu-tab>
        <mu-tab>{{$t('message')}}({{userExtra.commentCount}})</mu-tab>
      </mu-tabs>
      <mu-load-more v-if="tabIndex===0" @refresh="refresh" :refreshing="arefreshing" :loading="aloading" @load="load">
        <mu-list>
          <ArticleSimpleTile v-for="article in articles" :key="article.id" :article="article"></ArticleSimpleTile>
        </mu-list>
      </mu-load-more>
      <mu-load-more v-if="tabIndex===1" @refresh="refresh" :refreshing="brefreshing" :loading="bloading" @load="load">
        <mu-list>
          <BookItemTile v-for="book in books" :key="book.id" :book="book"></BookItemTile>
        </mu-list>
      </mu-load-more>
      <mu-load-more v-if="tabIndex===2" @refresh="refresh" :refreshing="crefreshing" :loading="cloading" @load="load">
        <mu-list>
          <CommentItemTile v-for="comment in comments" :key="comment.id" :comment="comment" @reply="showSendSheet"></CommentItemTile>
        </mu-list>
      </mu-load-more>
      <mu-bottom-sheet :open.sync="commentOpen">
        <mu-flex class="comment_send_container container" justify-content="center">
          <input class="comment_send_input" v-model="sendForm.content" :placeholder="commentPlaceHolder">
          <mu-button color="primary" style="margin-left: 16px;" @click="sendComment">
            {{$t('send')}}
          </mu-button>
        </mu-flex>
      </mu-bottom-sheet>
    </mu-container>
  </mu-container>
</template>

<script>
import {api} from '../service/api'
import ArticleSimpleTile from '../views/ArticleSimpleTile'
import BookItemTile from '../views/BookItemTile'
import CommentItemTile from '../views/CommentItemTile'

export default {
  name: 'User',
  components: {ArticleSimpleTile, BookItemTile, CommentItemTile},
  data: function () {
    return {
      profileSize: 120,
      user: null,
      userExtra: null,
      relation: null,
      tabIndex: 0,
      commentOpen: false,
      commentPlaceHolder: '',
      articles: [],
      books: [],
      comments: [],
      arefreshing: false,
      brefreshing: false,
      crefreshing: false,
      aloading: false,
      bloading: false,
      cloading: false,
      articleForm: {
        ownerId: this.$route.params.id,
        pageNumber: 1,
        pageSize: 16
      },
      bookForm: {
        ownerId: this.$route.params.id,
        pageNumber: 1,
        pageSize: 16
      },
      commentForm: {
        pageNumber: 1,
        pageSize: 16
      },
      sendForm: {
        target: 0,
        type: 0,
        targetId: this.$route.params.id,
        content: '',
        replyId: ''
      }
    }
  },
  created: function () {
    api.getUserInfo(this.$route.params.id)
      .then(result => {
        this.user = result.data.user
        this.userExtra = result.data.extra
        this.relation = result.data.relation
      })
      .catch(error => {
        this.$toast.error(error.data)
        this.$router.back()
      })
    this.tabIndex = 2
    this.refresh()
    this.tabIndex = 1
    this.refresh()
    this.tabIndex = 0
    this.refresh()
  },
  methods: {
    doFollow (flag) {
      api.follow(this.user.id, flag, 0)
        .then(result => {
          this.relation.type = (flag === 1) ? 0 : -1
          this.userExtra.followCount = this.userExtra.followCount + (flag === 1 ? 1 : -1)
          this.$toast.success(this.$t('success'))
        })
        .catch(error => {
          this.$toast.success(error.data)
        })
    },
    refresh () {
      if (this.tabIndex === 0) {
        this.arefreshing = true
        this.articleForm.pageNumber = 1
        api.getArticleList(this.articleForm)
          .then((result) => {
            this.arefreshing = false
            this.articles = result.data
          })
      } else if (this.tabIndex === 2) {
        this.crefreshing = true
        this.commentForm.pageNumber = 1
        api.getCommentList(0, 0, this.$route.params.id, this.commentForm)
          .then(result => {
            this.crefreshing = false
            this.comments = result.data
          })
      } else {
        this.brefreshing = true
        this.bookForm.pageNumber = 1
        api.getBookList(this.bookForm)
          .then((result) => {
            this.brefreshing = false
            this.books = result.data
          })
      }
    },
    load () {
      if (this.tabIndex === 0) {
        this.aloading = true
        this.articleForm.pageNumber++
        api.getArticleList(this.articleForm)
          .then((result) => {
            this.aloading = false
            this.articles = this.articles.concat(result.data)
          })
      } else if (this.tabIndex === 2) {
        this.cloading = true
        this.commentForm.pageNumber++
        api.getCommentList(0, 0, this.user.id, this.commentForm)
          .then(result => {
            this.cloading = false
            this.comments = this.comments.concat(result.data)
          })
      } else {
        this.bloading = true
        this.bookForm.pageNumber++
        api.getBookList(this.bookForm)
          .then((result) => {
            this.bloading = false
            this.books = this.books.concat(result.data)
          })
      }
    },
    showSendSheet (cmt) {
      if (cmt && cmt.senderName) {
        this.commentPlaceHolder = this.$t('reply') + ': ' + cmt.senderName
        this.sendForm.replyId = cmt.id
      } else {
        this.commentPlaceHolder = this.$t('message')
        this.sendForm.replyId = ''
      }
      this.commentOpen = true
    },
    sendComment () {
      api.sendComment(this.sendForm)
        .then(result => {
          this.comments.unshift(result.data)
          this.sendForm.replyId = ''
          this.sendForm.content = ''
          this.tabindex = 2
          this.userExtra.commentCount++
          this.commentOpen = false
        })
        .catch(error => {
          this.$toast.error(error.data)
        })
    }
  }
}
</script>

<style scoped>
  .profile_social_item{
    width: 50%;
    float: left;
    cursor: pointer;
    padding-bottom: 10px;
  }
  .profile_social_tag{
    display: block;
    font-size: 12px;
    color: dimgray;
  }
  .profile_social_number{
    display: block;
    font-weight:bold;
    font-size: 15px;
    color: black;
  }
  .send_comment_button{
    position: fixed;
    bottom: 20px;
    right: 20px;
  }
  .comment_send_container {
    vertical-align: middle;
    border-top: 1px solid #bdbdbd;
    padding: 8px;
  }

  .comment_send_input {
    height: 36px;
    width: 100%;
    border: 1px solid #bdbdbd;
    border-radius: 8px;
    background: none;
    outline: none;
    padding: 5px;
  }
</style>
