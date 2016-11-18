<template lang="html">
  <transition name="fade">
    <div v-if="$$show.quickPayment">
      <!-- 支付模块 获取验证码 -->
      <div class="space" v-if="$$show.submitSpace">
        <div class="checkout">
          <div class="space_t">
            <div class="xx"></div>
            <div class="word">使用银行卡支付</div>
          </div>
          <div class="ul" @click="goList">
            <!--<div class="logo">logo</div>-->
            <div class="word">{{default_card.info.bankCode}}</div>
            <div class="word">{{default_card.info.cardNo}}</div>
            <div class="small">储蓄卡</div>
            <div class="list"></div>
          </div>
          <div class="ul">
            <input type="text" placeholder="请输入验证码" v-model="valid_code.vCode">
            <button v-show="!$$valid_code.sending" @click="send($event,'by_cid')">获取验证码</button>
            <button v-show="$$valid_code.sending">{{$$valid_code.timer}}</button>
          </div>
          <div class="btn" @click="pay($event,'by_cid')">确认付款 ￥{{from_data.price}}</div>
        </div>
      </div>
      <!-- 卡列表 选择支付卡 -->
      <div class="space" v-if="$$show.cardListSpace">
        <div class="bankList">
          <div class="space_t">
            <div class="xx"></div>
            <div class="word">选择银行卡</div>
          </div>
          <ul>
            <li v-for="(card,index) in card_list" v-bind:class="{active:default_card.index==index}"
                @click="select($event,card,index)">
              <!--<div class="logo">logo</div>-->
              <div class="word">{{card.bankCode}}</div>
              <div class="word">{{card.cardNo}}</div>
              <div class="small">储蓄卡</div>
            </li>
          </ul>
          <div class="new" @click="newCard">使用新卡支付</div>
          <div class="btn" @click="goDetail">确认付款 ￥{{from_data.price}}</div>
        </div>
      </div>
      <!-- 首次绑卡 首次支付 -->
      <div class="space" v-if="$$show.firstBindSpace">
        <div class="first">
          <div class="space_t">
            <div class="xx"></div>
            <div class="word">使用银行卡支付</div>
          </div>
          <div class="prompt">
            <div class="word">*请务必添加本人名下银行卡</div>
          </div>
          <div class="form">
            <div class="group">
              <div>姓名</div>
              <input type="text" placeholder="银行卡开户姓名" v-model="fill_from.capCrdNm">
            </div>
            <div class="group">
              <div>身份证</div>
              <input type="text" placeholder="请输入身份证号" v-model="fill_from.idNo">
            </div>
            <div class="group">
              <div>银行卡</div>
              <input type="text" placeholder="请输入银行卡号(仅储蓄卡)" v-model="fill_from.crdNo" @blur="bin($event)">
            </div>
            <div class="group">
              <div>手机号</div>
              <input type="text" placeholder="开户银行预留手机号" v-model="fill_from.phoneNo">
              <button v-show="!$$valid_code.sending" @click="send($event,'by_card')">获取验证码</button>
              <button v-show="$$valid_code.sending">{{$$valid_code.timer}}</button>
            </div>
            <div class="group">
              <div>验证码</div>
              <input type="text" placeholder="输入短信验证码" v-model="valid_code.vCode">
            </div>
            <div class="checkbox" @click="agree">
              <div class="btn" v-bind:class="{active:fill_from.isAgree==0}"></div>
              <div class="word">已阅读并同意《支付服务规则》</div>
            </div>
            <div class="submit" @click="pay($event,'by_card')">确认付款 ￥{{from_data.price}}</div>
          </div>
        </div>
      </div>
      <!-- 二次绑卡 实名信息已确定 -->
      <div class="space" v-if="$$show.secondBindSpace">
        <div class="second">
          <div class="space_t">
            <div class="xx"></div>
            <div class="word">使用银行卡支付</div>
          </div>
          <div class="prompt">
            <div class="word">*请务必添加本人名下银行卡</div>
          </div>
          <div class="form">
            <div class="group">
              <div>姓名</div>
              <div class="no-margin">{{user_info.accountName}}</div>
            </div>
            <div class="group">
              <div>银行卡</div>
              <input type="text" placeholder="请输入银行卡号(仅储蓄卡)" v-model="fill_from.crdNo" @blur="bin($event)">
            </div>
            <div class="group">
              <div>手机号</div>
              <input type="text" placeholder="开户银行预留手机号" v-model="fill_from.phoneNo">
              <button v-show="!$$valid_code.sending" @click="send($event,'by_card')">获取验证码</button>
              <button v-show="$$valid_code.sending">{{$$valid_code.timer}}</button>
            </div>
            <div class="group">
              <div>验证码</div>
              <input type="text" placeholder="输入短信验证码" v-model="valid_code.vCode">
            </div>
            <div class="checkbox" @click="agree">
              <div class="btn" v-bind:class="{active:fill_from.isAgree==0}"></div>
              <div class="word">已阅读并同意《支付服务规则》</div>
            </div>
            <div class="submit" @click="pay($event,'by_card')">确认付款 ￥{{from_data.price}}</div>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script lang="babel">

  const randomStr = function () {
    let data = new Date(),
      year = data.getFullYear() + '',
      month = data.getMonth() + 1 + '',
      day = data.getDate() + '',
      hour = data.getHours() + '',
      min = data.getMinutes() + '',
      ss = data.getSeconds() + '';
    let random = parseInt(Math.random() * 89999 + 10000) + '';
    return year + month + day + hour + min + ss + random;
  };
  export default {
    name: 'menu',
    data () {
      return {
        pay_type: {
          pre_by_cid: {
            code: '5',
            payAddress: '/authen/toPayByCid',
            goAddress: '/ticket/refund-success'
          },
          pre_by_card: {
            code: '4',
            payAddress: '/authen/toPay',
            goAddress: '/ticket/refund-success'
          },
          rob_by_cid: {
            code: '7',
            payAddress: '/authen/toPayGrabByCid',
            goAddress: '/ticket/rob-detail'
          },
          rob_by_card: {
            code: '6',
            payAddress: '/authen/toPayGrab',
            goAddress: '/ticket/rob-detail'
          }
        },
        from_data: {
          appid: this.$store.state.payment.pay.pay_form.appid,
          uid: this.$store.state.payment.pay.pay_form.uid,
          orderId: this.$store.state.payment.pay.pay_form.orderId,
          price: this.$store.state.payment.pay.pay_form.price,
          type: this.$store.state.payment.pay.pay_form.type
        },
        valid_code: {
          sending: false,
          timer: 60,
          vCode: '',
          sn: ''
        },
        card_list: {},
        default_card: {
          index: 0,
          info: {}
        },
        user_info: {},
        fill_from: {
          capCrdNm: "",
          idNo: "",
          crdNo: '',
          phoneNo: '',
          isAgree: 0,
          bankCode: ''
        },
        space_ctrl: {
          submitSpace: false,
          cardListSpace: false,
          firstBindSpace: false,
          secondBindSpace: false
        }
      }
    },
    created: function () {
      this.$http.post('/card/list', {
        appid: this.$data.from_data.appid,
        uid: this.$data.from_data.uid
      }).then(function (res) {
        if (res.body.code == 1) {
          if (res.body.data.cardList) {
            this.$data.card_list = res.body.data.cardList;
            this.$data.default_card.info = res.body.data.cardList[0];
            this.$data.user_info = res.body.data.userCardInfo;
            this.$data.space_ctrl.submitSpace = true;
          } else {
            this.$data.space_ctrl.firstBindSpace = true;
          }
        } else {
          this.$store.commit('MESSAGE_DELAY_SHOW', {
            text: res.body.message
          });
        }
      }, function (err) {
        this.$store.commit('MESSAGE_DELAY_SHOW', {
          text: err
        });
      });
    },
    methods: {
      reset: function () {
        this.$data.valid_code = {
          sending: false,
          timer: 60,
          vCode: '',
          sn: ''
        }
      },
      send: function (event, type) {
        this.$data.valid_code.sending = true;
        let submitData = {};
        if (type == 'by_cid') {
          submitData = {
            appid: this.$data.from_data.appid,
            uid: this.$data.from_data.uid,
            phone: this.$data.default_card.info.phone,
            amount: this.$data.from_data.price,
            verificationCodeType: this.$data.pay_type[this.$data.from_data.type + '_' + type].code
          }
        } else if (type == 'by_card') {
          submitData = {
            appid: this.$data.from_data.appid,
            uid: this.$data.from_data.uid,
            phone: this.$data.fill_from.phoneNo,
            amount: this.$data.from_data.price,
            verificationCodeType: this.$data.pay_type[this.$data.from_data.type + '_' + type].code
          }
        }
        this.$http.post('/authen/getCode', submitData).then(function (res) {
          if (res.data.code == 1) {
            this.$data.valid_code.sn = res.data.data;
            let polling = '';
            const pollFun = ()=>{
              this.$data.valid_code.timer--;
              if (this.$data.valid_code.sending) {
                if (this.$data.valid_code.timer < 0) {
                  this.$data.valid_code.timer = 60;
                  this.$data.valid_code.sending = false;
                  clearInterval(polling);
                }
              } else {
                this.$data.valid_code.timer = 60;
                clearInterval(polling);
              }
            }
            polling = setInterval(pollFun, 1000);
          } else {
            this.$data.valid_code.sending = false;
            this.$store.commit('MESSAGE_DELAY_SHOW', {
              text: res.data.message
            });
          }
        }, function (err) {
          this.$data.valid_code.sending = false;
          this.$store.commit('MESSAGE_DELAY_SHOW', {
            text: err
          });
        })
      },
      pay: function (event, type) {
        let submitData = {};
        if (type == 'by_cid') {
          submitData = {
            appid: this.$data.from_data.appid,
            uid: this.$data.from_data.uid,
            orderId: this.$data.from_data.orderId,
            nonceStr: randomStr(),
            cId: this.$data.default_card.info.id,
            vCode: this.$data.valid_code.vCode,
            sn: this.$data.valid_code.sn
          }
        } else if (type == 'by_card') {
          submitData = {
            appid: this.$data.from_data.appid,
            uid: this.$data.from_data.uid,
            orderId: this.$data.from_data.orderId,
            nonceStr: randomStr(),
            capCrdNm: this.$data.user_info.accountName ? this.$data.user_info.accountName : this.$data.fill_from.capCrdNm,
            idNo: this.$data.user_info.cardId ? this.$data.user_info.cardId : this.$data.fill_from.idNo,
            crdNo: this.$data.fill_from.crdNo,
            phoneNo: this.$data.fill_from.phoneNo,
            isAgree: this.$data.fill_from.isAgree,
            vCode: this.$data.valid_code.vCode,
            bankCode: this.$data.fill_from.bankCode,
            sn: this.$data.valid_code.sn
          }
        }
        this.$http.post(this.$data.pay_type[this.$data.from_data.type + '_' + type].payAddress, submitData).then(function (res) {
          if (res.data.code == 1) {
            this.$router.push({
              path: this.$data.pay_type[this.$data.from_data.type + '_' + type].goAddress, query: {
                appid: this.$data.from_data.appid,
                uid: this.$data.from_data.uid,
                orderid: this.$data.from_data.orderId
              }
            })
          } else {
            this.$store.commit('MESSAGE_DELAY_SHOW', {
              text: res.body.message
            });
          }
        }, function (err) {
          this.$store.commit('MESSAGE_DELAY_SHOW', {
            text: err
          });
        })
      },
      goList: function () {
        this.reset();
        this.$data.space_ctrl.submitSpace = false;
        this.$data.space_ctrl.cardListSpace = true;
      },
      goDetail: function () {
        this.reset();
        this.$data.space_ctrl.cardListSpace = false;
        this.$data.space_ctrl.submitSpace = true;
      },
      select: function (event, card, index) {
        this.$data.default_card.index = index;
        this.$data.default_card.info = card;
      },
      newCard: function () {
        this.reset();
        this.$data.space_ctrl.cardListSpace = false;
        this.$data.space_ctrl.secondBindSpace = true;
      },
      agree: function () {
        if (this.$data.fill_from.isAgree == 0) {
          this.$data.fill_from.isAgree = 1;
        } else {
          this.$data.fill_from.isAgree = 0;
        }
      },
      bin: function () {
        const val = this.$data.fill_from.crdNo;
        this.$http.post('/bankCardBin/cardNoInfo', {cardNo: val}).then(function (res) {
          if (res.data.code == 1) {
            this.$data.fill_from.bankCode = res.data.data.bankName;
          } else {
            this.$store.commit('MESSAGE_DELAY_SHOW', {
              text: res.data.message
            });
          }
        }, function (err) {
          this.$store.commit('MESSAGE_DELAY_SHOW', {
            text: err
          });
        })
      }
    },
    computed: {
      $$show: function () {
        return {
          quickPayment: this.$store.state.payment.quickPayment.show,
          submitSpace: this.$data.space_ctrl.submitSpace,
          cardListSpace: this.$data.space_ctrl.cardListSpace,
          firstBindSpace: this.$data.space_ctrl.firstBindSpace,
          secondBindSpace: this.$data.space_ctrl.secondBindSpace
        }
      },
      $$valid_code: function () {
        return this.$data.valid_code;
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .space_t {
    width: 100%;
    height: 49px;
    line-height: 49px;
    border-bottom: 1px solid #f5f5f5;
    position: relative;
    .xx {
      width: 14px;
      height: 14px;
      background: url("../../assets/xx.png") no-repeat center;
      background-size: 14px 14px;
      padding: 15px;
      position: absolute;
      top: 9px;
      left: 10px;
    }
    .word {
      font-size: 15px;
      color: #111;
      width: 100%;
      height: 49px;
      line-height: 49px;
      text-align: center;
    }
  }

  .bankList, .checkout {
    position: absolute;
    width: 100%;
    height: 50%;
    background-color: #FFF;
    left: 0;
    bottom: 0;
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
      .list {
        float: right;
        margin-top: 15px;
        margin-right: 15px;
        width: 7px;
        height: 12px;
        background: url("../../assets/prompt-arrow.png") no-repeat center;
        background-size: 7px 12px;
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

  .first, .second {
    position: absolute;
    width: 100%;
    height: 70%;
    background-color: #FFF;
    left: 0;
    bottom: 0;
    .prompt {
      padding: 0 15px;
      overflow: hidden;
      .word {
        float: left;
        text-align: right;
        font-size: 12px;
        line-height: 45px;
        color: #ff6565;
      }
    }

    .form {
      .group {
        width: 100%;
        height: 45px;
        line-height: 45px;
        border-bottom: 1px solid #f5f5f5;
        div {
          display: inline-block;
          float: left;
          margin-left: 15px;
          text-align: left;
          width: 50px;
          font-size: 15px;
          color: #000;
        }
        input {
          width: 200px;
          height: 42px;
          float: left;
        }
        button {
          float: right;
          font-size: 12px;
          color: #37abe5;
          margin-right: 15px;
          line-height: 44px;
          background-color: #FFF;
        }
      }
      .checkbox {
        .btn {
          width: 23px;
          height: 23px;
          float: left;
          margin: 10px 10px 0 15px;
          background: url("../../assets/option.png") no-repeat center;
          background-size: 23px 23px;
          &.active {
            background: url("../../assets/option-active.png") no-repeat center;
            background-size: 23px 23px;
          }
        }
        .word {
          float: left;
          font-size: 12px;
          color: #111;
          line-height: 45px;
        }
      }
      .submit {
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
