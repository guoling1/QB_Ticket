<template lang="html">
  <div class="main">
    <div class="date-time">
      <div class="btn left dis">前一天</div>
      <div class="middle">10月28日 周五</div>
      <div class="btn right">后一天</div>
    </div>
    <div class="space">
      <div class="left">
        <div class="time">07:10</div>
        <div class="place">北京西</div>
        <div class="date">11-20 周五</div>
      </div>
      <div class="middle">
        <div class="trains">G208</div>
        <div class="ch"></div>
        <div class="date">耗时1小时52分</div>
      </div>
      <div class="right">
        <div class="time">17:10</div>
        <div class="place">北京东</div>
        <div class="date">11-20 周五</div>
      </div>
    </div>
    <div class="space no-padding">
      <div class="group">
        <div class="left">二等座</div>
        <div class="left"><span class="active">128.5</span>元</div>
        <div class="left"><span>321</span>张</div>
        <div class="right blue">预定</div>
      </div>
      <div class="group">
        <div class="left">一等座</div>
        <div class="left"><span>128.5</span>元</div>
        <div class="left"><span>321</span>张</div>
        <div class="right blue">预定</div>
      </div>
      <div class="group no-border">
        <div class="left">商务座</div>
        <div class="left"><span>128.5</span>元</div>
        <div class="left"><span>0</span>张</div>
        <div class="right red">抢票</div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'menu',
    data () {
      return {
        msg: 'Welcome to Your Vue.js App'
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

  .group {
    width: 100%;
    height: 49px;
    padding: 0 15px;
    border-bottom: 1px solid #ebebeb;
    &.no-border {
      border: none;
    }
    .left {
      float: left;
      margin-right: 30px;
      font-size: 15px;
      line-height: 49px;
      color: #000;
      span.active {
        color: #f44848;
      }
    }
    .right {
      border-radius: 3px;
      padding: 6px 18px;
      font-size: 12px;
      color: #FFF;
      margin-top: 10px;
      &.blue {
        background-color: #4ab9f1;
      }
      &.red {
        background-color: #fe6969;
      }
      float: right;
    }
  }

  .space {
    padding: 20px 15px;
    background-color: #FFF;
    margin-bottom: 5px;
    overflow: hidden;
    position: relative;
    &.no-padding {
      padding: 0;
    }
    .left {
      float: left;
    }
    .middle {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      text-align: center;
      margin-top: 24px;
      .ch {
        width: 105px;
        height: 4px;
        margin: 5px auto 10px;
        background: url("../../assets/ch.png") no-repeat center;
        background-size: 105px 4px;
      }
    }
    .right {
      float: right;
    }
    .time {
      font-size: 18px;
      color: #111;
      font-weight: bold;
    }
    .place {
      font-size: 20px;
      color: #111;
      font-weight: bold;
    }
    .date {
      font-size: 12px;
      color: #999;
    }
    .trains {
      font-size: 18px;
      color: #1ca0e2;
      font-weight: bold;
    }
  }

  .date-time {
    width: 100%;
    height: 45px;
    background-color: #4ab9f1;
    position: relative;
    .middle {
      width: 122px;
      height: 25px;
      border: 1px solid #84d0f6;
      border-radius: 3px;
      position: absolute;
      top: 10px;
      left: 50%;
      margin-left: -61px;
      background: url("../../assets/calendar.png") no-repeat 100px 3px;
      background-size: 16px 16px;
      font-size: 12px;
      color: #FFF;
      line-height: 25px;
      text-align: left;
      font-weight: bold;
      padding-left: 10px;
    }
    .btn {
      padding: 0 15px;
      font-size: 12px;
      color: #FFF;
      line-height: 45px;
      &.left {
        float: left;
      }
      &.right {
        float: right;
      }
      &.dis {
        color: #91daff;
      }
    }
  }
</style>
