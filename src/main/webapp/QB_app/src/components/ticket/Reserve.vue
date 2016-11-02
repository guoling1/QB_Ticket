<template lang="html">
  <div class="main">
    <div class="banner">
      <img src="../../assets/banner.png">
    </div>
    <div class="from">
      <div class="goto">
        <div class="side show">
          <div class="left">出发城市</div>
          <div class="right">到达城市</div>
        </div>
        <div class="side write">
          <div class="left">北京</div>
          <img class="middle" src="../../assets/exchange.png">

          <div class="right">上海</div>
        </div>
      </div>
      <div class="time">
        <div class="show">12月20日 周六</div>
      </div>
      <div class="option">
        <div class="show">只看高铁/动车</div>
        <div class="check active">
          <div class="btn"></div>
        </div>
      </div>
      <div class="submit">查询</div>
      <ul class="history">
        <li>查询历史</li>
        <li>清空</li>
      </ul>
    </div>


    <div @click="open">调用时间选择组件</div>
    <datetime></datetime>
    <router-link to="/ticket/train-menu/train">立即预定</router-link>
  </div>
</template>

<script lang="babel">
  import Datetime from './Datetime.vue';
  export default {
    name: 'menu',
    components: {
      Datetime
    },
    data () {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },
    methods: {
      open: function () {
        this.$store.commit('TIME_CTRL', true);
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

  .time {
    height: 56px;
    border-bottom: 1px solid #efefef;
    .show {
      line-height: 56px;
      font-size: 16px;
      color: #111;
      text-align: left;
      font-weight: bold;
    }
  }

  .option {
    height: 56px;
    .show {
      line-height: 56px;
      font-size: 15px;
      color: #999;
      float: left;
    }
    .check {
      width: 50px;
      height: 26px;
      float: right;
      margin-top: 15px;
      border-radius: 13px;
      background-color: #eaeaea;
      padding: 2px;
      .btn {
        width: 22px;
        height: 22px;
        border-radius: 50%;
        background-color: #FFF;
      }
      &.active {
        background-color: #4ab9f1;
        .btn {
          float: right;
        }
      }
    }
  }

  .submit {
    width: 100%;
    height: 47px;
    line-height: 47px;
    border-radius: 3px;
    background-color: #4ab9f1;
    font-size: 18px;
    font-weight: bold;
    color: #FFF;
  }

  .history {
    margin-top: 15px;
    font-size: 12px;
    color: #c1c1c1;
    li {
      float: left;
      line-height: 24px;
      margin-right: 15px;
    }
  }
</style>
