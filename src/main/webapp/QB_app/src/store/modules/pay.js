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
    // 存储支付数据
    state.pay.pay_form = data;
    // 唤起支付
    state.pay.show = true;
    // 跳过支付方式选择,直接唤起快捷支付
    //state.pay.methodPayment = true;
    state.quickPayment.show = true;
  },
  PAY_CLOSE (state) {
    state.pay.show = false;
    state.quickPayment.show = false;
  },
  QUICK_PAYMENT_CALL (state) {
    state.quickPayment.show = true;
  }
};

export default {
  state,
  mutations
}
