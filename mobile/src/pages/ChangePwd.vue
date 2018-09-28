<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{$t('change_pwd')}}
    </mu-appbar>
    <mu-form ref="form" :model="form" label-width="150" style="margin-top: 50px">
      <mu-form-item :label="$t('old_pwd')" prop="password" :rules="oldPwdRules">
        <mu-text-field type="password" v-model="form.oldPwd" prop="password"></mu-text-field>
      </mu-form-item>
      <mu-form-item :label="$t('new_pwd')" prop="password" :rules="newPwdRules">
        <mu-text-field type="password" v-model="form.newPwd" prop="password"></mu-text-field>
      </mu-form-item>
      <mu-form-item :label="$t('repeat_pwd')" prop="password" :rules="repeatRules">
        <mu-text-field type="password" v-model="form.repeat" prop="password"></mu-text-field>
      </mu-form-item>
      <div style="margin-top: 50px">
        <mu-button color="primary" @click="submit">{{$t('confirm')}}</mu-button>
        <mu-button @click="$router.back()">{{$t('cancel')}}</mu-button>
      </div>
    </mu-form>
  </mu-container>
</template>

<script>
import {api} from '../service/api'

export default {
  name: 'ChangePwd',
  data: function () {
    return {
      oldPwdRules: [
        {validate: (val) => !!this.form.oldPwd, message: this.$t('tip_need_password')},
        {validate: (val) => this.form.oldPwd.length >= 6 && this.form.oldPwd.length <= 10, message: this.$t('tip_need_password_length')}
      ],
      newPwdRules: [
        {validate: (val) => !!this.form.newPwd, message: this.$t('tip_need_password')},
        {validate: (val) => this.form.newPwd.length >= 6 && this.form.oldPwd.length <= 10, message: this.$t('tip_need_password_length')}
      ],
      repeatRules: [
        {validate: (val) => this.form.newPwd === this.form.repeat, message: this.$t('tip_need_repeat_password')}
      ],
      form: {
        oldPwd: '',
        newPwd: '',
        repeat: ''
      }
    }
  },
  methods: {
    submit () {
      this.$refs.form.validate().then((result) => {
        if (result) {
          api.changePassword(this.form)
            .then((result) => {
              this.$toast.success(this.$t('tip_need_change_pwd_success'))
              this.$router.back()
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
