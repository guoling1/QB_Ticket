/**
 * Created by administrator on 16/11/9.
 */

const state = {
  ctrl: false,
  config: {
    table: {
      edz: true,
      ydz: true,
      swz: true,
      tdz: true,
      yz: true,
      yw: true,
      rz: true,
      rw: true,
      wz: true,
      dw: true
    },
    trains: {
      D: true,
      G: true,
      Z: true,
      K: true
    },
    startTime: '',
    endTime: ''
  }
};

const mutations = {
  SCREEN_OPEN (state, obj) {
    state.ctrl = obj.ctrl;
  },
  SCREEN_CLOSE (state, obj) {
    state.ctrl = obj.ctrl;
    state.config = {};
  }
};

export default {
  state,
  mutations
}
