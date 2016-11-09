/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  title: '中华万年历',
  router: {
    'ticketReserve': '车票预定',
    'ticketRob': '抢票',
    'ticketRobOrder': '确认订单',
    'ticketRobDetail': '抢票详情',
    'ticketPrivate': '私人定制',
    'ticketOrder': '我的订单',
    'ticketSubmitOrder': '提交订单',
    'ticketSureOrder': '确认订单',
    'ticketPayOrder': '订单详情',
    'ticketTrain': '北京 → 深圳',
    'ticketContacts': '常用联系人',
    'ticketLogin': '12306登录'
  }
};

// mutations
const mutations = {
  TITLE_CHANGE (state, name) {
    state.title = state.router[name];
  }
};

export default {
  state,
  mutations
}
