import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Index from '@/components/Index'
import Demo from '@/components/Demo'
import Main from '@/components/index/Main'
import Hot from '@/components/index/Hot'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index,
      redirect:'main',
      children:[
        {
          path: 'main',
          name: 'Main',
          component: Main,
        },
        {
          path: 'hot',
          name: 'Hot',
          component: Hot,
        }
      ]
    },
  ]
})
