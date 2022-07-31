<template>
  <div>
    <el-row type="flex" justify="center" :gutter="40">
      <div class="markdown">
        <el-page-header class="back" @back="goBack" content="" />
        <el-empty :description="noDataMsg" v-show="noDataShow"></el-empty>
        <img class="headPic" :src="cover"/>
        <el-row type="flex" class="row" justify="space-around">
            <el-col :span="6" :offset="6"><span style="float:left"><i class="el-icon-view"> {{ viewNum }} </i></span></el-col>
            <el-col :span="6" ><span style="float:right"><i class="el-icon-date"> {{ updateTime }} </i></span></el-col>
            <el-col :span="6"></el-col>
        </el-row>
        <tag :tagVOList="tagVOList"></tag>
        
        <!-- markdown-it插件解析 -->
        <!-- <markdown :content="content"></markdown> -->

        <el-row :gutter="40">
          <el-col :span="18">
            <!-- markdown-it-vue插件解析 -->
            <markdown-it-vue class="md-body" :content="content" :options="options"></markdown-it-vue>
          </el-col>
          <el-col :span="6">
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
          </el-col>
        </el-row>

        <!-- <reply></reply> -->
        <!-- <reply-2></reply-2> -->
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
       
        <el-page-header class="back" @back="goBack" content="" />
      </div>
    </el-row>
  </div>
</template>

<script>
import Markdown from "./Markdown.vue";
import { formatDate } from "@/utils";
import { parseEmoji } from "@/utils";
import { isEmail} from "@/utils/validate";
import Tag from './index/Tag.vue';
import MarkdownItVue from 'markdown-it-vue'
import 'markdown-it-vue/dist/markdown-it-vue.css'
import Reply from './Reply.vue';
import comment from "bright-comment";

export default {
  components: { Markdown, Tag,  MarkdownItVue, Reply, comment},
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
    this.blogId = this.$route.query.blogId;
    this.getBlog();
  },
  methods: {
    getBlog() {
      this.$http({
        url: this.$http.adornUrl("api/blog/user/view/" + this.blogId),
        method: "get",
      }).then(({ data }) => {
        // console.log(data);
        if (data.code === 10000) {
          this.title = data.blog.title;
          this.subTitle = data.blog.subTitle;
          this.markdownAddr = data.blog.markdownAddr;
          this.updateTime = formatDate(data.blog.updateTime);
          this.cover = data.blog.cover;
          this.viewNum = data.blog.viewNum;
          this.tagVOList = data.blog.tagVOList;

          // banner标题显示
          this.$emit("bannerTitle", [this.title, this.subTitle]);
          // 显示MarkDown文档
          this.getMarkdown();
          // 获取推荐阅读列表
          this.getSimilar();
          // 获取回复列表
          this.getReplyList();
        } else if (data.code == 11004) {
          this.noDataMsg = data.msg;
          this.noDataShow = true;
        } else {
          this.$message.error(data.msg);
        }
      });
    },
    getMarkdown() {
      // console.log(this.markdownAddr);
      this.$http({
        url: this.$http.adornUrlByProxy(
          this.markdownAddr,
          "/proxyOssApi/"
        ),
        method: "get",
      }).then(({ data }) => {
        this.content = data;
      });
    },
    getTagIds() {
      var str = '';
      this.tagVOList.forEach(function (element, index){
        str = str + element.key + ',';
      });
      this.tagIdStr = str;
    },
    getSimilar() {
      this.getTagIds();
      this.$http({
        url: this.$http.adornUrl("api/blog/user/similar"),
        method: "get",
        params: this.$http.adornParams({
          keyStr: this.tagIdStr,
          selfId: this.blogId,
        }),
      }).then(({ data }) => {
        if (data.code === 10000) {
          this.simpleVOList = data.blogs
        } else {
          this.$message.error(data.msg);
        }
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