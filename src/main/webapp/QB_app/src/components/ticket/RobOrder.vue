<template lang="html">
  <div class="main">
    <div class="from">
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
          <div class="write no-prompt right">{{dateONE}}</div>
        </div>
        <div class="group no-border" @click="time('dateONE')">
          <div class="prompt">已选坐席</div>
          <div class="write no-prompt right empty">{{dateONE}}</div>
        </div>
      </div>
      <div class="space">
        <div class="group no-border" @click="time('dateONE')">
          <div class="logo"></div>
          <div class="write">使用12306账号登录</div>
        </div>
      </div>
      <div class="space no-border">
        <div class="group no-border" @click="time('dateONE')">
          <div class="list"></div>
          <div class="write">
            <span class="name">成龙</span>
            120********1234
            <span class="info">成人票</span>
          </div>
        </div>
        <div class="group no-border" @click="time('dateONE')">
          <div class="list"></div>
          <div class="write">
            <span class="name">成凤</span>
            120********4321
            <span class="info">成人票</span>
          </div>
        </div>
      </div>
      <div class="space no-padding">
        <div class="handle">
          <div class="btn">添加/编辑乘客</div>
          <div class="line"></div>
          <div class="btn">添加儿童</div>
        </div>
      </div>
      <div class="space">
        <div class="group no-border" @click="time('dateONE')">
          <div class="prompt">套餐类型</div>
          <div class="write empty">{{dateONE}}</div>
        </div>
      </div>
      <div class="space">
        <div class="group no-border" @click="time('dateONE')">
          <div class="prompt">联系手机</div>
          <div class="write no-prompt empty">通知出票信息</div>
        </div>
      </div>
      <router-link class="submit" to="/ticket/train-menu/train">立即抢票</router-link>
    </div>
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

  .from {
    .space {
      padding: 0 15px;
      background-color: #FFF;
      border-bottom: 5px solid #f5f5f5;
      &.no-border {
        border: none;
      }
      &.no-padding {
        padding: 0;
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
