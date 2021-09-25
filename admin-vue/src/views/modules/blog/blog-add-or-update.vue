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
      label-width="80px"
    >
      <el-form-item label="博客标题" prop="title">
        <el-input
          v-model="dataForm.title"
          placeholder="输入标题"
          :disabled="act === 'show'"
        ></el-input>
      </el-form-item>
      <el-form-item label="博客内容" prop="content">
        <el-input
          v-model="dataForm.content"
          placeholder="输入内容"
          :disabled="act === 'show'"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status" :disabled="act === 'show'">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
          <el-radio :label="2">草稿</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        label="创建时间"
        prop="createTime"
        v-if="act === 'show' || act === 'mod'"
      >
        <el-date-picker
          v-model="dataForm.createTime"
          type="date"
          placeholder="选择日期"
          :disabled="act === 'show'"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item
        label="修改时间"
        prop="updateTime"
        v-if="act === 'show' || act === 'mod'"
      >
        <el-date-picker
          v-model="dataForm.updateTime"
          type="date"
          placeholder="选择日期"
          :disabled="act === 'show'"
        ></el-date-picker>
      </el-form-item>
      <el-form-item
        label="点赞数"
        prop="likeNum"
        v-if="act === 'show' || act === 'mod'"
      >
        <el-input
          v-model="dataForm.likeNum"
          placeholder=""
          :disabled="act === 'show'"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="转发数"
        prop="forwardNum"
        v-if="act === 'show' || act === 'mod'"
      >
        <el-input
          v-model="dataForm.forwardNum"
          placeholder=""
          :disabled="act === 'show'"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="收藏数"
        prop="collectNum"
        v-if="act === 'show' || act === 'mod'"
      >
        <el-input
          v-model="dataForm.collectNum"
          placeholder=""
          :disabled="act === 'show'"
        ></el-input>
      </el-form-item>
      <el-form-item label="置顶" size="mini" prop="isTop">
        <el-radio-group v-model="dataForm.isTop" :disabled="act === 'show'">
          <el-radio :label="false">否</el-radio>
          <el-radio :label="true">是</el-radio>
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
// import { isEmail, isMobile } from "@/utils/validate";
export default {
  data() {
    /** 
    var validatePassword = (rule, value, callback) => {
      if (!this.dataForm.id && !/\S/.test(value)) {
        callback(new Error("密码不能为空"));
      } else {
        callback();
      }
    };
    var validateComfirmPassword = (rule, value, callback) => {
      if (!this.dataForm.id && !/\S/.test(value)) {
        callback(new Error("确认密码不能为空"));
      } else if (this.dataForm.password !== value) {
        callback(new Error("确认密码与密码输入不一致"));
      } else {
        callback();
      }
    };
    var validateEmail = (rule, value, callback) => {
      if (!isEmail(value)) {
        callback(new Error("邮箱格式错误"));
      } else {
        callback();
      }
    };
    var validateMobile = (rule, value, callback) => {
      if (!isMobile(value)) {
        callback(new Error("手机号格式错误"));
      } else {
        callback();
      }
    };
    */
    return {
      visible: false,
      dialog: "",
      act: "",
      roleList: [],
      dataForm: {
        id: 0,
        title: "",
        content: "",
        status: 1,
        createTime: "",
        updateTime: "",
        likeNum: "",
        forwardNum: "",
        collectNum: "",
        isTop: false,
      },
      dataRule: {
        title: [
          { required: true, message: "博客标题不能为空", trigger: "blur" },
        ],
        /*           password: [
            { validator: validatePassword, trigger: 'blur' }
          ],
          comfirmPassword: [
            { validator: validateComfirmPassword, trigger: 'blur' }
          ],
          email: [
            { required: true, message: '邮箱不能为空', trigger: 'blur' },
            { validator: validateEmail, trigger: 'blur' }
          ],
          mobile: [
            { required: true, message: '手机号不能为空', trigger: 'blur' },
            { validator: validateMobile, trigger: 'blur' }
          ] */
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
          url: this.$http.adornUrl(`/api/blog/info/${this.dataForm.id}`),
          method: "get",
        }).then(({ data }) => {
          if (data && data.code === 10000) {
            this.visible = true;
            this.dataForm.title = data.blog.title;
            this.dataForm.content = data.blog.content;
            this.dataForm.status = data.blog.status;
            this.dataForm.createTime = data.blog.createTime;
            this.dataForm.updateTime = data.blog.updateTime;
            this.dataForm.likeNum = data.blog.likeNum;
            this.dataForm.forwardNum = data.blog.forwardNum;
            this.dataForm.collectNum = data.blog.collectNum;
            this.dataForm.isTop = data.blog.isTop;
          }
        });
      } else {
        if (this.dataForm.title) {
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
          var url =  this.act == "new" ? '/api/blog/save' : '/api/blog/update';
          this.$http({
            url: this.$http.adornUrl(url),
            method: "post",
            data: this.$http.adornData({
              id: this.dataForm.id || undefined,
              title: this.dataForm.title,
              content: this.dataForm.content,
              status: this.dataForm.status,
              //createTime: this.dataForm.createTime,
              //updateTime: this.dataForm.updateTime,
              likeNum: this.dataForm.likeNum || undefined,
              forwardNum: this.dataForm.forwardNum || undefined,
              collectNum: this.dataForm.collectNum || undefined,
              isTop: this.dataForm.isTop,
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
