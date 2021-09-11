
var blogList = [
  {
    'id': 6,
    'title': '第6篇博客',
    'content': '这里记录6篇博客',
    'status': 1,
    'createTime': '2021-07-19T14:06:50.000+0000',
    'updateTime': '2021-07-19T14:06:50.000+0000'
  },
  {
    'id': 7,
    'title': '第7篇博客',
    'content': '这里记录7篇博客',
    'status': 1,
    'createTime': '2021-07-19T14:06:50.000+0000',
    'updateTime': '2021-07-19T14:06:50.000+0000'
  },
  {
    'id': 8,
    'title': '第8篇博客',
    'content': '这里记录8篇博客',
    'status': 1,
    'createTime': '2021-07-19T14:06:50.000+0000',
    'updateTime': '2021-07-19T14:06:50.000+0000'
  },
  {
    'id': 9,
    'title': '第9篇博客',
    'content': '这里记录9篇博客',
    'status': 0,
    'createTime': '2021-07-19T14:06:50.000+0000',
    'updateTime': '2021-07-19T14:06:50.000+0000'
  },
  {
    'id': 10,
    'title': '第10篇博客',
    'content': '这里记录10篇博客',
    'status': 2,
    'createTime': '2021-07-19T14:06:50.000+0000',
    'updateTime': '2021-07-19T14:06:50.000+0000'
  }
]

// 获取列表
export function list () {
  return {
        // isOpen: false,
    url: '/blog/blog/list',
    type: 'get',
    data: blogList
  }
}
