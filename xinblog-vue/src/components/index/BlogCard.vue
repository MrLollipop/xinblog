<template>
  <div>
    <el-card :body-style="{ padding: '0px' }" class="card" shadow="hover">
      <img
        src="https://www.keaidian.com/uploads/allimg/190424/24110307_8.jpg"
        class="cardImage"
      />
      <!-- <div class="title">
        <h1 style="height: 10px; margin: 2px; padding: 2px">
          {{ "文章标题" }}
        </h1>
      </div> -->
      <div class="title">
        <h1>{{ "文章标题" }}</h1>
        <tag></tag>
      </div>
    </el-card>
  </div>
</template>

<script>
import Tag from "./Tag.vue";
export default {
  components: { Tag },
};
</script>

<style scoped>
.card {
  border-radius: 15px;
  padding: 0;
}
.card:hover h1{
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
</style>
