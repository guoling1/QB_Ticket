/**
 * Created by administrator on 16/11/2.
 */

import Vue from 'vue'
import Vuex from 'vuex'
//import * as actions from './actions'
//import * as getters from './getters'
import title from './modules/title'
import date from './modules/date'
import station from './modules/station'
import screen from './modules/screen'

Vue.use(Vuex);

export default new Vuex.Store({
  //actions,
  //getters,
  modules: {
    title,
    date,
    station,
    screen
  }
})
