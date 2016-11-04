/**
 * Created by administrator on 16/11/2.
 */

const state = {
  ctrl: false,
  scope: {
    stationONE: {
      station: '北京'
    },
    stationTWO: {
      station: '深圳'
    }
  },
  now: ''
};

const mutations = {
  STATION_OPEN (state, obj) {
    state.now = obj.name;
    state.ctrl = obj.ctrl;
  },
  STATION_CLOSE (state, obj) {
    state.ctrl = obj.ctrl;
    state.scope[state.now].station = obj.station;
  }
};

export default {
  state,
  mutations
}
