/**
 * Created by administrator on 16/11/2.
 */

import App from '../../App';
// 应用 火车票 流程组件
const ticketMainMenu = r => require.ensure([], () => r(require('../../components/ticket/MainMenu')), 'group-ticket');
const ticketReserve = r => require.ensure([], () => r(require('../../components/ticket/Reserve')), 'group-ticket');
const ticketRob = r => require.ensure([], () => r(require('../../components/ticket/Rob')), 'group-ticket');
const ticketRobOrder = r => require.ensure([], () => r(require('../../components/ticket/RobOrder')), 'group-ticket');
const ticketRobDetail = r => require.ensure([], () => r(require('../../components/ticket/RobDetail')), 'group-ticket');
const ticketPrivate = r => require.ensure([], () => r(require('../../components/ticket/Private')), 'group-ticket');
const ticketOrder = r => require.ensure([], () => r(require('../../components/ticket/Order')), 'group-ticket');
const ticketTrainMenu = r => require.ensure([], () => r(require('../../components/ticket/TrainMenu')), 'group-ticket');
const ticketTrain = r => require.ensure([], () => r(require('../../components/ticket/Train')), 'group-ticket');
const ticketSubmitOrder = r => require.ensure([], () => r(require('../../components/ticket/SubmitOrder')), 'group-ticket');
const ticketSureOrder = r => require.ensure([], () => r(require('../../components/ticket/SureOrder')), 'group-ticket');
const ticketPayOrder = r => require.ensure([], () => r(require('../../components/ticket/PayOrder')), 'group-ticket');
const ticketLogin = r => require.ensure([], () => r(require('../../components/ticket/Login')), 'group-ticket');
const ticketRefundDetail = r => require.ensure([], () => r(require('../../components/ticket/RefundDetail')), 'group-ticket');
const ticketRefundSuccess = r => require.ensure([], () => r(require('../../components/ticket/RefundSuccess')), 'group-ticket');

export default {
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
          path: 'rob',
          name: 'ticketRob',
          component: ticketRob
        },
        {
          path: 'private',
          name: 'ticketPrivate',
          component: ticketPrivate
        },
        {
          path: 'order',
          name: 'ticketOrder',
          component: ticketOrder
        }
      ]
    },
    {
      path: 'refund-detail',
      name: 'ticketRefundDetail',
      component: ticketRefundDetail
    },
    {
      path: 'refund-success',
      name: 'ticketRefundSuccess',
      component: ticketRefundSuccess
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
    },
    {
      path: 'rob-order',
      name: 'ticketRobOrder',
      component: ticketRobOrder
    },
    {
      path: 'rob-detail',
      name: 'ticketRobDetail',
      component: ticketRobDetail
    },
    {
      path: 'submit-order',
      name: 'ticketSubmitOrder',
      component: ticketSubmitOrder
    },
    {
      path: 'sure-order',
      name: 'ticketSureOrder',
      component: ticketSureOrder
    },
    {
      path: 'pay-order',
      name: 'ticketPayOrder',
      component: ticketPayOrder
    },
    {
      path: 'login',
      name: 'ticketLogin',
      component: ticketLogin
    }
  ]
}
