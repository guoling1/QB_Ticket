
!重点:区分每一种API的使用场景,优缺点,尽可能选择最合适的方式

命名规范

vue 内置命名

  this 指向实例化的组件本身

  $data 指向this的数据

  $http/http 指向vue-resource

  $store/store 指向vuex

  $router/$route 指向vue-router


自定义指令

  指令都以 v- 开头 所以不需要特别注意 小写 _连接 意思明确即可

自定义过滤器

  过滤器 模版中 | 后使用 小写 _连接 意思明确即可

自定义插件

  全局方法 以 __ 开头表示区分

  实例方法 以 _$ 开头表示区分

计算属性 computed

  以 $$ 开头 主要用于监听页面数据变化

  data的数据不直接输出到页面上,必须经过计算属性 computed 转一次

方法 methods 数据 data

  不需要特殊处理 正常的驼峰命名 意思明确即可

beforeRouteEnter 在这个项目中弃用
采用 beforeCreate 代替 ( beforeCreate 内可以使用this,不再需要全局变量Vue)
同时理解 vue 的生命周期 了解 beforeCreate created beforeMount mounted 的区别
了解其他生命周期钩子函数 beforeUpdate updated activated deactivated beforeDestroy destroyed

[store的使用]

store 分类使用

  组件/模版 控制器 model_ctrl

[data filter computed]

* 基础数据 data 存储页面存储页面传递,ajax请求的数据(尽量避免中文/会被url转码的符号)
* 过滤器 filter 主要用于处理页面展示数据时的转换
* 计算属性 computed 主要处理数据变化(保证引起数据变化的点唯一) 或者就直接return,相当于监控data的变化
