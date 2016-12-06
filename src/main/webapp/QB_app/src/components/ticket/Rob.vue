<template lang="html">
  <div class="main">
    <div class="from">
      <div class="goto">
        <div class="side show">
          <div class="left">出发城市</div>
          <div class="right">到达城市</div>
        </div>
        <div class="side write">
          <div class="left" id="left" @click="station('stationTHREE')">{{$dataT.from}}</div>
          <img class="middle" @click="change" id="middle" src="../../assets/exchange-blue.png">

          <div class="right" id="right" v-bind:class="{empty:$from.to==0}" @click="station('stationFOUR')">
            {{$dataT.to}}
          </div>
        </div>
      </div>
      <div class="group" @click="time('dateTWO')">
        <div class="prompt">出发日期</div>
        <div class="write no-prompt">{{$dataT.date}}</div>
      </div>
      <div class="group" @click="trainsShowFun">
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
        <div class="write red empty no-prompt">{{$rate}}%</div>
        <div class="promote" v-show="$rate<50&&!(submitInfo.grabTicketPackageId==5)" @click="promote">提升成功率</div>
      </div>
      <div class="submit" @click="submit">下一步</div>
      <div class="history">抢票须预付票款，若抢票失败，全额退回</div>
      <div class="know" @click="show">抢票须知<span></span></div>
    </div>
    <transition name="fade">
      <div class="trains-bg flex-box-column flexBox" v-show="trainsShow">
        <div class="title">
          <div class="back" @click="trainsShow=false"></div>
          <h1>{{$dataT.from}} → {{$dataT.to}}</h1>
        </div>
        <div class="result">
          <div class="cont">
            <div class="emptyInfo" v-if="!$trains||$trains.length==0">暂无所查询的车次信息</div>
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
    </transition>
    <transition name="fade">
      <div class="pack" v-show="pack" @click="packBGHide($event)">
        <div class="select">
          <div class="space_t">
            <div class="xx" @click="pack=false"></div>
            <div class="word">选择抢票套餐</div>
          </div>
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
    </transition>
    <transition name="fade">
      <div class="timer" v-show="timer" @click="timerBGHide($event)">
        <div class="select">
          <div class="space_t">
            <div class="xx" @click="timer=false"></div>
            <div class="word">选择抢票时效</div>
          </div>
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
    </transition>
    <transition name="fade">
      <div class="seat" v-show="seat" @click="seatBGHide($event)">
        <div class="select">
          <div class="space_t">
            <div class="xx" @click="seat=false"></div>
            <div class="word">选择坐席</div>
          </div>
          <ul>
            <li @click="$shortSeat[0]=!$shortSeat[0]" v-bind:class="{active:$shortSeat[0]}"><span>无座</span></li>
            <li @click="$shortSeat[1]=!$shortSeat[1]" v-bind:class="{active:$shortSeat[1]}"><span>硬座</span></li>
            <li @click="$shortSeat[2]=!$shortSeat[2]" v-bind:class="{active:$shortSeat[2]}"><span>软座</span></li>
            <li @click="$shortSeat[3]=!$shortSeat[3]" v-bind:class="{active:$shortSeat[3]}"><span>硬卧</span></li>
            <li @click="$shortSeat[4]=!$shortSeat[4]" v-bind:class="{active:$shortSeat[4]}"><span>软卧</span></li>
            <li @click="$shortSeat[6]=!$shortSeat[6]" v-bind:class="{active:$shortSeat[6]}"><span>高级软卧</span></li>
            <li @click="$shortSeat.O=!$shortSeat.O" v-bind:class="{active:$shortSeat.O}"><span>二等座</span></li>
            <li @click="$shortSeat.M=!$shortSeat.M" v-bind:class="{active:$shortSeat.M}"><span>一等座</span></li>
            <li @click="$shortSeat.P=!$shortSeat.P" v-bind:class="{active:$shortSeat.P}"><span>特等座</span></li>
            <li @click="$shortSeat[9]=!$shortSeat[9]" v-bind:class="{active:$shortSeat[9]}"><span>商务座</span></li>
          </ul>
          <div class="btn" @click="seatEnter">确定</div>
        </div>
      </div>
    </transition>
    <!-- 抢票须知 -->
    <transition name="fade">
      <div class="notice" v-if="this.$data.$show">
        <div class="content">
          <h4>抢票须知</h4>
          <ol>
            <li> 我们在收到您的票款后，立即为您抢票</li>
            <li> 抢票时间较长，请您耐心等待，历史数据表明发车前一到两天抢票成功率最高</li>
            <li> 抢票成功或失败后，我们会短信通知您</li>
            <li> 我们会在抢票时效到期或您取消抢票后立即为您发起退款，退款到账一般会有1-3个工作日的时间，部分银行较慢，不会超过7个工作日。</li>
            <li> 我们会按照所选车次和席别的最高价预收票款，会根据抢票结果退回余款。</li>
          </ol>
          <h4>预订说明</h4>
          <ol>
            <li> 我们通过铁路官网或授权代销点为客户提供火车票代购服务。</li>
            <li> 您可以自由选择出票套餐和抢票套餐，也可以只选择火车票代购服务。</li>
            <li> 因受全国各铁路局的不同规定与要求，<span class="red">无法承诺百分之百代购成功</span>，我司预先收取您的票款，如果代购失败，退款会退至您支付时使用的银行卡。</li>
            <li> 我们提供的是火车票代购服务，您接受本协议，意味着您同意我们使用您填写的乘客信息进行代购，包括但不限于授权我们使用您的乘客信息进行注册，代购，退票等操作，同时您必须遵守12306的购票规定的服务条款。</li>
          </ol>
          <h4>取票说明</h4>

          <p>发车前凭订票时登记的证件和电子订单号，可在全国任意火车站或代售点取票。部分高铁动车组列车可持二代居民身份证直接检票进站</p>
          <h4>退票说明</h4>
          <ol>
            <li> 在线退票时间：6:00-22:55（其他时间须前往火车站窗口办理。）</li>
            <li> 您可以自由选择出票套餐和抢票套餐，也可以只选择火车票代购服务。</li>
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
    </transition>
    <datetime></datetime>
    <stationName></stationName>
    <message></message>
  </div>
</template>

<script lang="babel">
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
          seatTypes: "0,1,2,3,4,6,O,M,P,9",
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
        success_rate: 17,
        checi_length: 0,
        table_length: 0,
        canGrabTimeType: 4,
        trains: [],
        trainsShow: false,
        pack: false,
        timer: false,
        seat: false,
        $show: false
      }
    },
    created: function () {
      let query = this.$route.query;
      this.$data.submitInfo.appId = query.appid;
      this.$data.submitInfo.uid = query.uid;
      if(query.to_name){
        this.$store.state.station.scope.stationTHREE.station = query.from_name;
        this.$store.state.station.scope.stationTHREE.code = query.from_code;
        this.$store.state.station.scope.stationFOUR.station = query.to_name;
        this.$store.state.station.scope.stationFOUR.code = query.to_code;
        this.$data.submitInfo.trainCodes = query.train_code;
        this.$data.submitInfo.seatTypes = query.table;
        this.$data.submitInfo.firstStartTime = query.firstStartTime;
      }
    },
    methods: {
      trainsShowFun: function () {
        this.$http.post('/grabTicketQuery/query', {
          appid: this.$data.submitInfo.appId,
          uid: this.$data.submitInfo.uid,
          from_station: this.$store.state.station.scope.stationTHREE.code,
          to_station: this.$store.state.station.scope.stationFOUR.code,
          from_station_name: this.$store.state.station.scope.stationTHREE.station,
          to_station_name: this.$store.state.station.scope.stationFOUR.station,
          train_date: this.$store.state.date.scope.dateTWO.code
        }).then(function (res) {
          if(res.data){
            for (let i = 0; i < res.data.length; i++) {
              res.data[i].select = false;
            }
          }
          this.$data.trains = res.data;
          this.$data.trainsShow = true;
        },function(){
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '查询车次信息失败'
          });
        });
      },
      packBGHide: function (event) {
        if (event.target.className == "pack") {
          this.$data.pack = false;
        }
      },
      timerBGHide: function (event) {
        if (event.target.className == "timer") {
          this.$data.timer = false;
        }
      },
      seatBGHide: function (event) {
        if (event.target.className == "seat") {
          this.$data.seat = false;
        }
      },
      select: function (event, station) {
        station.select = !station.select;
      },
      promote: function () {
        this.$data.submitInfo.grabTicketPackageId++;
      },
      seatEnter: function () {
        //先判断用户是否选择了坐席,没选择则为全部坐席
        this.$data.submitInfo.seatTypes = '';
        this.$data.table_length = 0;
        for (let i in this.$data.shortSeat) {
          if (this.$data.shortSeat[i]) {
            this.$data.table_length++;
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
        let firstTime = false;
        this.$data.checi_length = 0;
        for (let i = 0; i < this.$data.trains.length; i++) {
          if (this.$data.trains[i].select) {
            if (!firstTime) {
              firstTime = this.$data.trains[i].start_time
            }
            this.$data.checi_length++;
            checi += this.$data.trains[i].train_code + ','
          }
        }
        checi = checi.substr(0, checi.length - 1);
        this.$data.submitInfo.trainCodes = checi;
        this.$data.trainsShow = false;
        // 计算最早的发车时间 firstStartTime
        this.$data.submitInfo.firstStartTime = this.$store.state.date.scope.dateTWO.code + ' ' + firstTime;
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
        if (this.$data.submitInfo.toStationName == "选择城市") {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '请选择到达城市'
          })
        }else if (this.$data.submitInfo.trainCodes == "请选择车次") {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '请指定车次'
          })
        }else{
          this.$router.push({
            path: '/ticket/rob-order',
            query: {appid: this.$data.submitInfo.appId, uid: this.$data.submitInfo.uid}
          })
        }
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
          tmp = null;
          document.querySelector("#middle").className = "middle"
          document.querySelector("#left").className = "left"
          document.querySelector("#right").className = "right"
        },400);
      }
    },
    computed: {
      $rate: function () {
        let rate = this.$data.success_rate;
        let packageId = this.$data.submitInfo.grabTicketPackageId;
        let checi = this.$data.checi_length;
        let table = this.$data.table_length;
        let rateType = ['', '0', '28', '35', '46', '76'];
        rate = (rate / 1) + (rateType[packageId] / 1) + ((checi * 3) / 1) + (table / 1);
        if (rate > 88) {
          rate = 88;
        }
        return rate;
      },
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
        if (data.seatTypes == '0,1,2,3,4,6,O,M,P,9') {
          data.seatTypes = '全部坐席';
        } else {
          data.seatTypes = data.seatTypes.replace('0', '无座');
          data.seatTypes = data.seatTypes.replace('1', '硬座');
          data.seatTypes = data.seatTypes.replace('2', '软座');
          data.seatTypes = data.seatTypes.replace('3', '硬卧');
          data.seatTypes = data.seatTypes.replace('4', '软卧');
          data.seatTypes = data.seatTypes.replace('6', '高级软卧');
          data.seatTypes = data.seatTypes.replace('O', '二等座');
          data.seatTypes = data.seatTypes.replace('M', '一等座');
          data.seatTypes = data.seatTypes.replace('P', '特等座');
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

  .fade-enter-active, .fade-leave-active {
    transition: opacity .3s
  }

  .fade-enter, .fade-leave-active {
    opacity: 0
  }

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
    .group {
      height: 56px;
      border-bottom: 1px solid #efefef;
      position: relative;
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
        height: 56px;
        overflow: hidden;
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
      .promote {
        position: absolute;
        right: 15px;
        top: 14px;
        padding: 5px;
        border: 1px solid #4ab9f1;
        border-radius: 3px;
        font-size: 12px;
        color: #11aaee;
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
        position: relative;
        left: 0;
        top: 0;
      }
      .right {
        float: right;
        position: relative;
        left: 0;
        top: 0;
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
      height: 50px;
      color: #fefefe;
      background-color: #4ab9f1;
      padding: 15px 10px 0;
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
      height: 100%;
      overflow: auto;
      position: relative;
      -webkit-overflow-scrolling: touch;
      .cont {
        width: 100%;
        height: 100%;
        overflow: auto;
        padding-bottom: 50px;
        .emptyInfo{
          margin-top: 15px;
        }
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

  .pack,.timer,.seat {
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
      height: 60%;
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

  .history {
    margin-top: 15px;
    font-size: 12px;
    color: #c1c1c1;
    float: left;
    line-height: 24px;
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
