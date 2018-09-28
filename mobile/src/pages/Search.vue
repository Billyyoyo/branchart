<template>
  <mu-container style="background-color: white; padding-left: 0px; padding-right: 0px">
    <mu-appbar color="primary">
      <mu-button slot="left" icon @click="$router.back()">
        <mu-icon value="arrow_back"></mu-icon>
      </mu-button>
      <mu-text-field id="input_box" color="white" :rows-max="rowsMax" :full-width="fullWidth" v-model="keyword"
                     :placeholder="$t('enter_keyword')"></mu-text-field>
      <mu-button v-if="keyword" slot="right" flat @click="doSearch">
        {{$t('search')}}
      </mu-button>
    </mu-appbar>
    <mu-list>
      <mu-list-item v-show="latestKeywords.length>0" :riple="false">
        <mu-list-item-content>
          <mu-list-item-title style="font-weight: bold; font-size: 14px; color: #424242;">{{$t('latest_keywords')}}
          </mu-list-item-title>
        </mu-list-item-content>
      </mu-list-item>
      <div style="text-align: left">
        <mu-chip class="search_tag_item" text-color="white" v-show="latestKeywords.length>0" v-for="kwd in latestKeywords" :key="kwd" @click="doTagSearch(kwd)">{{kwd}}</mu-chip>
      </div>
      <mu-divider v-show="latestKeywords.length>0 && tagList.length>0" style="margin-top: 16px"></mu-divider>
      <mu-list-item v-show="tagList.length>0" :riple="false">
        <mu-list-item-content>
          <mu-list-item-title style="font-weight: bold; font-size: 14px; color: #424242;">{{$t('hot_tags')}}</mu-list-item-title>
        </mu-list-item-content>
      </mu-list-item>
      <div style="text-align: left">
        <mu-chip class="search_tag_item" text-color="white" :color="indexColor(i)" v-show="tagList.length>0" v-for="(tag, i) in tagList" :key="tag.tag" @click="doTagSearch(tag.tag)">
          {{tag.tag+' ('+tag.count+')'}}
        </mu-chip>
      </div>
    </mu-list>
  </mu-container>
</template>

<script>
import {util} from '../utils/util'
import {api} from '../service/api'

export default {
  name: 'Search',
  mixins: [util],
  data: function () {
    return {
      fullWidth: true,
      rowsMax: 0,
      title: '',
      tagList: []
    }
  },
  computed: {
    latestKeywords () {
      let latestString = localStorage.getItem('ba_search_keywords')
      console.log('load keywords: ' + latestString)
      if (latestString) {
        return JSON.parse(latestString)
      } else {
        return []
      }
    }
  },
  methods: {
    doTagSearch (tag) {
      this.keyword = tag
      this.doSearch()
    },
    doSearch () {
      if (this.latestKeywords.indexOf(this.title) < 0) {
        this.latestKeywords.unshift(this.title)
        if (this.latestKeywords.length > 10) {
          this.latestKeywords.pop()
        }
      }
      localStorage.setItem('ba_search_keywords', JSON.stringify(this.latestKeywords))
      this.$router.push({path: '/searchresult', query: {title: this.title}})
    }
  },
  created: function () {
    api.getTagList({pageNumber: 1, pageSize: 20})
      .then(result => {
        this.tagList = result.data
      })
  }
}
</script>

<style scoped>
  #input_box input {
    color: white;
  }
  .search_tag_item {
    margin: 8px;
  }
</style>
