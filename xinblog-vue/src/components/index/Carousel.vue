<template>
  <div class="block">
    <el-carousel height="350px" type="card" :interval="4000">
      <el-carousel-item v-for="item in topList" :key="item.id">
        <router-link :to="{ path: 'detail', query: { blogId: item.id } }">
          <el-card :body-style="{ padding: '0px' }" style="border-radius: 15px">
            <img :src="item.cover" class="carouselImage" />
          </el-card>
        </router-link>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  // data() {
  //   return {

  //   };
  // },
  props: ["topList"],
};
</script>


<style>
.block {
  /* width: 880px; */
  margin: 0;
}
.carouselImage {
  height: 350px;
  /* width: 444px; */
  display: block;
}
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