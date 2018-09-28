<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar z-depth="0" color="transparent" text-color="grey800">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      <mu-button v-show="article" icon slot="right" @click="menuOpen=true">
        <mu-icon value="more_vert"></mu-icon>
      </mu-button>
      <mu-menu slot="right" placement="left-end" cover :open.sync="menuOpen">
        <mu-list slot="content">
          <mu-list-item button @click="share">
            <mu-list-item-title>{{$t('share')}}</mu-list-item-title>
          </mu-list-item>
          <mu-list-item button @click="addToBook">
            <mu-list-item-title>{{$t('add_to_book')}}</mu-list-item-title>
          </mu-list-item>
          <mu-list-item button @click="addFav">
            <mu-list-item-title>{{$t('favorite')}}</mu-list-item-title>
          </mu-list-item>
          <mu-list-item button @click="report">
            <mu-list-item-title>{{$t('report')}}</mu-list-item-title>
          </mu-list-item>
        </mu-list>
      </mu-menu>
    </mu-appbar>
    <mu-load-more v-show="article" :refreshing="refreshing" :loading="loading" @load="loadComment">
      <h2>{{article.title}}</h2>
      <div style="text-align: right;margin-right: 30px;">
        <mu-button v-if="article.rootId!=='root'" flat text-color="primary" @click="goRoot">
          {{$t('root')}}
        </mu-button>
        <mu-button v-if="article.upperIds && article.upperIds.length>0" flat text-color="primary" @click="goPrequel">
          {{$t('prequel')}}
        </mu-button>
        <mu-button v-if="article.rootId!=='root'" flat text-color="primary" @click="goBranch">
          {{$t('branch')}}
        </mu-button>
        <mu-button flat text-color="primary" @click="goSequel">
          {{$t('sequel')}}
        </mu-button>
      </div>
      <p class="article_description">{{article.description}}</p>
      <div class="article_content">
        {{content.content}}
      </div>
      <mu-flex style="margin-top: 16px" align-items="center">
        <mu-flex class="article_time" style="margin-left: 48px" justify-content="start" fill>
          {{formatDate(article.modifyTime)}}
        </mu-flex>
        <mu-flex style="margin-right: 48px" justify-content="end" fill>
          <mu-button text-color="primary" flat small @click="showVersions">
            {{$t('versions') + article.version}}
          </mu-button>
        </mu-flex>
      </mu-flex>
      <mu-flex style="margin-top: 16px" align-items="center">
        <mu-flex style="margin-left: 48px;cursor: pointer;" justify-content="start" fill @click="$router.push('/user/' + article.ownerId)">
          <mu-avatar :size="profileSize" style="vertical-align: middle;">
            <img src="../assets/profile/user_profile.png">
          </mu-avatar>
          <span
            style="margin-left: 5px;font-weight: bold;color: #999999;vertical-align: middle;">{{article.ownerName}}</span>
        </mu-flex>
        <mu-flex style="margin-right: 48px" justify-content="end" fill>
          <mu-button v-if="socialType===-1" round small color="red" @click="followIdol(1)">{{$t('follow')}}</mu-button>
          <mu-button v-else-if="socialType===0" round small color="red" @click="followIdol(0)">{{$t('unfollow')}}</mu-button>
        </mu-flex>
      </mu-flex>
      <mu-flex style="margin-top: 16px;padding-left: 100px;padding-right: 100px;" align-items="center">
        <mu-flex justify-content="center" fill>
          <mu-button flat color="white" text-color="grey500" style="font-size: 24px;font-weight: bold;" @click="likeArticle">
            <mu-icon left value="thumb_up"/>
            {{article.likeCount}}
          </mu-button>
        </mu-flex>
        <mu-flex justify-content="center" fill>
          <mu-button flat color="white" text-color="grey500" style="font-size: 24px;font-weight: bold;" @click="dislikeArticle">
            <mu-icon left value="thumb_down"/>
            {{article.dislikeCount}}
          </mu-button>
        </mu-flex>
      </mu-flex>
      <div style="margin: 30px;text-align: left;">
        <mu-chip class="search_tag_item" :color="indexColor(i)" text-color="white" v-for="(tag, i) in article.tags"
                 :key="tag" @click="searchTag(tag)">{{tag}}
        </mu-chip>
      </div>
      <mu-flex justify-content="center" style="margin: 36px;" fill>
        <mu-flex justify-content="center" fill>
          <mu-button color="primary" text-color="white" @click="continue_write(null, article.id)">
            <mu-icon left value="chevron_left"></mu-icon>
            {{$t('write_prequel')}}
          </mu-button>
        </mu-flex>
        <mu-flex justify-content="center" fill>
          <mu-button color="primary" text-color="white" @click="continue_write(article.id, null)">
            {{$t('write_sequel')}}
            <mu-icon right value="chevron_right"></mu-icon>
          </mu-button>
        </mu-flex>
      </mu-flex>
      <mu-flex class="comment_bar" justify-content="center">
        <h3 style="text-align: left;width: 50%;padding-left: 30px;">{{$t('number_of_comment',
          [article.commentCount])}}</h3>
        <div style="width: 50%;text-align: right;padding-top: 10px; padding-right: 30px;">
          <mu-button color="primary" class="comment_reply_btn" @click="showSendSheet">{{$t('comment')}}</mu-button>
        </div>
      </mu-flex>
      <mu-divider/>
      <mu-list>
        <CommentItemTile v-for="comment in commentList" :key="comment.id" :comment="comment"
                         @reply="showSendSheet"></CommentItemTile>
      </mu-list>
    </mu-load-more>
    <div v-show="errorMessage" class="search_result_empty">
      {{this.errorMessage}}
    </div>
    <mu-bottom-sheet :open.sync="commentOpen">
      <mu-flex class="comment_send_container container" justify-content="center">
        <input class="comment_send_input" v-model="sendForm.content" :placeholder="commentPlaceHolder">
        <mu-button color="primary" style="margin-left: 16px;" @click="sendTextComment">
          {{$t('send')}}
        </mu-button>
      </mu-flex>
    </mu-bottom-sheet>
    <mu-bottom-sheet :open.sync="booksOpen">
      <mu-list>
        <mu-sub-header>Select One</mu-sub-header>
        <mu-list-item button v-for="book in books" :key="book.id" @click="selectedBook(book.id)">
          <mu-list-item-action>
            <mu-icon value="import_contacts" color="orange"></mu-icon>
          </mu-list-item-action>
          <mu-list-item-title>{{book.title}}</mu-list-item-title>
        </mu-list-item>
      </mu-list>
    </mu-bottom-sheet>
  </mu-container>
</template>

<script>
import CommentItemTile from '../views/CommentItemTile'
import {api} from '../service/api'
import {util} from '../utils/util'

export default {
  name: 'Article',
  mixins: [util],
  components: {CommentItemTile},
  data: function () {
    return {
      article: {},
      content: {},
      booksOpen: false,
      commentOpen: false,
      menuOpen: false,
      commentPlaceHolder: '',
      loading: false,
      refreshing: false,
      profileSize: 24,
      errorMessage: '',
      socialType: -2,
      commentList: [],
      books: [],
      commentForm: {
        pageNumber: 1,
        pageSize: 20
      },
      sendForm: {
        target: 1,
        type: 0,
        targetId: '',
        content: '',
        replyId: ''
      }
    }
  },
  created: function () {
    this.loadArticle()
  },
  computed: {
    loginUser () {
      return this.$store.getters.getLoginUser
    },
    loginUserExtra () {
      return this.$store.getters.getLoginUserExtra
    }
  },
  methods: {
    loadArticle () {
      api.getArticle(this.$route.params.id)
        .then((result) => {
          this.article = result.data.article
          this.content = result.data.content
          this.sendForm.targetId = this.article.id
          this.loadComment()
          this.checkSocial()
        })
        .catch((error) => {
          this.errorMessage = error.data
        })
    },
    checkSocial () {
      if (this.loginUser !== null && this.loginUser.id !== this.article.ownerId) {
        api.checkSocial(this.article.ownerId)
          .then(result => {
            this.socialType = result.data.type
          })
      }
    },
    followIdol (flag) {
      api.follow(this.article.ownerId, flag, 0)
        .then(result => {
          this.socialType = (flag === 1) ? 0 : -1
          this.loginUserExtra.followCount = this.loginUserExtra.followCount + (flag === 1 ? 1 : -1)
        })
    },
    loadComment () {
      this.loading = true
      api.getCommentList(1, 0, this.article.id, this.commentForm)
        .then(result => {
          this.commentForm.pageNumber++
          this.loading = false
          this.commentList = this.commentList.concat(result.data)
          // this.commentList = result.data
        })
        .catch(() => {
          this.loading = false
        })
    },
    showSendSheet (cmt) {
      if (cmt && cmt.senderName) {
        this.commentPlaceHolder = this.$t('reply') + ': ' + cmt.senderName
        this.sendForm.replyId = cmt.id
      } else {
        this.commentPlaceHolder = this.$t('comment')
        this.sendForm.replyId = ''
      }
      this.commentOpen = true
    },
    showVersions () {
      this.$router.push('/article/versions/' + this.article.id)
    },
    likeArticle () {
      this.sendForm.type = 1
      this.sendForm.replyId = ''
      this.sendForm.content = ''
      this.sendComment()
    },
    dislikeArticle () {
      this.sendForm.type = 2
      this.sendForm.replyId = ''
      this.sendForm.content = ''
      this.sendComment()
    },
    sendTextComment () {
      this.sendForm.type = 0
      if (this.sendForm.content) {
        this.sendComment()
        this.commentOpen = false
      }
    },
    sendComment () {
      api.sendComment(this.sendForm)
        .then(result => {
          if (result.data.type === 0) {
            this.commentList.unshift(result.data)
          } else if (result.data.type === 1) {
            this.article.likeCount++
          } else if (result.data.type === 2) {
            this.article.dislikeCount++
          }
          this.sendForm.replyId = ''
          this.sendForm.content = ''
        })
    },
    continue_write (upperId, lowerId) {
      this.$router.push({
        path: '/createarticle',
        query: {
          rootId: this.article.rootId === 'root' ? this.article.id : this.article.rootId,
          upperId: upperId,
          lowerId: lowerId
        }
      })
    },
    goRoot () {
      this.$router.push('/article/' + this.article.rootId)
    },
    goBranch () {
      this.$router.push({
        path: '/articlelist',
        query: {
          title: this.$t('branch'),
          upperId: this.article.upperIds[0]
        }
      })
    },
    goPrequel () {
      this.$router.push({
        path: '/articlelist',
        query: {
          title: this.$t('prequel'),
          lowerId: this.article.id
        }
      })
    },
    goSequel () {
      this.$router.push({
        path: '/articlelist',
        query: {
          title: this.$t('pequel'),
          upperId: this.article.id
        }
      })
    },
    searchTag (tag) {
      this.$router.push({
        path: '/articlelist',
        query: {
          title: tag,
          tag: tag
        }
      })
    },
    share () {
      alert('Building')
    },
    addToBook () {
      if (this.books.length === 0) {
        api.getBookList({
          ownerId: this.loginUser.id,
          pageNumber: 1,
          pageSize: 16
        })
          .then(result => {
            this.books = result.data
            this.booksOpen = true
          })
      }
      this.booksOpen = true
    },
    selectedBook (bookId) {
      api.addArticleInBook(bookId, this.article.id)
        .then(result => {
          this.$toast.success(this.$t('success'))
          this.booksOpen = false
        })
        .catch(error => {
          this.$toast.error(this.$t('failed') + ': ' + error.data())
        })
    },
    addFav () {
      api.addFavorite(0, this.article.id)
        .then(result => {
          this.$toast.success(this.$t('success'))
        })
        .catch(error => {
          this.$toast.success(error.data)
        })
    },
    report () {
      alert('Building')
    }
  }
}
</script>

<style scoped>
  .article_description {
    border-radius: 5px;
    border: 1px dashed #bdbdbd;
    padding: 8px;
    margin-left: 48px;
    margin-right: 48px;
    color: #bdbdbd;
    font-size: 16px;
    text-align: left;
  }

  .article_content {
    text-align: left;
    font-size: 20px;
    margin-left: 40px;
    margin-right: 40px;
  }

  .article_time {
    text-align: right;
    font-style: italic;
    color: grey;
    font-size: 14px;
    margin-left: 40px;
    margin-right: 40px;
  }

  .article_content:first-letter {
    font-size: 200%;
    font-weight: bold;
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

  .comment_bar {
    width: 100%;
    vertical-align: middle;
    height: auto;
    margin-left: 8px;
    margin-right: 8px;
  }

  .search_tag_item {
    margin-left: 8px;
    margin-right: 8px;
  }
</style>
