<template lang="html">
  <div class="main">
    <div class="from">
      <div class="goto">
        <div class="side show">
          <div class="left">出发城市</div>
          <div class="right">到达城市</div>
        </div>
        <div class="side write">
          <div class="left" @click="station">北京</div>
          <img class="middle" src="../../assets/exchange-blue.png">

          <div class="right" @click="station">上海</div>
        </div>
      </div>
      <div class="group" @click="time('dateONE')">
        <div class="prompt">出发日期</div>
        <div class="write">{{dateONE}}</div>
      </div>
      <div class="group" @click="time('dateONE')">
        <div class="prompt">指定车次</div>
        <div class="write empty">{{dateONE}}</div>
      </div>
      <div class="group" @click="time('dateONE')">
        <div class="prompt">坐席类型</div>
        <div class="write empty">{{dateONE}}</div>
      </div>
      <div class="group" @click="time('dateONE')">
        <div class="prompt">抢票时效</div>
        <div class="write empty">{{dateONE}}</div>
      </div>
      <div class="group" @click="time('dateONE')">
        <div class="prompt">抢票套餐</div>
        <div class="write empty no-prompt">{{dateONE}}</div>
      </div>
      <div class="group no-border" @click="time('dateONE')">
        <div class="prompt">抢票成功率约</div>
        <div class="write red empty no-prompt">99%</div>
      </div>
      <router-link class="submit" to="/ticket/rob-order">下一步</router-link>
      <div class="know">抢票须知<span></span></div>
    </div>
    <datetime></datetime>
  </div>
</template>

<script lang="babel">
  import Datetime from './Datetime.vue';
  export default {
    name: 'menu',
    components: {
      Datetime
    },
    data: function () {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },
    methods: {
      time: function (name) {
        this.$store.commit('TIME_OPEN', {
          name: name,
          ctrl: true
        });
      },
      station: function () {
        this.$store.commit('STATION_CTRL', true);
      }
    },
    computed: {
      dateONE () {
        return this.$store.state.date.scope.dateONE.time;
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

  .main {
    width: 100%;
    height: 100%;
    overflow: hidden;
    .flexItem(1, 100%);
  }

  .banner {
    width: 100%;
    img {
      width: 100%;
    }
  }

  .from {
    padding: 0 15px;
    .group {
      height: 56px;
      border-bottom: 1px solid #efefef;
      &.no-border {
        border: none;
      }
      .prompt {
        float: left;
        font-size: 16px;
        color: #999;
        line-height: 56px;
        margin-right: 18px;
      }
      .write {
        &.empty {
          color: #CCC;
        }
        &.no-prompt {
          background: none;
        }
        &.red {
          color: #fe6969;
        }
        line-height: 56px;
        font-size: 16px;
        color: #111;
        text-align: left;
        background: url("../../assets/prompt-arrow.png") no-repeat right;
        background-size: 7px 12px;
      }
    }

  }

  .goto {
    padding: 14px 0 6px;
    border-bottom: 1px solid #efefef;
    .side {
      overflow: hidden;
      &.show {
        font-size: 12px;
        color: #999;
      }
      &.write {
        margin-top: 9px;
        font-size: 24px;
        font-weight: bold;
        color: #111;
        text-align: center;
        .middle {
          width: 27px;
          height: 27px;
          margin-top: 4px;
        }
      }
      .left {
        float: left;
      }
      .right {
        float: right;
      }
    }
  }

  .submit {
    display: block;
    width: 100%;
    height: 47px;
    line-height: 47px;
    border-radius: 3px;
    background-color: #4ab9f1;
    font-size: 18px;
    font-weight: bold;
    color: #FFF;
  }

  .know {
    margin-top: 15px;
    font-size: 12px;
    color: #c1c1c1;
    line-height: 24px;
    float: right;
    span {
      display: block;
      width: 13px;
      height: 13px;
      float: right;
      margin-top: 5px;
      margin-left: 5px;
      background: url('../../assets/know.png') no-repeat;
      background-size: 13px 13px;
    }
  }
</style>
