<!-- // components新建的Demo.vue -->
<template>
  <div>
    <!-- 置顶内容 -->
    <el-row type="flex" justify="center">
      <el-col :sm="24" :lg="20"><carousel></carousel></el-col>
    </el-row>

    <!-- 最新发布 -->
    <div class="blogClass">
      <span><i class="el-icon-document"></i> 最新发布</span>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="60">
      <el-col :span="8" v-for="o in 3" :key="o">
        <router-link :to="{ path: 'detail', query: { blogId: 'aaa' } }">
          <blog-card :blogEntity=blogEntity></blog-card>
        </router-link>
      </el-col>
    </el-row>

    <!-- 热门 -->
    <div class="blogClass">
      <span><i class="el-icon-document"></i> 热门</span>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="60">
      <el-col :span="8" v-for="o in 3" :key="o">
        <router-link :to="{ path: 'detail', query: { blogId: 'aaa' } }">
          <blog-card :blogEntity=blogEntity></blog-card>
        </router-link>
      </el-col>
    </el-row>
  </div>
</template>
 
<script>
import BlogCard from "./BlogCard.vue";
import carousel from "./Carousel.vue";
export default {
  name: "Main",
  data() {
    return {
      currentDate: new Date(),
      blogEntity: {
        title: "博客标题",
        times: "5000",
        updateTime: "06-30",
      },
    };
  },
  components: {
    carousel,
    BlogCard,
  },
  mounted() {
    this.$emit("bannerTitle", ["欣 哥 工 作 室", "为程序员创造价值"]);
  },
};
</script>
 
<style>
.blogClass {
  height: 10px;
  margin-left: 0;
  margin-top: 25px;
  text-align: left;
  font-size: 20px;
  color: #999;
  /* border-bottom: 1px solid #999; */
}
</style>
 
 
 