<template lang="html">
  <div class="main">

  </div>
</template>
<script lang="babel">
  import Vue from 'vue';
  export default {
    name: 'menu',
    data () {
      return {
        name: '',
        massages: [],
        selected: {},
        people: '',
        $index: '',
        keepID: [],
        uid: '',
        appid: ''
      }
    },
    computed: {},
    methods: {}
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
    background-color: #f5f5f5;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 99;
  }
</style>
