<template>
  <li class="article_item" @click="$router.push('/article/' + chapter.articleId)">
    <div class="article_content">
      <mu-avatar :color="indexColor(index+1)" style="margin-right: 16px;">
        {{index+1}}
      </mu-avatar>
      {{chapter.description}}
    </div>
    <div class="article_flag_container">
      <div class="article_owner" @click.stop="$router.push('/user/' + chapter.ownerId)">
        <mu-avatar :size="profileSize" style="vertical-align: middle;">
          <img src="../assets/profile/user_profile.png">
        </mu-avatar>
        <span style="margin-left: 5px;font-weight: bold;color: #999999;">{{chapter.ownerName}}</span>
      </div>
    </div>
  </li>
</template>

<script>
import {util} from '../utils/util'
export default {
  name: 'ChapterItemTile',
  mixins: [util],
  data: function () {
    return {
      profileSize: 24
    }
  },
  props: {
    index: 0,
    chapter: null
  }
}
</script>

<style scoped>
  .article_item{
    cursor: pointer;
    padding-top: 5px;
    padding-left: 16px;
    padding-right: 16px;
    padding-bottom: 56px;
  }
  .article_content{
    text-align: left;
    font-size: 18px;
    color: #444444;
  }
  .article_flag_container{
    margin-top: 8px;
    text-align: right;
  }
  .article_owner{
    margin-right: 8px;
    position: absolute;
    right: 16px;
  }
</style>
