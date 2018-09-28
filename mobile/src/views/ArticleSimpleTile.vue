<template>
  <div class="article_item" @click="$router.push('/article/' + article.id)">
    <div class="article_title">
      {{$t('auther')}}: {{article.ownerName}}
      <span class="article_item_time">{{formatAgo(article.createTime, $t('date_name'), $t('ago'), $t('just_now'))}}</span>
    </div>
    <div class="article_content">{{article.description}}</div>
    <div style="text-align: right;">
      <mu-button flat text-color="primary">
        {{$t('read')}}
      </mu-button>
    </div>
  </div>
</template>

<script>
import {util} from '../utils/util'
export default {
  name: 'ArticleSimpleTile',
  mixins: [util],
  data: function () {
    return {
    }
  },
  props: {
    article: null
  }
}
</script>

<style scoped>
  .article_item{
    cursor: pointer;
    padding-top: 16px;
    padding-left: 16px;
    padding-right: 16px;
    padding-bottom: 5px;
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
