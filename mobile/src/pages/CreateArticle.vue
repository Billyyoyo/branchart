<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button icon slot="left" @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      {{this.$t('create_new_article')}}
      <mu-button flat slot="right" @click="create">
        {{$t('publish')}}
      </mu-button>
    </mu-appbar>
    <div class="relation_article" v-if="upperArticle">
      <div>{{$t('prequel')}}</div>
      <ArticleSimpleTile :article="upperArticle"></ArticleSimpleTile>
    </div>
    <div class="relation_article" v-if="lowerArticle">
      <div>{{$t('sequel')}}</div>
      <ArticleSimpleTile :article="lowerArticle"></ArticleSimpleTile>
    </div>
    <mu-form ref="form" :model="form" label-width="150" style="margin-top: 50px;padding-left: 30px;padding-right: 30px;">
      <mu-form-item prop="input" :label="$t('title')" :rules="titleRules">
        <mu-text-field v-model="form.title" :disabled="form.rootId===''" prop="loginName"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="textarea" :label="$t('description')" :rules="descriptionRules">
        <mu-text-field multi-line :rows="3" :rows-max="6" :max-length="200" v-model="form.description"></mu-text-field>
      </mu-form-item>
      <mu-form-item prop="textarea" :label="$t('content')"  :rules="contentRules">
        <mu-text-field multi-line :rows="10" :rows-max="6" :max-length="5000" v-model="form.content"></mu-text-field>
      </mu-form-item>
      <mu-form-item :label="$t('tags')">
        <div style="text-align: left">
          <mu-chip class="search_tag_item" :color="indexColor(i)" text-color="white" v-for="(tag, i) in selectedTags" :key="tag" @delete="deleteTag(tag)" delete>{{tag}}</mu-chip>
        </div>
      </mu-form-item>
      <div style="text-align: right">
        <mu-button color="blue" @click="tagDialog.openSelectTags=true">{{$t('select')}}</mu-button>
        <mu-button color="red" @click="addTag">{{$t('add')}}</mu-button>
      </div>
    </mu-form>
    <mu-dialog width="360" transition="slide-bottom" fullscreen :open.sync="tagDialog.openSelectTags">
      <mu-appbar color="primary" :title="$t('select_tags')">
        <mu-button slot="left" icon @click="tagDialog.openSelectTags=false">
          <mu-icon value="close"></mu-icon>
        </mu-button>
        <mu-button icon slot="right" @click="selectTags">
          <mu-icon value="check"></mu-icon>
        </mu-button>
      </mu-appbar>
      <div style="padding: 24px;">
        <mu-checkbox :key="tag.id" v-for="tag in tagDialog.allTags" :value="tag.tag" v-model="tagDialog.checkedTags" :label="tag.tag" style="margin-right: 16px;"></mu-checkbox>
      </div>
    </mu-dialog>
  </mu-container>
</template>

<script>
import ArticleSimpleTile from '../views/ArticleSimpleTile'
import {api} from '../service/api'
import {util} from '../utils/util'

export default {
  name: 'CreateArticle',
  mixins: [util],
  components: {ArticleSimpleTile},
  computed: {
    loginUser () {
      return this.$store.getters.getLoginUser
    },
    loginUserExtra () {
      return this.$store.getters.getLoginUserExtra
    }
  },
  data: function () {
    return {
      tagDialog: {
        openSelectTags: false,
        allTags: [],
        checkedTags: [],
        writedTags: []
      },
      lowerArticle: null,
      upperArticle: null,
      titleRules: [
        {validate: (val) => !!this.form.title, message: this.$t('tip_need_title')}
      ],
      descriptionRules: [
        {validate: (val) => !!this.form.description, message: this.$t('tip_need_description')}
      ],
      contentRules: [
        {validate: (val) => !!this.form.content, message: this.$t('tip_need_content')}
      ],
      selectedTags: [],
      form: {
        title: '',
        description: '',
        content: '',
        tags: '',
        upperId: this.$route.query.upperId,
        lowerId: this.$route.query.lowerId,
        rootId: this.$route.query.rootId
      }
    }
  },
  created: function () {
    console.log(this.$route.query.rootId)
    console.log(this.$route.query.upperId)
    console.log(this.$route.query.lowerId)
    console.log(this.form.lowerId)
    console.log(this.form.upperId)
    if (this.form.upperId) {
      api.getArticle(this.form.upperId)
        .then(result => {
          this.upperArticle = result.data.article
          console.log(JSON.stringify(this.upperArticle))
          this.form.title = this.upperArticle.title
        })
    }
    if (this.form.lowerId) {
      api.getArticle(this.form.lowerId)
        .then(result => {
          console.log(JSON.stringify(this.lowerArticle))
          this.lowerArticle = result.data.article
          this.form.title = this.lowerArticle.title
        })
    }
    api.getTagList({pageNumber: 1, pageSize: 50})
      .then(result => {
        this.tagDialog.allTags = result.data
      })
  },
  methods: {
    deleteTag (tag) {
      this.selectedTags = this.deleteItem(this.selectedTags, tag)
      this.tagDialog.checkedTags = this.deleteItem(this.tagDialog.checkedTags, tag)
      this.tagDialog.writedTags = this.deleteItem(this.form.writedTags, tag)
    },
    create () {
      this.$refs.form.validate().then((result) => {
        if (result) {
          if (this.selectedTags) {
            this.form.tags = this.selectedTags.join(',')
          }
          api.createArticle(this.form)
            .then(response => {
              this.loginUserExtra.articleCount++
              this.$confirm(this.$t('tip_create_article_success'), this.$t('success'),
                {
                  type: 'success',
                  okLabel: this.$t('go_article'),
                  cancelLabel: this.$t('back_to_home')
                })
                .then(({result}) => {
                  if (result) {
                    this.$router.replace('/article/' + response.data.article.id)
                  } else {
                    this.$router.replace('/')
                  }
                })
            })
            .catch(error => {
              this.$toast.error(error.data.msg)
            })
        }
      })
    },
    selectTags () {
      this.combineTags()
      this.tagDialog.openSelectTags = false
    },
    addTag () {
      this.$prompt(this.$t('tip_enter_tag'), this.$t('tips'), {
        okLabel: this.$t('add'),
        cancelLabel: this.$t('cancel'),
        validator (value) {
          return {
            valid: value,
            message: 'need tag'
          }
        }
      }).then(({ result, value }) => {
        if (result) {
          let ab = value.split(',')
          this.tagDialog.writedTags = this.tagDialog.writedTags.concat(ab)
          this.combineTags()
        }
      })
    },
    combineTags () {
      this.selectedTags = this.tagDialog.writedTags.concat(this.tagDialog.checkedTags)
      let kv = {}
      this.selectedTags.forEach((tag, i) => {
        tag = tag + ''
        kv[tag.toLowerCase()] = tag
      })
      this.selectedTags = []
      for (let key in kv) {
        this.selectedTags.push(kv[key].trim())
      }
    },
    deleteItem (array, item) {
      return array.filter((it) => {
        return item !== it
      })
    }
  }
}
</script>

<style scoped>
  .relation_article{
    border-radius: 5px;
    border: 1px dashed #bdbdbd;
    padding: 8px;
    margin-left: 48px;
    margin-right: 48px;
    margin-top: 16px;
    color: #bdbdbd;
    font-size: 16px;
    text-align: left;
  }
  .search_tag_item {
    margin: 8px;
  }
</style>
