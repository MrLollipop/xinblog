<template>
  <div>
    <el-button class="singleTag" :type="changeColor()" size="mini" plain v-for="tag in tagVOList" :key="tag.key" @click="toUrl(tag)">
      {{ tag.label }}
    </el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    changeColor() {
      const colors = ["primary", "success", "info", "danger", "warning"];
      return colors[Math.floor(Math.random() * colors.length)];
    },
    toUrl(tag) {
      this.$router.push({
        name: 'topic', 
        params: {
          'key':tag.key,
          'label':tag.label,
          } 
      });
    },
  },
  props: ["tagVOList"],
};
</script>

<style scoped>
.singleTag{
  margin: 5px;
  /* margin:3px 5px; */
}
</style>