<template lang="html">
  <div id="app">
    <h1>{{title}}</h1>

    <p>
      <router-link to="/">Go to Index</router-link>
      <router-link to="/foo">Go to Foo</router-link>
      <router-link to="/bar">Go to Bar</router-link>
    </p>
    <router-view></router-view>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'app',
    data: function () {
      return {
        title: '车票预订--title'
      }
    }
  }
</script>

<style lang="less">
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
</style>
