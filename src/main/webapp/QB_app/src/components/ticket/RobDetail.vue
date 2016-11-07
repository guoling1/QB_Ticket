<template lang="html">
  <div class="main">
    <div class="state">
      <i></i>
      <span>正在奋力抢票中...</span>
    </div>
    <div class="space">
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
        <div class="prompt">已选车次</div>
        <div class="write no-prompt right">  K51  T508  G203</div>
      </div>
      <div class="group no-border" @click="time('dateONE')">
        <div class="prompt">已选坐席</div>
        <div class="write no-prompt right"> 硬座  二等座  硬卧</div>
      </div>
    </div>
    <div class="space padding-word">
      <div class="left">
        <div class="big">乘车人</div>
        <div class="small">总金额</div>
      </div>
      <div class="right">
        <div class="list">
          <div>陈杰智<span></span></div>
          <div>刘思思<span>(儿童)</span></div>
        </div>
        <div class="small">￥<span>356.5</span></div>
      </div>
    </div>
    <div class="space">
      <div class="cancel">取消抢票</div>
    </div>
    <router-link class="submit" to="/ticket/train-menu/train">返回</router-link>
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
    background-color: #f5f5f5;
  }

  .state {
    width: 100%;
    height: 46px;
    line-height: 46px;
    text-align: center;
    background-color: #fef0f0;
    border: 1px solid #fed4d4;
    font-size: 15px;
    color: #fe6969;
    i {
      display: inline-block;
      width: 16px;
      height: 16px;
      background: url("../../assets/state.png") no-repeat center;
      background-size: 16px 16px;
      transform: translate3d(0, 2px, 0);
    }
  }

  .cancel {
    width: 100%;
    height: 50%;
    line-height: 50px;
    font-size: 15px;
    color: #1ca0e2;
  }

  .space {
    padding: 0 15px;
    background-color: #FFF;
    border-bottom: 5px solid #f5f5f5;
    overflow: hidden;
    &.no-border {
      border: none;
    }
    &.no-padding {
      padding: 0;
    }
    &.padding-word {
      padding: 8px 15px;
    }
    .left {
      float: left;
      text-align: left;
    }
    .right {
      float: right;
      text-align: right;
    }
    .big {
      line-height: 25px;
      font-size: 15px;
      color: #111;
    }
    .list {
      line-height: 25px;
      font-size: 12px;
      color: #111;
      div {
        float: right;
        margin-left: 5px;
        span {
          color: #999;
        }
      }
    }
    .small {
      line-height: 20px;
      font-size: 12px;
      color: #999;
    }
  }

  .handle {
    height: 56px;
    border-top: 1px solid #efefef;
    border-bottom: 1px solid #efefef;
    position: relative;
    .btn {
      width: 50%;
      height: 56px;
      line-height: 56px;
      font-size: 15px;
      color: #1ca0e2;
      float: left;
      text-align: center;
    }
    .line {
      position: absolute;
      width: 1px;
      height: 56px;
      background-color: #efefef;
      top: 0;
      left: 50%;
    }
  }

  .group {
    height: 56px;
    border-bottom: 1px dashed #efefef;
    &.no-border {
      border: none;
    }
    .prompt {
      float: left;
      font-size: 16px;
      color: #111;
      line-height: 56px;
      margin-right: 18px;
    }
    .logo {
      width: 26px;
      height: 26px;
      float: left;
      margin-top: 14px;
      margin-right: 10px;
      background: url("../../assets/12306.png") no-repeat;
      background-size: 26px 26px;
    }
    .list {
      width: 19px;
      height: 19px;
      float: left;
      margin-top: 18px;
      margin-right: 10px;
      background: url("../../assets/list.png") no-repeat;
      background-size: 19px 19px;
    }
    .write {
      &.empty {
        color: #CCC;
      }
      &.no-prompt {
        background: none;
      }
      &.right {
        text-align: right;
      }
      .info {
        float: right;
        font-size: 12px;
        color: #b6b6b6;
      }
      .name {
        font-weight: bold;
      }
      .price {
        color: #f14242;
        margin-right: 15px;
      }
      line-height: 56px;
      font-size: 16px;
      color: #111;
      text-align: left;
      background: url("../../assets/prompt-arrow.png") no-repeat right;
      background-size: 7px 12px;
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
    height: 49px;
    line-height: 49px;
    background-color: #4ab9f1;
    font-size: 18px;
    font-weight: bold;
    color: #FFF;
    position: fixed;
    bottom: 0;
    left: 0;
  }
</style>
