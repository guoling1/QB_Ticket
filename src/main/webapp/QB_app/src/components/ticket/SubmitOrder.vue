<template lang="html">
  <div class="main">
    <div class="date-time">
      <!--<div class="btn left dis">前一天</div>-->
      <div class="middle" @click="timer('dateFour')"> {{$$data.dateWeek}}</div>
      <!--<div class="btn right">后一天</div>-->
    </div>
    <div class="space">
      <div class="left">
        <div class="time">{{orderInfo.start_time}}</div>
        <div class="place">{{orderInfo.start_station_name}}</div>
        <div class="date">{{time.startTime.show}}</div>
      </div>
      <div class="middle">
        <div class="trains">{{orderInfo.train_code}}</div>
        <div class="ch"></div>
        <div class="date">耗时{{time.runTime}}</div>
      </div>
      <div class="right">
        <div class="time">{{orderInfo.arrive_time}}</div>
        <div class="place">{{orderInfo.to_station_name}}</div>
        <div class="date">{{time.arriveTime.show}}</div>
      </div>
    </div>
    <div class="space no-padding">
      <div class="group" v-if="orderInfo.edz_num!='--'">
        <div class="left">二等座</div>
        <div class="left"><span v-bind:class="{active:orderInfo.edz_num>0}">{{orderInfo.edz_price}}</span>元</div>
        <div class="left"><span>{{orderInfo.edz_num}}</span>张</div>
        <router-link class="right blue" v-if="orderInfo.edz_num>0"
                     :to="{path:'/ticket/sure-order',query:{appid:common.appid,uid:common.uid,table:'二等座',price:orderInfo.edz_price}}">预定
        </router-link>
        <router-link class="right red" v-if="orderInfo.edz_num<=0"
                     :to="{path:'/ticket/main-menu/rob',query:{appid:common.appid,uid:common.uid}}">抢票</router-link>
      </div>
      <div class="group" v-if="orderInfo.ydz_num!='--'">
        <div class="left">一等座</div>
        <div class="left"><span v-bind:class="{active:orderInfo.edz_num>0}">{{orderInfo.ydz_price}}</span>元</div>
        <div class="left"><span>{{orderInfo.ydz_num}}</span>张</div>
        <router-link class="right blue" v-if="orderInfo.ydz_num>0"
                     :to="{path:'/ticket/sure-order',query:{appid:common.appid,uid:common.uid,table:'一等座',price:orderInfo.ydz_price}}">预定
        </router-link>
        <router-link class="right red" v-if="orderInfo.ydz_num<=0"
                     :to="{path:'/ticket/main-menu/rob',query:{appid:common.appid,uid:common.uid}}">抢票</router-link>
      </div>
      <div class="group" v-if="orderInfo.swz_num!='--'">
        <div class="left">商务座</div>
        <div class="left"><span v-bind:class="{active:orderInfo.swz_num>0}">{{orderInfo.swz_price}}</span>元</div>
        <div class="left"><span>{{orderInfo.swz_num}}</span>张</div>
        <router-link class="right blue" v-if="orderInfo.swz_num>0"
                     :to="{path:'/ticket/sure-order',query:{appid:common.appid,uid:common.uid,table:'商务座',price:orderInfo.swz_price}}">预定
        </router-link>
        <router-link class="right red" v-if="orderInfo.swz_num<=0"
                     :to="{path:'/ticket/main-menu/rob',query:{appid:common.appid,uid:common.uid}}">抢票</router-link>
      </div>
      <div class="group" v-if="orderInfo.yz_num!='--'">
        <div class="left">硬座</div>
        <div class="left"><span v-bind:class="{active:orderInfo.yz_num>0}">{{orderInfo.yz_price}}</span>元</div>
        <div class="left"><span>{{orderInfo.yz_num}}</span>张</div>
        <router-link class="right blue" v-if="orderInfo.yz_num>0"
                     :to="{path:'/ticket/sure-order',query:{appid:common.appid,uid:common.uid,table:'硬座',price:orderInfo.yz_price}}">预定
        </router-link>
        <router-link class="right red" v-if="orderInfo.yz_num<=0"
                     :to="{path:'/ticket/main-menu/rob',query:{appid:common.appid,uid:common.uid}}">抢票</router-link>
      </div>
      <div class="group" v-if="orderInfo.rz_num!='--'">
        <div class="left">软座</div>
        <div class="left"><span v-bind:class="{active:orderInfo.rz_num>0}">{{orderInfo.rz_price}}</span>元</div>
        <div class="left"><span>{{orderInfo.rz_num}}</span>张</div>
        <router-link class="right blue" v-if="orderInfo.rz_num>0"
                     :to="{path:'/ticket/sure-order',query:{appid:common.appid,uid:common.uid,table:'软座',price:orderInfo.rz_price}}">预定
        </router-link>
        <router-link class="right red" v-if="orderInfo.rz_num<=0"
                     :to="{path:'/ticket/main-menu/rob',query:{appid:common.appid,uid:common.uid}}">抢票</router-link>
      </div>
      <div class="group" v-if="orderInfo.yw_num!='--'">
        <div class="left">硬卧</div>
        <div class="left"><span v-bind:class="{active:orderInfo.yw_num>0}">{{orderInfo.yw_price}}</span>元</div>
        <div class="left"><span>{{orderInfo.yw_num}}</span>张</div>
        <router-link class="right blue" v-if="orderInfo.yw_num>0"
                     :to="{path:'/ticket/sure-order',query:{appid:common.appid,uid:common.uid,table:'硬卧',price:orderInfo.yw_price}}">预定
        </router-link>
        <router-link class="right red" v-if="orderInfo.yw_num<=0"
                     :to="{path:'/ticket/main-menu/rob',query:{appid:common.appid,uid:common.uid}}">抢票</router-link>
      </div>
      <div class="group" v-if="orderInfo.rw_num!='--'">
        <div class="left">软卧</div>
        <div class="left"><span v-bind:class="{active:orderInfo.rw_num>0}">{{orderInfo.rw_price}}</span>元</div>
        <div class="left"><span>{{orderInfo.rw_num}}</span>张</div>
        <router-link class="right blue" v-if="orderInfo.rw_num>0"
                     :to="{path:'/ticket/sure-order',query:{appid:common.appid,uid:common.uid,table:'软卧',price:orderInfo.rw_price}}">预定
        </router-link>
        <router-link class="right red" v-if="orderInfo.rw_num<=0"
                     :to="{path:'/ticket/main-menu/rob',query:{appid:common.appid,uid:common.uid}}">抢票</router-link>
      </div>
      <div class="group" v-if="orderInfo.wz_num!='--'">
        <div class="left">无座</div>
        <div class="left"><span v-bind:class="{active:orderInfo.wz_num>0}">{{orderInfo.wz_price}}</span>元</div>
        <div class="left"><span>{{orderInfo.wz_num}}</span>张</div>
        <router-link class="right blue" v-if="orderInfo.wz_num>0"
                     :to="{path:'/ticket/sure-order',query:{appid:common.appid,uid:common.uid,table:'无座',price:orderInfo.wz_price}}">预定
        </router-link>
        <router-link class="right red" v-if="orderInfo.wz_num<=0"
                     :to="{path:'/ticket/main-menu/rob',query:{appid:common.appid,uid:common.uid}}">抢票</router-link>
      </div>
    </div>
    <datetime></datetime>
    <message></message>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  import Message from '../Message.vue'
  import Datetime from './Datetime.vue'

  export default {
    name: 'menu',
    components: {
      Message,
      Datetime
    },
    data () {
      // 获取 sessionStorage 的数据 注意转回json
      return {
        common: {
          appid: '',
          uid: ''
        },
        orderInfo: JSON.parse(sessionStorage.getItem('preOrder')),
        dateHttp: '',
        dateWeek: ''
      }
    },
    beforeRouteEnter (to, from, next) {
      next(function (vm) {
        vm.$data.dateHttp = to.query.dateHttp;
        vm.$data.dateWeek = to.query.dateWeek;
        vm.$data.common.appid = to.query.appid;
        vm.$data.common.uid = to.query.uid;
      });
    },
    methods: {
      timer: function (name) {
        this.$store.commit('TIME_OPEN', {
          name: name,
          ctrl: true
        });
      }
    },
    /*watch: {
      dateHttp: function (val, oldVal) {
        console.log(this.$data.orderInfo.train_code)

      }
    },*/
    computed: {
      time: function () {
        let startD = (this.$data.dateHttp + '').split("-");
        let startT = this.$data.orderInfo.start_time.split(":");
        let start = new Date(startD[0], startD[1] - 1, startD[2], startT[0], startT[1]);
        let runMin = this.$data.orderInfo.run_time_minute;
        let runH = 0;
        let runM = 0;
        runH = parseInt(runMin / 60);
        runM = runMin % 60;
        let weekWord = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
        let arriveMs = start.getTime() + (runMin * 60 * 1000);
        let arrive = new Date(arriveMs);
        let sessionDate = {
          startTime: {
            post: this.$data.dateHttp,
            show: (start.getMonth() + 1) + '月' + start.getDate() + '日 ' + weekWord[start.getDay()]
          },
          arriveTime: {
            post: arrive.getFullYear() + '-' + (arrive.getMonth() + 1) + '-' + arrive.getDate(),
            show: (arrive.getMonth() + 1) + '月' + arrive.getDate() + '日 ' + weekWord[arrive.getDay()]
          },
          runTime: runH + '小时' + runM + '分钟'
        };
        sessionStorage.setItem('preDate', JSON.stringify(sessionDate));
        return sessionDate;
      },
      $$data: function () {
        this.$data.dateHttp=this.$store.state.date.scope.dateFour.code;
        this.$data.dateWeek=this.$store.state.date.scope.dateFour.time;
        return this.$data;
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
    background-color: #f5f5f5;
  }

  .group {
    width: 100%;
    height: 49px;
    padding: 0 15px;
    border-bottom: 1px solid #ebebeb;
    &.no-border {
      border: none;
    }
    .left {
      float: left;
      margin-right: 30px;
      font-size: 15px;
      line-height: 49px;
      color: #000;
      span.active {
        color: #f44848;
      }
    }
    .right {
      border-radius: 3px;
      padding: 6px 18px;
      font-size: 12px;
      color: #FFF;
      margin-top: 10px;
      &.blue {
        background-color: #4ab9f1;
      }
      &.red {
        background-color: #fe6969;
      }
      float: right;
    }
  }

  .space {
    padding: 20px 15px;
    background-color: #FFF;
    margin-bottom: 5px;
    overflow: hidden;
    position: relative;
    &.no-padding {
      padding: 0;
    }
    .left {
      float: left;
    }
    .middle {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      text-align: center;
      margin-top: 24px;
      .ch {
        width: 105px;
        height: 4px;
        margin: 5px auto 10px;
        background: url("../../assets/ch.png") no-repeat center;
        background-size: 105px 4px;
      }
    }
    .right {
      float: right;
    }
    .time {
      font-size: 18px;
      color: #111;
      font-weight: bold;
    }
    .place {
      font-size: 20px;
      color: #111;
      font-weight: bold;
    }
    .date {
      font-size: 12px;
      color: #999;
    }
    .trains {
      font-size: 18px;
      color: #1ca0e2;
      font-weight: bold;
    }
  }

  .date-time {
    width: 100%;
    height: 45px;
    background-color: #4ab9f1;
    position: relative;
    .middle {
      width: 122px;
      height: 25px;
      border: 1px solid #84d0f6;
      border-radius: 3px;
      position: absolute;
      top: 10px;
      left: 50%;
      margin-left: -61px;
      background: url("../../assets/calendar.png") no-repeat 100px 3px;
      background-size: 16px 16px;
      font-size: 12px;
      color: #FFF;
      line-height: 25px;
      text-align: left;
      font-weight: bold;
      padding-left: 10px;
    }
    .btn {
      padding: 0 15px;
      font-size: 12px;
      color: #FFF;
      line-height: 45px;
      &.left {
        float: left;
      }
      &.right {
        float: right;
      }
      &.dis {
        color: #91daff;
      }
    }
  }
</style>
