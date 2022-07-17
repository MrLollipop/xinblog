<!-- // components新建的Demo.vue -->
<template>
  <div>
    <el-row type="flex" justify="center" :gutter="40">
      <div class="topic">
        <tag :tagVOList="tagVOList"></tag>
        <el-page-header class="back" @back="goBack" content="" />
        <el-row class="row" :gutter="60" v-for="item in blogs" :key="item.id">
          <!-- <router-link :to="{ path: '/detail', query: { blogId: item.id } }"> -->
            <blog-card-2 :blogEntity="item"></blog-card-2>
          <!-- </router-link> -->
        </el-row>
        <el-empty :description="noDataMsg" v-show="noDataShow"></el-empty>
        <el-page-header class="back" @back="goBack" content="" />
      </div>
    </el-row>
  </div>
</template>
 
<script>
import BlogCard2 from "./BlogCard2.vue";
import Tag from "./index/Tag.vue";

export default {
  components: { Tag, BlogCard2 },
  name: "Hot",
  data() {
    return {
      tagKey: this.$route.params.key,
      label: "专题清单",
      tagVOList: [],
      blogs: [],
      noDataMsg: "",
      noDataShow: false,
    };
  },
  watch: {
    // 监听路由变化 $route.path == this.$route.path
    "$route.path": function (newVal, oldVal) {
      // console.log(newVal);
      this.blogs = [];
      this.noDataShow = false;
      this.tagKey = this.$route.params.key;
      this.label = this.$route.params.label;
      this.getBlogsList();
      this.$emit("bannerTitle", ["欣 哥 工 作 室", this.label]);
    },
  },
  mounted() {
    this.$emit("bannerTitle", ["欣 哥 工 作 室", "专题清单"]);
    this.getTagList();
    this.getBlogsList();
  },
  methods: {
    // 获取缓存Tags清单
    getTagList() {
      this.$http({
        url: this.$http.adornUrl("api/blog/user/tags"),
        method: "get",
        params: this.$http.adornParams({}),
      }).then(({ data }) => {
        console.log(data);
        if (data.code === 10000) {
          this.tagVOList = data.tags;
        }
      });
    },
    // 获取标签所属博客清单
    getBlogsList() {
      this.$http({
        url: this.$http.adornUrl("api/blog/user/blogs/tag"),
        method: "get",
        params: this.$http.adornParams({
          key: this.tagKey,
        }),
      }).then(({ data }) => {
        console.log(data);
        if (data.code === 10000) {
          this.blogs = data.blogs;
        } else if (data.code === 10006) {
          this.noDataMsg = data.msg;
          this.noDataShow = true;
        } else {
        }
      });
    },
    goBack() {
      // console.log("go back");
      history.go(-1);
    },
  },
};
</script>
 
<style>
.topic {
  width: 50vw;
}
.row {
  margin-top: 20px;
  margin-bottom: 10px;
}
</style>
 
 
 