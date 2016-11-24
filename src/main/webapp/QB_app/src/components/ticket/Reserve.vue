<template lang="html">
  <div class="main">
    <div class="banner">
      <img src="../../assets/banner.png">
    </div>
    <div class="from">
      <div class="goto">
        <div class="side show">
          <div class="left">出发城市</div>
          <div class="right">到达城市</div>
        </div>
        <div class="side write">
          <div class="left" id="left" @click="station('stationONE')">{{$$data.form_name}}</div>
          <img class="middle" id="middle" src="../../assets/exchange.png" @click="change">

          <div class="right" id="right" @click="station('stationTWO')">{{$$data.to_name}}</div>
        </div>
      </div>
      <div class="time" @click="time('dateONE')">
        <div class="show">{{$$data.date_week}}</div>
      </div>
      <div class="option">
        <div class="show">只看高铁/动车</div>
        <div class="check" v-bind:class="{active:$$data.only_GD}" @click="onlyU">
          <div class="btn"></div>
        </div>
      </div>
      <div class="submit" @click="query">查询</div>
      <div class="flexBox">
        <div class="history">
          <div v-for="his in $$data.history" @click="chooseByHistory($event,his)">{{his.fromStationName}} →
            {{his.toStationName}}
          </div>
          <div @click="clearHistory">清空</div>
        </div>
        <div class="know" @click="show">订票须知<span></span></div>
      </div>
    </div>
    <div class="notice" v-if="this.$data.$show">
      <div class="content">
        <h4>预订说明</h4>
        <ol>
          <li> 我们通过铁路官网或授权代销点为客户提供火车票代购服务。</li>
          <li> 您可以自由选择出票套餐和抢票套餐，也可以只选择火车票代购服务。</li>
          <li> 因受全国各铁路局的不同规定与要求，<span class="red">无法承诺百分之百代购成功</span>，我司预先收取您的票款，如果代购失败，退款会退至您支付时使用的银行卡。</li>
          <li> 我们提供的是火车票代购服务，您接受本协议，意味着您同意我们使用您填写的乘客信息进行代购，包括但不限于授权我们使用您的乘客信息进行注册，代购，退票等操作，同时您必须遵守12306的购票规定的服务条款。</li>
        </ol>
        <h4>取票说明</h4>

        <p>发车前凭订票时登记的证件和电子订单号，可在全国任意火车站或代售点取票。</p>

        <p>部分高铁动车组列车可持二代居民身份证直接检票进站</p>
        <h4>退票说明</h4>
        <ol>
          <li> 在线退票时间：6:00-22:55（其他时间须前往火车站窗口办理。）</li>
          <li> 发车前35分钟且未打印纸质车票，可在“我的订单”中申请退票</li>
          <li> 已经打印车票，需要携带车票前往火车站窗口办理。</li>
        </ol>
        <h4>退票手续费</h4>

        <p>每张票按梯次收取退票手续费：</p>

        <p>发车前15天（不含）以上，不收取退票费；</p>

        <p>发车前49小时以上，手续费5%；</p>

        <p>发车前25-49小时，手续费10%；</p>

        <p>发车前25小时内，手续费20%；</p>

        <p class="red">最终退款以铁路局实退金额为准。</p>

        <p>如果您购买了出票套餐，我们在退保险成功后和票款一同退还至支付银行卡。</p>
        <h4>改签说明</h4>

        <p>本系统暂不支持在线改签，您可以在线发起退票后重新购买车票，或者前往火车站窗口办理。</p>

        <p>如果在车站改签的车票低于原车票金额，我司将会退还差价至您支付时使用的银行卡。</p>
      </div>
      <div class="x" @click="show">×</div>
    </div>
    <datetime></datetime>
    <stationName></stationName>
    <message></message>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  import Datetime from './Datetime.vue'
  import StationName from './StationName.vue'
  import Message from '../Message.vue'

  export default {
    name: 'menu',
    components: {
      Datetime,
      StationName,
      Message
    },
    data: function () {
      return {
        appid: '',
        uid: '',
        history: [],
        form_name: this.$store.state.station.scope.stationONE.station,
        form_code: this.$store.state.station.scope.stationONE.code,
        to_name: this.$store.state.station.scope.stationTWO.station,
        to_code: this.$store.state.station.scope.stationTWO.code,
        date_http: this.$store.state.date.scope.dateONE.code,
        date_week: this.$store.state.date.scope.dateONE.time,
        only_GD: false,
        $show: false
      }
    },
    beforeCreate:function () {
      let query = this.$route.query;
      this.$http.post('/queryHistory/query', {
        appid: query.appid,
        uid: query.uid
      }).then(function (res) {
        this.$data.appid = query.appid;
        this.$data.uid = query.uid;
        this.$data.history = res.data;
      },function(){
        this.$store.commit('MESSAGE_ACCORD_SHOW', {
          text: '获取历史查询记录失败'
        })
      });
    },
    methods: {
      //点击查询按钮,跳转页面,带上查询必须的参数
      query: function () {
        this.$router.push({
          path: '/ticket/train-menu/train', query: {
            appid: this.$data.appid,
            uid: this.$data.uid,
            formName: this.$data.form_name,
            formCode: this.$data.form_code,
            toName: this.$data.to_name,
            toCode: this.$data.to_code,
            dateHttp: this.$data.date_http,
            dateWeek: this.$data.date_week,
            onlyGD: this.$data.only_GD
          }
        })
      },
      chooseByHistory: function (event, his) {
        this.$store.state.station.scope.stationONE.code = his.fromStation;
        this.$store.state.station.scope.stationONE.station = his.fromStationName;
        this.$store.state.station.scope.stationTWO.code = his.toStation;
        this.$store.state.station.scope.stationTWO.station = his.toStationName;
      },
      clearHistory: function () {
        this.$http.post('/deleteHistory/delete', {
          appid: this.$data.appid,
          uid: this.$data.uid
        }).then(function (res) {
          this.$data.history = [];
        },function(){
          console.log('获取历史查询记录失败');
        });
      },
      time: function (name) {
        this.$store.commit('TIME_OPEN', {
          name: name,
          ctrl: true
        });
      },
      station: function (name) {
        this.$store.commit('STATION_OPEN', {
          name: name,
          ctrl: true
        });
      },
      onlyU: function () {
        this.$data.only_GD = !this.$data.only_GD;
      },
      show: function () {
        this.$data.$show = !this.$data.$show
      },
      change: function () {
        document.querySelector("#middle").className = "middle rotate";
        document.querySelector("#left").className = "left changeLeft";
        document.querySelector("#right").className = "right changeRight";
        setTimeout(()=>{
          let tmp = "";
          tmp = this.$data.form_name;
          this.$data.form_name = this.$data.to_name;
          this.$data.to_name = tmp;
          tmp = this.$data.form_code;
          this.$data.form_code = this.$data.to_code;
          this.$data.to_code = tmp;
          tmp = null;
          document.querySelector("#middle").className = "middle"
          document.querySelector("#left").className = "left"
          document.querySelector("#right").className = "right"
        },400)
      }
    },
    computed: {
      $$data: function () {
        this.$data.form_name = this.$store.state.station.scope.stationONE.station;
        this.$data.form_code = this.$store.state.station.scope.stationONE.code;
        this.$data.to_name = this.$store.state.station.scope.stationTWO.station;
        this.$data.to_code = this.$store.state.station.scope.stationTWO.code;
        this.$data.date_http = this.$store.state.date.scope.dateONE.code;
        this.$data.date_week = this.$store.state.date.scope.dateONE.time;
        return this.$data;
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

  .banner {
    width: 100%;
    img {
      width: 100%;
    }
  }

  .from {
    padding: 0 15px;
  }

  .goto {
    padding: 14px 0 6px;
    border-bottom: 1px solid #efefef;
    .side {
      overflow: hidden;
      position: relative;
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
        /*transition: all 1.5s;*/
        position: relative;
        left: 0;
        top: 0;
      }
      .right {
        float: right;
        /*transition: all 1.5s;*/
        position: relative;
        left: 0;
        top: 0;
      }
    }
  }

  .time {
    height: 56px;
    border-bottom: 1px solid #efefef;
    .show {
      line-height: 56px;
      font-size: 16px;
      color: #111;
      text-align: left;
      font-weight: bold;
    }
  }

  .option {
    height: 56px;
    .show {
      line-height: 56px;
      font-size: 15px;
      color: #999;
      float: left;
    }
    .check {
      width: 50px;
      height: 26px;
      float: right;
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

  .submit {
    display: block;
    width: 100%;
    height: 47px;
    line-height: 47px;
    border-radius: 3px;
    background-color: #4ab9f1;
    font-size: 18px;
    font-weight: bold;
    color: #FFF;
  }

  .history {
    .flexItem(1, 100%);
    margin-top: 15px;
    font-size: 12px;
    color: #c1c1c1;
    float: left;
    div {
      float: left;
      line-height: 24px;
      margin-right: 15px;
    }
  }

  .know {
    margin-top: 15px;
    font-size: 12px;
    color: #c1c1c1;
    line-height: 24px;
    float: right;
    span {
      display: block;
      width: 13px;
      height: 13px;
      float: right;
      margin-top: 5px;
      margin-left: 5px;
      background: url('../../assets/know.png') no-repeat;
      background-size: 13px 13px;
    }
  }

  .notice {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 99;
    padding: 0 15px;
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, .9);
    .content {
      height: 90%;
      overflow: auto;
      text-align: left;
      font-size: 12px;
      line-height: 21px;
    }
    h4 {
      font-size: 14px;
      font-weight: bold;
      line-height: 21px;
      padding-top: 17px;
    }
    p {
      line-height: 21px;
    }
    ol {
      padding-left: 15px;
      list-style: decimal;
      li {
        line-height: 21px;
      }
    }
    .red {
      color: #ff6565;
    }
    .x {
      height: 20%;
      width: 100%;
      font-size: 30px;
      padding: 20px 0 40px;
      color: #999;
      text-align: center;
    }
  }

  .rotate {
    -webkit-animation: rotateIn 1s ease 0s 1 both;
    animation: rotateIn 1s ease 0s 1 both;
  }

  .changeLeft {
    -webkit-animation: left 0.5s ease 0s 1 both;
    animation: left 0.5s ease 0s 1 both;
  }

  .changeRight {
    -webkit-animation: right 0.5s ease 0s 1 both;
    animation: right 0.5s ease 0s 1 both;
  }

  @-webkit-keyframes rotateIn {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(180deg);
    }
  }

  @keyframes rotateIn {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(180deg);
    }
  }

  @-webkit-keyframes left {
    0% {
      left: 0;
    }
    100% {
      left: 83%;
    }
  }

  @keyframes left {
    0% {
      left: 0;
    }
    100% {

      left: 83%;
    }
  }

  @-webkit-keyframes right {
    0% {
      left: 0;
    }
    100% {
      left: -83%;
    }
  }

  @keyframes right {
    0% {
      left: 0;
    }
    100% {
      left: -83%;
    }
  }
</style>
