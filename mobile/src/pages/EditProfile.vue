<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{$t('edit_profile')}}
    </mu-appbar>
    <mu-avatar :size="profileSize" style="margin-top: 50px">
      <img src="../assets/profile/user_profile.png">
    </mu-avatar>
    <mu-form ref="form" :model="form" label-width="150" style="margin-top: 50px">
      <mu-form-item prop="input" :label="$t('nick_name')" :rules="nickNameRules" :label-position="labelPosition">
        <mu-text-field v-model="form.nickName" prop="nickName"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="radio" :label="$t('gender')" :label-position="labelPosition">
        <mu-radio v-model="form.gender" value="1" :label="$t('male')"></mu-radio>
        <mu-radio v-model="form.gender" value="2" :label="$t('female')"></mu-radio>
      </mu-form-item>
      <mu-form-item prop="textarea" :label="$t('bio')">
        <mu-text-field multi-line :rows="3" :rows-max="6" :max-length="200" v-model="form.bio"></mu-text-field>
      </mu-form-item>
      <div style="margin-top: 50px">
        <mu-button color="primary" @click="submit">{{$t('submit')}}</mu-button>
      </div>
    </mu-form>
  </mu-container>
</template>

<script>
import {api} from '../service/api'

export default {
  name: 'EditProfile',
  data: function () {
    return {
      profileSize: 100,
      labelPosition: 'left',
      nickNameRules: [
        {validate: (val) => !!this.form.nickName, message: this.$t('tip_need_login_name')},
        {validate: (val) => this.form.nickName.length <= 30, message: this.$t('tip_need_login_length')}
      ],
      form: {
        nickName: this.$store.state.user.nickName,
        gender: this.$store.state.user.gender + '',
        bio: this.$store.state.userExtra.bio
      }
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
    // console.log('user: ' + JSON.stringify(this.$store.state.user))
    // console.log('userExtra: ' + JSON.stringify(this.$store.state.userExtra))
    // this.nickName = this.$store.state.user.nickName
    // this.gender = this.$store.state.user.gender
    // this.bio = this.$store.state.userExtra.bio
  },
  methods: {
    submit () {
      this.$refs.form.validate().then((result) => {
        if (result) {
          api.editUserInfo(this.form)
            .then((result) => {
              this.$store.commit('setLoginUser', result.data)
              this.$router.replace('/')
            })
            .catch((error) => {
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
