<template lang="html">
  <transition name="fade">
    <div class="main flex-box-column flexBox" v-show="show">
      <div class="result flex-box-column flexBox"></div>
      <div class="bg"></div>
      <div class="body">
        <div class="banner">
          <span @click="reset">重置</span>
          <span class="sure" @click="enter">确定</span>
        </div>
        <div class="content">
          <p>坐席选择</p>
          <ul>
            <li v-bind:class="{active:page.table.edz}" @click="select('edz')">二等座</li>
            <li v-bind:class="{active:page.table.ydz}" @click="select('ydz')">一等座</li>
            <li v-bind:class="{active:page.table.swz}" @click="select('swz')">商务座</li>
            <li v-bind:class="{active:page.table.yz}" @click="select('yz')">硬座</li>
            <li v-bind:class="{active:page.table.rz}" @click="select('rz')">软座</li>
            <li v-bind:class="{active:page.table.yw}" @click="select('yw')">硬卧</li>
            <li v-bind:class="{active:page.table.rw}" @click="select('rw')">软卧</li>
            <li v-bind:class="{active:page.table.wz}" @click="select('wz')">无座</li>
          </ul>
          <p>车次类型</p>
          <ul>
            <li v-bind:class="{active:page.trains.D}" @click="selectA('D')">D 动车</li>
            <li v-bind:class="{active:page.trains.G}" @click="selectA('G')">G/C 高铁</li>
            <li v-bind:class="{active:page.trains.Z}" @click="selectA('Z')">Z 直达特快</li>
            <li v-bind:class="{active:page.trains.K}" @click="selectA('K')">T/K 普通</li>
          </ul>
          <p>出发时间</p>
          <ul>
            <li v-bind:class="{active:page.startTime!=0}" @click="selectB('0')">00:00-24:00</li>
            <li v-bind:class="{active:page.startTime!=1}" @click="selectB('1')">00:00-06:00</li>
            <li v-bind:class="{active:page.startTime!=2}" @click="selectB('2')">06:00-12:00</li>
            <li v-bind:class="{active:page.startTime!=3}" @click="selectB('3')">12:00-18:00</li>
            <li v-bind:class="{active:page.startTime!=4}" @click="selectB('4')">18:00-24:00</li>
          </ul>
          <p>到达时间</p>
          <ul>
            <li v-bind:class="{active:page.endTime!=0}" @click="selectC('0')">00:00-24:00</li>
            <li v-bind:class="{active:page.endTime!=1}" @click="selectC('1')">00:00-06:00</li>
            <li v-bind:class="{active:page.endTime!=2}" @click="selectC('2')">06:00-12:00</li>
            <li v-bind:class="{active:page.endTime!=3}" @click="selectC('3')">12:00-18:00</li>
            <li v-bind:class="{active:page.endTime!=4}" @click="selectC('4')">18:00-24:00</li>
          </ul>
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
          endTime: '0',
          ticket:true,
          sort:true
        }
      }
    },
    methods: {
      enter: function () {
        // 根据pageConfig得出新的config,然后修改store数据
        let newConfig = {};
        newConfig=this.$data.pageConfig;
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
        this.$data.pageConfig=newConfig={
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
          endTime: '0',
          ticket:true,
          sort:true
        };
        // 必须整体重新赋值 config
        this.$store.commit('SCREEN_CLOSE', {
          ctrl: false,
          config: newConfig
        });
      },
      select:function (msg){
        for (var i in this.$data.pageConfig.table){
          if(msg==i){
            this.$data.pageConfig.table[i]=!this.$data.pageConfig.table[i];
            break;
          }
        }
      },
      selectA:function (msg) {
        for (var i in this.$data.pageConfig.trains){
          if(msg==i){
            this.$data.pageConfig.trains[i]=!this.$data.pageConfig.trains[i];
            break;
          }
        }
      },
      selectB:function (msg) {
        this.$data.pageConfig.startTime=msg;
      },
      selectC:function (msg) {
        this.$data.pageConfig.endTime=msg;
      },
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
    transition: opacity .5s;
    opacity: 0.5
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
    z-index: 99;
  }
  .bg{
    width: 100%;
    height: 100%;
    opacity: 0.7;
    background: #000;
  }
  .body{
    width: 100%;
    position: fixed;
    bottom: 0;
    left: 0;
    background: #FFF;

    .banner{
      height: 43px;
      line-height: 43px;
      width: 100%;
      padding:0 15px;
      margin-bottom: 10px;
      font-size: 15px;
      background: #f7f7f7;
      color: #111;
      span{
        float: left;
      }

      .sure{
        float: right;
        color: #2da6e3;
      }
    }
    .content{
      width: 100%;
      padding-left:15px;
      p{
        text-align: left;
        height: 25px;
        line-height: 18px;
        font-size: 12px;
        font-weight: bold;
      }
      ul{
        overflow: hidden;;
        li{
          float: left;
          width: 30%;
          margin-right: 3%;
          margin-bottom: 10px;
          height: 30px;
          line-height: 30px;
          text-align: center;

          border-radius: 2px;
          font-size: 15px;

          color: #2da6e3;
          border: 1px solid #2da6e3;
          &.active{
            border: 1px solid #dadada;
            color: #999;
          }

        }
      }
    }
  }

</style>
