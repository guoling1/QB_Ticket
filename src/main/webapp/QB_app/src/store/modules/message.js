/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  message: false,
  delay: false,
  accord: false,
  prompt: false,
  text: ''
};

// mutations
let timer;

const mutations = {
  MESSAGE_DELAY_SHOW (state, obj) {
    state.message = true;
    state.delay = true;
    state.text = obj.text;
    setTimeout(function () {
      state.message = false;
      state.delay = false;
    }, 1000);
  },
  MESSAGE_DELAY_HIDE (state) {
    state.message = false;
    state.delay = false;
  },
  MESSAGE_ACCORD_SHOW (state, obj) {
    state.message = true;
    state.accord = true;
    state.text = obj.text;
  },
  MESSAGE_ACCORD_HIDE (state) {
    state.message = false;
    state.accord = false;
  },
  MESSAGE_PROMPT_SHOW (state, obj) {
    clearTimeout(timer);
    state.prompt = true;
    state.text = obj.text;
    timer = setTimeout(function () {
      state.prompt = false;
    }, 1000);
  }
};

export default {
  state,
  mutations
}
