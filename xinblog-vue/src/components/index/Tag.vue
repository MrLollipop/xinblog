<template>
  <div>
    <el-button :type="changeColor()" size="mini" plain v-for="tag in tagVOList" :key="tag.key" @click="toUrl(tag.key)">
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
    toUrl(key) {
      this.$router.push({ name: 'topic', params: {'key':key} });
    },
  },
  props: ["tagVOList"],
};
</script>
