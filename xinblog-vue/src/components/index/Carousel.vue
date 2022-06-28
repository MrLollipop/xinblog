<template>
  <div class="block">
    <!-- <span class="demonstration">默认 Hover 指示器触发</span> -->
    <el-carousel height="250px" type="card" :interval="4000">
      <el-carousel-item v-for="item in 4" :key="item">
        <!-- <h3 class="small">{{ item }}</h3> -->
        <el-card :body-style="{ padding: '0px' }" style="border-radius: 15px">
          <img
            src="https://www.keaidian.com/uploads/allimg/190424/24110307_8.jpg"
            class="cardImage"
          />
        </el-card>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
// export default (
// )
//
</script>


<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  /* //  background-color: #99a9bf; */
}

.el-carousel__item:nth-child(2n + 1) {
  /* //  background-color: #d3dce6; */
}
</style>