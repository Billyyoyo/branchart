<template>
  <div class="article_item" @click="goItem">
    <div class="article_title">{{item.itemTitle}}</div>
    <div v-if="item.itemDescription" class="article_content">{{item.itemDescription}}</div>
    <div class="article_author">
      {{$t('auther')}}: {{item.ownerName}}
      <span class="article_item_time">{{formatAgo(item.createTime, $t('date_name'), $t('ago'), $t('just_now'))}}</span>
    </div>
  </div>
</template>

<script>
import {util} from '../utils/util'

export default {
  name: 'FavItemTile',
  props: ['item'],
  mixins: [util],
  methods: {
    goItem () {
      if (this.item.type === 0) {
        this.$router.push('/article/' + this.item.itemId)
      } else {
        this.$router.push('/book/' + this.item.itemId)
      }
    }
  }
}
</script>

<style scoped>
  .article_item{
    cursor: pointer;
    padding-top: 16px;
    padding-left: 16px;
    padding-right: 16px;
    padding-bottom: 24px;
  }
  .article_author{
    text-align: left;
    font-size: 14px;
    font-style: italic;
    color: #888888;
  }
  .article_title{
    text-align: left;
    font-size: 17px;
    font-weight: bold;
  }
  .article_content{
    text-align: left;
    font-size: 15px;
    color: #888888;
  }
  .article_item_time{
    text-align: left;
    margin-top: 16px;
    margin-left: 36px;
    font-style: italic;
    color: grey;
    font-size: 12px;
  }
</style>
