<template lang="html">
  <div class="main">
    <div class="prompt">
      <div class="cancel">取消</div>
      <div class="word">*请务必添加本人名下银行卡</div>
    </div>
    <div class="form">
      <div class="group">
        <div>姓名</div>
        <input type="text" placeholder="银行卡开户姓名" v-model="submitInfo.capCrdNm">
      </div>
      <div class="group">
        <div>身份证</div>
        <input type="text" placeholder="请输入身份证号" v-model="submitInfo.idNo">
      </div>
      <div class="group">
        <div>银行卡</div>
        <input type="text" placeholder="请输入银行卡号(仅储蓄卡)" v-model="submitInfo.crdNo" @blur="bin($event)">
      </div>
      <div class="group">
        <div>手机号</div>
        <input type="text" placeholder="开户银行预留手机号" v-model="submitInfo.phoneNo">
        <button v-show="sendCtrl" @click="send">获取验证码</button>
        <button v-show="!sendCtrl">{{timer}}</button>
      </div>
      <div class="group">
        <div>验证码</div>
        <input type="text" placeholder="输入短信验证码" v-model="submitInfo.vCode">
      </div>
      <div class="checkbox" @click="select">
        <div class="btn" v-bind:class="{active:submitInfo.isAgree==0}"></div>
        <div class="word">已阅读并同意《支付服务规则》</div>
      </div>
      <div class="submit" @click="submit">
        确认付款 ￥{{price}}
      </div>
    </div>
    <message></message>
  </div>
</template>

<script lang="babel">
  import Message from '../Message.vue'
  export default {
    name: 'menu',
    components: {
      Message
    },
    data () {
      return {
        payAddress: ['/authen/toPay', '/authen/toPayGrab'],
        goAddress: ['/ticket/refund-success', '/ticket/rob-detail'],
        payCode: ['4', '6'],
        payType: '',
        submitInfo: {
          appid: '',
          uid: '',
          orderId: "", //订单编号，不是订单号，是金开门系统唯一id
          nonceStr: "", //随机字符串，每次请求都不一样，生成规则（yyyyMMddHHmmssSSS+5个随机数字）
          crdNo: "", //卡号
          capCrdNm: "", //银行账户名
          idNo: "", //证件号
          phoneNo: "", //手机号
          isAgree: 0, //是否同意服务协议
          vCode: "", //验证码
          bankCode: "", //卡bin
          sn: ''
        },
        price: 0.0,
        sendCtrl: true,
        timer: 60
      }
    },
    beforeRouteEnter (to, from, next) {
      next(function (vm) {
        vm.$data.submitInfo.orderId = to.query.id;
        vm.$data.submitInfo.appid = to.query.appid;
        vm.$data.submitInfo.uid = to.query.uid;
        vm.$data.price = to.query.price;
        vm.$data.payType = to.query.payType;
      });
    },
    methods: {
      select: function () {
        if (this.$data.submitInfo.isAgree == 0) {
          this.$data.submitInfo.isAgree = 1;
        } else {
          this.$data.submitInfo.isAgree = 0;
        }
      },
      bin: function (event) {
        const val = event.target.value;
        this.$http.post('/bankCardBin/cardNoInfo', {cardNo: val}).then(function (res) {
          if (res.data.code == 1) {
            this.$data.submitInfo.bankCode = res.data.data.shorthand;
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
      },
      send: function () {
        this.$http.post('/authen/getCode', {
          phone: this.$data.submitInfo.phoneNo,//手机号
          amount: this.$data.price, //支付金额
          verificationCodeType: this.$data.payCode[this.$data.payType],
          uid: this.$data.submitInfo.uid, //三方商户用户id
          appid: this.$data.submitInfo.appid //三方商户唯一标示appid
        }).then(function (res) {
          if (res.data.code == 1) {
            this.$data.sendCtrl = false;
            this.$data.submitInfo.sn = res.data.data;
            let polling = '';
            const pollFun = ()=>{
              this.$data.timer--;
              if (this.$data.timer < 0) {
                this.$data.timer = 60;
                this.$data.sendCtrl = true;
                clearInterval(polling);
              }
            }
            polling = setInterval(pollFun, 1000);
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
      },
      submit: function () {
        // 生成随机字符串
        let data = new Date(),
          year = data.getFullYear() + '',
          month = data.getMonth() + 1 + '',
          day = data.getDate() + '',
          hour = data.getHours() + '',
          min = data.getMinutes() + '',
          ss = data.getSeconds() + '';
        let random = parseInt(Math.random() * 89999 + 10000) + '';
        this.$data.submitInfo.nonceStr = year + month + day + hour + min + ss + random;
        this.$http.post(this.$data.payAddress[this.$data.payType], this.$data.submitInfo).then(function (res) {
          if (res.data.code == 1) {
            this.$router.push({
              path: this.$data.goAddress[this.$data.payType], query: {
                appid: this.$data.submitInfo.appid,
                uid: this.$data.submitInfo.uid,
                orderid: massage.grabTicketFormId
              }
            })
          } else {
            this.$store.commit('MESSAGE_DELAY_SHOW', {
              text: res.data.message
            });
          }
        }, function (err) {
          this.$store.commit('MESSAGE_DELAY_SHOW', {
            text: err
          });
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

  .main {
    width: 100%;
    height: 100%;
    overflow: auto;
    .flexItem(1, 100%);
  }

  .prompt {
    padding: 0 15px;
    overflow: hidden;
    .cancel {
      float: left;
      text-align: left;
      font-size: 15px;
      line-height: 45px;
      color: #4ab9f1;
    }
    .word {
      float: right;
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
</style>
