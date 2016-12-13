import 'babel-polyfill'

import Vue from 'vue'
// 使用 vuex
import store from './store'
// 使用 vue-router
import router from './routers'
// 使用 vue-resource
import VueResource from 'vue-resource'
Vue.use(VueResource);

// ajax 请求的全局拦截器
Vue.http.interceptors.push((request, next) => {
  next((response) => {
    let {status,body} = response;
    if (status == 200) {
      if (body.code != 1) {
        response.status = 500;
        response.statusMessage = body.message || '系统异常';
        response.statusText = 'Internal Server Error';
        response.ok = false;
      } else {
        response.data = body.data;
      }
    } else if (status == 506) {
      console.log(response);
      console.log('禁止操作');
    } else {
      response.statusMessage = '系统异常';
    }
    return response;
  })
});

// 添加 全局 directive
import './directives'

// 添加 全局 filter
import './filters'

// 添加自定义 插件

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
