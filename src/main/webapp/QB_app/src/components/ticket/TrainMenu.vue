<template lang="html">
  <div class="menu flex-box-column flexBox">
    <router-view></router-view>

    <div class="bottom">
      <div class="btn" :class="">
        <div class="icon icon-time"></div>
        <p>出发 早→晚</p>
      </div>
      <div class="btn" :class="">
        <div class="icon icon-quick"></div>
        <p>只看高铁动车</p>
      </div>
      <div class="btn" :class="">
        <div class="icon icon-still"></div>
        <p>只看有票</p>
      </div>
      <div class="btn" :class="" @click="open">
        <div class="icon icon-screen"></div>
        <p>综合筛选1</p>
      </div>
    </div>
    <screen></screen>
  </div>
</template>

<script lang="babel">
  import Screen from './Screen.vue';
  export default {
    name: 'menu',
    components: {
      Screen
    },
    data () {
      return {
        pathName: this.$route.name
      }
    },
    watch: {
      $route: function (val) {
        this.$data.pathName = val.name;
      }
    },
    methods: {
      open: function () {
        this.$store.commit('SCREEN_OPEN', {
          ctrl: true
        });
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
    height: 49px;
    background-color: #FFF;
    border-top: 1px solid #ebebeb;
    .btn {
      float: left;
      width: 25%;
      height: 49px;
      text-align: center;
      display: block;
      .icon {
        margin: 10px auto 0;
        &.icon-time {
          width: 16px;
          height: 16px;
          background-image: url("../../assets/time.png");
          background-size: 16px 16px;
        }
        &.icon-quick {
          width: 19px;
          height: 15px;
          background-image: url("../../assets/quick.png");
          background-size: 19px 15px;
        }
        &.icon-still {
          width: 20px;
          height: 15px;
          background-image: url("../../assets/still.png");
          background-size: 20px 15px;
        }
        &.icon-screen {
          width: 16px;
          height: 16px;
          background-image: url("../../assets/screen.png");
          background-size: 16px 16px;
        }
      }
      p {
        font-size: 11px;
        color: #585858;
        margin-top: 5px;
      }
    }
  }
</style>
