<template>
  <div>
    <el-button :type=changeColor() size="mini" plain v-for="tag in tagVOList" :key="tag.key">{{tag.label}}</el-button>
  </div>
</template>

<script>
export default {
  methods: {
    changeColor() {
      const colors = ["primary", "success", "info", "danger", "warning"];
      return colors[Math.floor(Math.random() * colors.length)];
    },
  },
  props:['tags','tagVOList'],
};
</script>
