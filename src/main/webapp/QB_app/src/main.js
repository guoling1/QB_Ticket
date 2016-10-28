import Vue from 'vue'
import VueRouter from 'vue-router'

// 0. 如果使用模块化机制编程， 要调用 Vue.use(VueRouter)
Vue.use(VueRouter);

// 1. 定义（路由）组件。
// 可以从其他文件 import 进来
import App from './App';
import Hello from './components/Hello';

const Foo = {template: '<div>Foo</div>'};
const Default = {template: '<div>车票预定</div>'};

// 2. 定义路由
// 每个路由应该映射一个组件。 其中"component" 可以是
// 通过 Vue.extend() 创建的组件构造器，
// 或者，只是一个组件配置对象。
// 这里我们将App作为顶级路由,其他采用嵌套路由。
const routes = [
  {
    path: '/', component: App,
    children: [
      {
        path: '',
        component: Default
      },
      {
        path: 'bar',
        component: Hello
      },
      {
        path: 'foo',
        component: Foo
      }
    ]
  }
];

// 3. 创建 router 实例，然后传 `routes` 配置
// 你还可以传别的配置参数, 不过先这么简单着吧。
const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
});

// 4. 创建和挂载根实例。
// 记得要通过 router 配置参数注入路由，
// 从而让整个应用都有路由功能
const app = new Vue({
  router
}).$mount('#app');
