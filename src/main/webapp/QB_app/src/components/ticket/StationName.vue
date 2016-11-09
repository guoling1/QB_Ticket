<template lang="html">
  <transition name="fade">
    <div class="main" v-show="show">
      <div class="float flex-box-column flexBox">
        <div class="header">
          <div class="close" @click='close'></div>
          <input type="text" placeholder="城市、车站的中文或拼音" @input='input'>
        </div>
        <div class="result flexBox" v-show="result">
          <div class="empty" v-show="empty">很抱歉,没有查询到您需要的信息</div>
          <ul v-show="!empty">
            <li v-for="st in search" @click="close($event,st.code,st.city)">
              {{st.city}}
            </li>
          </ul>
        </div>
      </div>
      <div class="hot">
        <div class="t">热门</div>
        <ul>
          <li>北京</li>
          <li>上海</li>
          <li>广州</li>
          <li>深圳</li>
        </ul>
      </div>
      <div class="shortcut">
        <div class="i">A</div>
        <ul>
          <li>安顺</li>
          <li>安阳</li>
        </ul>
        <div class="i">B</div>
        <ul>
          <li>霸州</li>
        </ul>
      </div>
    </div>
  </transition>
</template>

<script lang="babel">
  // 车站信息解析
  const station_names = require('../../station_name');
  const stationList = {};
  for (let i = 0; i < station_names.split('@').length; i++) {
    let parsing_middleware = station_names.split('@')[i].split('|');
    stationList[parsing_middleware[5]] = {};
    stationList[parsing_middleware[5]].city = parsing_middleware[1];
    stationList[parsing_middleware[5]].code = parsing_middleware[2];
    stationList[parsing_middleware[5]].fullname = parsing_middleware[3];
    stationList[parsing_middleware[5]].shortname = parsing_middleware[4];
    stationList['length'] = i;
  }
  export default {
    name: 'Datetime',
    data () {
      return {
        $result: false,
        $empty: false,
        $search: []
      }
    },
    methods: {
      close: function (event, code, station) {
        this.$data.$result = false;
        this.$store.commit('STATION_CLOSE', {
          ctrl: false,
          code: code,
          station: station
        });
      },
      input: function (event) {
        let val = event.target.value;
        if (!val || val == "") {
          this.$data.$result = false;
          return;
        }
        let matchList = [];
        // 根据用户输入,进行模糊匹配
        // matchList里只记录id,完成后去重
        for (let i = 0; i < stationList.length; i++) {
          let stationObj = {};
          stationObj.code = stationList[i].code;
          stationObj.city = stationList[i].city;
          if (stationList[i].city.indexOf(val) > -1) {
            matchList.push(stationObj);
            continue;
          }
          if (stationList[i].fullname.indexOf(val) > -1) {
            matchList.push(stationObj);
            continue;
          }
          if (stationList[i].shortname.indexOf(val) > -1) {
            matchList.push(stationObj);
          }
        }
        // 根据匹配结果添加匹配列表
        this.$data.$empty = (matchList.length == 0);
        this.$data.$search = matchList;
        this.$data.$result = true;
      }
    },
    computed: {
      show: function () {
        return this.$store.state.station.ctrl
      },
      result: function () {
        return this.$data.$result
      },
      empty: function () {
        return this.$data.$empty
      },
      search: function () {
        return this.$data.$search
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

  .float {
    width: 100%;
    height: 100%;
    position: absolute;
    z-index: 188;
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
    input {
      width: 80%;
      height: 30px;
      transform: translate3d(0, -5px, 0);
      border: none;
      border-radius: 3px;
      padding-left: 5px;
    }
  }

  .result {
    width: 100%;
    height: 100%;
    .flexItem(1, 100%);
    background-color: #FFF;
    .empty {
      width: 100%;
      text-align: center;
      margin-top: 15px;
    }
    ul {
      width: 100%;
      li {
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 15px;
        text-align: left;
        padding-left: 15px;
        border-bottom: 1px solid #f5f5f5;
      }
      li:last-child {
        border: none;
      }
    }
  }

  .hot {
    width: 100%;
    padding: 15px;
    margin-top: 64px;
    overflow: auto;
    .t {
      font-size: 13px;
      color: #111;
      text-align: left;
    }
    ul {
      li {
        float: left;
        width: 28%;
        height: 40px;
        line-height: 40px;
        text-align: center;
        background-color: #FFF;
        border-radius: 3px;
        margin-top: 10px;
        margin-right: 3%;
      }
    }
  }

  .shortcut {
    .i {
      font-size: 14px;
      color: #111;
      padding-left: 10px;
      text-align: left;
    }
    ul {
      border-top: 1px solid #f5f5f5;
      border-bottom: 1px solid #f5f5f5;
      padding-left: 30px;
      background-color: #FFF;
      li {
        width: 100%;
        height: 45px;
        line-height: 45px;
        text-align: left;
        border-bottom: 1px solid #f5f5f5;
      }
      li:last-child {
        border: none;
      }
    }
  }
</style>
