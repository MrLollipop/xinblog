<template>
  <div>
    <el-card :body-style="{ padding: '0px' }" class="card" shadow="hover">
      <router-link :to="{ path: 'detail', query: { blogId: id } }">
        <img :src="cover" class="cardImage" />
      </router-link>
      <div class="title">
        <router-link :to="{ path: 'detail', query: { blogId: id } }">
          <h1 style="  text-decoration: none;">{{ title }}</h1>
        </router-link>
        <tag  :tagVOList="tagVOList"></tag>
        <span id="times"><i class="el-icon-view"></i> {{ viewNum }} 次</span>
        <span id="updateTime"><i class="el-icon-date"></i> {{ updateTime }}</span>
      </div>
    </el-card>
  </div>
</template>

<script>
import Tag from "./Tag.vue";
import { formatDate } from '@/utils';

export default {
  data() {
    return {
      id: this.blogEntity.id,
      title: this.blogEntity.title,
      viewNum: this.blogEntity.viewNum,
      updateTime: formatDate(this.blogEntity.updateTime),
      cover: this.blogEntity.cover,
      tagVOList:this.blogEntity.tagVOList,
    };
  },
  components: { Tag },
  props: ["blogEntity"],
  // mounted() {
  //   console.log(this.blogEntity); //父组件传递过来的数据
  // },
  methods: {
    toUrl(id) {
      this.$router.push({ name: 'detail2', query: {blogId:id} });
    },
  }
};
</script>

<style scoped>
.card {
  border-radius: 15px;
  padding: 0;
}
.card:hover { 
  width: 101%;
  /* height: 110%; */
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
  height: auto;
}
.title h1 {
  color: #000;
  font-size: 15px;
  font-family:"Microsoft Yahei", "PingFang SC";
  height: 10px;
  margin-top: 2px;
  margin-bottom: 15px;
  padding: 2px;
}
#times {
  color: #999;
  font-size: 12px;
  font-family:"Microsoft Yahei", "PingFang SC";
  margin-top: 5px;
  margin-left: 25px;
  float: left;
  padding: 2px;
}
#updateTime {
  color: #999;
  font-size: 12px;
  font-family:"Microsoft Yahei", "PingFang SC";
  margin-top: 5px;
  margin-right: 25px;
  float: right;
  padding: 2px;
}
</style>
