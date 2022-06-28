<template>
  <div>
    <h1>{{ msg }}</h1>
  </div>
</template>

<script>
export default {
    data(){
        return{
            msg : 'aa',
        };
    },
    mounted() {
    this.$emit("bannerTitle", ["文章标题", "文章幅标题，可以详细说明"]);
    this.msg=this.$route.query.blogId;
    },
};
</script>

<style scoped>
</style>