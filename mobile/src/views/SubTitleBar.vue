<template>
  <mu-list-item :riple="false">
    <mu-list-item-content>
      <mu-list-item-title style="font-weight: bold; font-size: 14px; color: #424242;">{{subTitle}}</mu-list-item-title>
    </mu-list-item-content>
    <mu-list-item-action v-show="action">
      <mu-badge :content="action" color="primary" @click="goAction"></mu-badge>
    </mu-list-item-action>
  </mu-list-item>
</template>

<script>
export default {
  name: 'SubTitleBar',
  props: {
    subTitle: '',
    action: '',
    go: Function},
  methods: {
    goAction () {
      this.go()
    }
  }
}
</script>

<style scoped>
</style>
