<template>
  <div class="user-square" @click.stop="$router.push('/user/' + user.userId)">
    <mu-avatar :size="profileSize">
      <img src="../assets/profile/user_profile.png">
    </mu-avatar>
    <p class="user-square-nickname">{{user.userName}}</p>
  </div>
</template>

<script>
export default {
  name: 'UserSquare',
  props: ['user'],
  data: function () {
    return {
      profileSize: 80
    }
  }
}
</script>

<style scoped>
.user-square{
  cursor: pointer;
  padding-left: 10px;
  padding-right: 10px;
}
.user-square-nickname{
  font-size: 12px;
  font-weight: bold;
  color: dimgray;
}
</style>
