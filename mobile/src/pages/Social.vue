<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{$t($route.params.type)}}
    </mu-appbar>
    <mu-load-more @refresh="refresh" :refreshing="refreshing" :loading="loading" @load="load">
    <mu-list>
      <mu-list-item avatar button :ripple="false" v-for="social in socialList" :key="social.id" @click="goUser(social)">
        <mu-list-item-action>
          <mu-avatar>
            <img src="../assets/profile/user_profile.png">
          </mu-avatar>
        </mu-list-item-action>
        <mu-list-item-title>{{$route.params.type === 'follow' ? social.idolName : social.fansName}}</mu-list-item-title>
        <mu-list-item-action>
          <mu-icon value="navigate_next"></mu-icon>
        </mu-list-item-action>
      </mu-list-item>
    </mu-list>
    </mu-load-more>
  </mu-container>
</template>

<script>
import {api} from '../service/api'

export default {
  name: 'Social',
  data: function () {
    return {
      socialList: [],
      refreshing: false,
      loading: false,
      form: {
        pageNumber: 1,
        pageSize: 30
      }
    }
  },
  created: function () {
    this.refresh()
  },
  methods: {
    refresh () {
      this.refreshing = true
      this.form.pageNumber = 1
      api.getSocial(this.$route.params.type === 'fans' ? 1 : 0, 0, this.$route.params.userId, this.form)
        .then((result) => {
          this.refreshing = false
          this.socialList = result.data
        })
    },
    load () {
      this.loading = true
      this.form.pageNumber++
      api.getSocial(this.$route.params.type === 'fans' ? 1 : 0, 0, this.$route.params.userId, this.form)
        .then((result) => {
          this.loading = false
          this.socialList = this.socialList.concat(result.data)
        })
    },
    goUser (social) {
      if (this.$route.params.type === 'follow') {
        this.$router.push('/user/' + social.idolId)
      } else {
        this.$router.push('/user/' + social.fansId)
      }
    }
  }
}
</script>

<style scoped>

</style>
