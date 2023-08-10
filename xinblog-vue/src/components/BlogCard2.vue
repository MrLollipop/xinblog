<template>
  <div>
    <el-row type="flex" justify="center" :gutter="40">
      <el-col :span="8">
        <router-link :to="{ path: '/detail', query: { blogId: id } }">
          <el-card :body-style="{ padding: '0px' }" class="card" shadow="hover">
            <img :src="cover" class="cardImage" />
          </el-card>
        </router-link>
      </el-col>
      <el-col :span="16">
        <div class="title">
          <router-link :to="{ path: '/detail', query: { blogId: id } }">
            <el-row><h1>{{ title }}</h1></el-row>
          </router-link>  
          <el-row><h2>{{ subTitle }}</h2></el-row>
          <el-row><tag class="tag" :tagVOList="tagVOList"></tag></el-row>
          <el-row class="bottomMsg">
            <span id="times"><i class="el-icon-reading"></i> {{ viewNum }} 次</span>
            <span id="updateTime"><i class="el-icon-date"></i> {{ updateTime }}</span>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Tag from "./index/Tag.vue";
import { formatDate } from "@/utils";

export default {
  data() {
    return {
      id: this.blogEntity.id,
      title: this.blogEntity.title,
      subTitle: this.blogEntity.subTitle,
      viewNum: this.blogEntity.viewNum,
      updateTime: formatDate(this.blogEntity.updateTime),
      cover: this.blogEntity.cover,
      tagVOList: this.blogEntity.tagVOList,
    };
  },
  components: { Tag },
  props: ["blogEntity"],
  // mounted() {
  //   console.log(this.blogEntity); //父组件传递过来的数据
  // },
};
</script>

<style scoped>
.card {
  border-radius: 15px;
  padding: 0;
}
.card:hover h1 {
  font-size: 17px;
}
.cardImage {
  width: 100%;
  display: block;
}
.title {
  width: 100%;
  margin-top: 0;
  margin-bottom: 8px;
  height: 100%;
  border-bottom: 1px solid rgb(227, 228, 228);
  font-family:"Microsoft Yahei", "PingFang SC";
}
.title h1 {
  /* color: rgb(92, 84, 84); */
  color: black;
  font-size: 35px;
  margin-top: 2px;
  margin-bottom: 5px;
  padding: 2px;
  float: left;
}
.title h2 {
  color: #999;
  font-size: 20px;
  margin-top: 2px;
  margin-bottom: 4px;
  padding: 2px;
  float: left;
}
.tag {
  float:left; 
  margin-top:5px;
}
.bottomMsg {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
  margin-bottom: 2px;
  padding: 2px;
}
#times {
  float: left;
}
#updateTime {
  margin-right: 25px;
  float: right;
}
</style>
