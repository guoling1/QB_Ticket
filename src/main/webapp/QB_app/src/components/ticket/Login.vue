<template lang="html">
  <div class="main">
    <div class="banner">
      <div class="word">使用12306账号登录</div>
      <img src="../../assets/12306-bg.png">
    </div>
    <div class="from">
      <div class="framework">
        <div class="ipt">
          <i class="icon-account"></i>
          <input type="text" placeholder="12306用户名/邮箱/手机号">
        </div>
        <div class="ipt">
          <i class="icon-password"></i>
          <input type="text" placeholder="请输入12306密码">
        </div>
        <div class="ipt no-border">
          <i class="icon-remember"></i>

          <div class="option">
            记住密码
            <div class="check active">
              <div class="btn"></div>
            </div>
          </div>
        </div>
      </div>
      <router-link class="submit" :to="{path:'/ticket/train-menu/train',query:{date:$from.date}}">
        <i></i>登录12306
      </router-link>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'menu',
    data: function () {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },
    methods: {
      time: function (name) {
        this.$store.commit('TIME_OPEN', {
          name: name,
          ctrl: true
        });
      },
      station: function () {
        this.$store.commit('STATION_CTRL', true);
      }
    },
    computed: {
      $data: function () {
        return {
          date: this.$store.state.date.scope.dateONE.time,
          form: this.$store.state.station.scope.stationONE.station,
          to: this.$store.state.station.scope.stationTWO.station
        }
      },
      $from: function () {
        return {
          date: '2016-12-06'
        };
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
  }

  .banner {
    width: 100%;
    position: absolute;
    z-index: -1;
    .word {
      position: absolute;
      width: 100%;
      font-size: 15px;
      color: #FFF;
      margin-top: 35px;
      text-align: center;
    }
    img {
      width: 100%;
    }
  }

  .from {
    padding: 0 15px;
    .framework {
      width: 100%;
      padding: 15px 25px 5px 15px;
      margin-top: 78px;
      margin-bottom: 38px;
      background-color: #FFF;
      box-shadow: 0 0 5px #f5f5f5;
      .ipt {
        width: 100%;
        height: 52px;
        line-height: 52px;
        text-align: left;
        border-bottom: 1px solid #f2f2f2;
        position: relative;
        &.no-border {
          border: none;
        }
        i {
          display: inline-block;
          width: 16px;
          height: 16px;
          margin-right: 8px;
          transform: translate3d(0, 1px, 0);
          &.icon-account {
            background: url("../../assets/account.png") no-repeat center;
            background-size: 16px 16px;
          }
          &.icon-password {
            background: url("../../assets/password.png") no-repeat center;
            background-size: 16px 16px;
          }
          &.icon-remember {
            background: url("../../assets/remember.png") no-repeat center;
            background-size: 16px 16px;
          }
        }
        input {
          width: 70%;
        }
        .option {
          display: inline-block;
          .check {
            position: absolute;
            top: 0;
            right: 0;
            width: 50px;
            height: 26px;
            margin-top: 15px;
            border-radius: 13px;
            background-color: #eaeaea;
            padding: 2px;
            .btn {
              width: 22px;
              height: 22px;
              border-radius: 50%;
              background-color: #FFF;
            }
            &.active {
              background-color: #4ab9f1;
              .btn {
                float: right;
              }
            }
          }
        }
      }
    }
  }

  .submit {
    display: block;
    width: 100%;
    height: 47px;
    line-height: 47px;
    border-radius: 3px;
    background-color: #4ab9f1;
    font-size: 15px;
    font-weight: bold;
    color: #FFF;
    i {
      display: inline-block;
      width: 13px;
      height: 15px;
      background: url("../../assets/12306-submit.png") no-repeat center;
      background-size: 13px 15px;
      margin-right: 8px;
      transform: translate3d(0, 1px, 0);
    }
  }
</style>
