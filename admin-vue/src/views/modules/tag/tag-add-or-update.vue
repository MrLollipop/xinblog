<template>
  <el-dialog
    :title="this.dialog"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="100px"
    >
      <el-form-item label="标签" prop="label">
        <el-input
          v-model="dataForm.label"
          placeholder="一些通用的关键词"
          :disabled="act === 'show'"
        ></el-input>
      </el-form-item>

      <el-form-item label="是否可选" size="mini" prop="disabled">
        <el-radio-group v-model="dataForm.disabled" :disabled="act === 'show'">
          <el-radio :label="false">可选</el-radio>
          <el-radio :label="true">不可选</el-radio>
        </el-radio-group>
      </el-form-item>
      
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button
        type="primary"
        @click="dataFormSubmit()"
        v-if="act === 'mod' || act === 'new'"
        >确定</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      dialog: "",
      act: '',
      dataForm: {
        id: 0,
        label: "",
        disabled: false,
      },
      dataRule: {
        label: [
          { required: true, message: "标签不能为空", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    init(id, act) {
      this.visible = true;
      this.getTitle(act);

      if (act != "new") {
        this.dataForm.id = id || 0;
        this.$http({
          url: this.$http.adornUrl(`/api/blog/tag/info/${this.dataForm.id}`),
          method: "get",
        }).then(({ data }) => {
          if (data && data.code === 10000) {
            this.visible = true;
            this.dataForm.label = data.tag.label;
            this.dataForm.disabled = data.tag.disabled;
          }
        });
      } else {
        if (this.dataForm.label) {
          this.$refs["dataForm"].resetFields();
          this.dataForm.id = undefined;
        }
      }
    },
    getTitle(act) {
      this.act = act;
      if (act == "new") {
        this.dialog = "新增";
      } else if (act == "show") {
        this.dialog = "查看";
      } else {
        this.dialog = "编辑";
      }
    },

    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          var url =  this.act == "new" ? '/api/blog/tag/create' : '/api/blog/tag/update';
          this.$http({
            url: this.$http.adornUrl(url),
            method: "post",
            data: this.$http.adornData({
              key: this.dataForm.id || undefined,
              label: this.dataForm.label,
              disabled: this.dataForm.disabled,
            }),
          }).then(({ data }) => {
            if (data && data.code === 10000) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1200,
                onClose: () => {
                  this.visible = false;
                  this.$emit("refreshDataList");
                },
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        }
      });
    },
  },
};
</script>
