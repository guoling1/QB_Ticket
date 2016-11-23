/**
 * Created by administrator on 16/11/17.
 */

import Vue from 'vue'

Vue.filter('minute2time', function (minute) {
  if (minute < 60) {
    return '0小时' + minute + '分钟';
  } else {
    let H = parseInt(minute / 60);
    let M = minute % 60;
    return H + '小时' + M + '分钟';
  }
});
