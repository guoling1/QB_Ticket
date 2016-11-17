<template lang="html">
  <transition name="fade">
    <div class="main" v-if="$$message">
      <div class="delay" v-show="$$delay">
        {{$$text}}
      </div>
      <div class="accord" v-show="$$accord">
        {{$$text}}
      </div>
    </div>
  </transition>
</template>
<script lang="babel">
  import Vue from 'vue';
  export default {
    name: 'message',
    data () {
      return {
        delay: false,
        accord: false
      }
    },
    methods: {},
    computed: {
      $$message: function () {
        return this.$store.state.message.message
      },
      $$delay: function () {
        return this.$store.state.message.delay
      },
      $$accord: function () {
        return this.$store.state.message.accord
      },
      $$text: function () {
        return this.$store.state.message.text
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
    height: 100%;
    overflow: auto;
    .flexItem(1, 100%);
    position: absolute;
    top: 0;
    left: 0;
    z-index: 9999;
    background: rgba(0, 0, 0, 0.5);
  }

  .delay {
    width: 90%;
    height: auto;
    min-height: 100px;
    background-color: #FFF;
    border-radius: 5px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
    font-size: 18px;
    color: #111;
    line-height: 30px;
    padding: 35px 0;
  }
</style>
