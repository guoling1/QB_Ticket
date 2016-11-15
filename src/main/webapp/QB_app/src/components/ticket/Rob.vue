<template lang="html">
  <div class="main">
    <div class="from">
      <div class="goto">
        <div class="side show">
          <div class="left">出发城市</div>
          <div class="right">到达城市</div>
        </div>
        <div class="side write">
          <div class="left" @click="station('stationTHREE')">{{$dataT.from}}</div>
          <img class="middle" src="../../assets/exchange-blue.png">

          <div class="right" v-bind:class="{empty:$from.to==0}" @click="station('stationFOUR')">{{$dataT.to}}</div>
        </div>
      </div>
      <div class="group" @click="time('dateTWO')">
        <div class="prompt">出发日期</div>
        <div class="write no-prompt">{{$dataT.date}}</div>
      </div>
      <div class="group" @click="trainsShow=true">
        <div class="prompt">指定车次</div>
        <div class="write no-prompt" v-bind:class="{empty:$submitInfo.trainCodes=='请选择车次'}">{{$submitInfo.trainCodes}}
        </div>
      </div>
      <div class="group" @click="seat=true">
        <div class="prompt">坐席类型</div>
        <div class="write no-prompt" v-bind:class="{empty:$submitInfo.seatTypes=='全部坐席'}">{{$submitInfo.seatTypes}}
        </div>
      </div>
      <div class="group" @click="timer=true">
        <div class="prompt">抢票时效</div>
        <div class="write no-prompt" v-if="submitInfo.grabTimeType==1">抢至发车前3小时</div>
        <div class="write no-prompt" v-if="submitInfo.grabTimeType==2">抢至发车前6小时</div>
        <div class="write no-prompt" v-if="submitInfo.grabTimeType==3">抢至发车前一天</div>
        <div class="write no-prompt" v-if="submitInfo.grabTimeType==4">抢至发车前三天</div>
      </div>
      <div class="group" @click="pack=true">
        <div class="prompt">抢票套餐</div>
        <div class="write no-prompt" v-if="submitInfo.grabTicketPackageId==1">不购买</div>
        <div class="write no-prompt" v-if="submitInfo.grabTicketPackageId==2">￥10/人 高速网络</div>
        <div class="write no-prompt" v-if="submitInfo.grabTicketPackageId==3">￥20/人 高速网络，多个任务同时抢票</div>
        <div class="write no-prompt" v-if="submitInfo.grabTicketPackageId==4">￥30/人 极速网络，优先多任务同时抢票</div>
        <div class="write no-prompt" v-if="submitInfo.grabTicketPackageId==5">￥66/人 专享网络，优先出票</div>
      </div>
      <div class="group no-border">
        <div class="prompt">抢票成功率约</div>
        <div class="write red empty no-prompt">99%</div>
      </div>
      <div class="submit" @click="submit">下一步</div>
      <div class="know">抢票须知<span></span></div>
    </div>
    <div class="trains-bg flex-box-column flexBox" v-show="trainsShow">
      <div class="title">
        <div class="back" @click="trainsShow=false"></div>
        <h1>{{$dataT.from}} → {{$dataT.to}}</h1>
      </div>
      <div class="result">
        <div class="cont">
          <div class="top" v-for="station in $trains" @click="select($event,station)">
            <span class="checi">{{station.train_code}}</span>

            <div class="topRight">
              <div class="option" v-bind:class="{active:station.select}"></div>
            </div>
            <div class="topMiddle">
              <div class="form">
                <span class="static">{{station.from_station_name}}</span>
                <span class="formTime">{{station.start_time}}</span>
              </div>
              <div class="line"></div>
              <div class="to">
                <span class="static">{{station.to_station_name}}</span>
                <span class="toTime">{{station.arrive_time}}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="btn" @click="trainsEnter">确定</div>
      </div>
    </div>
    <div class="pack" v-show="pack">
      <div class="select">
        <div class="xx"></div>
        <ul>
          <li @click="packHide(2)" v-bind:class="{active:submitInfo.grabTicketPackageId==2}"><span>¥ 10/人套餐</span>
            高速网络
          </li>
          <li @click="packHide(3)" v-bind:class="{active:submitInfo.grabTicketPackageId==3}"><span>¥ 20/人套餐</span>
            高速网络，多个任务同时抢票
          </li>
          <li @click="packHide(4)" v-bind:class="{active:submitInfo.grabTicketPackageId==4}"><span>¥ 30/人套餐</span>
            极速网络，优先多任务同时抢票
          </li>
          <li @click="packHide(5)" v-bind:class="{active:submitInfo.grabTicketPackageId==5}"><span>¥ 66/人 VIP套餐</span>
            专享网络，优先出票
          </li>
          <li @click="packHide(1)" v-bind:class="{active:submitInfo.grabTicketPackageId==1}">不购买 速度慢，人多时需要排队</li>
        </ul>
      </div>
    </div>
    <div class="pack" v-show="timer">
      <div class="select">
        <div class="xx"></div>
        <ul>
          <li @click="timerHide(1)" v-if="$canGrabTimeType>=1" v-bind:class="{active:submitInfo.grabTimeType==1}"><span>抢至发车前3小时</span>
          </li>
          <li @click="timerHide(2)" v-if="$canGrabTimeType>=2" v-bind:class="{active:submitInfo.grabTimeType==2}"><span>抢至发车前6小时</span>
          </li>
          <li @click="timerHide(3)" v-if="$canGrabTimeType>=3" v-bind:class="{active:submitInfo.grabTimeType==3}"><span>抢至发车前一天</span>
          </li>
          <li @click="timerHide(4)" v-if="$canGrabTimeType>=4" v-bind:class="{active:submitInfo.grabTimeType==4}"><span>抢至发车前三天</span>
          </li>
        </ul>
      </div>
    </div>
    <div class="pack" v-show="seat">
      <div class="select">
        <div class="xx"></div>
        <ul>
          <!--<li @click="$shortSeat[0]=!$shortSeat[0]" v-bind:class="{active:$shortSeat[0]}"><span>无座</span></li>-->
          <li @click="$shortSeat[1]=!$shortSeat[1]" v-bind:class="{active:$shortSeat[1]}"><span>硬座</span></li>
          <!--<li @click="$shortSeat[2]=!$shortSeat[2]" v-bind:class="{active:$shortSeat[2]}"><span>软座</span></li>-->
          <li @click="$shortSeat[3]=!$shortSeat[3]" v-bind:class="{active:$shortSeat[3]}"><span>硬卧</span></li>
          <li @click="$shortSeat[4]=!$shortSeat[4]" v-bind:class="{active:$shortSeat[4]}"><span>软卧</span></li>
          <!--<li @click="$shortSeat[6]=!$shortSeat[6]" v-bind:class="{active:$shortSeat[6]}"><span>高级软卧</span></li>-->
          <li @click="$shortSeat.O=!$shortSeat.O" v-bind:class="{active:$shortSeat.O}"><span>二等座</span></li>
          <li @click="$shortSeat.M=!$shortSeat.M" v-bind:class="{active:$shortSeat.M}"><span>一等座</span></li>
          <!--<li @click="$shortSeat.P=!$shortSeat.P" v-bind:class="{active:$shortSeat.P}"><span>特等座</span></li>-->
          <li @click="$shortSeat[9]=!$shortSeat[9]" v-bind:class="{active:$shortSeat[9]}"><span>商务座</span></li>
        </ul>
        <div class="btn" @click="seatEnter">确定</div>
      </div>
    </div>
    <datetime></datetime>
    <stationName></stationName>
  </div>
</template>

<script lang="babel">
  import Datetime from './Datetime.vue';
  import StationName from './StationName.vue';
  export default {
    name: 'menu',
    components: {
      Datetime,
      StationName
    },
    data: function () {
      return {
        submitInfo: {
          appId: "",
          uid: "",
          fromStationName: this.$store.state.station.scope.stationTHREE.station,
          fromStationCode: this.$store.state.station.scope.stationTHREE.code,
          toStationName: this.$store.state.station.scope.stationFOUR.station,
          toStationCode: this.$store.state.station.scope.stationFOUR.code,
          grabStartTime: this.$store.state.date.scope.dateTWO.code,
          grabTimeType: 1,
          firstStartTime: "",
          trainCodes: "请选择车次",
          seatTypes: "1,3,4,O,M,9",
          buyTicketPackageId: 3,
          grabTicketPackageId: 4,
          phone: "",
          grabPassengers: []
        },
        shortSeat: {
          0: false,
          1: false,
          2: false,
          3: false,
          4: false,
          6: false,
          O: false,
          M: false,
          P: false,
          9: false
        },
        canGrabTimeType: 4,
        trains: '',
        trainsShow: false,
        pack: false,
        timer: false,
        seat: false
      }
    },
    beforeRouteEnter (to, from, next) {
      next(function (vm) {
        vm.$data.submitInfo.appId = to.query.appid;
        vm.$data.submitInfo.uid = to.query.uid;
      });
    },
    methods: {
      select: function (event, station) {
        station.select = !station.select;
      },
      seatEnter: function () {
        //先判断用户是否选择了坐席,没选择则为全部坐席
        this.$data.submitInfo.seatTypes = '';
        for (let i in this.$data.shortSeat) {
          if (this.$data.shortSeat[i]) {
            this.$data.submitInfo.seatTypes += i + ',';
          }
        }
        this.$data.submitInfo.seatTypes = this.$data.submitInfo.seatTypes.substr(0, this.$data.submitInfo.seatTypes.length - 1);
        this.$data.seat = false;
      },
      timerHide: function (num) {
        this.$data.timer = false;
        this.$data.submitInfo.grabTimeType = num;
      },
      packHide: function (num) {
        this.$data.pack = false;
        this.$data.submitInfo.grabTicketPackageId = num;
      },
      trainsEnter: function () {
        let checi = '';
        for (let i = 0; i < this.$data.trains.length; i++) {
          if (this.$data.trains[i].select) {
            checi += this.$data.trains[i].train_code + ','
          }
        }
        checi = checi.substr(0, checi.length - 1);
        this.$data.submitInfo.trainCodes = checi;
        this.$data.trainsShow = false;
        // 计算最早的发车时间 firstStartTime
        this.$data.submitInfo.firstStartTime = this.$store.state.date.scope.dateTWO.code + ' ' + this.$data.trains[0].start_time;
        let time = new Date(this.$data.submitInfo.firstStartTime).getTime();
        let now = new Date().getTime();
        let poor = time - now;
        if (poor > 3 * 24 * 60 * 60 * 1000) {
          this.$data.canGrabTimeType = 4;
        } else if (poor > 24 * 60 * 60 * 1000) {
          this.$data.canGrabTimeType = 3;
        } else if (poor > 6 * 60 * 60 * 1000) {
          this.$data.canGrabTimeType = 2;
        } else if (poor > 3 * 60 * 60 * 1000) {
          this.$data.canGrabTimeType = 1;
        } else {
          this.$data.canGrabTimeType = 0;
        }
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
      submit: function () {
        sessionStorage.setItem('robOrder', JSON.stringify(this.$data.submitInfo));
        this.$router.push({path: '/ticket/rob-order',query:{appid:this.$data.submitInfo.appId,uid:this.$data.submitInfo.uid}})
      }
    },
    watch: {
      $dataT: function () {
        this.$http.post('/grabTicketQuery/query', {
          from_station: this.$store.state.station.scope.stationTHREE.code, //出发站简码
          to_station: this.$store.state.station.scope.stationFOUR.code, //到达站简码
          train_date: this.$store.state.date.scope.dateTWO.code //乘车日期（yyyy-MM-dd）
        }).then(function (res) {
          if(res.data.code==1&&!!res.data.data){
            for (let i = 0; i < res.data.data.length; i++) {
              res.data.data[i].select = false;
            }
            this.$data.trains = res.data.data;
          }
        }, function (err) {
          console.log(err);
        })
      }
    },
    computed: {
      $dataT: function () {
        return {
          date: this.$store.state.date.scope.dateTWO.time,
          from: this.$store.state.station.scope.stationTHREE.station,
          to: this.$store.state.station.scope.stationFOUR.station
        }
      },
      $from: function () {
        return {
          date: this.$store.state.date.scope.dateTWO.code,
          from: this.$store.state.station.scope.stationTHREE.code,
          to: this.$store.state.station.scope.stationFOUR.code
        }
      },
      $shortSeat: function () {
        return this.$data.shortSeat;
      },
      $canGrabTimeType: function () {
        return this.$data.canGrabTimeType;
      },
      $trains: function () {
        return this.$data.trains;
      },
      $submitInfo: function () {
        this.$data.submitInfo.fromStationName = this.$store.state.station.scope.stationTHREE.station;
        this.$data.submitInfo.fromStationCode = this.$store.state.station.scope.stationTHREE.code;
        this.$data.submitInfo.toStationName = this.$store.state.station.scope.stationFOUR.station;
        this.$data.submitInfo.toStationCode = this.$store.state.station.scope.stationFOUR.code;
        this.$data.submitInfo.grabStartTime = this.$store.state.date.scope.dateTWO.code;
        let data = this.$data.submitInfo;
        if (data.seatTypes == '1,3,4,O,M,9') {
          data.seatTypes = '全部坐席';
        } else {
          //data.seatTypes = data.seatTypes.replace('0', '无座');
          data.seatTypes = data.seatTypes.replace('1', '硬座');
          //data.seatTypes = data.seatTypes.replace('2', '软座');
          data.seatTypes = data.seatTypes.replace('3', '硬卧');
          data.seatTypes = data.seatTypes.replace('4', '软卧');
          //data.seatTypes = data.seatTypes.replace('6', '高级软卧');
          data.seatTypes = data.seatTypes.replace('O', '二等座');
          data.seatTypes = data.seatTypes.replace('M', '一等座');
          //data.seatTypes = data.seatTypes.replace('P', '特等座');
          data.seatTypes = data.seatTypes.replace('9', '商务座');
        }
        return data;
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
    img {
      width: 100%;
    }
  }

  .from {
    padding: 0 15px;
    .group {
      height: 56px;
      border-bottom: 1px solid #efefef;
      &.no-border {
        border: none;
      }
      .prompt {
        float: left;
        font-size: 16px;
        color: #999;
        line-height: 56px;
        margin-right: 18px;
      }
      .write {
        &.empty {
          color: #CCC;
        }
        &.no-prompt {
          background: none;
        }
        &.red {
          color: #fe6969;
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
        &.empty {
          color: #999;
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

  .trains-bg {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 88;
    background-color: #FFF;
    .title {
      width: 100%;
      height: 64px;
      color: #fefefe;
      background-color: #4ab9f1;
      padding: 30px 10px 0;
      .back {
        width: 19px;
        height: 24px;
        background: url("../../assets/back.png") no-repeat center;
        background-size: 9px 14px;
        float: left;
        padding: 5px;
        position: absolute;
      }
      h1 {
        font-size: 18px;
        line-height: 24px;
      }
    }
    .result {
      .flexItem(1);
      width: 100%;
      overflow: hidden;
      position: relative;
      .cont {
        width: 100%;
        height: 100%;
        overflow: auto;
        padding-bottom: 50px;
        .top {
          padding: 15px 20px;
          height: 70px;
          line-height: 34px;
          border-bottom: 5px solid #f5f5f5;
          .checi {
            float: left;
            font-size: 22px;
            font-weight: bold;
          }
          .topRight {
            float: right;
            padding-top: 6px;
            .option {
              width: 23px;
              height: 23px;
              background: url("../../assets/option.png") no-repeat center;
              background-size: 23px 23px;
              &.active {
                background: url("../../assets/option-active.png") no-repeat center;
                background-size: 23px 23px;
              }
            }
          }
          .topMiddle {
            margin: 0 auto;
            height: 34px;
            width: 184px;
            position: relative;
            .form {
              float: left;
              font-size: 16px;
              height: 34px;
              line-height: 17px;
              .static {
                font-weight: bold;
              }
              .formTime {
                font-size: 12px;
                color: #999;
                position: relative;
                top: 4px;
              }
            }
            .line {
              width: 24px;
              height: 4px;
              background: url("../../assets/jiantou.png") no-repeat center;
              background-size: 24px 4px;
              position: absolute;
              left: 80px;
              top: 10px;
            }
            .to {
              float: right;
              font-size: 16px;
              height: 34px;
              line-height: 17px;
              .static {
                font-weight: bold;
              }
              .toTime {
                font-size: 12px;
                color: #999;
                position: relative;
                top: 4px;
              }
            }
            span {
              display: block;
            }
          }
        }
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
      }
    }
  }

  .pack {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 88;
    background: rgba(0, 0, 0, 0.5);
    .select {
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
      ul {
        width: 100%;
        height: 100%;
        overflow: auto;
        padding-bottom: 50px;
        li {
          width: 100%;
          height: 45px;
          line-height: 45px;
          text-align: left;
          padding-left: 15px;
          color: #999;
          border-bottom: 1px solid #f5f5f5;
          span {
            color: #000;
          }
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
      .btn {
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 50px;
        line-height: 50px;
        text-align: center;
        font-size: 15px;
        color: #FFF;
        background-color: #4ab9f1;
      }
    }
  }
</style>
