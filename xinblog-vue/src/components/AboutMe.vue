<template>
  <div>
    <el-row type="flex" justify="center" :gutter="40">
      <div class="markdown">
        <el-page-header class="back" @back="goBack" content="" />
        <el-empty :description="noDataMsg" v-show="noDataShow"></el-empty>
        <h1 id="title">{{ title }}</h1>
        <h2 id="subTitle">{{ subTitle }}</h2>
        <img class="headPic" :src="cover"/>
        
        <el-row :gutter="40">
          <el-col :span="18">
            <!-- markdown-it-vue插件解析 -->
            <markdown-it-vue-light class="md-body" :content="content" :options="options" />
          </el-col>
          <!-- <el-col :span="6">
            <div class="rightBox">
              <h3>推荐阅读</h3>
              <el-row>
                <ul>
                  <li v-for="simpleVO in simpleVOList" :key="simpleVO.id" >
                    <router-link :to="{ path: 'detail', query: { blogId: simpleVO.id } }">{{simpleVO.title}}</router-link>
                  </li>
                </ul>
              </el-row>
            </div>
          </el-col> -->
        </el-row>

        <!--
        <el-row>
          <el-col><div class="sendComment">发表评论</div></el-col>
        </el-row>
        <el-row :gutter="40" style="margin-top:20px;">
          <el-form :model="form" :rules="rule" label-width="60px">
            <el-col :span="9" :offset="1">
              <el-form-item label="昵称" prop="nickName">
                <el-input v-model="form.nickName" placeholder="程序员里最帅的"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="abc@def.com"></el-input>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>
        <comment :commentNum="commentList.length" :commentList="commentList" @doSend="reply" @doChidSend="reply"></comment>
        -->
       
        <el-page-header class="back" @back="goBack" content="" />
      </div>
    </el-row>
  </div>
</template>

<script>
import { parseEmoji } from "@/utils";
import { isEmail} from "@/utils/validate";
import MarkdownItVueLight from 'markdown-it-vue/dist/markdown-it-vue-light.umd.min.js'
import 'markdown-it-vue/dist/markdown-it-vue-light.css'
import Reply from './Reply.vue';
import comment from "bright-comment";

export default {
  components: {  MarkdownItVueLight, Reply, comment},
    data() {
      var validateEmail = (rule, value, callback) => {
        if (!isEmail(value)) {
          callback(new Error("邮箱格式错误"));
        } else {
          callback();
        }
      };
    return {
      title: "文章加载中...",
      subTitle: "",
      markdownAddr: "",
      updateTime: "",
      viewNum: "",
      cover: "",
      content: "",
      noDataMsg: "",
      noDataShow: false,
      tagVOList: [],
      tagIdStr: '',
      simpleVOList: [],
      options: {
        markdownIt: {
          linkify: true
        },
        linkAttributes: {
          attrs: {
            target: '_blank',
            rel: 'noopener'
          }
        },
        katex: {
          throwOnError: false,
          errorColor: '#cc0000'
        },
        icons: 'font-awesome',
        githubToc: {
          tocFirstLevel: 2,
          tocLastLevel: 3,
          tocClassName: 'toc',
          anchorLinkSymbol: '',
          anchorLinkSpace: false,
          anchorClassName: 'anchor',
          anchorLinkSymbolClassName: 'octicon octicon-link'
        },
      },
      form:{
        nickName: '',
        email:'',
      },
      rule: {
        nickName: [
          { required: true, message: "昵称不能为空", trigger: "blur" },
        ],
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' }
        ],
      },
      commentList:[],
    };
  },
  mounted() {
    // this.blogId = this.$route.query.blogId;
    this.getBlog();
  },
  methods: {
    getBlog() {
      this.title = "关于我";
      this.subTitle = "程序员欣哥";
      this.markdownAddr = 'aboutMe/aboutMe.md';
      this.cover = 'https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/aboutMe/aboutMe.png';
      // banner标题显示
      // this.$emit("bannerTitle", [this.title, this.subTitle]);
      // this.$emit("isTitleShow", true);
        // 显示MarkDown文档
        this.getMarkdown();
    },
    getMarkdown() {
      this.$httpNoCredentials({
        url: this.$httpNoCredentials.adornUrlByProxy(
          this.markdownAddr,
          "/proxyOssApi/"
        ),
        method: "get",
      }).then(({ data }) => {
        this.content = data;
      });
    },
    checkInput(content){
      if (content == null || content == "") {
        this.$message.warning("评论内容不可为空");
        return false;
      }
      if (this.form.nickName == "") {
        this.$message.warning("昵称不可为空");
        return false;
      }
      if (this.form.email == "") {
        this.$message.warning("邮箱不可为空");
        return false;
      }
      return true;
    },
    reply(content){
      if(this.checkInput(content)) {
        content = parseEmoji(content);
        this.$http({
          url: this.$http.adornUrl("api/blog/user/reply"),
          method: "post",
          params: this.$http.adornParams({
              blogId: this.blogId,
              replyerNickName: this.form.nickName,
              replyerMail: this.form.email,
              content: content,
          }),
        }).then(({ data }) => {
          if (data.code === 10000) {
            this.$message.success("发送成功");
            // 回复方法是异步写入，收到success可能还没写完，故过500毫秒调用
            setTimeout(() => {
              // 方法区，触发获取回复
              this.getReplyList();
            }, 500);
          } else {
            this.$message.error(data.msg);
          }
        });
      }
    },
    getReplyList(){
      this.$http({
        url: this.$http.adornUrl("api/blog/user/reply/list"),
        method: "get",
        params: this.$http.adornParams({
            blogId: this.blogId,
        }),
      }).then(({ data }) => {
        if (data.code === 10000) {
          this.commentList = data.reply;
        } else {
          this.$message.error(data.msg);
        }
      });
    },
    goBack() {
      history.go(-1);
    },
  },
};
</script>

<style scoped>
#title {
  font-size: 35px;
  font-weight: 900;
  /* color: white; */
  /* color: #3366cc; */
  color: black;
  /* -webkit-text-stroke: 1px black; */
  width: fit-content;
  margin: 10px auto;
  font-family:"Microsoft Yahei", "PingFang SC";
}

#subTitle {
  font-size: 17px;
  /* font-weight: 900; */
  /* color: white; */
  /* color: #3366cc; */
  color: black;
  width: fit-content;
  margin: 20px auto;
  font-family:"Microsoft Yahei", "PingFang SC";
}
.md-body {
  text-align: left;
}

.markdown {
  width: 60vw;
  margin: 2px 0px;
}

.back {
  margin-top: 15px;
  margin-bottom: 10px;
}
.row {
  margin-bottom: 20px;
}
.headPic {
  width: 60%;
  margin-bottom: 20px;
}

.rightBox {
  float: left;
  text-align: left;
  /* border-left: 1px solid lightgray; */
}
.rightBox h3 {
  margin:3px 20px 5px;
}
.sendComment {
  float: left;
  font-size: 20px;
  color: rgb(51,51,51);
  border-left: 5px solid rgb(60, 179, 113);
  margin: 40px 0 0 20px;
  padding-left: 10px;
}
</style>