<template lang="html">
  <div class="main">
    <div class="delay">
      这是全局message,延时自动消失{{$$delay}}
    </div>
    <div class="accord">
      这是全局message,需要点击消失
    </div>
  </div>
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
      $$delay:function(){
        return this.$store.state.message.delay
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
    overflow: auto;
    .flexItem(1, 100%);
    position: absolute;
    top: 0;
    left: 0;
    z-index: 9999;
  }

  .delay {

  }
</style>
