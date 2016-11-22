import Vue from 'vue'
// 使用 vuex
import store from './store'
// 使用 vue-router
import router from './routers'
// 使用 vue-resource
import VueResource from 'vue-resource'
Vue.use(VueResource);

// 添加 全局 directive
import './directives'

// 添加 全局 filter
import './filters'

// 添加自定义 插件
import Resource from './plugin/resource'
Vue.use(Resource);

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
