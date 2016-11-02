import Vue from 'vue'
import VueRouter from 'vue-router'
// 使用 vuex
import store from './store'
// 使用 vue-resource
import VueResource from 'vue-resource';
Vue.use(VueResource);

// 定义全局指令
// 全局火车站搜索指令
Vue.directive('search-station', {
  inserted: function (el) {
    // 车站信息解析
    const station_names = require('./station_name');
    const stationList = {};
    for (let i = 0; i < station_names.split('@').length; i++) {
      let parsing_middleware = station_names.split('@')[i].split('|');
      stationList[parsing_middleware[5]] = {};
      stationList[parsing_middleware[5]].city = parsing_middleware[1];
      stationList[parsing_middleware[5]].fullname = parsing_middleware[3];
      stationList[parsing_middleware[5]].shortname = parsing_middleware[4];
      stationList['length'] = i;
    }
    // 添加 搜索input 和 模糊匹配显示区ul
    var input = document.createElement('input');
    var ul = document.createElement('ul');
    input.addEventListener('input', function () {
      let val = input.value;
      let matchList = [];
      // 根据用户输入,进行模糊匹配
      // matchList里只记录id,完成后去重
      for (let i = 0; i < stationList.length; i++) {
        if (stationList[i].city.indexOf(val) > -1) {
          matchList.push(stationList[i].city);
          continue;
        }
        if (stationList[i].fullname.indexOf(val) > -1) {
          matchList.push(stationList[i].city);
          continue;
        }
        if (stationList[i].shortname.indexOf(val) > -1) {
          matchList.push(stationList[i].city);
        }
      }
      // 根据匹配结果添加匹配列表
      ul.innerHTML = '';
      if (matchList.length == 0) {
        var empty = document.createElement('li');
        empty.innerHTML = '啥也没搜到';
        ul.appendChild(empty);
      }
      for (let i = 0; i < matchList.length; i++) {
        var li = document.createElement('li');
        li.innerHTML = matchList[i];
        ul.appendChild(li);
      }
      el.appendChild(ul);
    });
    el.appendChild(input);
  }
});

// 使用路由 vue-router
Vue.use(VueRouter);

// 1. 定义（路由）组件。
// 可以从其他文件 import 进来
// !* 更棒的是,采用按需加载的方式
import App from './App';
// 应用 火车票 流程组件
const ticketMainMenu = r => require.ensure([], () => r(require('./components/ticket/MainMenu')), 'group-ticket');
const ticketReserve = r => require.ensure([], () => r(require('./components/ticket/Reserve')), 'group-ticket');
const ticketOrder = r => require.ensure([], () => r(require('./components/ticket/Order')), 'group-ticket');
const ticketTrainMenu = r => require.ensure([], () => r(require('./components/ticket/TrainMenu')), 'group-ticket');
const ticketTrain = r => require.ensure([], () => r(require('./components/ticket/Train')), 'group-ticket');
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
    redirect: '/ticket/main-menu/reserve',
    component: App,
    children: [
      {
        path: 'main-menu',
        redirect: '/ticket/main-menu/reserve',
        component: ticketMainMenu,
        children: [
          {
            path: 'reserve',
            name: 'ticketReserve',
            component: ticketReserve
          },
          {
            path: 'order',
            name: 'ticketOrder',
            component: ticketOrder
          }
        ]
      },
      {
        path: 'train-menu',
        redirect: '/ticket/train-menu/train',
        component: ticketTrainMenu,
        children: [
          {
            path: 'train',
            name: 'ticketTrain',
            component: ticketTrain
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

// 添加路由变化监听
router.beforeEach((to, from, next) => {
  next();
});

// 4. 创建和挂载根实例。
// 记得要通过 router 配置参数注入路由，
// 从而让整个应用都有路由功能
const app = new Vue({
  store,
  router
}).$mount('#app');
