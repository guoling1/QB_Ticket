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
          <div class="left" @click="station('stationONE')">{{$dataT.from}}</div>
          <img class="middle" src="../../assets/exchange.png">

          <div class="right" @click="station('stationTWO')">{{$dataT.to}}</div>
        </div>
      </div>
      <div class="time" @click="time('dateONE')">
        <div class="show">{{$dataT.date}}</div>
      </div>
      <div class="option">
        <div class="show">只看高铁/动车</div>
        <div class="check" v-bind:class="{active:$onlyG}" @click="onlyU">
          <div class="btn"></div>
        </div>
      </div>
      <router-link class="submit"
                   :to="{path:'/ticket/train-menu/train',query:{date:$from.date,from:$from.from,to:$from.to,only:$onlyG}}">
        查询
      </router-link>
      <div class="history">
        <div>查询历史</div>
        <div>清空</div>
      </div>
      <div class="know">订票须知<span></span></div>
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
        onlyG: false
      }
    },
    methods: {
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
        this.$data.onlyG = !this.$data.onlyG;
      }
    },
    computed: {
      $onlyG: function () {
        return this.$data.onlyG
      },
      $dataT: function () {
        return {
          date: this.$store.state.date.scope.dateONE.time,
          from: this.$store.state.station.scope.stationONE.station,
          to: this.$store.state.station.scope.stationTWO.station
        }
      },
      $from: function () {
        return {
          date: this.$store.state.date.scope.dateONE.code,
          from: this.$store.state.station.scope.stationONE.code,
          to: this.$store.state.station.scope.stationTWO.code
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
</style>
