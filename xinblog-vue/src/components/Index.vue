// components新建的Demo.vue
<template>
  <div id="index">
    <el-container>
      <el-header>
        <h1 class="title">欣哥工作室</h1>
        <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal">
          <!-- @select="handleSelect" -->
          <el-menu-item index="1"><span class="menu-font">首页</span></el-menu-item>
          <el-submenu index="2">
            <template slot="title"><span class="menu-font">大纲</span></template>
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
          <el-menu-item index="3"><span class="menu-font">热门</span></el-menu-item>
          <el-menu-item index="4"><span class="menu-font"><a href="http://xinge.studio" target="_blank">关于我</a></span>
          </el-menu-item>
        </el-menu>
      </el-header>
      <el-container>
        <div class="banner">
          <!-- <img src="@static/pic/banner.png" /> -->
          <h1>欣哥工作室</h1>
          <h2>为程序员创造价值</h2>
        </div>
      </el-container>
      <el-container>
        <!-- <el-aside width="200px">Aside</el-aside> -->
        <el-main>
          <div class="main">
            <index-main></index-main>
            <el-row>
              <h1>{{ mes }}</h1>
            </el-row>
          </div>
          <el-footer>
            <p><a href="http://xinge.studio">欣哥工作室</a> © 2021 浙ICP备2021021057号
            </p>
          </el-footer>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
 
<script>
import Main from './index/Main.vue';

export default {
  name: "Index",
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
  },
}
</script>
 
<style>
.el-header {
  background-color: #00a8e2;
  /* opacity:0.3; */
  height: 70px;
}

.title {
  float: left;
  font-size: 15px;
  font-weight: 600;
  color: white;
  height: 70px;
  margin-top: 22px;
}

.el-menu {
  background: none;
  width: fit-content;
  float: right;
  /* margin: 0 auto; */
}

.el-menu.el-menu--horizontal {
  border-bottom: none;
}

.menu-font {
  font-size: 15px;
  font-weight: 600;
  color: white;
}

.menu-font:hover {
  color: #00a8e2;
  border-bottom: 1px solid #00a8e2;
}

.banner {
  /* top: -20px; */
  width: fit-content;
  /* margin: 0 auto; */
  background-color: #00a8e2;
  /* opacity:0.3; */
}

.banner h1 {
  font-size: 60px;
  font-weight: 900;
  color: white;
  /* -webkit-text-stroke: 1px black; */
  width: fit-content;
  margin: 40px auto;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04) */
}

.banner h2 {
  font-size: 20px;
  /* font-weight: 900; */
  color: white;
  width: fit-content;
  margin: 20px auto;
}


.el-footer {
  background-color: #F7FAFC;
  color: #333;
  text-align: center;
  line-height: 30px;
}

.el-aside {
  /* background-color: #D3DCE6; */
  background: none;
  color: #333;
  text-align: center;

  /* // 设置左侧 aside 高度 */
  height: calc(100vh - 70px);

}

.el-main {
  /* background-color: #E9EEF3; */
  background: none;
  color: #333;
  text-align: center;

  /* // 设置主体 main 高度 */
  height: calc(100vh - 70px);
}

.main {
  width: 80%;
  margin: 0 auto;
}

body {
  overflow-y: hidden;
}

/* body>.el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
} */
</style>
 
 
 