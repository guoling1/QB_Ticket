<template lang="html">
  <div class="main">
    <div class="banner">
      <span class="first">前一天</span>
      <div class="calendar">
        10月31日 周五
        <img src="../../assets/calendar.png" alt="" />
      </div>
      <span class="next show">后一天</span>
    </div>
    <ul>
      <li>
        <div class="top">
          <span class="checi">G307</span>
          <div class="topRight">
            <p>￥850<span>起</span></p>
            <span class="time">7小时25分</span>
          </div>
          <div class="topMiddle">
            <div class="midTop">
              <span class="form">北京西</span>
              <span class="icon"></span>
              <img src="../../assets/jiantou.png" alt="" />
              <span class="to">深圳</span>
            </div>
            <div class="midBottom">
              <span class="fromTime">19:24</span>
              <span class="toTime">14:00</span>
            </div>
          </div>
        </div>
        <div class="bottom">
          <p>
            二等座:<span class="num">12</span>张
          </p>
          <p>
            二等座:<span class="num">12</span>张
          </p>
        </div>
      </li>
      <li class="select">
        <div class="top">
          <span class="checi">G307</span>
          <div class="topRight">
            <p>￥850<span>起</span></p>
            <span class="time">7小时25分</span>
          </div>
          <div class="topMiddle">
            <div class="midTop">
              <span class="form">北京西</span>
              <span class="icon"></span>
              <img src="../../assets/jiantou.png" alt="" />
              <span class="to">深圳</span>
            </div>
            <div class="midBottom">
              <span class="fromTime">19:24</span>
              <span class="toTime">14:00</span>
            </div>
          </div>
        </div>
        <div class="bottom">
          <p>
            二等座:<span class="num">12</span>张
          </p>
          <p>
            二等座:<span class="num">12</span>张
          </p>
        </div>
      </li>
    </ul>

  </div>
</template>

<script lang="babel">
  export default {
    name: 'menu',
    data () {
      return {
        msg: 'Welcome to Your Vue.js App'
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
    background: #f6f6f6;
    .flexItem(1, 100%);

    .banner{
      padding:9px 20px;
      height: 45px;
      width: 100%;
      background: #4ab9f1;

      .first,.next{
        height:27px;
        line-height: 27px;
        font-size: 12px;
        color: #91daff;
        float: left;
      }

      .next{
        float: right;

      }

      .show{
        color: #fff;
      }

      .calendar{
        display: inline-block;
        border:1px solid #84d0f6;
        border-radius: 2px;
        width: 124px;
        height: 26px;
        line-height: 26px;
        color: #fff;
        font-size: 12px;
        overflow: hidden;

        img{
          width: 16px;
          height: 16px;
          vertical-align: -3px;
          margin-left: 12px;
        }
      }
    }
    ul li{
      margin-top: 5px;
      padding:15px;
      height: 101px;
      width: 100%;
      background: #fff;

      &.select{
        background: #f6f6f6;
      }

      .top{
        margin-bottom: 17px;
        height: 34px;
        line-height: 34px;

        .checi{
          float: left;
          font-size: 22px;
        }

        .topRight{
          float: right;
          margin:0 0 17px 0;
          height: 34px;
          line-height: 34px;

          p{
            color: #f14242;
            font-size: 14px;
            span{
              font-size: 10px;
              color: #f14242;
            }
          }
          >span{
            color: #999;
            font-size: 12px;
            position: relative;
            top:-8px;
            left: 0;
          }
        }
        .topMiddle{
          margin:0 auto;
          height: 34px;
          .midTop{
            height: 16px;
            position: relative;
            top:-10px;
            margin-bottom: 7px;
            span{
              font-size: 16px;
              display: inline-block;;
              &.icon{
                vertical-align: middle;
                width: 24px;
                border-bottom: 2px solid #000;
                position: relative;
                left: 4px;
              }

            }
            img{
              //float: left;
              position: relative;
              top:-5px;
              left: -7px;
            }

          }
          .midBottom{
            margin:0 auto;
            position: relative;
            top:-10px;
            width: 233px;
            height: 17px;
            span{
              display: inline-block;
              color: #999;
              font-size: 12px;
              &.fromTime{
                margin-right: 60px;
              }
            }
          }

        }
      }

      .bottom{
        height: 33.5px;
        line-height: 33.5px;
        border-top: 1px dashed #e0e0e0;
        font-size:12px;
        color: #999;
        text-align: left;

        p{
          display: inline-block;
          height: 33.5px;
          line-height: 33.5px;
          margin-right: 18px;
        }
        span{
          color: #3cb687;
        }
      }
    }
  }
</style>
