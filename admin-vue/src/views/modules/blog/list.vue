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
      <el-button type="danger" @click="handleDelete()" :disabled="multipleSelection.length <= 0"
        >批量删除</el-button
      >
    </div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="search" class="demo-form-inline">
      <el-form-item label="标题">
        <el-input v-model="search.title" placeholder="搜索标题"></el-input>
      </el-form-item>

      <el-form-item label="内容">
        <el-input v-model="search.content" placeholder="搜索内容"></el-input>
      </el-form-item>

      <el-form-item label="状态">
        <el-select clearable="true" v-model="search.status" placeholder="选择状态">
          <el-option label="正常" value="1"></el-option>
          <el-option label="草稿" value="2"></el-option>
          <el-option label="删除" value="0"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="置顶">
        <el-select v-model="search.isTop" placeholder="是否置顶">
          <el-option label="是" value="true"></el-option>
          <el-option label="否" value="false"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="日期">
        <!-- 需要指定格式，否则返回UTC时间 -->
        <el-date-picker
          v-model="search.queryDate"
          value-format="yyyy-MM-dd"
          type="daterange"
          align="center"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
        >
        </el-date-picker
      ></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getBlogList">查询</el-button>
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
      <el-table-column prop="id" label="博客ID" width="180"> </el-table-column>
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
        width="150"
        :formatter="formatDate"
      >
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        width="150"
        :formatter="formatDate"
      >
      </el-table-column>
      <el-table-column prop="likeNum" label="点赞数" width="70">
      </el-table-column>
      <el-table-column prop="forwardNum" label="转发数" width="70">
      </el-table-column>
      <el-table-column prop="collectNum" label="收藏数" width="70">
      </el-table-column>
      <el-table-column prop="isTop" label="置顶" width="70">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isTop" size="small" type="danger">是 </el-tag>
          <el-tag v-else size="small">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="240">
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
          <el-button :disabled="scope.row.status == 0"
            @click="handleDelete(scope.row.id)"
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
      layout="prev, pager, next, total, jumper"
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
        status: "1",
        queryDate: "",
        startDate: "",
        endDate: "",
        isTop: "",
      },
      pickerOptions: {
        shortcuts: [
          {
            text: "当天",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
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
        url: this.$http.adornUrl("/api/blog/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          title: this.search.title,
          content: this.search.content,
          status: this.search.status,
          startDate:
            null != this.search.queryDate ? this.search.queryDate[0] : "",
          endDate:
            null != this.search.queryDate ? this.search.queryDate[1] : "",
          isTop: this.search.isTop,
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
    // UTC时间转为+8区时间

    // 删除
    handleDelete(id) {
      var blogIds = id
        ? [id]
        : this.multipleSelection.map((item) => {
            return item.id;
          });
      this.$confirm(
        `确定对[id=${blogIds.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/api/blog/delete"),
            method: "post",
            data: this.$http.adornData(blogIds, false),
          }).then(({ data }) => {
            if (data && data.code === 10000) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.getBlogList();
                },
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
</style>