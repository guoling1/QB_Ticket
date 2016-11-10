<template lang="html">
  <div class="main">
    <div class="from">
      <div class="space">
        <div class="train-info">
          <div class="left">
            <div class="time">07:10</div>
            <div class="place">北京西</div>
            <div class="date">11-20 周五</div>
          </div>
          <div class="middle">
            <div class="trains">G208</div>
            <div class="ch"></div>
            <div class="date">耗时1小时52分</div>
          </div>
          <div class="right">
            <div class="time">17:10</div>
            <div class="place">北京东</div>
            <div class="date">11-20 周五</div>
          </div>
        </div>
        <div class="man-info">
          <div class="left">
            <div class="big">刘思思 <span class="small">成人票</span></div>
            <div class="small">120***********0565</div>
            <div class="red">待支付</div>
          </div>
          <div class="right">
            <div class="big">13号车厢01A号</div>
            <div class="small">二等座</div>
            <div class="red">￥<span>128.5</span></div>
          </div>
        </div>
      </div>
      <div class="space">
        <div class="cancel">取消订单</div>
      </div>
      {{orderInfo}}
      <div class="submit">
        <div class="detail">
          <div class="tt">金额详情</div>
          <div class="tt_detail">
            <div class="left">火车票</div>
            <div class="right">x1人</div>
            <div class="right margin">￥128.5</div>
          </div>
          <div class="tt_detail">
            <div class="left">火车票</div>
            <div class="right">x1人</div>
            <div class="right margin">￥128.5</div>
          </div>
        </div>
        <div class="btn">
          <div class="left">
            <div class="amount">实付款<span class="red">￥</span><span class="red big">128.5</span></div>
            <div class="i"></div>
          </div>
          <div class="right" @click="submit">立即支付</div>
        </div>
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
        orderInfo: ''
      }
    },
    beforeRouteEnter (to, from, next) {
      Vue.http.post('/order/queryById', {
        "orderFormId": to.query.id
      }).then(function (res) {
        if (res.data.code == 1) {
          next(function (vm) {
            vm.$data.orderInfo = res.data.data;
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
      submit: function () {
        this.$router.push({
          path: '/pay/first-add',
          query: {
            id: this.$data.orderInfo.orderFormId,
            price: this.$data.orderInfo.price
          }
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
    overflow: hidden;
    .flexItem(1, 100%);
    background-color: #f5f5f5;
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
      top: -88px;
      left: 0;
      padding: 15px;
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
      }
    }
  }
</style>
