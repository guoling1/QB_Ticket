/**
 * Created by administrator on 16/11/2.
 */

const now = function () {
  let date = new Date();
  let weekWord = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
  return {
    time: date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日 ' + weekWord[date.getDay()],
    code: date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate()
  }
};

const state = {
  ctrl: false,
  scope: {
    dateONE: {
      time: now().time,
      code: now().code
    },
    dateTWO: {
      time: now().time,
      code: now().code
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
    state.scope[state.now].code = obj.code;
    state.scope[state.now].time = obj.time;
  }
};

export default {
  state,
  mutations
}
