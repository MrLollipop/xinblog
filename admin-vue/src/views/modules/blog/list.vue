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
      <el-button type="info" plain @click="toggleSelection()"
        >取消选择</el-button
      >
      <el-button type="primary">新建博客</el-button>
      <el-button type="danger" :disabled="multipleSelection.length <= 0"
        >批量删除</el-button
      >
    </div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="search" class="demo-form-inline">
      <el-form-item label="博客标题">
        <el-input v-model="search.title" placeholder="搜索标题"></el-input>
      </el-form-item>
      <el-form-item label="博客内容">
        <el-input v-model="search.content" placeholder="搜索内容"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="search.status" placeholder="状态">
          <el-option label="正常" value="1"></el-option>
          <el-option label="草稿" value="2"></el-option>
          <el-option label="删除" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 列表清单 -->
    <el-table
      ref="blogList"
      :data="tableData"
      border
      stripe
      style="width: 100%"
      v-loading="dataListLoading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="博客ID" width="90"> </el-table-column>
      <el-table-column prop="title" label="标题" width="180"> </el-table-column>
      <el-table-column prop="status" label="状态" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" size="small" type="danger"
            >删除</el-tag
          >
          <el-tag v-else-if="scope.row.status === 1" size="small">正常</el-tag>
          <el-tag v-else-if="scope.row.status === 2" size="small" type="info"
            >草稿</el-tag
          >
        </template>
      </el-table-column>
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
          <el-button
            @click="handleShow(scope.$index, scope.row)"
            type="primary"
            round
            size="small"
            >查看</el-button
          >
          <el-button
            @click="handleEdit(scope.$index, scope.row)"
            type="warning"
            round
            size="small"
            >编辑</el-button
          >
          <el-button
            @click="handleDelete(scope.$index, scope.row)"
            type="danger"
            round
            size="small"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="prev, pager, next, jumper"
      :total="totalCount"
      :page-size="pageSize"
      :current-page="pageIndex"
      @current-change="currentChangeHandle"
    >
    </el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      totalCount: 0,
      dataListLoading: false,
      //   addOrUpdateVisible: false,
      multipleSelection: [],
      search: {
        title: "",
        content: "",
        status: "",
      },
    };
  },

  activated() {
    this.getBlogList();
  },

  methods: {
    // 获取数据列表
    getBlogList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("api/blog/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
        }),
      }).then(({ data }) => {
        console.log(data);
        if (data.code === 10000) {
          this.tableData = data.page.list;
          this.totalPage = data.page.totalPage;
          this.totalCount = data.page.totalCount;
        } else {
          this.dataList = [];
          this.totalPage = 0;
          this.totalCount = 0;
        }
        this.dataListLoading = false;
      });
    },

    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getBlogList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getBlogList();
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
    // 展示
    handleShow(index, row) {
      console.log(index, row.id);
    },
    // 提交查询
    onSubmit() {
      console.log("submit!");
    },
  },
};
</script>

<style lang="scss" scoped>
</style>