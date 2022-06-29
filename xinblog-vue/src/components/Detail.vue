<template>
  <div>
    <h1>{{ msg }}</h1>
    <markdown :content="content"></markdown>
  </div>
</template>

<script>
import Markdown from "./Markdown.vue";
export default {
  components: { Markdown },
  data() {
    return {
      msg: "aa",
      content: "",
    };
  },
  mounted() {
    this.$emit("bannerTitle", ["文章标题", "文章幅标题，可以详细说明"]);
    this.msg = this.$route.query.blogId;
    this.getBlog();
  },
  methods: {
    getBlog() {
      this.$http({
        url: this.$http.adornUrlByProxy(
          "5.Spring5-%E6%B3%A8%E8%A7%A3%E9%A9%B1%E5%8A%A8%E5%BC%80%E5%8F%91-1.%E7%BB%84%E4%BB%B6%E6%B3%A8%E5%86%8C.md",
          "/proxyOssApi/"
        ),
        method: "get",
      }).then(({ data }) => {
        this.content = data;
      });
    },
  },
};
</script>

<style scoped>
</style>