<template lang="html">
  <div class="main">
    <div class="state"
         v-if="$$orderInfo.status==3||$$orderInfo.status==5||$$orderInfo.status==6||$$orderInfo.status==7||$$orderInfo.status==11">
      <i></i>
      <span>正在奋力抢票中...</span>
    </div>
    <div class="state" v-if="$$orderInfo.status==8">
      <span>抢票失败</span>
    </div>
    <div class="state" v-if="$$orderInfo.status==9">
      <span>抢票成功</span>
    </div>
    <div class="state"
         v-if="$$orderInfo.status==10||$$orderInfo.status==14||$$orderInfo.status==15||$$orderInfo.status==16">
      <span>订单已取消</span>
    </div>
    <div class="space">
      <div class="goto">
        <div class="side show">
          <div class="left">出发城市</div>
          <div class="right">到达城市</div>
        </div>
        <div class="side write">
          <div class="left">{{$$orderInfo.fromStationName}}</div>
          <img class="middle" src="../../assets/exchange-blue.png">

          <div class="right">{{$$orderInfo.toStationName}}</div>
        </div>
      </div>
      <div class="group">
        <div class="prompt">已选车次</div>
        <div class="write no-prompt right">{{$$orderInfo.trainCodes}}</div>
      </div>
      <div class="group no-border">
        <div class="prompt">已选坐席</div>
        <div class="write no-prompt right">{{$$orderInfo.seatTypes}}</div>
      </div>
    </div>
    <div class="space padding-word">
      <div class="left">
        <div class="big">乘车人</div>
        <div class="small">总金额</div>
      </div>
      <div class="right">
        <div class="list">
          <div v-for="passenger in $$orderInfo.passengerInfo">{{passenger.name}}<span
            v-if="passenger.piaoType==2">(儿童)</span></div>
        </div>
        <div class="small">￥<span>{{$$orderInfo.grabTotalPrice}}</span></div>
      </div>
    </div>
    <div class="space"
         v-if="$$orderInfo.status!=4&&$$orderInfo.status!=10&&$$orderInfo.status!=14&&$$orderInfo.status!=15&&$$orderInfo.status!=16">
      <div class="cancel" @click="cancel">取消抢票</div>
    </div>
    <div class="submit" @click="back">返回</div>
    <message></message>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  import Datetime from './Datetime.vue'
  import Message from '../Message.vue'

  export default {
    name: 'menu',
    components: {
      Datetime,
      Message
    },
    data: function () {
      return {
        common: {
          appid: '',
          uid: ''
        },
        orderInfo: ''
      }
    },
    beforeCreate: function () {
      let query = this.$route.query;
      this.$http.post('/order/grab/queryById', {
        grabTicketFormId: query.orderid,
        appid: query.appid,
        uid: query.uid
      }).then(function (res) {
        let info = res.data;
        info.passengerInfo = JSON.parse(info.passengerInfo);
        this.$data.common.appid = query.appid;
        this.$data.common.uid = query.uid;
        this.$data.orderInfo = info;
      }, function () {
        this.$store.commit('MESSAGE_PROMPT_SHOW', {
          text: '查询订单信息失败'
        })
      });
    },
    methods: {
      back: function () {
        this.$router.push({
          path: '/ticket/main-menu/reserve',
          query: {
            appid: this.$data.common.appid,
            uid: this.$data.common.uid
          }
        })
      },
      cancel: function () {
        this.$http.post('/ticket/cancel/grab', {
          grabTicketFormId: this.$data.orderInfo.grabTicketFormId
        }).then(function () {
          this.$http.post('/order/grab/queryById', {
            grabTicketFormId: this.$data.orderInfo.grabTicketFormId,
            appid: this.$data.common.appid,
            uid: this.$data.common.uid
          }).then(function (res) {
            let info = res.data;
            info.passengerInfo = JSON.parse(info.passengerInfo);
            this.$data.orderInfo = info;
          }, function () {
            this.$store.commit('MESSAGE_PROMPT_SHOW', {
              text: '查询订单信息失败'
            })
          });
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '订单取消成功'
          });
        }, function () {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '订单取消失败'
          })
        });
      }
    },
    computed: {
      $$orderInfo: function () {
        let orderInfo = this.$data.orderInfo;
        if (typeof (orderInfo.seatTypes) == 'string') {
          orderInfo.seatTypes = orderInfo.seatTypes.replace('0', '无座');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('1', '硬座');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('2', '软座');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('3', '硬卧');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('4', '软卧');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('6', '高级软卧');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('O', '二等座');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('M', '一等座');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('P', '特等座');
          orderInfo.seatTypes = orderInfo.seatTypes.replace('9', '商务座');
        }
        return orderInfo;
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
    overflow: hidden;
    .flexItem(1, 100%);
    background-color: #f5f5f5;
  }

  .state {
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

  .cancel {
    width: 100%;
    height: 50%;
    line-height: 50px;
    font-size: 15px;
    color: #1ca0e2;
  }

  .space {
    padding: 0 15px;
    background-color: #FFF;
    border-bottom: 5px solid #f5f5f5;
    overflow: hidden;
    &.no-border {
      border: none;
    }
    &.no-padding {
      padding: 0;
    }
    &.padding-word {
      padding: 8px 15px;
    }
    .left {
      float: left;
      text-align: left;
    }
    .right {
      float: right;
      text-align: right;
    }
    .big {
      line-height: 25px;
      font-size: 15px;
      color: #111;
    }
    .list {
      line-height: 25px;
      font-size: 12px;
      color: #111;
      div {
        float: right;
        margin-left: 5px;
        span {
          color: #999;
        }
      }
    }
    .small {
      line-height: 20px;
      font-size: 12px;
      color: #999;
    }
  }

  .handle {
    height: 56px;
    border-top: 1px solid #efefef;
    border-bottom: 1px solid #efefef;
    position: relative;
    .btn {
      width: 50%;
      height: 56px;
      line-height: 56px;
      font-size: 15px;
      color: #1ca0e2;
      float: left;
      text-align: center;
    }
    .line {
      position: absolute;
      width: 1px;
      height: 56px;
      background-color: #efefef;
      top: 0;
      left: 50%;
    }
  }

  .group {
    height: 56px;
    border-bottom: 1px dashed #efefef;
    &.no-border {
      border: none;
    }
    .prompt {
      float: left;
      font-size: 16px;
      color: #111;
      line-height: 56px;
      margin-right: 18px;
    }
    .logo {
      width: 26px;
      height: 26px;
      float: left;
      margin-top: 14px;
      margin-right: 10px;
      background: url("../../assets/12306.png") no-repeat;
      background-size: 26px 26px;
    }
    .list {
      width: 19px;
      height: 19px;
      float: left;
      margin-top: 18px;
      margin-right: 10px;
      background: url("../../assets/list.png") no-repeat;
      background-size: 19px 19px;
    }
    .write {
      &.empty {
        color: #CCC;
      }
      &.no-prompt {
        background: none;
      }
      &.right {
        text-align: right;
      }
      .info {
        float: right;
        font-size: 12px;
        color: #b6b6b6;
      }
      .name {
        font-weight: bold;
      }
      .price {
        color: #f14242;
        margin-right: 15px;
      }
      line-height: 56px;
      font-size: 16px;
      color: #111;
      text-align: left;
      background: url("../../assets/prompt-arrow.png") no-repeat right;
      background-size: 7px 12px;
    }
  }

  .goto {
    padding: 14px 0 6px;
    border-bottom: 1px solid #efefef;
    .side {
      overflow: hidden;
      &.show {
        font-size: 12px;
        color: #999;
      }
      &.write {
        margin-top: 9px;
        font-size: 24px;
        font-weight: bold;
        color: #111;
        text-align: center;
        .middle {
          width: 27px;
          height: 27px;
          margin-top: 4px;
        }
      }
      .left {
        float: left;
      }
      .right {
        float: right;
      }
    }
  }

  .submit {
    display: block;
    width: 100%;
    height: 49px;
    line-height: 49px;
    background-color: #4ab9f1;
    font-size: 18px;
    font-weight: bold;
    color: #FFF;
    position: fixed;
    bottom: 0;
    left: 0;
  }
</style>
