<template> 
  <div>
    <el-upload
      :action="this.dataOSS.host"
      :data="dataOSS"
      :multiple="false" :show-file-list="showFileList"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只支持Markdown格式的文档！</div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" :close="closeHandle">
      <img width="100%" :src="fileList[0].url" alt="">
    </el-dialog>
  </div>
</template>
<script>
  import {policy} from './policy'
  import {getFileType} from '@/utils'   
  import { getUUID } from '@/utils'

  export default {
    name: 'singleUpload-md',
    props: {
      value: String
    },
    computed: {
      fileUrl() {
        return this.value;
      },
      fileName() {
        if (this.value != null && this.value !== '') {
          return this.value.substr(this.value.lastIndexOf("/") + 1);
        } else {
          return null;
        }
      },
      fileList() {
        return [{
          name: this.fileName,
          url: this.fileUrl
        }]
      },
      showFileList: {
        get: function () {
          return this.value !== null && this.value !== ''&& this.value!==undefined;
        },
        set: function (newValue) {
        }
      }
    },
    data() {
      return {
        dataOSS: {
          policy: '',
          signature: '',
          key: '',
          ossaccessKeyId: '',
          dir: 'blog-markdown/',
          host: '',
          // callback:'',
        },
        dialogVisible: false
      };
    },
    methods: {
      emitInput(val) {
        this.$emit('input', val)
      },
      handleRemove(file, fileList) {
        this.emitInput('');
      },
      handlePreview(file) {
        this.dialogVisible = true;
      },
      beforeUpload(file) {
         
        if (getFileType(file.name) !== 'md') {
          this.$message.error('只支持Markdown格式的文件！')
          return false
        }

        return new Promise((resolve, reject) => {
          policy(this.dataOSS.dir).then(response => {
            this.dataOSS.policy = response.data.policy;
            this.dataOSS.signature = response.data.signature;
            this.dataOSS.ossaccessKeyId = response.data.accessid;
            this.dataOSS.key = this.dataOSS.dir + getUUID() + '_${filename}';
            this.dataOSS.host = response.data.host;
            console.log("响应的数据",this.dataOSS);
            resolve(true)
          }).catch(err => {
            reject(false)
          })
        })
      },
      handleUploadSuccess(res, file) {
        console.log("上传成功...")
        this.showFileList = true;
        this.fileList.pop();
        this.fileList.push({name: file.name, url: this.dataOSS.host + '/' + this.dataOSS.key.replace("${filename}",file.name) });
        this.emitInput(this.fileList[0].url);
      },
      // 弹窗关闭时
      closeHandle () {
        this.fileList = []
        this.$emit('refreshDataList')
      }
    }
  }
</script>
<style>

</style>


