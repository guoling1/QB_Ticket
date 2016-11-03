/**
 * Created by administrator on 16/11/2.
 */

import App from '../../App';
// 应用 火车票 流程组件
const ticketMainMenu = r => require.ensure([], () => r(require('../../components/ticket/MainMenu')), 'group-ticket');
const ticketReserve = r => require.ensure([], () => r(require('../../components/ticket/Reserve')), 'group-ticket');
const ticketRob = r => require.ensure([], () => r(require('../../components/ticket/Rob')), 'group-ticket');
const ticketRobOrder = r => require.ensure([], () => r(require('../../components/ticket/RobOrder')), 'group-ticket');
const ticketPrivate = r => require.ensure([], () => r(require('../../components/ticket/Private')), 'group-ticket');
const ticketOrder = r => require.ensure([], () => r(require('../../components/ticket/Order')), 'group-ticket');
const ticketTrainMenu = r => require.ensure([], () => r(require('../../components/ticket/TrainMenu')), 'group-ticket');
const ticketTrain = r => require.ensure([], () => r(require('../../components/ticket/Train')), 'group-ticket');

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
    }
  ]
}
