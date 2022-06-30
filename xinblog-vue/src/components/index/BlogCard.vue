<template>
  <div>
    <el-card :body-style="{ padding: '0px' }" class="card" shadow="hover">
      <img
        src="https://www.keaidian.com/uploads/allimg/190424/24110307_8.jpg"
        class="cardImage"
      />
      <div class="title">
        <h1>{{ "文章标题" }}</h1>
        <tag></tag>
        <span id="times"><i class="el-icon-reading"></i> {{ times }} 次</span>
        <span id="updateTime"><i class="el-icon-date"></i> {{ date }}</span>
      </div>
    </el-card>
  </div>
</template>

<script>
import Tag from "./Tag.vue";
export default {
  data() {
    return {
      times: "2000",
      date: "05-05",
    };
  },
  components: { Tag },
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
  /* background-color: rgb(183, 200, 255); */
  /* background-color: #c7e2eb; */
  /* height: 200px;
  width: 100%;
  /* padding: 4px; */
  width: 100%;
  margin-top: 0;
  margin-bottom: 8px;
  height: auto;
}
.title h1 {
  color: #000;
  font-size: 15px;
  height: 10px;
  margin-top: 2px;
  margin-bottom: 15px;
  padding: 2px;
}
#times {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
  margin-left: 25px;
  float: left;
  padding: 2px;
}
#updateTime {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
  margin-right: 25px;
  float: right;
  padding: 2px;
}
</style>
