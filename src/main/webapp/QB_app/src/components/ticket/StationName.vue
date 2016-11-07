<template lang="html">
  <transition name="fade">
    <div v-if="show" class="main">
      <div v-search-station="{closeEvent:close}"></div>
      <div @click="close">取消车站选择组件</div>
    </div>
  </transition>
</template>

<script lang="babel">
  export default {
    name: 'Datetime',
    data () {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },
    methods: {
      close: function (code, station) {
        this.$store.commit('STATION_CLOSE', {
          ctrl: false,
          code: code,
          station: station
        });
      }
    },
    computed: {
      show () {
        return this.$store.state.station.ctrl
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
    overflow: hidden;
    .flexItem(1, 100%);
    background-color: #EEE;
  }
</style>
