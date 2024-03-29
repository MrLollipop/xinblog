import Vue from 'vue'
import router from '@/router'
// import store from '@/store'

/**
 * 获取uuid
 */
export function getUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
  })
}

/**
 * 是否有权限
 * @param {*} key
 */
export function isAuth(key) {
  return JSON.parse(sessionStorage.getItem('permissions') || '[]').indexOf(key) !== -1 || false
}

/**
 * 树形数据转换
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
export function treeDataTranslate(data, id = 'id', pid = 'parentId') {
  var res = []
  var temp = {}
  for (var i = 0; i < data.length; i++) {
    temp[data[i][id]] = data[i]
  }
  for (var k = 0; k < data.length; k++) {
    if (temp[data[k][pid]] && data[k][id] !== data[k][pid]) {
      if (!temp[data[k][pid]]['children']) {
        temp[data[k][pid]]['children'] = []
      }
      if (!temp[data[k][pid]]['_level']) {
        temp[data[k][pid]]['_level'] = 1
      }
      data[k]['_level'] = temp[data[k][pid]]._level + 1
      temp[data[k][pid]]['children'].push(data[k])
    } else {
      res.push(data[k])
    }
  }
  return res
}

/**
 * 清除登录信息
 */
// export function clearLoginInfo () {
//   Vue.cookie.delete('token')
//   store.commit('resetStore')
//   router.options.isAddDynamicMenuRoutes = false
// }

/**
 * 时间格式化工具
 * @param {Date toString 字符串} timeStr 
 * @returns 
 */
export function formatDate(timeStr) {
  // 获取单元格数据
  if (timeStr == null) {
    return null;
  }
  let dt = new Date(timeStr);
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
}

/**
 * 将[xxx]emoji字符串 转义为<emoji>xxx</emoji>
 * @param {*} text 文本
 */
export function parseEmoji(text) {
  text = text.replace(new RegExp("\\[","g"), "<emoji>");
  text = text.replace(new RegExp("\\]","g"), "</emoji>");
  return text;
}

/**
 * 将<emoji>xxx</emoji>字符串 转义为[xxx]
 * @param {*} text 文本
 * @returns 
 */
export function parseText2Emoji(text) {
  text = text.replace(new RegExp("<emoji>","g"), "[");
  text = text.replace(new RegExp("</emoji>","g"), "]");
  return text;
}