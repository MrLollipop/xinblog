<template>
  <div class="list-comment">
    <div style="margin: 20px 0px">
      <el-button type="info" plain @click="toggleSelection()"
        >取消选择</el-button>

      <el-button
        type="danger"
        @click="handleDelete()"
        :disabled="multipleSelection.length <= 0"
        >批量删除</el-button>
    </div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="search" class="demo-form-inline">
      <el-form-item label="博客id">
        <el-input v-model="search.blogId" placeholder="输入博客id" clearable ></el-input>
      </el-form-item>

      <el-form-item label="状态">
        <el-select clearable v-model="search.status" placeholder="选择状态">
          <el-option label="正常" value="1"></el-option>
          <el-option label="删除" value="0"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="getCommentList">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 列表清单 -->
    <el-table
      ref="commentList"
      :data="tableData"
      border
      stripe
      style="width: 100%"
      v-loading="dataListLoading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="评论id" width="180"> </el-table-column>
      <el-table-column prop="blogId" label="博客id" width="180"> </el-table-column>
      <el-table-column prop="replyId" label="回复id" width="180"> </el-table-column>
      <el-table-column prop="replyerId" label="评论者id" width="180"> </el-table-column>
      <el-table-column prop="replyerNickName" label="评论者昵称" width="110"> </el-table-column>
      <el-table-column prop="replyerMail" label="评论者邮箱" width="150"> </el-table-column>
      <el-table-column prop="content" label="评论内容" width="220"> </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" :formatter="formatDate"> </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" size="small" type="danger">删除</el-tag>
          <el-tag v-else-if="scope.row.status === 1" size="small">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="110">
        <template slot-scope="scope">
          <el-button
            :disabled="scope.row.status == 0"
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

    <tag-add-or-update ref="tagData" v-if="addOrUpdateVisible" @refreshDataList="getTagList()"></tag-add-or-update>
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
      multipleSelection: [],
      addOrUpdateVisible: false,
      search: {
        blogId: "",
        status: "1",
      },
    };
  },
  activated() {
    this.getCommentList();
  },
  methods: {
    // 获取数据列表
    getCommentList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/api/blog/reply/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          status: this.search.status,
          blogId: this.search.blogId,
        }),
      }).then(({ data }) => {
        // console.log(data);
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
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getCommentList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getCommentList();
    },

    //切换选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.commentList.toggleRowSelection(row);
        });
      } else {
        this.$refs.commentList.clearSelection();
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
      var ids = id ? [id] : this.multipleSelection.map((item) => {
            return item.id;
          });
      this.$confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/api/blog/reply/delete"),
            method: "post",
            data: this.$http.adornData(ids, false),
          }).then(({ data }) => {
            if (data && data.code === 10000) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1200,
                onClose: () => {
                  this.getCommentList();
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