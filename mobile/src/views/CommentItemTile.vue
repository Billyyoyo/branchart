<template>
  <li style="margin-top: 16px; margin-bottom: 16px;">
    <mu-flex justify-content="start">
      <mu-flex class="comment_profile" justify-content="center">
        <mu-avatar style="display: inline;float: left;cursor: pointer;" @click="$router.push('/user/' + comment.senderId)">
          <img src="../assets/profile/user_profile.png">
        </mu-avatar>
      </mu-flex>
      <mu-flex class="comment_container" justify-content="center">
        <div style="width: 100%;">
          <div class="comment_text" style="color: #607d8b;cursor: pointer;" @click="$router.push('/user/' + comment.senderId)">{{comment.senderName}}</div>
          <div v-show="comment.replyId" class="comment_reference_container">
            <div class="comment_text" style="color: #607d8b;">
              {{$t('reference')}}:{{comment.replyUserName}}
            </div>
            <div class="comment_text">
              {{comment.replyContent}}
            </div>
          </div>
          <div class="comment_text" style="margin-top: 5px;">
            {{comment.content}}
          </div>
          <div class="comment_text" style="margin-top: 5px;font-size: 12px;color: #9e9e9e;">
            <mu-flex class="comment_container" justify-content="center">
              <div style="width: 50%;">
                {{formatAgo(comment.createTime, $t('date_name'), $t('ago'), $t('just_now'))}}
              </div>
              <div style="width: 50%;text-align: right;">
                <span class="comment_reply_btn" @click="replyComment">{{$t('reply')}}</span>
              </div>
            </mu-flex>
          </div>
        </div>
      </mu-flex>
    </mu-flex>
  </li>
</template>

<script>
import {util} from '../utils/util'
export default {
  name: 'CommentItemTile',
  mixins: [util],
  props: {
    comment: null
  },
  methods: {
    replyComment () {
      this.$emit('reply', this.comment)
    }
  }
}
</script>

<style scoped>
  .comment_profile {
    width: 48px;
    height: 48px;
  }

  .comment_container {
    width: 100%;
    height: auto;
    margin-left: 8px;
    margin-right: 8px;
  }

  .comment_text {
    text-align: left;
  }

  .comment_reply_btn{
    cursor: pointer;
    margin-right: 16px;
    border-radius: 5px;
    border: 1px solid #bdbdbd;
    padding: 5px;
  }
  .comment_reference_container{
    margin: 8px 16px;
    border: 1px solid #bdbdbd;
    padding: 5px;
  }
</style>
