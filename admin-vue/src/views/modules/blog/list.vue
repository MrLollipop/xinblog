<template>
  <div class="mod-user">
    <!--     <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getBlogList()"
    >
      <el-form-item>
        <el-input
          v-model="dataForm.userName"
          placeholder="用户名"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button
          v-if="isAuth('sys:user:save')"
          type="primary"
          @click="addOrUpdateHandle()"
          >新增</el-button
        >
        <el-button
          v-if="isAuth('sys:user:delete')"
          type="danger"
          @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0"
          >批量删除</el-button
        >
      </el-form-item>
    </el-form> -->

    <div style="margin: 20px 0px">
      <el-button @click="toggleSelection()">取消选择</el-button>
    </div>
    <el-table
      ref="blogList"
      :data="tableData"
      border
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="博客ID" width="90"> </el-table-column>
      <el-table-column prop="title" label="标题" width="180"> </el-table-column>
      <el-table-column prop="status" label="状态" width="90"> </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
        :formatter="formatDate"
      >
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        width="180"
        :formatter="formatDate"
      >
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button @click="handleShow(scope.$index, scope.row)" type="primary" round size="small">查看</el-button>
          <el-button @click="handleShow(scope.$index, scope.row)" type="warning" round size="small">编辑</el-button>
          <el-button @click="handleShow(scope.$index, scope.row)" type="danger" round size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      //   dataList: [],
      //   pageIndex: 1,
      //   pageSize: 10,
      //   totalPage: 0,
      //   dataListLoading: false,
      //   dataListSelections: [],
      //   addOrUpdateVisible: false,
      tableData: [],
      multipleSelection: [],
    };
  },

  activated() {
    this.getBlogList();
  },

  methods: {
    // 获取数据列表
    getBlogList() {
      //   this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/blog/blog/list"),
        method: "get",
        //     params: this.$http.adornParams({
        //       page: this.pageIndex,
        //       limit: this.pageSize,
        //     }),
        // this.$axios.get('/blog/blog/list')
      }).then(({ data }) => {
        console.log(data);

        this.tableData = data;
        // if (data && data.code === 0) {
        //   this.dataList = data.page.list;
        //   this.totalPage = data.page.totalCount;
        // } else {
        //   this.dataList = [];
        //   this.totalPage = 0;
        // }
        // this.dataListLoading = false;
      });
    },

    //时间格式化
    formatDate(row, column) {
      // 获取单元格数据
      let data = row[column.property];
      if (data == null) {
        return null;
      }
      let dt = new Date(data);
      return (
        dt.getFullYear() +
        "-" +
        (dt.getMonth() + 1) +
        "-" +
        dt.getDate() +
        " " +
        dt.getHours() +
        ":" +
        dt.getMinutes() +
        ":" +
        dt.getSeconds()
      );
    },

    //切换选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.blogList.toggleRowSelection(row);
        });
      } else {
        this.$refs.blogList.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
  },
};
</script>

<style lang="scss" scoped>
</style>