<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{$t('settings')}}
    </mu-appbar>
    <mu-list>
      <mu-list-item button :ripple="false" @click="$router.push('/editprofile')">
        <mu-list-item-action>
          <mu-icon value="person"></mu-icon>
        </mu-list-item-action>
        <mu-list-item-title>{{$t('edit_profile')}}</mu-list-item-title>
      </mu-list-item>
      <mu-list-item button :ripple="false" @click="$router.push('/changepwd')">
        <mu-list-item-action>
          <mu-icon value="more_horiz"></mu-icon>
        </mu-list-item-action>
        <mu-list-item-title>{{$t('change_pwd')}}</mu-list-item-title>
      </mu-list-item>
      <mu-list-item button :ripple="false">
        <mu-list-item-action>
          <mu-icon value="share"></mu-icon>
        </mu-list-item-action>
        <mu-list-item-title>{{$t('share')}}</mu-list-item-title>
      </mu-list-item>
      <mu-list-item button :ripple="false">
        <mu-list-item-action>
          <mu-icon value="priority_high"></mu-icon>
        </mu-list-item-action>
        <mu-list-item-title>{{$t('about_us')}}</mu-list-item-title>
      </mu-list-item>
      <mu-divider></mu-divider>
      <mu-list-item button :ripple="false" @click="logout">
        <mu-list-item-action>
          <mu-icon value="power_settings_new"></mu-icon>
        </mu-list-item-action>
        <mu-list-item-content>
          <mu-list-item-title>{{$t('logout')}}</mu-list-item-title>
        </mu-list-item-content>
      </mu-list-item>
    </mu-list>
  </mu-container>
</template>

<script>
import {api} from '../service/api'

export default {
  name: 'Settings',
  methods: {
    logout: function () {
      this.$confirm(this.$t('tip_confirm_logout'), this.$t('tips'), {
        type: 'warning'
      }).then(({ result }) => {
        if (result) {
          api.logout()
            .then(() => {
              this.$store.commit('removeLoginUser')
              this.$toast.success(this.$t('tip_need_logout_success'))
              this.$router.replace('/')
            })
            .catch(error => {
              this.$toast.error(error.data.msg)
            })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
