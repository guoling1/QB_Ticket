<template lang="html">
  <transition name="fade">
    <div v-if="show" class="main">
      这是时间选择的组件
      <div @click="close">取消时间选择组件</div>
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
      close: function () {
        this.$store.commit('TIME_CTRL', false);
      }
    },
    computed: {
      show () {
        return this.$store.state.date.openCtrl
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
