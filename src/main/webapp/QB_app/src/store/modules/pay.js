/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  pay: {
    show: false, // 全局唤醒支付模版
    methodPayment: false, // 唤醒支付方式选择
    pay_form: {}
  },
  quickPayment: {
    show: false // 唤醒快捷支付
  }
};

// mutations
const mutations = {
  PAY_CALL (state, data) {
    state.pay.show = true;
    //state.pay.methodPayment = true;
    state.pay.pay_form = data;
  },
  QUICK_PAYMENT_CALL (state) {
    state.quickPayment.show = true;
  }
};

export default {
  state,
  mutations
}
