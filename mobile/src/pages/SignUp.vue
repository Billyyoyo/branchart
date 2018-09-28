<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{$t('register')}}
    </mu-appbar>
    <mu-form ref="form" :model="form" label-width="150" style="margin-top: 50px">
      <mu-form-item prop="input" :label="$t('login_name')" :rules="loginNameRules">
        <mu-text-field v-model="form.loginName" prop="loginName"></mu-text-field>
      </mu-form-item>
      <mu-form-item :label="$t('password')" prop="password" :rules="passwordRules">
        <mu-text-field type="password" v-model="form.password" prop="password"></mu-text-field>
      </mu-form-item>
      <mu-form-item :label="$t('repeat_pwd')" prop="password" :rules="repeatRules">
        <mu-text-field type="password" v-model="form.repeat" prop="repeat"></mu-text-field>
      </mu-form-item>
      <div style="margin-top: 50px">
        <mu-button color="primary" @click="submit">{{$t('submit')}}</mu-button>
        <mu-button @click="$router.back()">{{$t('cancel')}}</mu-button>
      </div>
    </mu-form>
  </mu-container>
</template>

<script>
import {api} from '../service/api.js'

export default {
  name: 'SignUp',
  data: function () {
    return {
      loginNameRules: [
        {validate: (val) => !!this.form.loginName, message: this.$t('tip_need_login_name')},
        {validate: (val) => this.form.loginName.length >= 6, message: this.$t('tip_need_login_length')}
      ],
      passwordRules: [
        {validate: (val) => !!this.form.password, message: this.$t('tip_need_password')},
        {validate: (val) => this.form.password.length >= 6 && this.form.password.length <= 10, message: this.$t('tip_need_password_length')}
      ],
      repeatRules: [
        {validate: (val) => this.form.password === this.form.repeat, message: this.$t('tip_need_repeat_password')}
      ],
      form: {
        loginName: '',
        password: 'hanjing',
        repeat: 'hanjing'
      }
    }
  },
  methods: {
    submit () {
      this.$refs.form.validate().then((result) => {
        if (result) {
          api.register(this.form.loginName, this.form.password)
            .then(result => {
              this.$store.commit('setLoginUser', result.data)
              localStorage.setItem('ba_token_code', result.data.token)
              this.$router.replace('/editprofile')
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
