<template>
  <div class="list-tag">
    <div style="margin: 20px 0px">
      <el-button type="info" plain @click="toggleSelection()"
        >取消选择</el-button
      >
      <el-button type="primary" @click="addOrUpdateHandle(0, 'new')"
        >新建标签</el-button
      >
      <el-button
        type="danger"
        @click="handleDelete()"
        :disabled="multipleSelection.length <= 0"
        >批量删除</el-button
      >
    </div>

    <!-- 列表清单 -->
    <el-table
      ref="tagList"
      :data="tableData"
      border
      stripe
      style="width: 100%"
      v-loading="dataListLoading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="标签ID(KEY)" width="180">
      </el-table-column>
      <el-table-column prop="label" label="标题" width="180"> </el-table-column>
      <el-table-column prop="disabled" label="是否可选" width="180">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.disabled === true" size="small" type="danger">不可选</el-tag>
          <el-tag v-else-if="scope.row.disabled === false" size="small">可选</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template slot-scope="scope">

          <el-button
            @click="addOrUpdateHandle(scope.row.id, 'mod')"
            type="warning"
            round
            size="small"
            >编辑</el-button
          >
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
import TagAddOrUpdate from './tag-add-or-update.vue';
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
        title: "",
      },
    };
  },

  components: {
    TagAddOrUpdate
  },

  activated() {
    this.getTagList();
  },

  methods: {
    // 获取数据列表
    getTagList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/api/blog/tag/list"),
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
      this.getTagList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getTagList();
    },

    //切换选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.tagList.toggleRowSelection(row);
        });
      } else {
        this.$refs.tagList.clearSelection();
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
            url: this.$http.adornUrl("/api/blog/tag/delete"),
            method: "post",
            data: this.$http.adornData(ids, false),
          }).then(({ data }) => {
            if (data && data.code === 10000) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1200,
                onClose: () => {
                  this.getTagList();
                },
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        })
        .catch(() => {});
    },

    // 新增 修改 弹窗
    addOrUpdateHandle(id, act) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.tagData.init(id, act);
      });
    },
  },
};
</script>

<style lang="scss" scoped>
</style>