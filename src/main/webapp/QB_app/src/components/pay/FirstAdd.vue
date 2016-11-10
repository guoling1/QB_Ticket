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
        <input type="text" placeholder="请输入银行卡号(仅储蓄卡)" v-model="submitInfo.crdNo">
      </div>
      <div class="group">
        <div>手机号</div>
        <input type="text" placeholder="开户银行预留手机号" v-model="submitInfo.phoneNo">
        <button @click="send">获取验证码</button>
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
  </div>
</template>

<script lang="babel">
  export default {
    name: 'menu',
    data () {
      return {
        submitInfo: {
          orderId: "", //订单编号，不是订单号，是金开门系统唯一id
          nonceStr: "", //随机字符串，每次请求都不一样，生成规则（yyyyMMddHHmmssSSS+5个随机数字）
          crdNo: "", //卡号
          capCrdNm: "", //银行账户名
          idNo: "", //证件号
          phoneNo: "", //手机号
          isAgree: 0, //是否同意服务协议
          vCode: ""
        },
        price: 0.0
      }
    },
    beforeRouteEnter (to, from, next) {
      next(function (vm) {
        vm.$data.submitInfo.orderId = to.query.id;
        vm.$data.price = to.query.price;
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
      send: function () {
        console.log('发送验证码');
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
        this.$data.nonceStr = year + month + day + hour + min + ss + random;
        console.log(this.$data.submitInfo);
        this.$http.post('/authen/toPay', this.$data.submitInfo).then(function (res) {
          if (res.data.code == 1) {
            console.log('跳转出票页');
          } else {
            console.log(res.data.message);
          }
        }, function (err) {
          console.log(err);
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
