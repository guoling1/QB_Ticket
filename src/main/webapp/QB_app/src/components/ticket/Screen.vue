<template lang="html">
  <transition name="fade">
    <div class="main flex-box-column flexBox" v-show="show">
      <div class="result flex-box-column flexBox">
        <div class="week" @click="enter">
          <div class="weekend">日</div>
          <div>一</div>
          <div>二</div>
          <div>三</div>
          <div>四</div>
          <div>五</div>
          <div class="weekend">六</div>
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
        screenConfig: this.$store.state.screen.config,
        pageConfig: {
          table: {
            edz: true,
            ydz: true,
            swz: true,
            tdz: true,
            yz: true,
            yw: true,
            rz: true,
            rw: true,
            wz: true,
            dw: true
          },
          trains: {
            D: true,
            G: true,
            Z: true,
            K: true
          },
          // 用 0,1,2,3,4分别表示5个时间段,0表示全部
          startTime: '0',
          endTime: '0'
        }
      }
    },
    methods: {
      enter: function () {
        // 根据pageConfig得出新的config,然后修改store数据
        let newConfig = {};
        // 必须整体重新赋值 config
        this.$store.commit('SCREEN_CLOSE', {
          ctrl: false,
          config: newConfig
        });
      },
      // 重置数据
      reset: function () {
        // 根据pageConfig得出新的config,然后修改store数据
        let newConfig = {};
        // 必须整体重新赋值 config
        this.$store.commit('SCREEN_CLOSE', {
          ctrl: false,
          config: newConfig
        });
      }
    },
    computed: {
      show () {
        return this.$store.state.screen.ctrl
      },
      page () {
        return this.$data.pageConfig
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
