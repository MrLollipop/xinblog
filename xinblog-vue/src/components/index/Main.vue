<!-- // components新建的Demo.vue -->
<template>
  <div>
    <div v-for="x in 9" :key="x">
      <span>热门</span>
      <el-row :gutter="60">
        <el-col :span="8" v-for="(o) in 3" :key="o">
          <el-card :body-style="{ padding: '0px' }" style="border-radius: 15px;">
            <!-- https://img95.699pic.com/photo/50059/6763.jpg_wh300.jpg!/fh/300/quality/90 -->
            <img src="https://www.keaidian.com/uploads/allimg/190424/24110307_8.jpg" class="cardImage">
            <div style="padding: 4px;">
              <h1 style="height:10px;margin: 2px; padding: 2px;">{{ '文章标题' + o }}</h1>
              <div class="bottom clearfix">
                <!-- <time class="time">{{ currentDate }}</time> -->
                <el-button type="text" class="button">操作按钮</el-button>
              </div>
            </div>
          </el-card>

        </el-col>
      </el-row>
      <el-divider></el-divider>
    </div>
  </div>
</template>
 
<script>
export default {
  name: "Main",
  data() {
    return {
      currentDate: new Date()
    };
  }
};
</script>
 
<style>
/* .time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 3px;
  line-height: 5px;
} */

/* .button {
  padding: 0;
  float: right;
} */

.cardImage {
  width: 100%;
  display: block;
}

.cardImage:hover {
  width: 105%;
  /* height: 110%; */
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>
 
 
 