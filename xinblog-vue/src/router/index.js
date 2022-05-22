import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Index from '@/components/Index'
import Demo from '@/components/Demo'
import Main from '@/components/index/Main'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index
    },
    // {
    //   path: '/main',
    //   name: 'index-main',
    //   component: Main
    // }
    // {
    //   path: '/demo',
    //   name: 'Demo',
    //   component: Demo
    // }
  ]
})
