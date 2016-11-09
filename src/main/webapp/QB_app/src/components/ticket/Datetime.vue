<template lang="html">
  <transition name="fade">
    <div class="main flex-box-column flexBox" v-show="show">
      <div class="header">
        <div class="close" @click='close'></div>
        <h1>日期选择</h1>
      </div>
      <div class="result flex-box-column flexBox">
        <div class="week">
          <div class="weekend">日</div>
          <div>一</div>
          <div>二</div>
          <div>三</div>
          <div>四</div>
          <div>五</div>
          <div class="weekend">六</div>
        </div>
        <div class="day">
          <div class="module" v-for="month in calendar">
            <div>{{month.year}}年{{month.month}}月</div>
            <ul>
              <li v-for="(day,index) in month.days" v-if="index < month.firstDay"></li>
              <li v-for="(day,index) in month.days" @click="close(month.year,month.month,index+1)"
                  v-bind:class="{lost:!day.active,weekend:day.weekend}">{{day.day}}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script lang="babel">
  // 计算日历数据

  // 获取当前月份的数据
  const getNowDate = function () {
    const date = new Date();
    return {
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate(),
      week: date.getDay()
    };
  };

  // 计算 每个月的天数
  const getMaxDayOfMonth = function (year, month) {
    let dayOfEveryMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    if (year % 4 == 0) {
      dayOfEveryMonth[1] = 29;
    }

    return dayOfEveryMonth[month - 1];
  };

  // 计算 每个月第一天是星期几
  const getFirstDayIsWeek = function (year, month) {
    return (new Date(year, month - 1, 1)).getDay();
  };

  // 计算 每个月的日期数组
  const getDayOfMonth = function (year, month) {
    let maxDay = getMaxDayOfMonth(year, month);
    let days = [];
    for (let i = 1; i <= maxDay; i++) {
      let week = (new Date(year, month - 1, i)).getDay();
      let info = {
        weekend: (week == 0 || week == 6),
        active: false,
        day: i
      };
      days.push(info);
    }
    return days;
  };

  // 转换方法
  const transform = function (middleware, now, openTime) {
    let activeDay = 0;
    for (let i = 0; i < middleware.length; i++) {
      for (let m = 0; m < middleware[i]['days'].length; m++) {
        if (middleware[i]['days'][m]['day'] == now || activeDay > 0) {
          middleware[i]['days'][m]['active'] = true;
          activeDay++;
        }
        if (activeDay == openTime)break;
      }
    }
    return middleware;
  };

  // 基础数据/方法
  const openTime = 60; // 火车票预售时间 60天
  // 生成 日历 所需要的的数据 从当前月份开始
  const createCalendar = function (maxMonth) {
    let calendarData = [];
    // 基础数据
    let now = getNowDate();
    for (let i = 0; i < maxMonth; i++) {
      // 变化后的年份 月份
      let middleware = {
        year: now.year,
        month: now.month + i
      };
      if (middleware.month > 12) {
        middleware.year += 1;
        middleware.month -= 12;
      }
      // 计算 月份第一天是星期几
      middleware['firstDay'] = getFirstDayIsWeek(middleware.year, middleware.month);
      middleware['days'] = getDayOfMonth(middleware.year, middleware.month);
      calendarData.push(middleware);
    }
    // 转换 days 添加参数是否激活
    calendarData = transform(calendarData, now.day, openTime);
    return calendarData;
  };

  export default {
    name: 'Datetime',
    data () {
      return {
        datetime: '今天',
        calendar: createCalendar(3)
      }
    },
    methods: {
      close: function (year, month, day) {
        let week = (new Date(year, month - 1, day)).getDay();
        let weekWord = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
        if (day < 10) {
          day = '0' + day;
        }
        this.$store.commit('TIME_CLOSE', {
          ctrl: false,
          code: year + '-' + month + '-' + day,
          time: month + '月' + day + '日 ' + weekWord[week]
        });
      }
    },
    computed: {
      show () {
        return this.$store.state.date.ctrl
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .flexItem(@val,@width:0) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
    width: @width;
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s
  }

  .fade-enter, .fade-leave-active {
    opacity: 0
  }

  .main {
    width: 100%;
    height: 100%;
    overflow: auto;
    .flexItem(1, 100%);
    background-color: #f5f5f5;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 99;
  }

  .header {
    width: 100%;
    height: 64px;
    color: #fefefe;
    background-color: #4ab9f1;
    padding: 30px 10px 0;
    .close {
      width: 19px;
      height: 24px;
      background: url("../../assets/back.png") no-repeat center;
      background-size: 9px 14px;
      float: left;
      padding: 5px;
      position: absolute;
    }
    h1 {
      font-size: 18px;
      line-height: 24px;
    }
  }

  .result {
    width: 100%;
    height: 100%;
    .flexItem(1, 100%);
    background-color: #FFF;
    .week {
      div {
        width: 14.285%;
        height: 24px;
        line-height: 24px;
        font-size: 14px;
        color: #111;
        text-align: center;
        float: left;
        &.weekend {
          color: orange;
        }
      }
    }
    .day {
      width: 100%;
      overflow: auto;
      .module {
        div {
          width: 100%;
          height: 35px;
          line-height: 35px;
          font-size: 15px;
          color: #111;
          text-align: center;
        }
        ul {
          width: 100%;
          overflow: hidden;
          li {
            width: 14.285%;
            height: 35px;
            line-height: 35px;
            font-size: 14px;
            color: #111;
            text-align: center;
            float: left;
            &.weekend {
              color: orange;
            }
            &.lost {
              color: #999;
            }
          }
        }
      }
    }
  }
</style>
