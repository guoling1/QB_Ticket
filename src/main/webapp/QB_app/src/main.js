import Vue from 'vue'
import VueRouter from 'vue-router'

// 0. 如果使用模块化机制编程， 要调用 Vue.use(VueRouter)
Vue.use(VueRouter);

// 1. 定义（路由）组件。
// 可以从其他文件 import 进来
// !* 更棒的是,采用按需加载的方式
import App from './App';
// 应用 火车票 流程组件
const ticketMenu = r => require.ensure([], () => r(require('./components/ticket/Menu')), 'group-ticket');
const ticketMain = r => require.ensure([], () => r(require('./components/ticket/Main')), 'group-ticket');
// 支付 组件 紧密关联账户组件,注意留下接口,方便修改
// 按需加载 1,组件单独打包
// const payMenu = resolve => require(['./components/pay/Menu'], resolve);
// 按需加载 2.组件分组打包
const payMenu = r => require.ensure([], () => r(require('./components/pay/Menu')), 'group-pay');
const payMain = r => require.ensure([], () => r(require('./components/pay/Main')), 'group-pay');

// 2. 定义路由
// 这里我们将App作为顶级路由,其他采用嵌套路由。
// 充分利用嵌套路由
// 1级作为顶部路由,2级作为底部路由,3级作为主内容区路由
// !* 赋予 命名路由 单独作用 : 用于路径判断,以显示顶部title
const routes = [
  {
    path: '/',
    redirect: '/ticket'
  },
  {
    path: '/ticket',
    redirect: '/ticket/menu/main',
    component: App,
    children: [
      {
        path: 'menu',
        redirect: '/ticket/menu/main',
        component: ticketMenu,
        children: [
          {
            path: 'main',
            name: 'ticketReservation',
            component: ticketMain
          }
        ]
      }
    ]
  },
  {
    path: '/pay',
    redirect: '/pay/menu/main',
    component: App,
    children: [
      {
        path: 'menu',
        redirect: '/pay/menu/main',
        component: payMenu,
        children: [
          {
            path: 'main',
            component: payMain
          }
        ]
      }
    ]
  }
];

// 3. 创建 router 实例，然后传 `routes` 配置
// 你还可以传别的配置参数, 不过先这么简单着吧。
const router = new VueRouter({
  mode: 'history',
  routes, // （缩写）相当于 routes: routes
  scrollBehavior (to, from, savedPosition) {
    // return 期望滚动到哪个的位置
  }
});

// 4. 创建和挂载根实例。
// 记得要通过 router 配置参数注入路由，
// 从而让整个应用都有路由功能
const app = new Vue({
  router
}).$mount('#app');
