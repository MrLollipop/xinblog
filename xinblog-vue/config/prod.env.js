'use strict'
// let params = process.argv[3]
// let baseUrl = ''
// let ossUrl = ''
// switch (params) {
//     case 'test':
//       baseUrl = '"http://47.98.130.159/gateway/"'
//       break
//     case 'prod':
//       baseUrl = '"http://47.98.130.159/gateway/"'
//       ossUrl = '"https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/"'
//       break
    // default:
    //   baseUrl = '"http://c.com"'
// }
module.exports = {
  NODE_ENV: '"production"',
  OPEN_PROXY: false, // 生产环境，无法使用VUE虚拟代理，无法识别路径。只能通过Nginx配置反向代理
  baseUrl: '"http://xinge1024.com/gateway/"', //避免跨域问题，使用域名访问
  ossUrl: '"https://xinblog-a.oss-cn-hangzhou.aliyuncs.com/"',
};
