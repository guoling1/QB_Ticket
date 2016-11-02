/**
 * Created by administrator on 16/11/2.
 */

const state = {
  ctrl: false,
  scope: {
    dateONE: {
      time: '12月20日 周六'
    },
    dateTWO: {
      time: '12月20日 周六'
    }
  },
  now: ''
};

const mutations = {
  TIME_OPEN (state, obj) {
    state.now = obj.name;
    state.ctrl = obj.ctrl;
  },
  TIME_CLOSE (state, obj) {
    state.ctrl = obj.ctrl;
    state.scope[state.now].time = obj.time;
  }
};

export default {
  state,
  mutations
}
