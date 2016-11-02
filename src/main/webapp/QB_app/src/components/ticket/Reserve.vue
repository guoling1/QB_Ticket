<template lang="html">
  <div class="main">
    <div @click="dateShow=true">调用时间选择组件</div>
    <datetime v-bind:show="dateShow"></datetime>
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
        msg: 'Welcome to Your Vue.js App',
        dateShow: false
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
</style>
