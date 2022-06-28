import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import Main from '@/components/index/Main'
import Hot from '@/components/index/Hot'
import Detail from '@/components/Detail'

Vue.use(Router)

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
          path: 'hot',
          name: 'Hot',
          component: Hot,
        },
        {
          path: 'detail',
          name: 'Detail',
          component: Detail,
        }
      ]
    },
  ]
})
