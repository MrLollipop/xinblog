import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import Main from '@/components/index/Main'
import Detail from '@/components/Detail'
import Topic from '@/components/Topic'

Vue.use(Router)

const originalPush = Router.prototype.push

Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index,
      redirect: 'main',
      children: [
        {
          path: 'main',
          name: 'Main',
          component: Main,
        },
        {
          path: 'detail',
          name: 'detail',
          component: Detail,
        },
        {
          path: 'detail/:blogId',
          name: 'detail2',
          component: Detail,
        },
        {
          path: 'topic/:key/:label',
          name: 'topic',
          component: Topic,
        },
        {
          path: 'topic',
          name: 'topic2',
          component: Topic,
        },
      ]
    },
  ]
})
