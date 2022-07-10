<template>
  <div>
    <el-row type="flex" justify="center" :gutter="40">
      <div class="markdown">
        <el-page-header class="back" @back="goBack" content="主页" />
        <!-- <h1 style="font-size:30px">{{ title }}</h1> -->
        <el-row type="flex" class="row" justify="space-around">
            <el-col :span="12"><span style="float:left">浏览量：{{ viewNum }}</span></el-col>
            <el-col :span="12"><span style="float:right">最后更新时间：{{ updateTime }}</span></el-col>
        </el-row>    
        <img class="headPic" :src="cover"/>
        <markdown :content="content"></markdown>
        <el-page-header class="back" @back="goBack" content="主页" />
      </div>
    </el-row>
  </div>
</template>

<script>
import Markdown from "./Markdown.vue";
import { formatDate } from "@/utils";

export default {
  components: { Markdown },
  data() {
    return {
      title: "文章加载中...",
      subTitle: "",
      markdownAddr: "",
      updateTime: "",
      viewNum: "",
      cover: "",
      content: "",
    };
  },
  mounted() {
    this.blogId = this.$route.query.blogId;
    this.getBlog();
  },
  methods: {
    getBlog() {
      this.$http({
        url: this.$http.adornUrl("api/blogUser/view/" + this.blogId),
        method: "get",
      }).then(({ data }) => {
        console.log(data);
        if (data.code === 10000) {
          this.title = data.blog.title;
          this.subTitle = data.blog.subTitle;
          this.markdownAddr = data.blog.markdownAddr;
          this.updateTime = formatDate(data.blog.updateTime);
          this.cover = data.blog.cover;
          this.viewNum = data.blog.viewNum;

          this.$emit("bannerTitle", [this.title, this.subTitle]);

          this.getMarkdown();
        } else {
        }
      });
    },
    getMarkdown() {
      // console.log(this.markdownAddr.substr(47, this.markdownAddr.length));
      console.log(this.markdownAddr);
      this.$http({
        url: this.$http.adornUrlByProxy(
          // this.markdownAddr.substr(47, this.markdownAddr.length),
          this.markdownAddr,
          "/proxyOssApi/"
        ),
        method: "get",
      }).then(({ data }) => {
        this.content = data;
      });
    },
    goBack() {
      console.log("go back");
      history.go(-1);
    },
  },
};
</script>

<style scoped>
.markdown {
  width: 50vw;
  margin: 2px 0px;
}
.back {
  margin-top: 15px;
  margin-bottom: 10px;
}
.row {
  margin-top: 20px;
  margin-bottom: 20px;
}
.headPic {
  width: 60%;
  margin-bottom: 20px;
}
</style>