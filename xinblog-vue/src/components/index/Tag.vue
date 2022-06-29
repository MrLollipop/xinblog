<template>
  <div>
    <!-- <el-tag>标签一</el-tag>
    <el-tag type="success" :effect="dark">标签二</el-tag>
    <el-tag type="info">标签三</el-tag>
    <el-tag type="warning">标签四</el-tag>
    <el-tag type="danger">标签五</el-tag> -->
    <el-button :type=changeColor() size="mini" plain>Spring</el-button>
    <el-button :type=changeColor() size="mini" plain>Linux</el-button>
    <el-button :type=changeColor() size="mini" plain>MySQL</el-button>
    <el-button :type=changeColor() size="mini" plain>Redis</el-button>
    <el-button :type=changeColor() size="mini" plain>分布式</el-button>
  </div>
</template>

<script>
export default {
  methods: {
    changeColor() {
      const colors = ["primary", "success", "info", "danger", "warning"];
      return colors[Math.floor(Math.random() * colors.length)];
    },
  },
};
</script>
