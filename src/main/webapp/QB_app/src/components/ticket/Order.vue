<template lang="html">
  <div class="main flex-box-column flexBox">
    <div class="teb-title">
      <div class="reserve" v-bind:class="{show:preShow}" @click="teb(1)">预订订单</div>
      <div class="rob" v-bind:class="{show:robShow}" @click="teb(2)">抢票单</div>
    </div>
    <div class="window">
      <!-- 预定 -->
      <ul v-show="preShow">
        <li v-for="massage in preMassages" @click="preGo($event,massage)">
          <div class="top">
            <span class="date">{{massage.startDate}} {{massage.startTime}}出发</span><span
            class="checi">{{massage.checi}}</span>
          </div>
          <div class="bottom">
            <div>
              <span class="form">{{massage.fromStationName}}</span>
              <img src="../../assets/jiantou.png" alt=""/>
              <span class="to">{{massage.toStationName}}</span>
            </div>
            <span class="name" v-for="passenger in massage.passengers">{{passenger.name}}</span>
            <span class="price" v-for="passenger in massage.passengers">￥{{passenger.price}}</span>
            <p class="static" v-for="passenger in massage.passengers">{{orderStatus[massage.status+1]}}</p>
          </div>
        </li>
      </ul>
      <!-- 抢票 -->
      <ul v-show="robShow">
        <li v-for="massage in robMassages" @click="robGo($event,massage)">
          <div class="top" v-show="massage.isGrab==1">
            <span class="date">{{massage.startDate}} {{massage.startTime}} 出发</span>
            <span class="checi">{{massage.checi}}</span>
          </div>
          <div class="top" v-show="massage.isGrab==0">
            <span class="date">{{massage.grabStartTime}} 出发</span>
          </div>
          <div class="bottom">
            <div class="group">
              <span class="form">{{massage.fromStationName}}</span>
              <img src="../../assets/jiantou.png" alt=""/>
              <span class="to">{{massage.toStationName}}</span>
              <span class="price">￥{{massage.grabTotalPrice}}</span>
            </div>
            <div class="group" style="overflow:visible">
              <span class="name" v-for="passenger in massage.passengerInfo">{{passenger.name}}</span>

              <p class="static">{{robOrderStatus[massage.status]}}</p>
            </div>
          </div>
        </li>
      </ul>
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
        preMassages: false,
        preShow: true,
        robMassages: false,
        robShow: false,
        passengerStatus: ["票初始化", "出票成功", "出票失败", "退票中", "退票请求成功", "退票成功", "退票失败", "订单取消"],
        orderStatus: ["订单已删除", " ", "订单初始化", "占座申请中", "占座成功", "占座失败", "支付中", "客户付款成功", "客户付款失败", "确认出票请求失败", "确认出票请求成功", "出票成功", "出票失败", "订单已经退票", "订单取消", "退票中", "退票成功", "退票失败"],
        robOrderStatus: ["", "", "待支付", "抢票中", "未支付", "抢票中", "抢票中", "抢票中", "抢票失败", "抢票成功", "订单已取消", "抢票中", "", "", "退款中", "退款成功", "退款失败", "", "待支付", "待支付"]
      }
    },
    methods: {
      preGo: function (event, massage) {
        if(massage.status=="3"){
          this.$router.push({
            path: '/ticket/pay-order', query: {
              appid: this.$data.common.appid,
              uid: this.$data.common.uid,
              orderFormId: massage.orderFormId
            }
          })
        }else {
          this.$router.push({
            path: '/ticket/refund-detail', query: {
              appid: this.$data.common.appid,
              uid: this.$data.common.uid,
              orderid: massage.orderFormId
            }
          })
        }
      },
      robGo: function (event, massage) {
        this.$router.push({
          path: '/ticket/rob-detail', query: {
            appid: this.$data.common.appid,
            uid: this.$data.common.uid,
            orderid: massage.grabTicketFormId
          }
        })
      },
      teb: function (code) {
        if (code == 1) {
          this.$data.preShow = true;
          this.$data.robShow = false;
        } else {
          this.$data.preShow = false;
          this.$data.robShow = true;
          if (!this.$data.robMassages) {
            this.$http.post('/order/queryGrabOrder', {
              grabTicketFormId: 0,
              appid: this.$data.common.appid,
              uid: this.$data.common.uid
            }).then(function (res) {
              if (res.data.code == 1) {
                for (let i = 0; i < res.data.data.length; i++) {
                  res.data.data[i].passengerInfo = JSON.parse(res.data.data[i].passengerInfo);
                }
                this.$data.robMassages = res.data.data;
              } else {
                console.log(res.data.message);
              }
            }, function (err) {
              console.log(err);
            })
          }
        }
      }
    },
    computed: {
      $robMassages: function () {
        return this.$data.robMassages;
      }
    },
    beforeRouteEnter(to, from, next) {
      Vue.http.post('/order/queryMyOrder', {
        appid: to.query.appid,
        uid: to.query.uid,
        orderFormId: 0
      }).then(function (response) {
        next(function (vm) {
          vm.$data.common.appid = to.query.appid;
          vm.$data.common.uid = to.query.uid;
          if (response.data.code == 1) {
            vm.$data.preMassages = response.data.data;
          }
        })
      }).catch(function (err) {
        console.log(err);
        next(false)
      })
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
    overflow: hidden;
  }

  .teb-title {
    width: 100%;
    height: 44px;
  }

  .window {
    .flexItem(1);
    width: 100%;
    overflow: auto;
  }

  .reserve, .rob {
    width: 50%;
    height: 44px;
    color: #ccc;
    background: #fff;
    font-size: 15px;
    text-align: center;
    line-height: 44px;
    float: left;
  }

  .show {
    color: #4ab9f1;
  }

  ul {
    li {
      height: 113px;
      background: #fff;
      margin-top: 5px;
      .top {
        font-size: 14px;
        height: 37px;
        line-height: 37px;
        border-bottom: 1px solid #efefef;
        padding-left: 17px;
        padding-right: 17px;
        .date {
          float: left;
        }
        .checi {
          float: right;
        }
      }
      .bottom {
        font-size: 18px;
        padding: 17px 17px 10px;
        position: relative;
        overflow: hidden;
        height: 77px;
        div {
          width: 183px;
          margin-bottom: 15px;
          overflow: hidden;
          font-weight: bold;
          height: 20px;
          .form {
            float: left;
          }
          .to {
            float: right;
          }
          img {
            vertical-align: 5px;
          }
        }
        .name {
          float: left;
          font-size: 12px;
          color: #999;
          margin-right: 8px;
        }
        .price {
          color: #f14242;
          font-weight: bold;
          position: absolute;
          top: 18px;
          right: 17px;
        }
        .static {
          position: absolute;
          bottom: 10px;
          right: 15px;
          padding: 0 5px;
          border-radius: 2px;
          height: 23px;
          line-height: 23px;
          font-size: 12px;
          color: #fff;
          text-align: center;
          background: #fe6969;
        }
      }
    }
  }
</style>
