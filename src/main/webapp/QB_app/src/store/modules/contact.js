/**
 * Created by administrator on 16/11/9.
 */

const state = {
  ctrl: false,
  scope: {}
};

const mutations = {
  CONTACT_OPEN (state, obj) {
    state.ctrl = obj.ctrl;
  },
  CONTACT_CLOSE (state, obj) {
    state.ctrl = obj.ctrl;
  }
};

export default {
  state,
  mutations
}
