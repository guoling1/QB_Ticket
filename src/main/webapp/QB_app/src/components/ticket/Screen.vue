<template lang="html">
  <transition name="fade">
    <div class="main flex-box-column flexBox" v-show="show">
      <div class="header">
        <div class="close" @click='close'></div>
        <h1>日期选择</h1>
      </div>
      <div class="result flex-box-column flexBox">
        <div class="week">
          <div class="weekend">日</div>
          <div>一</div>
          <div>二</div>
          <div>三</div>
          <div>四</div>
          <div>五</div>
          <div class="weekend">六</div>
        </div>
        <div class="day">
          <div class="module" v-for="month in calendar">
            <div>{{month.year}}年{{month.month}}月</div>
            <ul>
              <li v-for="(day,index) in month.days" v-if="index < month.firstDay"></li>
              <li v-for="(day,index) in month.days" @click="close(month.year,month.month,index+1)"
                  v-bind:class="{lost:!day.active,weekend:day.weekend}">{{day.day}}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script lang="babel">

  export default {
    name: 'Datetime',
    data () {
      return {
        datetime: '今天'
      }
    },
    methods: {
      close: function () {
        this.$store.commit('SCREEN_CLOSE', {
          ctrl: false
        });
      }
    },
    computed: {
      show () {
        return this.$store.state.screen.ctrl
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
    background-color: #f5f5f5;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 99;
  }
</style>
