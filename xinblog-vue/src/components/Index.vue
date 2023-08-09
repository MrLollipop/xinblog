// components新建的Demo.vue
<template>
  <div id="index" class="index">
    <el-container>
      <el-header :style="opacityStyle">
        <h1 class="title">欣哥1024</h1>
        <el-menu
          :default-active="activeIndex"
          class="el-menu"
          mode="horizontal"
        >
          <!-- @select="handleSelect" -->
          <el-menu-item index="1"
            ><span class="menu-font"
              ><router-link to="/">首页</router-link></span
            ></el-menu-item
          >
          <el-menu-item index="3"
            ><span class="menu-font"
              ><router-link to="/topic">专题</router-link></span
            ></el-menu-item
          >
          <el-menu-item index="4"
            ><span class="menu-font"
              ><router-link to="/aboutMe">关于我</router-link></span
            >
          </el-menu-item>
        </el-menu>
      </el-header>
      <div class="banner">
        <h1>{{ bannerTitle }}</h1>
        <h2>{{ subBannerTitle }}</h2>
      </div>
      <el-container>
        <el-main>
          <div class="main">
            <router-view @bannerTitle="updateBannerTitle" :key="$route.fullPath"></router-view>
          </div>
          <el-footer>
            <p>
              <a href="http://xinge.studio">欣哥1024</a> © 2021
              浙ICP备2021021057号
            </p>
          </el-footer>
        </el-main>
        <!-- <el-aside width="60px">
          <tag></tag>
        </el-aside> -->
      </el-container>
    </el-container>
    <back-top></back-top>
  </div>
</template>
 
<script>
import BackTop from "./BackTop.vue";
export default {
  name: "Index",
  data() {
    return {
      isTitleShow: false,
      bannerTitle: "欣 哥 1024",
      subBannerTitle: "为程序员创造价值",
      mes: "这是第一个demo!!!",
      activeIndex: "1",
      opacityStyle: {
        opacity: 1,
      },
    };
  },
  created() {
    window.addEventListener("scroll", this.handleScroll);
  },
  deactivated() {
    //因为是对window绑定的,所以除了该页面，其他页面的滚动也会内存泄露
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    handleSelect(key, keyPath) {
      // console.log(key, keyPath);
    },
    handleScroll() {
      const top = document.documentElement.scrollTop;
      // console.log(top);
      let opacity = top / 10;
      opacity = opacity > 1 ? 0.6 : 1;
      this.opacityStyle = { opacity };
    },
    updateBannerTitle(title) {
      this.bannerTitle = title[0];
      this.subBannerTitle = title[1];
    },
  },
  components: {
    BackTop,
  },
};
</script>
 
<style>
.el-header {
  background-color: #6db8d1;
  height: 70px;
  width: 98.5vw;
  position: fixed;
  left: 8px;
  top: 0;
  padding-right: 8px;
  z-index: 10;
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
}

.el-menu.el-menu--horizontal,
.el-menu--horizontal > .el-menu-item.is-active {
  border-bottom: none;
}

.menu-font {
  font-size: 15px;
  font-weight: 600;
  color: white;
}

.menu-font:hover {
  color: #6db8d1;
  border-bottom: 1px solid #6db8d1;
}

.banner {
  width: 98.5vw;
  background-color: #6db8d1;
}

.banner > h1 {
  font-size: 60px;
  font-weight: 900;
  color: white;
  /* -webkit-text-stroke: 1px black; */
  width: fit-content;
  margin: 40px auto;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04) */
}

.banner > h2 {
  font-size: 20px;
  /* font-weight: 900; */
  color: white;
  width: fit-content;
  margin: 20px auto;
}

.el-footer {
  background-color: #f7fafc;
  color: #333;
  text-align: center;
  line-height: 60px;
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
  /* height: calc(100vh - 70px); */
}

.main {
  width: 80%;
  margin: 0 auto;
}

/* .index {
  height: 100vh;
  overflow-x: hidden;
} */

/* body {
  overflow-y: hidden;
} */

/*设置router-link点击前的样式 */
a{
  text-decoration: none;
  /* color: #000; */
}
/*设置router-link点击后的样式 */
.router-link-active {
  text-decoration: none;
    /* color: red; */
}
</style>
 
 
 