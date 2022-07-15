<template>
  <el-transfer
    v-model="choosed"
    :data="data"
    :titles="['可选择', '已选择']"
    @change="getTags"
  >
    <!-- <el-pagination
      small
      slot="left-footer"
      background
      layout="prev, pager, next, total"
      :total="lpage.total"
      :page-size="lpage.pageSize"
      :current-page="lpage.pageNo"
      @current-change="currentChangeHandleLeft"
    >
    </el-pagination> -->
    <!-- <el-pagination
      small
      slot="right-footer"
      background
      layout="prev, pager, next, total"
      :total="rpage.total"
      :page-size="rpage.pageSize"
      :current-page="rpage.pageNo"
      @current-change="currentChangeHandleRight"
    >
    </el-pagination> -->
  </el-transfer>
</template>

<script>
export default {
  data() {
    return {
      data: [], //可选择
      choosed: [], //已选择
      // checked: [],
      // page: { pageNo: 1, pageSize: 7, total: 0 },
      // lpage: { pageNo: 1, pageSize: 7, total: 0 },
      // rpage: { pageNo: 1, pageSize: 5, total: 0 },
    };
  },
  created() {
    this.getTagList();
  },
  props: ["tags"],
  methods: {
    // 获取数据列表
    getTagList() {
      this.$http({
        url: this.$http.adornUrl("/api/blog/tag/list/all"),
        method: "get",
        params: this.$http.adornParams({
          // page: this.lpage.pageNo,
          // limit: this.lpage.pageSize,
          // useVO: true,
        }),
      }).then(({ data }) => {
        console.log(data);
        if (data.code === 10000) {
          // totalCount: 13, pageSize: 10, totalPage: 2, currPage: 1,
          this.data = data.tags;
          this.choosed = this.tags;
          // this.data = data.page.list;
          // this.lpage.pageNo = data.page.currPage;
          // this.lpage.total = data.page.totalCount;
          // this.lpage.pageSize = data.page.pageSize;
        } else {
          this.data = [];
        }
      });
    },

    getTags() {
      this.$emit("tags", this.choosed);
      // console.log(this.value);
    },
    // 当前页
    currentChangeHandleLeft(val) {
      this.lpage.pageNo = val;
      this.getTagList();
    },
    // 当前页
    currentChangeHandleRight(val) {
      // this.rpage.pageNo = val;
      // this.getTagList();
    },
  },
};
</script>