// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'

// 引入element ui
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'; //避免后期打包样式不同，要放在import App from './App';之前
 
import App from './App'
import router from './router'

import 'github-markdown-css/github-markdown.css'
import httpRequest from '@/utils/httpRequest' // api: https://github.com/axios/axios

// 在Vue项目中使用element ui
Vue.use(ElementUI); 

Vue.config.productionTip = false
// 挂载全局
Vue.prototype.$http = httpRequest // ajax请求方法

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
