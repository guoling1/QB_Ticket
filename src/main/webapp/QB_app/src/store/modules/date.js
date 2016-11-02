/**
 * Created by administrator on 16/11/2.
 */

const state = {
  openCtrl: false
};

const mutations = {
  TIME_CTRL (state, n) {
    state.openCtrl = n;
  }
};

export default {
  state,
  mutations
}
