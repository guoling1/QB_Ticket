/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  title: '中华万年历',
  router: {
    'ticketReserve': '车票预定',
    'ticketRob': '抢票',
    'ticketPrivate': '私人定制',
    'ticketOrder': '我的订单'
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