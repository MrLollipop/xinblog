<template>
  <div>
    <h1>{{ title }}</h1>
    <markdown :content="content"></markdown>
    <el-page-header @back="goBack" content="详情页面"> </el-page-header>
  </div>
</template>

<script>
import Markdown from "./Markdown.vue";
export default {
  components: { Markdown },
  data() {
    return {
      title: "文章加载中...",
      subTitle: "",
      markdownAddr: "",
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
    },
  },
};
</script>

<style scoped>
</style>