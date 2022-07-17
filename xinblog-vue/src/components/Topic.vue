<!-- // components新建的Demo.vue -->
<template>
  <div>
    <el-row type="flex" justify="center" :gutter="40">
      <div class="topic">
        <tag :tagVOList="tagVOList"></tag>
        key: {{ this.$route.params.key }}
        <el-row :gutter="60">
          <el-col :span="8" v-for="item in blogs" :key="item.id">
            <router-link :to="{ path: 'detail', query: { blogId: item.id } }">
              <blog-card :blogEntity="item"></blog-card>
            </router-link>
          </el-col>
        </el-row>
      </div>
    </el-row>
  </div>
</template>
 
<script>
import BlogCard from './index/BlogCard.vue';
import Tag from "./index/Tag.vue";
export default {
  components: { Tag, BlogCard },
  name: "Hot",
  data() {
    return {
      tagVOList: [],
      tagKey: this.$route.params.key,
      blogs: [],
    };
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
    // 获取缓存Tags清单
    getBlogsList() {
      this.$http({
        url: this.$http.adornUrl("api/blog/user/blogs/tag"),
        method: "get",
        params: this.$http.adornParams({
          key: this.$route.params.key,
        }),
      }).then(({ data }) => {
        console.log(data);
        if (data.code === 10000) {
          this.blogs = data.blogs;
        }
      });
    },
  },
};
</script>
 
<style>
.topic {
  width: 50vw;
}
</style>
 
 
 