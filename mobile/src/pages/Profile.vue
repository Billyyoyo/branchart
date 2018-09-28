<template>
  <div>
    <div v-if="loginUser" class="profile_container">
      <router-link :to="'/user/' + loginUser.id">
        <mu-avatar :size="profileSize">
          <img src="../assets/profile/user_profile.png">
        </mu-avatar>
      </router-link>
      <h3>{{loginUser.nickName}}</h3>
      <div>
        <div class="profile_social_item" @click="$router.push('/social/follow/' + loginUser.id)">
            <div class="profile_social_number">{{loginUserExtra.followCount}}</div>
            <div class="profile_social_tag">{{this.$t('follow')}}</div>
        </div>
        <div class="profile_social_item" @click="$router.push('/social/fans/' + loginUser.id)">
            <div class="profile_social_number">{{loginUserExtra.fansCount}}</div>
            <div class="profile_social_tag">{{this.$t('fans')}}</div>
        </div>
      </div>
      <div>
        <mu-button color="primary" to="/createarticle">
          <mu-icon left value="edit"></mu-icon>
          {{$t('create_new_article')}}
        </mu-button>
      </div>
      <mu-divider></mu-divider>
      <mu-list>
        <mu-list-item button :riple="false" @click="goMyArticles">
          <mu-list-item-action>
            <mu-icon value="receipt"></mu-icon>
          </mu-list-item-action>
          <mu-list-item-content>
            <mu-list-item-title>{{$t('article')}}</mu-list-item-title>
          </mu-list-item-content>
          <mu-list-item-action>
            <mu-badge :content="loginUserExtra.articleCount + ''" color="red"></mu-badge>
          </mu-list-item-action>
        </mu-list-item>
        <mu-list-item button :riple="false"  @click="goMyBooks">
          <mu-list-item-action>
            <mu-icon value="import_contacts"></mu-icon>
          </mu-list-item-action>
          <mu-list-item-content>
            <mu-list-item-title>{{$t('book')}}</mu-list-item-title>
          </mu-list-item-content>
          <mu-list-item-action>
            <mu-badge :content="loginUserExtra.bookCount + ''" color="purple"></mu-badge>
          </mu-list-item-action>
        </mu-list-item>
        <mu-list-item button :riple="false">
          <mu-list-item-action>
            <mu-icon value="comment"></mu-icon>
          </mu-list-item-action>
          <mu-list-item-content>
            <mu-list-item-title>{{$t('comment')}}</mu-list-item-title>
          </mu-list-item-content>
          <mu-list-item-action>
            <mu-badge :content="loginUserExtra.bookCount + ''" color="blue"></mu-badge>
          </mu-list-item-action>
        </mu-list-item>
        <mu-list-item button :riple="false" to="/favoritelist">
          <mu-list-item-action>
            <mu-icon value="bookmark"></mu-icon>
          </mu-list-item-action>
          <mu-list-item-content>
            <mu-list-item-title>{{$t('favorite')}}</mu-list-item-title>
          </mu-list-item-content>
        </mu-list-item>
        <mu-list-item button :riple="false" to="/settings">
          <mu-list-item-action>
            <mu-icon value="settings"></mu-icon>
          </mu-list-item-action>
          <mu-list-item-content>
            <mu-list-item-title>{{$t('settings')}}</mu-list-item-title>
          </mu-list-item-content>
        </mu-list-item>
      </mu-list>
    </div>
    <div v-else  class="profile_container">
        <mu-avatar :size="profileSize" color="primary">
          <mu-icon :size="profileSize" value="account_circle"></mu-icon>
        </mu-avatar>
      <h3>{{$t('tip_no_login')}}</h3>
      <div style="margin-top: 100px">
        <router-link to="/signin">
          <mu-button color="primary" class="login_button">{{$t('login')}}</mu-button>
        </router-link>
      </div>
      <div style="margin-top: 20px">
        <router-link to="/signup">
          <mu-button color="red" class="login_button">{{$t('register')}}</mu-button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Profile',
  data: function () {
    return {
      profileSize: 100,
      fromStorage: '',
      fromMemory: ''
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
  methods: {
    goMyArticles () {
      this.$router.push({
        path: '/articlelist',
        query: {
          title: this.$t('my_articles'),
          ownerId: this.loginUser.id
        }
      })
    },
    goMyBooks () {
      this.$router.push({
        path: '/booklist',
        query: {
          title: this.$t('my_books'),
          ownerId: this.loginUser.id
        }
      })
    }
  }
}
</script>

<style scoped>
  .profile_container {
    margin-top: 50px;
  }
  .login_button {
    width: 80%;
  }
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
  .mu-item-action{
    min-width: 32px;
  }
</style>
