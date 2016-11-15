<template lang="html">
  <div class="main">
    <div class="banner">
      <!--<span class="first">前一天</span>-->

      <div class="calendar">
        {{dateWeek}}
        <img src="../../assets/calendar.png" alt=""/>
      </div>
      <!--<span class="next show">后一天</span>-->
    </div>
    <div v-if="stations.empty">没有符合查询条件的车次</div>
    <ul v-if="!stations.empty">
      <li v-for="station in stations.data" @click="router($event,station)">
        <div class="top">
          <span class="checi">{{station.train_code}}</span>

          <div class="topRight">
            <p>￥{{station.low_price}}<span>起</span></p>
            <span class="time">{{station.runTimeShow}}</span>
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
        <div class="bottom">
          <p v-if="station.edz_num!='--'">二等座:<span class="num">{{station.edz_num}}</span>张</p>

          <p v-if="station.ydz_num!='--'">一等座:<span class="num">{{station.ydz_num}}</span>张</p>

          <p v-if="station.swz_num!='--'">商务座:<span class="num">{{station.swz_num}}</span>张</p>

          <p v-if="station.yz_num!='--'">硬座:<span class="num">{{station.yz_num}}</span>张</p>

          <p v-if="station.rz_num!='--'">软座:<span class="num">{{station.rz_num}}</span>张</p>

          <p v-if="station.yw_num!='--'">硬卧:<span class="num">{{station.yw_num}}</span>张</p>

          <p v-if="station.rw_num!='--'">软卧:<span class="num">{{station.rw_num}}</span>张</p>

          <p v-if="station.wz_num!='--'">无座:<span class="num">{{station.wz_num}}</span>张</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  export default {
    name: 'menu',
    data () {
      return {
        common: {
          appid: '',
          uid: ''
        },
        dateHttp: '',
        dateWeek: '',
        only: false,
        initStations: [],
        // 火车票筛选信息
        screenConfig: this.$store.state.screen.config
      }
    },
    beforeRouteEnter (to, from, next) {
      Vue.http.post('/queryTicketPrice/query', {
        appid: to.query.appid, //商户
        uid: to.query.uid, //用户id
        from_station: to.query.formCode, //出发站简码
        to_station: to.query.toCode, //到达站简码
        from_station_name: to.query.formName,
        to_station_name: to.query.toName,
        train_date: to.query.dateHttp //乘车日期（yyyy-MM-dd）
      }).then(function (res) {
        next(function (vm) {
          vm.$data.only = to.query.only;
          for (let i = 0; i < res.body.data.length; i++) {
            let runMin = res.body.data[i].run_time_minute;
            let runH = 0;
            let runM = 0;
            if (runMin >= 60) {
              runH = parseInt(runMin / 60);
              runM = runMin % 60;
            }
            res.body.data[i]['runTimeShow'] = runH + '小时' + runM + '分钟';
          }
          vm.$data.initStations = res.body.data;
          vm.$data.dateHttp = to.query.dateHttp;
          vm.$data.dateWeek = to.query.dateWeek;
          vm.$data.common.appid = to.query.appid;
          vm.$data.common.uid = to.query.uid;
        });
      }, function (err) {
        console.log(err);
        next(false);
      })
    },
    methods: {
      router: function (event, station) {
        // 路由跳转前,查询信息必须被存储在 sessionStorage 存储时注意要转json
        sessionStorage.setItem('preOrder', JSON.stringify(station));
        this.$router.push({
          path: '/ticket/submit-order',
          query: {
            appid: this.$data.common.appid,
            uid: this.$data.common.uid,
            dateHttp: this.$data.dateHttp,
            dateWeek: this.$data.dateWeek
          }
        });
      }
    },
    computed: {
      time: function () {
        let runMin = this.$data.initStations.run_time_minute;
        let runH = 0;
        let runM = 0;
        if (runMin >= 60) {
          runH = parseInt(runMin / 60);
          runM = runMin % 60;
        }
        return runH + '小时' + runM + '分钟';
      },
      stations () {
        if (this.initStations) {
          // 优先筛选条件
          //只看动车
          if (this.$data.only) {
            for (var i = 0; i < this.initStations.length; i++) {
              if (this.initStations[i].train_type == ("G" || "D")) {
                this.$store.state.screen.config.trains.D = false;
                this.$store.state.screen.config.trains.G = false;
                this.$store.state.screen.config.trains.Z = true;
                this.$store.state.screen.config.trains.K = true;
              } else {
              }
            }
          }
          let config = this.$store.state.screen.config;
          //坐席
          function table(arr) {
            let ary = [];
            var flag = false;
            for (let j in config.table) {
              if (config.table[j] == false) {
                flag = true
              }
            }
            if (flag) {
              for (let i = 0; i < arr.length; i++) {
                for (let j in config.table) {
                  if (config.table[j] == false && !!arr[i][j + "_num"] && arr[i][j + "_num"] != "--") {
                    ary.push(arr[i]);
                    break;
                  }
                }
              }
              return ary;
            }
            return arr;
          }

          //车次类型
          function trains(arr) {
            let ary = [];
            var flag = false;
            for (let j in config.trains) {
              if (config.trains[j] == false) {
                flag = true;
              }
            }
            if (flag) {
              for (let i = 0; i < arr.length; i++) {
                for (let j in config.trains) {
                  if (config.trains[j] == false && arr[i].train_type == j) {
                    ary.push(arr[i]);
                    break;
                  }
                }
              }
              return ary;
            }
            return arr;
          }

          //出发时间
          function fromTime(arr) {
            let ary = [];
            for (var i = 0; i < arr.length; i++) {
              var t = parseInt(arr[i].start_time)
              if (0 <= t && t < 6) {
                t = 1
              } else if (6 <= t && t < 12) {
                t = 2;
              } else if (12 <= t && t < 18) {
                t = 3;
              } else if (18 <= t && t < 24) {
                t = 4
              }
              ;
              if ((t == config.startTime) || (0 == config.startTime)) {
                ary.push(arr[i]);
              }
            }
            return ary;
          }

          //到达时间
          function toTime(arr) {
            let ary = [];
            for (var i = 0; i < arr.length; i++) {
              var t = parseInt(arr[i].arrive_time)
              if (0 < t && t <= 6) {
                t = 1
              } else if (6 < t && t <= 12) {
                t = 2;
              } else if (12 < t && t <= 18) {
                t = 3;
              } else if (18 < t && t <= 24) {
                t = 4
              }
              ;
              if (t == config.endTime || 0 == config.endTime) {
                ary.push(arr[i]);
              }
            }
            return ary;
          }

          var arr = toTime(fromTime(trains(table(this.initStations))))
          //出发早晚
          function sort(arr) {
            arr.sort(function (a, b) {
              return a.run_time_minute - b.run_time_minute;
            })
          }

          if (!config.sort) {
            sort(arr)
          }
          //只看有票
          function ticket(arr) {
            let ary = [];
            for (let i = 0; i < arr.length; i++) {
              var reg = /_num/g;
              for (var j in arr[i]) {
                if (reg.test(j) == true && arr[i][j] != "0" && arr[i][j] != "--"&& arr[i][j] != "*") {
                  ary.push(arr[i]);
                  break
                }
              }
              continue;
            }
            return ary;
          }

          if (!config.ticket) {
            arr = ticket(arr);
          }


          // 计算最低价
          let checkPrice = ['edz_price', 'gjrw_price', 'gjrws_price', 'qtxb_price', 'rw_price', 'rwx_price', 'rz_price', 'swz_price', 'tdz_price', 'wz_price', 'ydz_price', 'yw_price', 'ywx_price', 'ywz_price', 'yz_price'];
          for (let i = 0; i < arr.length; i++) {
            let lowPrice = 1000000000;
            for (let m = 0; m < checkPrice.length; m++) {
              if (arr[i][checkPrice[m]] != 0) {
                if (lowPrice >= arr[i][checkPrice[m]]) {
                  lowPrice = arr[i][checkPrice[m]]
                }
              }
            }
            arr[i]['low_price'] = lowPrice;
          }
          return {
            empty: false,
            data: arr
          };
        }
        return {
          empty: true
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
    overflow: auto;
    background: #f6f6f6;
    .flexItem(1, 100%);

    .banner {
      padding: 9px 20px;
      height: 45px;
      width: 100%;
      background: #4ab9f1;

      .first, .next {
        height: 27px;
        line-height: 27px;
        font-size: 12px;
        color: #91daff;
        float: left;
      }

      .next {
        float: right;

      }

      .show {
        color: #fff;
      }

      .calendar {
        display: inline-block;
        border: 1px solid #84d0f6;
        border-radius: 2px;
        width: 124px;
        height: 26px;
        line-height: 26px;
        color: #fff;
        font-size: 12px;
        overflow: hidden;

        img {
          width: 16px;
          height: 16px;
          vertical-align: -3px;
          margin-left: 12px;
        }
      }
    }
    ul li {
      margin-top: 5px;
      padding: 15px;
      height: 101px;
      width: 100%;
      background: #fff;

      &.select {
        background: #f6f6f6;
      }

      .top {
        margin-bottom: 17px;
        height: 34px;
        line-height: 34px;

        .checi {
          float: left;
          font-size: 22px;
          font-weight: bold;
        }

        .topRight {
          float: right;
          margin: 0 0 17px 0;
          height: 34px;
          line-height: 34px;

          p {
            color: #f14242;
            font-size: 14px;
            font-weight: bold;
            span {
              font-size: 10px;
              color: #f14242;
              font-weight: normal;
            }
          }
          > span {
            color: #999;
            font-size: 12px;

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

      .bottom {
        height: 33.5px;
        line-height: 33.5px;
        border-top: 1px dashed #e0e0e0;
        font-size: 12px;
        color: #999;
        text-align: left;

        p {
          display: inline-block;
          height: 33.5px;
          line-height: 33.5px;
          margin-right: 18px;
        }
        span {
          color: #3cb687;
        }
      }
    }
  }
</style>
