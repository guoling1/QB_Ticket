/**
 * Created by administrator on 16/11/2.
 */

import Vue from 'vue'

// 定义全局指令
import stationSearch from './stationSearch'

// 全局火车站搜索指令
Vue.directive('search-station', stationSearch);
