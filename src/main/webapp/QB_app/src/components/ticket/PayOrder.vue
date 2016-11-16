<template lang="html">
  <div class="main">
    <div class="from">
      <div class="state" v-show="orderInfo.status==2">
        <i></i>正在为您占座中...
      </div>
      <div class="state" v-show="orderInfo.status==3">
        <span v-if="countdown">您的座位还会为您保留{{countdown}}分钟，请尽快完成支付</span>
        <span v-if="!countdown">您的订单已失效</span>
      </div>
      <div class="state" v-show="orderInfo.status==13">
        <span>您的订单已取消</span>
      </div>
      <div class="space">
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
        <div v-show="orderInfo.status==3">
          <div class="man-info" v-for="people in orderInfo.passengers">
            <div class="left">
              <div class="big">{{people.name}} <span class="small">{{people.piaoTypeName}}</span></div>
              <div class="small">{{people.passportSeNo}}</div>
              <div class="red">待支付</div>
            </div>
            <div class="right">
              <div class="big">{{people.cxin}}</div>
              <div class="small">{{orderInfo.zwName}}</div>
              <div class="red">￥<span>{{people.price}}</span></div>
            </div>
          </div>
        </div>
      </div>
      <div class="space" v-show="orderInfo.status==3" @click="cancel">
        <div class="cancel">取消订单</div>
      </div>
      <div class="submit">
        <div class="detail" v-bind:class="{active:detail}">
          <div class="tt">金额详情</div>
          <div class="tt_detail">
            <div class="left">火车票</div>
            <div class="right">x{{otherInfo.passengersNum}}人</div>
            <div class="right margin">￥{{otherInfo.ticket}}</div>
          </div>
          <div class="tt_detail">
            <div class="left">出票套餐</div>
            <div class="right">x{{otherInfo.passengersNum}}人</div>
            <div class="right margin">￥{{otherInfo.insurance}}</div>
          </div>
        </div>
        <div class="btn">
          <div class="left" @click="detailShow">
            <div class="amount">实付款<span class="red">￥</span><span class="red big">{{orderInfo.totalPrice}}</span></div>
            <div class="i" v-bind:class="{active:detail}"></div>
          </div>
          <div class="right" v-bind:class="{dis:orderInfo.status!=3}" @click="submit">立即支付</div>
        </div>
      </div>
    </div>
    <div class="bank" v-show="payInfo.bank">
      <div class="bankList" v-show="payInfo.list">
        <div class="xx"></div>
        <ul>
          <li v-for="(card,index) in payInfo.cardList" v-bind:class="{active:payInfo.index==index}"
              @click="select($event,card,index)">
            <div class="logo">logo</div>
            <div class="word">这是银行</div>
            <div class="word">{{card.cardNo}}</div>
            <div class="small">储蓄卡</div>
          </li>
        </ul>
        <div class="new" @click="newCard">使用新卡支付</div>
        <div class="btn" @click="choose">确认付款 ￥{{$payInfo.price}}</div>
      </div>
      <div class="checkout" v-show="!payInfo.list">
        <div class="xx"></div>
        <div class="ul">
          <div class="logo">logo</div>
          <div class="word">这是银行</div>
          <div class="word">{{payInfo.checkout.cardNo}}</div>
          <div class="small">储蓄卡</div>
        </div>
        <div class="ul">
          <input type="text" placeholder="请输入验证码" v-model="payInfo.checkCode">
          <button v-show="payInfo.sendCtrl" @click="send">获取验证码</button>
          <button v-show="!payInfo.sendCtrl">{{payInfo.timer}}</button>
        </div>

        <div class="btn" @click="pay">确认付款 ￥{{$payInfo.price}}</div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  import Datetime from './Datetime.vue';

  export default {
    name: 'menu',
    components: {
      Datetime
    },
    data: function () {
      return {
        common: {
          appid: '',
          uid: ''
        },
        orderInfo: '',
        countdown: '15:00',
        payInfo: {
          orderId: '',
          price: 00.0,
          bank: false,
          list: true,
          sendCtrl: true,
          timer: 60,
          cardList: '',
          peopleInfo: '',
          checkout: {},
          index: 0,
          checkCode: '',
          sn: ''
        },
        detail: false
      }
    },
    created: function () {
      // 这里 轮询 等待回调
      let polling = '';
      const pollFun = ()=>{
        this.$http.post('/order/queryById', {orderFormId: this.$data.orderInfo.orderFormId}).then(function (res) {
          if (res.data.code == 1 && res.data.data.status == 3) {
            clearInterval(polling);
            console.log('改变信息');
            this.$data.orderInfo = res.data.data;
            this.$data.payInfo.price = res.data.data.totalPrice;
            // 调用定时器
            this.timer(this.$data.orderInfo.expireTime);
          } else if (res.data.code == 1 && res.data.data.status == 4) {
            clearInterval(polling);
            console.log("占座失败");
          } else if (res.data.code == 1 && res.data.data.status == 13) {
            clearInterval(polling);
            console.log("订单已取消");
          }
        })
      }
      polling = setInterval(pollFun, 1500);
    },
    beforeRouteEnter (to, from, next) {
      Vue.http.post('/order/queryById', {
        "orderFormId": to.query.id
      }).then(function (res) {
        if (res.data.code == 1) {
          next(function (vm) {
            vm.$data.orderInfo = res.data.data;
            vm.$data.common.appid = to.query.appid;
            vm.$data.common.uid = to.query.uid;
            vm.$data.payInfo.orderId = to.query.id;
            vm.$data.payInfo.price = res.data.data.totalPrice;
          });
        } else {
          console.log(res.data.message);
        }
      }, function (err) {
        console.log(err);
        next(false);
      });
    },
    methods: {
      detailShow: function () {
        this.$data.detail = !this.$data.detail;
      },
      timer: function (t) {
        let polling2 = '';
        const pollFun2 = ()=>{
          let time = t - new Date().getTime();
          if (time <= 0) {
            this.$data.countdown = false;
            clearInterval(polling2);
          } else {
            let min = parseInt(time / (1000 * 60));
            let ss = parseInt((time - min*(1000*60)) / 1000);
            if(ss<10){
              ss = '0' + ss;
            }
            this.$data.countdown = min + ':' + ss;
          }
        }
        polling2 = setInterval(pollFun2, 1000);
      },
      newCard: function () {
        this.$router.push({
          path: '/pay/second-add', query: {
            appid: this.$data.common.appid,
            uid: this.$data.common.uid,
            orderid: this.$data.payInfo.orderId,
            price: this.$data.payInfo.price,
            name: this.$data.payInfo.peopleInfo.accountName,
            card: this.$data.payInfo.peopleInfo.cardId,
            payType: 0
          }
        });
      },
      select: function (event, card, index) {
        this.$data.payInfo.checkout = card;
        this.$data.payInfo.index = index;
      },
      choose: function () {
        this.$data.payInfo.list = false;
      },
      send: function () {
        this.$http.post('/authen/getCode', {
          phone: this.$data.payInfo.checkout.phone,//手机号
          amount: this.$data.payInfo.price, //支付金额
          uid: this.$data.common.uid, //三方商户用户id
          appid: this.$data.common.appid //三方商户唯一标示appid
        }).then(function (res) {
          if (res.data.code == 1) {
            this.$data.payInfo.sendCtrl = false;
            this.$data.payInfo.sn = res.data.data;
            let polling = '';
            const pollFun = ()=>{
              this.$data.payInfo.timer--;
              if (this.$data.payInfo.timer < 0) {
                this.$data.payInfo.timer = 60;
                this.$data.payInfo.sendCtrl = true;
                clearInterval(polling);
              }
            }
            polling = setInterval(pollFun, 1000);
          } else {
            console.log(res.data.message);
          }
        }, function (err) {
          console.log(err);
        })
      },
      cancel: function () {
        this.$http.post('/ticket/cancelOrder', {
          orderFormId: this.$data.orderInfo.orderFormId
        }).then(function (res) {
          console.log(res);
        }, function (err) {
          console.log(err)
        })
      },
      pay: function () {
        let data = new Date(),
          year = data.getFullYear() + '',
          month = data.getMonth() + 1 + '',
          day = data.getDate() + '',
          hour = data.getHours() + '',
          min = data.getMinutes() + '',
          ss = data.getSeconds() + '';
        let random = parseInt(Math.random() * 89999 + 10000) + '';
        let nonceStr = year + month + day + hour + min + ss + random;
        this.$http.post('/authen/toPayByCid', {
          uid: this.$data.common.uid,
          appid: this.$data.common.appid,
          orderId: this.$data.payInfo.orderId, //订单编号，不是订单号，是金开门系统唯一id
          nonceStr: nonceStr, //随机字符串，每次请求都不一样，生成规则（yyyyMMddHHmmssSSS+5个随机数字）
          cId: this.$data.payInfo.checkout.id,//银行卡id
          vCode: this.$data.payInfo.checkCode, //手机验证码
          sn: this.$data.payInfo.sn //调用短信接口返回的值
        }).then(function (res) {
          if (res.data.code == 1) {
            this.$router.push({path:'/ticket/refund-success',query:{
              appid: this.$data.common.appid,
              uid: this.$data.common.uid,
              orderid: this.$data.payInfo.orderId
            }})
          } else {
            console.log(res);
          }
        }, function (err) {
          console.log(err);
        })
      },
      submit: function () {
        // 首先判断订单状态,决定是否能支付
        if (this.$data.orderInfo.status == 3 /*&& this.$data.countdown*/) {
          this.$http.post('/card/list', {
            appid: this.$data.common.appid,
            uid: this.$data.common.uid
          }).then(function (res) {
            console.log(res);
            if (res.data.code == 1) {
              if (res.data.data.cardList) {
                this.$data.payInfo.cardList = res.data.data.cardList;
                this.$data.payInfo.checkout = res.data.data.cardList[0];
                this.$data.payInfo.peopleInfo = res.data.data.userCardInfo;
                this.$data.payInfo.bank = true;
              } else {
                this.$router.push({
                  path: '/pay/first-add',
                  query: {
                    appid: this.$data.common.appid,
                    uid: this.$data.common.uid,
                    id: this.$data.payInfo.orderId,
                    price: res.data.payInfo.price,
                    payType: 0
                  }
                });
              }
            } else {
              console.log(res.data.message);
            }
          }, function (err) {
            console.log(err);
          });
        }
      }
    },
    computed: {
      $payInfo: function () {
        return this.$data.payInfo;
      },
      pageInfo: function () {
        return this.$data.orderInfo
      },
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
      },
      expireTime: function () {
        return this.$data.countdown;
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

  .train-info {
    padding: 20px 15px;
    background-color: #FFF;
    margin-bottom: 5px;
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

  .man-info {
    background-color: #FFF;
    padding: 10px 0;
    overflow: hidden;
    .left {
      float: left;
      text-align: left;
    }
    .right {
      float: right;
      text-align: right;
    }
    .big {
      line-height: 26px;
      font-size: 15px;
      color: #111;
    }
    .small {
      line-height: 20px;
      font-size: 12px;
      color: #999;
    }
    .red {
      line-height: 20px;
      font-size: 12px;
      color: #fe6969;
    }
  }

  .cancel {
    width: 100%;
    height: 50%;
    line-height: 50px;
    font-size: 15px;
    color: #1ca0e2;
  }

  .from {
    .space {
      padding: 0 15px;
      background-color: #FFF;
      border-bottom: 5px solid #f5f5f5;
      &.no-border {
        border: none;
      }
      &.no-padding {
        padding: 0;
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
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 49px;
    .detail {
      position: absolute;
      z-index: 1;
      background-color: #FFF;
      width: 100%;
      top: 0;
      left: 0;
      padding: 15px;
      &.active {
        top: -88px;
      }
      .tt {
        font-size: 12px;
        color: #111;
        text-align: left;
      }
      .tt_detail {
        font-size: 12px;
        color: #111;
        margin-top: 5px;
        overflow: hidden;
        .left {
          float: left;
        }
        .right {
          float: right;
        }
        .margin {
          margin-right: 50px;
        }
      }
    }
    .btn {
      position: absolute;
      z-index: 9;
      background-color: #FFF;
      width: 100%;
      height: 49px;
      .left {
        float: left;
        width: 50%;
        .amount {
          float: left;
          padding-left: 30px;
          line-height: 49px;
          font-size: 12px;
          color: #999;
          margin-right: 20px;
          .red {
            color: #fe6969;
          }
          .big {
            font-size: 18px;
          }
        }
        .i {
          width: 12px;
          height: 7px;
          float: left;
          margin-top: 20px;
          background: url("../../assets/detail.png") no-repeat center;
          background-size: 12px 7px;
          transform: rotate(180deg);
          &.active {
            transform: rotate(0deg);
          }
        }
      }
      .right {
        float: right;
        width: 50%;
        height: 49px;
        line-height: 49px;
        font-size: 15px;
        color: #FFF;
        background-color: #4ab9f1;
        &.dis {
          background-color: #8fcfef;
          color: #cfefff;
        }
      }
    }
  }

  .bank {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 88;
    background: rgba(0, 0, 0, 0.5);
    .bankList, .checkout {
      position: absolute;
      width: 100%;
      height: 50%;
      background-color: #FFF;
      left: 0;
      bottom: 0;
      .xx {
        width: 14px;
        height: 14px;
        background: url("../../assets/xx.png") no-repeat center;
        background-size: 14px 14px;
        padding: 15px;
      }
      .logo {
        float: left;
        margin-right: 15px;
      }
      .word {
        float: left;
        margin-right: 15px;
        font-size: 15px;
        color: #000;
      }
      .small {
        float: left;
        font-size: 12px;
        color: #999;
      }
      .ul {
        width: 100%;
        height: 45px;
        line-height: 45px;
        text-align: left;
        padding-left: 15px;
        color: #999;
        border-bottom: 1px solid #f5f5f5;
        button {
          float: right;
          height: 44px;
          padding: 0 15px;
          font-size: 12px;
          color: #37abe5;
          background-color: #FFF;
        }
      }
      ul {
        li {
          width: 100%;
          height: 45px;
          line-height: 45px;
          text-align: left;
          padding-left: 15px;
          color: #999;
          border-bottom: 1px solid #f5f5f5;
          &.active {
            color: #2ba7e5;
            background: url("../../assets/select.png") no-repeat 320px;
            background-size: 16px 11px;
            span {
              font-weight: bold;
            }
          }
        }
      }
      .new {
        width: 100%;
        height: 45px;
        padding-left: 50px;
        line-height: 45px;
        text-align: left;
        background: url("../../assets/new.png") no-repeat 15px 10px;
        background-size: 25px 25px;
        border-bottom: 1px solid #f5f5f5;
        font-size: 15px;
        color: #4ab9f1;
      }
      .btn {
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 50px;
        background-color: #4ab9f1;
        font-size: 15px;
        color: #FFF;
        line-height: 50px;
        text-align: center;
      }
    }
  }
</style>
