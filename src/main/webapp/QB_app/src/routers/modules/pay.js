/**
 * Created by administrator on 16/11/2.
 */

import App from '../../App';
// 支付 组件 紧密关联账户组件,注意留下接口,方便修改
// 按需加载 1,组件单独打包
// const payMenu = resolve => require(['../../components/pay/Menu'], resolve);
// 按需加载 2.组件分组打包
const payMain = r => require.ensure([], () => r(require('../../components/pay/Main')), 'group-pay');
const payFirstAdd = r => require.ensure([], () => r(require('../../components/pay/FirstAdd')), 'group-pay');
const paySecondAdd = r => require.ensure([], () => r(require('../../components/pay/SecondAdd')), 'group-pay');

export default {
  path: '/pay',
  redirect: '/pay/first-add',
  component: App,
  children: [
    {
      path: 'first-add',
      name: 'firstAdd',
      component: payFirstAdd
    },
    {
      path: 'second-add',
      name: 'secondAdd',
      component: paySecondAdd
    }
  ]
}
