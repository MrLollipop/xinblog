<!-- // components新建的Demo.vue -->
<template>
  <div>
    <el-row type="flex" justify="center" :gutter="40">
      <div class="topic">
        <tag :tagVOList="tagVOList"></tag>
        <el-row>
          <el-col :span="20"><el-page-header class="back" @back="goBack" content="" /></el-col> 
          <el-col :span="4"><el-button plain size="small" style="float:right" @click="getBlogsList()" icon="el-icon-refresh">换一换</el-button></el-col>
        </el-row>
        <el-row class="row" :gutter="60" v-for="item in blogs" :key="item.id">
            <blog-card-2 :blogEntity="item"></blog-card-2>
        </el-row>
        <el-empty :description="noDataMsg" v-show="noDataShow"></el-empty>
        <el-row>
          <el-col :span="20"><el-page-header class="back" @back="goBack" content="" /></el-col> 
          <el-col :span="4"><el-button plain size="small" style="float:right" @click="getBlogsList()" icon="el-icon-refresh">换一换</el-button></el-col>
        </el-row>
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
      label: this.$route.params.label,
      tagVOList: [],
      blogs: [],
      noDataMsg: "",
      noDataShow: false,
      pageSize: 5,
      from: 0 - 5,
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
      // this.$emit("bannerTitle", ["欣 哥 1024", this.label]);
      this.from = 0 - this.pageSize;
      this.getBlogsList();
    },
  },
  mounted() {
    // this.$emit("bannerTitle", ["欣 哥 1024", this.label]);
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
        // console.log(data);
        if (data.code === 10000) {
          this.tagVOList = data.tags;
        }
      });
    },
    // 获取标签所属博客清单
    getBlogsList() {
      if(this.tagKey === undefined) {
        this.noDataMsg = '请随便点你喜欢的标签看看';
        this.noDataShow = true;
        return ;
      }
      let from2 = this.from + this.pageSize;
      this.$http({
        url: this.$http.adornUrl("api/blog/user/blogs/tag"),
        method: "get",
        params: this.$http.adornParams({
          key: this.tagKey,
          from: from2,
          pageSize: this.pageSize,
        }),
      }).then(({ data }) => {
        // console.log(data);
        if (data.code === 10000) {
          this.blogs = data.blogs.list;
          if (data.blogs.end) {
            this.from = 0 - this.pageSize;
          } else {
            this.from = from2;
          }
        } else if (data.code === 10006) {
          this.noDataMsg = data.msg;
          this.noDataShow = true;
        } else {
          this.$message.error(data.msg);
        }
      });
    },
    goBack() {
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
 
 
 