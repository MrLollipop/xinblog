// components新建的Demo.vue
<template>
    <div>
        <el-row>
            <h1>{{mes}}</h1>
            <el-button>默认按钮</el-button>
            <el-button type="primary">主要按钮</el-button>
            <el-button type="success">成功按钮</el-button>
            <el-button type="info">信息按钮</el-button>
            <el-button type="warning">警告按钮</el-button>
            <el-button type="danger">危险按钮</el-button>
        </el-row>
    </div>
</template>
 
<script>
export default {
  name: "Demo",
  data() {
    return {
      mes: "这是第一个demo!!!"
    };
  }
};
</script>
 
<style>
</style>
 
 
 