<template lang="html">
  <div class="main">
    <div class="top">
      <div class="left">
        <p class="time">{{massages.startTime}}</p>

        <p class="static">{{massages.fromStationName}}</p>

        <p class="date">{{massages.startDate}}</p>
      </div>
      <div class="middle">
        <p class="checi time">{{massages.checi}}</p>

        <p class="line">
          <b></b>
          <img src="../../assets/jiantou.png" alt=""/>
        </p>

        <p class="suration date">{{massages.runTime}}</p>
      </div>
      <div class="right">
        <p class="time">{{massages.endTime}}</p>

        <p class="static">{{massages.toStationName}}</p>

        <p class="date">{{massages.endDate}}</p>
      </div>
    </div>
    <ul class="bottom">
      <li v-for="(passenger,index) in massages.passengers">
        <div class="left">
          <span class="name">{{passenger.name}}</span>
          <span class="type">{{passenger.piaoTypeName}}</span>

          <p class="number">
            {{passenger.passportSeNo}}
          </p>

          <p class="state">{{passengerStatus[passenger.status-1]}}
            <span @click="show(index)" v-if="passenger.status==2">退票</span>
          </p>
        </div>
        <div class="right">
          <p class="seat">{{passenger.cxin}}</p>

          <p class="seatType">{{massages.zwName}}</p>

          <p class="price">￥{{passenger.price}}</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue';
  export default {
    name: 'menu',
    data () {
      return {
        common:{
          appid:'',
          uid:''
        },
        orderInfo: []
      }
    },
    created: function () {
      // 这里 轮询 等待回调
      let polling = '';
      const pollFun = ()=>{
        this.$http.post('/order/queryById', {orderFormId: this.$data.orderInfo.orderFormId}).then(function (res) {
          if (res.data.code == 1 && res.data.data.status == 10) {
            clearInterval(polling);
            this.$data.orderInfo = res.data.data;
            // 调用定时器
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
        "orderFormId": to.query.orderid
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
      show: function (index) {
        if (index == "undefined") {
          this.$data.open = !this.$data.open;
        } else {
          this.$data.open = !this.$data.open;
          this.$data.$index = index;
        }
      },
      back: function () {
        this.$router.go(-1);
      },
      success: function () {
        this.$data.open = false;
        this.$data.$open = true;
        this.$data.massages.passengers[this.$data.$index].status = 6
      }
    },
    computed: {
      $open: function () {
        return this.$data.open
      },
      $$open: function () {
        return this.$data.$open;
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

  .top {
    background: #fff;
    overflow: hidden;
    border-bottom: 1px dashed #c9c9c9;
    div {
      display: inline-block;
    }
    padding: 30px 0;
    .left {
      float: left;
      padding-left: 15px;
    }
    .time {
      font-size: 18px;
    }
    .static {
      font-size: 20px;
      font-weight: bold;
      height: 37px;
      line-height: 37px;
    }
    .date {
      font-size: 12px;
      color: #999;
    }
    .middle {
      .line {
        height: 34px;
        line-height: 34px;
      }
      .checi {
        color: #1ca0e2;
        font-weight: bold;
      }
      b {
        display: inline-block;
        position: relative;
        left: 24px;
        width: 105px;
        height: 0;
        vertical-align: middle;
        border-top: 2px solid #000;
      }
      img {
        position: relative;
        left: -24px;
        top: -3px;
      }
    }
    .right {
      float: right;
      padding-right: 15px;
    }
  }

  .bottom {
    background: #fff;
    padding: 0 15px;
    overflow: hidden;
    li:last-child {
      border: none;
    }
    li {
      padding: 17px 0 14px 0;
      overflow: hidden;
      border-bottom: 1px solid #c9c9c9;
      color: #999;
      .left {
        float: left;
        position: relative;
        .name {
          position: absolute;;
          left: 0;
          color: #000;
          font-size: 15px;
        }
        .type {
          font-size: 12px;
        }
        .state {
          text-align: left;
          font-size: 12px;
          span {
            display: inline-block;
            margin-left: 12px;
            color: #1ca0e2;
          }
        }
        .number {
          height: 35px;
          line-height: 35px;
        }
      }

      .right {
        float: right;
        font-size: 12px;
        .seat {
          color: #000;
          font-size: 15px;
        }
        .seatType {
          height: 31px;
          line-height: 31px;
          text-align: right;
        }
        .price {
          color: #000;
          text-align: right;
        }
      }
    }

  }

  .mask {
    //display: none;
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    background: rgba(0, 0, 0, .7);

    .floor {
      background: #fff;
      width: 100%;
      position: absolute;;
      bottom: 0;
      left: 0;
      .x {
        font-size: 16px;
        color: #999999;
        height: 52px;
        line-height: 52px;
        margin-left: 21px;
        text-align: left;
      }
      .table {
        width: 350px;
        margin: 0 auto;
        border: 1px solid #ebebeb;
        border-radius: 1px;
        z-index: 10;
        overflow: hidden;
      }
      .notice {
        margin: 25px 0 14px 20px;
        font-size: 12px;
        color: #999;
        z-index: -2;
        p {
          line-height: 24px;
          text-align: left;
        }
      }
      .button {
        border-top: 1px solid #ebebeb;
        color: #4ab9f1;
        font-size: 15px;
        p {
          float: left;
          width: 50%;
          height: 50px;
          border-right: 1px solid #ebebeb;
          line-height: 50px;
          text-align: center;
        }
      }
    }
  }

  .success {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    background: #fff;

    .content {
      width: 100%;
      height: 295px;
      border-bottom: 1px solid #ededed;
      img {
        margin-top: 85px;
        display: inline-block;
        position: relative;
        top: 50%;
        width: 82px;
        height: 82px;
      }
      p {
        font-size: 18px;
        margin-top: 17px;
      }
    }
    .sure {
      height: 50px;
      line-height: 50px;
      font-size: 15px;
      color: #4ab9f1;
      font-weight: bold;
    }
  }

  .return {
    width: 100%;
    height: 50px;
    line-height: 50px;
    font-size: 15px;
    color: #4ab9f1;
    position: fixed;
    bottom: 0;
    left: 0;
    background: #FFF;
  }
</style>
