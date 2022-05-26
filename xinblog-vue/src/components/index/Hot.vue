<!-- // components新建的Demo.vue -->
<template>
  <div>
    <div v-for="x in 9" :key="x">
      <span>热门</span>
      <el-button size="small">{{ currentDate }}</el-button>
      <el-divider></el-divider>
    </div>
  </div>
</template>
 
<script>
export default {
  name: "Hot",
  data() {
    return {
      currentDate: new Date(),
      bannerTitle: "热 门 视 频",
    };
  }
};
</script>
 
<style>
</style>
 
 
 