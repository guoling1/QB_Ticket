/**
 * Created by administrator on 16/11/2.
 */

const state = {
  ctrl: false,
  scope: {
    stationONE: {
      code: 'BJP',
      station: '北京'
    },
    stationTWO: {
      code: 'SZQ',
      station: '深圳'
    },
    stationTHREE: {
      code: 'BJP',
      station: '北京'
    },
    stationFOUR: {
      code: '0',
      station: '选择城市'
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
    if (obj.code) {
      state.scope[state.now].code = obj.code;
      state.scope[state.now].station = obj.station;
    }
  }
};

export default {
  state,
  mutations
}
