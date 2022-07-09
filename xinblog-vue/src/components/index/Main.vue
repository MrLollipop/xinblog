<!-- // components新建的Demo.vue -->
<template>
  <div>
    <!-- 置顶内容 -->
    <el-row type="flex" justify="center">
      <el-col :sm="24" :lg="20"><carousel :topList=topList></carousel></el-col>
    </el-row>

    <!-- 最新发布 -->
    <div class="blogClass">
      <span><i class="el-icon-document"></i> 最新发布</span>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="60">
      <el-col :span="8" v-for="item in newestList" :key="item.id">
        <router-link :to="{ path: 'detail', query: { blogId: item.id } }">
          <blog-card :blogEntity=item></blog-card>
        </router-link>
      </el-col>
    </el-row>

    <!-- 热门 -->
    <div class="blogClass">
      <span><i class="el-icon-document"></i> 热门</span>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="60">
      <el-col :span="8" v-for="item in hotList" :key="item.id">
        <router-link :to="{ path: 'detail', query: { blogId: item.id } }">
          <blog-card :blogEntity=item></blog-card>
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
      topList: [],
      newestList: [],
      hotList: [],
    };
  },
  components: {
    carousel,
    BlogCard,
  },
  mounted() {
    this.$emit("bannerTitle", ["欣 哥 工 作 室", "为程序员创造价值"]);
    this.getIndexData();
  },
  methods: {
    // 获取首页数据列表
    getIndexData() {
      // this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("api/blogUser/indexData"),
        method: "get",
        params: this.$http.adornParams({
          from: 0,
          to: 3,
        }),
      }).then(({ data }) => {
        console.log(data);
        if (data.code === 10000) {
          this.topList = data.indexData.topList;
          this.newestList = data.indexData.newestList;
          this.hotList = data.indexData.hotList;
        } else {
          // this.dataList = [];

        }
        // this.dataListLoading = false;
      });
    },
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
 
 
 