<template lang="html">
  <div class="main">
    <div class="loading" v-show="orderInfo.status!=8&&orderInfo.status!=10">
      <i></i>正在为您出票中...
    </div>
    <div class="prote">
      <div class="left">{{orderInfo.startDate}} {{orderInfo.startTime}} 开</div>
      <div class="right">退改签说明</div>
    </div>
    <div class="top">
      <div class="train-info">
        <div class="left">
          <div class="time">{{orderInfo.startTime}}</div>
          <div class="place">{{orderInfo.fromStationName}}</div>
          <div class="date">{{otherInfo.startDate}}</div>
        </div>
        <div class="middle">
          <div class="trains">{{orderInfo.checi}}</div>
          <div class="ch"></div>
          <div class="date">耗时{{otherInfo.runTime}}</div>
        </div>
        <div class="right">
          <div class="time">{{orderInfo.endTime}}</div>
          <div class="place">{{orderInfo.toStationName}}</div>
          <div class="date">{{otherInfo.arriveDate}}</div>
        </div>
      </div>
    </div>
    <ul class="bottom">
      <li v-for="(passenger,index) in orderInfo.passengers">
        <div class="top">
          <span class="name">{{passenger.name}}</span>
          <span class="type">{{passenger.passportSeNo}}</span>
          <span class="seat">{{passenger.cxin}} {{orderInfo.zwName}}</span>
        </div>
        <div class="top2">
          <span class="number">{{passenger.piaoTypeName}}</span>
          <span class="price">￥{{passenger.price}}</span>
        </div>
      </li>
    </ul>
    <div class="total">
      <div class="state" v-show="orderInfo.status==10">出票成功</div>
      <div class="state" v-show="orderInfo.status!=8&&orderInfo.status!=10">出票中...</div>
      <div class="state" v-show="orderInfo.status==8">出票失败</div>
      <div class="price">总计 ￥<span>{{orderInfo.totalPrice}}</span></div>
    </div>
    <div class="btn">
      <div class="left" @click="check">查看订单</div>
      <div class="left" @click="tobuy">继续购票</div>
    </div>
    <message></message>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  import Message from '../Message.vue'

  export default {
    name: 'menu',
    components: {
      Message
    },
    data () {
      return {
        common: {
          appid: '',
          uid: ''
        },
        orderInfo: []
      }
    },
    beforeCreate: function () {
      let query = this.$route.query;
      this.$http.post('/order/queryById', {
        orderFormId: query.orderid
      }).then(function (res) {
        this.$data.orderInfo = res.data;
        this.$data.common.appid = query.appid;
        this.$data.common.uid = query.uid;
      }, function () {
        this.$store.commit('MESSAGE_ACCORD_SHOW', {
          text: '查询订单信息失败'
        })
      });
    },
    created: function () {
      // 这里 轮询 等待回调
      let polling = '';
      const pollFun = ()=>{
        this.$http.post('/order/queryById', {
          appid: query.appid,
          uid: query.uid
        }).then(function (res) {
          if (res.data.status == 10) {
            clearInterval(polling);
            this.$data.orderInfo = res.data;
          } else if (res.data.status == 8 || res.data.status == 11) {
            clearInterval(polling);
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '出票失败'
            });
          }
        }, function () {
          clearInterval(polling);
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '查询订单信息失败'
          })
        });
      }
      polling = setInterval(pollFun, 1500);
    },
    methods: {
      tobuy: function () {
        this.$router.push({
          path: '/ticket/main-menu/reserve', query: {
            appid: this.$data.common.appid,
            uid: this.$data.common.uid
          }
        })
      },
      check: function () {
        this.$router.push({
          path: '/ticket/main-menu/order', query: {
            appid: this.$data.common.appid,
            uid: this.$data.common.uid
          }
        })
      }
    },
    computed: {
      otherInfo: function () {
        let a = 0;
        let b = 0;
        // 乘车人数 车票价格
        for (let i in this.$data.orderInfo.passengers) {
          a++;
          b = this.$data.orderInfo.passengers[i]['price'];
        }
        // 套餐价格
        let c = (this.$data.orderInfo.totalPrice - this.$data.orderInfo.ticketTotalPrice) / a;
        // 运行时间
        let runMin = this.$data.orderInfo.runTime;
        let runH = 0;
        let runM = 0;
        if (runMin >= 60) {
          runH = parseInt(runMin / 60);
          runM = runMin % 60;
        }
        // 出发日期 到达日期
        let weekWord = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
        let start = new Date(this.$data.orderInfo.startDate);
        let arrive = new Date(this.$data.orderInfo.endDate);
        return {
          passengersNum: a,
          ticket: b,
          insurance: c,
          runTime: runH + '小时' + runM + '分钟',
          startDate: (start.getMonth() + 1) + '-' + start.getDate() + ' ' + weekWord[start.getDay()],
          arriveDate: (arrive.getMonth() + 1) + '-' + arrive.getDate() + ' ' + weekWord[arrive.getDay()],
          expire: false
        }
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
  }

  .main {
    width: 100%;
    height: 100%;
    overflow: hidden;
    background: #f5f5f5;
    .flexItem(1, 100%);
  }

  .loading {
    width: 100%;
    height: 50px;
    background-color: #fef0f0;
    border: 1px solid #fed4d4;
    line-height: 50px;
    font-size: 15px;
    color: #ff6565;
    text-align: center;
    i {
      display: inline-block;
      margin-right: 5px;
      transform: translate3d(0, 2px, 0);
      width: 16px;
      height: 16px;
      background: url("../../assets/time.gif") no-repeat center;
      background-size: 16px 16px;
    }
  }

  .prote {
    width: 100%;
    height: 45px;
    line-height: 45px;
    background-color: #FFF;
    padding: 0 15px;
    .left {
      float: left;
      font-size: 12px;
      color: #111;
    }
    .right {
      float: right;
      font-size: 12px;
      color: #999;
    }
  }

  .train-info {
    padding: 0 15px 20px;
    background-color: #FFF;
    overflow: hidden;
    position: relative;
    border-bottom: 1px dotted #f5f5f5;
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
      margin-top: 2px;
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

  .bottom {
    background: #fff;
    padding: 0 15px;
    overflow: hidden;
    li {
      padding: 17px 0 14px 0;
      overflow: hidden;
      border-bottom: 1px dotted #f5f5f5;
      line-height: 24px;
      .top {
        overflow: hidden;
        .name {
          float: left;
          font-size: 15px;
          color: #111;
          margin-right: 5px;
        }
        .type {
          float: left;
          font-size: 12px;
          color: #999;
        }
        .seat {
          float: right;
          font-size: 15px;
          color: #111;
        }
      }

      .top2 {
        overflow: hidden;
        .number {
          float: left;
          font-size: 12px;
          color: #999;
        }
        .price {
          float: right;
          font-size: 12px;
          color: #999;
        }
      }
    }
  }

  .total {
    width: 100%;
    height: 45px;
    line-height: 45px;
    padding: 0 15px;
    background-color: #FFF;
    overflow: hidden;
    .state {
      float: left;
      font-size: 12px;
      color: #3cb687;
    }
    .price {
      float: right;
      font-size: 12px;
      color: #111;
      span {
        color: #fe6969;
      }
    }
  }

  .btn {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 50px;
    line-height: 50px;
    background-color: #FFF;
    .left {
      float: left;
      width: 50%;
      height: 50px;
      font-size: 15px;
      color: #1ca0e2;
      text-align: center;
    }
    .right {
      float: right;
      width: 50%;
      height: 50px;
      font-size: 15px;
      color: #1ca0e2;
      text-align: center;
    }
  }
</style>
