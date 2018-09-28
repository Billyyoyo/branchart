<template>
  <mu-list-item avatar  :ripple="false">
    <mu-list-item-action>
      <mu-avatar  color="primary">
        <mu-icon value="import_contacts"></mu-icon>
      </mu-avatar>
    </mu-list-item-action>
    <mu-list-item-content style="cursor: pointer" @click="showBookInfo">
      <mu-list-item-title>{{book.title}}</mu-list-item-title>
    </mu-list-item-content>
    <mu-list-item-action>
      <mu-badge :content="$t('sum_chapter', [book.num.toString()])"></mu-badge>
    </mu-list-item-action>
  </mu-list-item>
</template>

<script>
export default {
  name: 'BookItemTile',
  props: {
    book: null
  },
  methods: {
    showBookInfo () {
      this.$router.push('/book/' + this.book.id)
    }
  }
}
</script>

<style scoped>

</style>
