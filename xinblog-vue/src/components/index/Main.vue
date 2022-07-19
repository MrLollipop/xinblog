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
      <el-button plain size="small" style="float:right" @click="getNewestList()" icon="el-icon-refresh">换一换</el-button>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="60">
      <el-col :span="8" v-for="item in newestList" :key="item.id">
          <blog-card :blogEntity=item></blog-card>
      </el-col>
    </el-row>

    <!-- 热门 -->
    <div class="blogClass">
      <span><i class="el-icon-document"></i> 热门</span>
      <el-button plain size="small" style="float:right" @click="getHotList()" icon="el-icon-refresh">换一换</el-button>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="60">
      <el-col :span="8" v-for="item in hotList" :key="item.id">
          <blog-card :blogEntity=item></blog-card>
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
      newestListFrom: 0,
      hotListFrom: 0,
      pageSize: 3,
    };
  },
  components: {
    carousel,
    BlogCard,
  },
  mounted() {
    this.$emit("bannerTitle", ["欣 哥 工 作 室", "为程序员创造价值"]);
    this.getIndexData();
    this.timer = setInterval(this.getNewestList, 8000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    // 获取首页数据列表
    getIndexData() {
      this.$http({
        url: this.$http.adornUrl("api/blog/user/indexData"),
        method: "get",
        params: this.$http.adornParams({
          from: 0,
          pageSize: this.pageSize,
        }),
      }).then(({ data }) => {
        // console.log(data);
        if (data.code === 10000) {
          this.topList = data.indexData.topList;
          this.newestList = data.indexData.newestList.list;
          this.hotList = data.indexData.hotList.list;
        } else {
          this.$message.error(data.msg);
        }
      });
    },
    // 获取最新博客列表
    getNewestList(){
      let from = this.newestListFrom + this.pageSize;
      this.$http({
        url: this.$http.adornUrl("api/blog/user/newestList"),
        method: "get",
        params: this.$http.adornParams({
          'from': from,
           pageSize: this.pageSize,
        }),
      }).then(({ data }) => {
        // console.log(data);
        if (data.code === 10000) {
          this.newestList = data.newestList.list;
          if (data.newestList.end) {
            this.newestListFrom = 0 - this.pageSize;
          } else {
            this.newestListFrom = from;
          }
        } else {
          this.$message.error(data.msg);
        }
      });
    },
    // 获取热门博客列表
    getHotList(){
      let from = this.hotListFrom + this.pageSize;
      this.$http({
        url: this.$http.adornUrl("api/blog/user/hotList"),
        method: "get",
        params: this.$http.adornParams({
          'from': from,
           pageSize: this.pageSize,
        }),
      }).then(({ data }) => {
        // console.log(data);
        if (data.code === 10000) {
          this.hotList = data.hotList.list;
          if (data.hotList.end) {
            this.hotListFrom = 0 - this.pageSize;
          } else {
            this.hotListFrom = from;
          }
        } else {
          this.$message.error(data.msg);
        }
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
 
 
 