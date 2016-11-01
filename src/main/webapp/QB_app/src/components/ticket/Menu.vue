<template lang="html">
  <div class="menu flex-box-column flexBox">
    <router-view></router-view>

    <div class="bottom">
      <div class="btn">车票预定</div>
      <div class="btn">我的订单</div>
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

  .menu {
    .flexItem(1, 100%);
  }

  .bottom {
    width: 100%;
    height: 50px;
    .btn {
      float: left;
      width: 50%;
      height: 50px;
      line-height: 50px;
      text-align: center;
    }
  }
</style>
