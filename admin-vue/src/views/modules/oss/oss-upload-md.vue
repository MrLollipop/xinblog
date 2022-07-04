<template>
  <div style="float:left">
    <el-upload
      drag
      action="http://xinblog-a.oss-cn-hangzhou.aliyuncs.com"
      :data="dataObj"
      :before-upload="beforeUploadHandle"
      :on-success="successHandle"
      :multiple="false"
      :file-list="fileList"
      :show-file-list="true"
      style="text-align: center;">
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip" slot="tip">只支持Markdown格式的文档！</div>
    </el-upload>
    <el-dialog :close="closeHandle"></el-dialog>
  </div>
  <!-- <el-dialog
    title="上传文件"
    :close-on-click-modal="false"
    @close="closeHandle"
    :visible.sync="visible">
    <el-upload
      drag
      :action="url"
      :before-upload="beforeUploadHandle"
      :on-success="successHandle"
      multiple
      :file-list="fileList"
      style="text-align: center;">
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip" slot="tip">只支持jpg、png、gif格式的图片！</div>
    </el-upload>
  </el-dialog> -->
</template>

<script>
  import {policy} from './policy'
  import {getFileType} from '@/utils'   
  import { getUUID } from '@/utils'

  export default {
    data () {
      return {
        dataObj: {
          policy: '',
          signature: '',
          key: '',
          OSSAccessKeyId: '',
          dir: 'blog-markdown/',
          host: '',
          // callback:'',
        },
        visible: false,
        num: 0,
        successNum: 0,
        fileList: [],
      }
    },
    props: {
      value: String
    },
    methods: {
      emitInput(val) {
        this.$emit('input', val)
      },
      // init (id) {
      //   this.url = this.$http.adornUrl(`/sys/oss/upload?token=${this.$cookie.get('token')}`)
      //   this.visible = true
      // },
      // 上传之前
      beforeUploadHandle (file) {
        if (getFileType(file.name) !== 'md') {
          this.$message.error('只支持Markdown格式的文件！')
          return false
        }
        this.getPolicy();
        this.num++
      },
      // 获取OSS的签名
      getPolicy() {
        // let _self = this;
        return new Promise((resolve, reject) => {
          policy(this.dataObj.dir).then(response => {
            // console.log("响应的数据",response);
            this.dataObj.policy = response.data.policy;
            this.dataObj.signature = response.data.signature;
            this.dataObj.OSSAccessKeyId = response.data.accessid;
            // this.dataObj.key = response.data.dir +getUUID()+'_${filename}';
            this.dataObj.key = this.dataObj.dir + getUUID() + '_${filename}';
            // this.dataObj.dir = response.data.dir;
            this.dataObj.host = response.data.host;
            console.log(this.dataObj);
            resolve(true);
          }).catch(err => {
            // console.log(err);
            reject(false);
          })
        })
      },
      // 上传成功
      successHandle (response, file, fileList) {
        console.log("上传成功...")
        this.fileList.pop();
        this.fileList.push({name: file.name, url: this.dataObj.host + '/' + this.dataObj.key.replace("${filename}",file.name) });
        this.emitInput(this.fileList[0].url);
      


        // this.fileList = fileList
        // this.successNum++
        // if (response && response.code === 0) {
        //   if (this.num === this.successNum) {
        //     this.$confirm('操作成功, 是否继续操作?', '提示', {
        //       confirmButtonText: '确定',
        //       cancelButtonText: '取消',
        //       type: 'warning'
        //     }).catch(() => {
        //       this.visible = false
        //     })
        //   }
        // } else {
        //   this.$message.error(response.msg)
        // }
      },
      // 弹窗关闭时
      closeHandle () {
        this.fileList = []
        this.$emit('refreshDataList')
      }
    }
  }
</script>
