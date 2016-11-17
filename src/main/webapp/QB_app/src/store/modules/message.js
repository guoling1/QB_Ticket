/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  message: false,
  delay: false,
  accord: false
};

// mutations
const mutations = {
  MESSAGE_DELAY (state, obj) {
    state.delay = obj.ctrl;
  },
  MESSAGE_ACCORD (state, obj) {
    state.accord = obj.ctrl;
  }
};

export default {
  state,
  mutations
}
