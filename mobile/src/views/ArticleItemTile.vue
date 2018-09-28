<template>
  <li class="article_item" @click="$router.push('/article/' + article.id)">
    <div class="article_title">{{article.title}}</div>
    <div class="article_content">{{article.description}}</div>
    <div class="article_flag_container">
      <div class="article_owner" @click.stop="$router.push('/user/' + article.ownerId)">
        <mu-avatar :size="profileSize" style="vertical-align: middle;">
          <img src="../assets/profile/user_profile.png">
        </mu-avatar>
        <span style="margin-left: 5px;font-weight: bold;color: #999999;">{{article.ownerName}}</span>
        <span class="article_item_time">{{formatAgo(article.createTime, $t('date_name'), $t('ago'), $t('just_now'))}}</span>
      </div>
      <div class="article_flag_item">
        <mu-icon value="remove_red_eye" color="grey400" size="16" class="article_flag_icon"/>
        <span class="article_flag_text">{{article.readCount}}</span>
        <mu-icon value="favorite" color="grey400" size="16" class="article_flag_icon"/>
        <span class="article_flag_text">{{article.likeCount}}</span>
        <mu-icon value="comment" color="grey400" size="16" class="article_flag_icon"/>
        <span class="article_flag_text">{{article.commentCount}}</span>
      </div>
    </div>
  </li>
</template>

<script>
import {util} from '../utils/util'
export default {
  name: 'ArticleItemTile',
  mixins: [util],
  data: function () {
    return {
      profileSize: 24
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
    padding-top: 5px;
    padding-left: 16px;
    padding-right: 16px;
    padding-bottom: 48px;
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
  .article_flag_icon{
    vertical-align: middle;
    margin-right: 2px;
  }
  .article_flag_text{
    margin-right: 8px;
    color: #999999;
  }
  .article_flag_container{
    margin-top: 12px;
    text-align: left;
  }
  .article_flag_item{
    vertical-align: middle;
    position: absolute;
    right: 16px;
  }
  .article_owner{
    margin-right: 8px;
    position: absolute;
    left: 16px;
  }
  .article_item_time{
    text-align: left;
    margin-top: 46px;
    font-style: italic;
    color: grey;
    font-size: 12px;
  }
</style>
