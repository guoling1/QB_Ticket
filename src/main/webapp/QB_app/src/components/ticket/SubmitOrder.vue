<template lang="html">
  <div class="main">
    <div class="date-time">
      <div class="btn left dis">前一天</div>
      <div class="middle">10月28日 周五</div>
      <div class="btn right">后一天</div>
    </div>
    <div class="space">
      <div class="left"></div>
      <div class="middle"></div>
      <div class="right"></div>
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

  .space {
    padding: 0 15px;
    background-color: #FFF;
    margin-bottom: 5px;
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
