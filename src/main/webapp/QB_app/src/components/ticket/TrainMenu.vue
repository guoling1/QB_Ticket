<template lang="html">
  <div class="menu flex-box-column flexBox">
    <router-view></router-view>

    <div class="bottom">
      <div class="btn" v-bind:class="{active:$selectTime}" @click="timer">
        <div class="icon icon-time"></div>
        <p id="form">出发 早→晚</p>
      </div>
      <div class="btn" v-bind:class="{active:$select}" @click="trains">
        <div class="icon icon-quick"></div>
        <p>只看高铁动车</p>
      </div>
      <div class="btn" :class="{active:$ticket}" @click="ticketFn">
        <div class="icon icon-still"></div>
        <p>只看有票</p>
      </div>
      <div class="btn" :class="" @click="open">
        <div class="icon icon-screen"></div>
        <p>综合筛选</p>
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
        pathName: this.$route.name,
        select:!this.$store.state.screen.config.trains.D&&!this.$store.state.screen.config.trains.G,
        selectTime:!this.$store.state.screen.config.sort,
        ticket:!this.$store.state.screen.config.ticket
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
      },
      //只看高铁动车
      trains:function(){
        var obj={D: false,G: false,Z: true,K: true};
        var obj1={D: true,G: true,Z: true,K: true};
        this.$data.select=!this.$data.select;
        if(this.$data.select){
          this.$store.state.screen.config.trains=obj;
        }else {
          this.$store.state.screen.config.trains=obj1;
        }
      },
      //出发早晚
      timer:function(){
        this.$data.selectTime=!this.$data.selectTime;
        if(this.$data.selectTime){
          this.$store.state.screen.config.sort=false;
          document.querySelector("#form").innerHTML="历时 短→长"
        }else {
          this.$store.state.screen.config.sort=true
          document.querySelector("#form").innerHTML="出发 早→晚"
        }
      },
      //是否有票
      ticketFn:function () {
        this.$data.ticket=!this.$data.ticket;
        if(this.$data.ticket){
          this.$store.state.screen.config.ticket=false;
        }else {
          this.$store.state.screen.config.ticket=true;
        }
      }
    },
    computed:{
      $select:function (){
        return this.$data.select
      },
      $selectTime:function(){
        return this.$data.selectTime
      },
      $ticket:function(){
        return this.$data.ticket
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
    .btn.active {
      p{
        color: #1ca0e2;
      }
      .icon.icon-time {
        background-image: url("../../assets/time-active.png");
        //background-size: 16px 16px;
      }
      .icon.icon-quick {
        background-image: url("../../assets/quick-active.png");
        //background-size: 16px 16px;
      }
      .icon.icon-still {
        background-image: url("../../assets/still-active.png");
        //background-size: 16px 16px;
      }
      .icon.icon-screen {
        background-image: url("../../assets/screen-active.png");
        //background-size: 16px 16px;
      }
    }
  }
</style>
