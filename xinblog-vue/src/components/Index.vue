// components新建的Demo.vue
<template>
  <el-container>
    <el-header>
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <el-menu-item index="1">首页</el-menu-item>
        <el-submenu index="2">
          <template slot="title">大纲</template>
          <el-menu-item index="2-1">选项1</el-menu-item>
          <el-menu-item index="2-2">选项2</el-menu-item>
          <el-menu-item index="2-3">选项3</el-menu-item>
          <el-submenu index="2-4">
            <template slot="title">选项4</template>
            <el-menu-item index="2-4-1">选项1</el-menu-item>
            <el-menu-item index="2-4-2">选项2</el-menu-item>
            <el-menu-item index="2-4-3">选项3</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-menu-item index="3">热门</el-menu-item>
        <el-menu-item index="4"><a href="http://xinge.studio" target="_blank">关于我</a></el-menu-item>
      </el-menu>
    </el-header>
    <el-container>
      <el-container>
        <el-aside width="200px">Aside</el-aside>
        <el-main>
          <div>
            <index-main></index-main>
            <el-row>
              <h1>{{ mes }}</h1>
            </el-row>
          </div>
        </el-main>
      </el-container>
      <el-footer>
        <p><a href="http://xinge.studio">欣哥工作室</a> © 2021 <a title="查看该备案号下的子备案信息"
            href="https://www.beianx.cn/search?keyword=%E6%B5%99ICP%E5%A4%872021021057%E5%8F%B7">浙ICP备2021021057号</a>
        </p>
      </el-footer>
    </el-container>
  </el-container>
</template>
 
<script>
import Main from './index/Main.vue';

export default {
  name: "Demo",
  data() {
    return {
      mes: "这是第一个demo!!!",
      activeIndex: '1',
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    }
  },
  components: {
    "index-main": Main
  }
};
</script>
 
<style>
.el-header {
  background-color: #F7FAFC;
}

.el-menu-demo {
  font: optional;
  background: none;
}

.el-footer {
  background-color: #F7FAFC;
  color: #333;
  text-align: center;
  line-height: 30px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  /* line-height: 160px; */
}

body>.el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>
 
 
 