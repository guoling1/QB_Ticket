<template lang="html">
  <div class="main">
    <div class="banner">
      <span class="first">前一天</span>

      <div class="calendar">
        10月31日 周五
        <img src="../../assets/calendar.png" alt=""/>
      </div>
      <span class="next show">后一天</span>
    </div>
    <div v-if="stations.empty">没有符合查询条件的车次</div>
    <ul v-if="!stations.empty">
      <li v-for="station in stations.data" @click="router($event,station)">
        <div class="top">
          <span class="checi">{{station.train_code}}</span>

          <div class="topRight">
            <p>￥{{station.low_price}}<span>起</span></p>
            <span class="time">{{station.run_time}}</span>
          </div>
          <div class="topMiddle">
            <div class="form">
              <span class="static">{{station.start_station_name}}</span>
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
        only: false,
        initStations: [],
        // 火车票筛选信息
        screenConfig: this.$store.state.screen.config
      }
    },
    beforeRouteEnter (to, from, next) {
      let date = to.query.date;
      let froms = to.query.from;
      let tos = to.query.to;
      Vue.http.post('/queryTicketPrice/query', {
        from_station: froms, //出发站简码
        to_station: tos, //到达站简码
        train_date: date //乘车日期（yyyy-MM-dd）
      }).then(function (res) {
        next(function (vm) {
          vm.$data.only = to.query.only;
          vm.$data.initStations = res.body.data;
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
        this.$router.push({path: '/ticket/submit-order'});
      }
    },
    computed: {
      stations () {
        if (this.initStations) {
          // 优先筛选条件
          let config = this.$store.state.screen.config;
          console.log(config);
          // 计算最低价
          let checkPrice = ['edz_price', 'gjrw_price', 'gjrws_price', 'qtxb_price', 'rw_price', 'rwx_price', 'rz_price', 'swz_price', 'tdz_price', 'wz_price', 'ydz_price', 'yw_price', 'ywx_price', 'ywz_price', 'yz_price'];
          for (let i = 0; i < this.initStations.length; i++) {
            let lowPrice = 1000000000;
            for (let m = 0; m < checkPrice.length; m++) {
              if (this.initStations[i][checkPrice[m]] != 0) {
                if (lowPrice >= this.initStations[i][checkPrice[m]]) {
                  lowPrice = this.initStations[i][checkPrice[m]]
                }
              }
            }
            this.initStations[i]['low_price'] = lowPrice;
          }
          return {
            empty: false,
            data: this.initStations
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
