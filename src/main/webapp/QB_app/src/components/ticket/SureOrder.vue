<template lang="html">
  <div class="main">
    <div class="from">
      <div class="train-info">
        <div class="left">
          <div class="time">{{sureOrder.startTime}}</div>
          <div class="place">{{sureOrder.fromStationName}}</div>
          <div class="date">{{otherData.startShow}}</div>
        </div>
        <div class="middle">
          <div class="trains">{{sureOrder.checi}}</div>
          <div class="ch"></div>
          <div class="date">耗时{{otherData.runShow}}</div>
        </div>
        <div class="right">
          <div class="time">{{sureOrder.endTime}}</div>
          <div class="place">{{sureOrder.toStationName}}</div>
          <div class="date">{{otherData.arriveShow}}</div>
        </div>
      </div>
      <div class="space">
        <div class="group no-border">
          <div class="prompt">{{pageInfo.table}}</div>
          <div class="write right"><span class="price">￥{{pageInfo.price}}</span></div>
        </div>
      </div>
      <div class="space">
        <div class="group no-border" @click="login">
          <div class="logo"></div>
          <div class="write">使用12306账号登录</div>
        </div>
      </div>
      <div class="space no-border">
        <div class="group no-border" v-for="passenger in passengers">
          <div class="list"></div>
          <div class="write no-prompt">
            <span class="name">{{passenger.name}}</span>
            {{passenger.identy}}
            <span class="info">{{passenger.personType}}票</span>
          </div>
        </div>
      </div>
      <div class="space no-padding">
        <div class="handle">
          <div class="btn" @click="contact">添加/编辑乘客</div>
          <div class="line"></div>
          <div class="btn">添加儿童</div>
        </div>
      </div>
      <div class="space">
        <div class="group no-border">
          <div class="prompt">联系手机</div>
          <input type="text" class="ipt" placeholder="通知出票信息" v-model="sureOrder.mobile">
        </div>
      </div>
      <div class="space">
        <div class="group no-border" @click="packShow">
          <div class="prompt">套餐类型</div>
          <div class="write empty" v-if="sureOrder.buyTicketPackageId==1">不购买</div>
          <div class="write empty" v-if="sureOrder.buyTicketPackageId==2">￥20/人 极速出票</div>
          <div class="write empty" v-if="sureOrder.buyTicketPackageId==3">￥30/人 优先出票</div>
        </div>
      </div>
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
          <div class="right" @click="submit($event,true)">提交订单</div>
        </div>
      </div>
    </div>
    <div class="loading" v-if="loading">占座中...</div>
    <div class="pack" v-show="pack">
      <div class="select">
        <div class="xx"></div>
        <ul>
          <li @click="packHide(2)" v-bind:class="{active:sureOrder.buyTicketPackageId==2}"><span>¥ 20/人套餐</span>
            极速出票，赠送78万保险
          </li>
          <li @click="packHide(3)" v-bind:class="{active:sureOrder.buyTicketPackageId==3}"><span>¥ 30/人套餐</span>
            优先出票，赠送300万保险
          </li>
          <li @click="packHide(1)" v-bind:class="{active:sureOrder.buyTicketPackageId==1}">不购买 出票慢，失败的可能性增加</li>
        </ul>
      </div>
    </div>
    <div class="skip" v-show="skip">
      <div class="show">
        <div class="xx"></div>
        <div class="btn">
          <div class="sub" @click="submit($event)">放弃</div>
          <div class="close" @click="skipHide">增加保障</div>
        </div>
      </div>
    </div>
    <contacts></contacts>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  import Contacts from './Contacts.vue'

  const zwCode = {
    '商务座': '9',
    '特等座': 'P',
    '一等座': 'M',
    '二等座': 'O',
    '高级软卧': '6',
    '软卧': '4',
    '硬卧': '3',
    '软座': '2',
    '硬座': '1'
  };

  export default {
    name: 'menu',
    components: {
      Contacts
    },
    data: function () {
      return {
        sureOrder: {
          appId: "",     //appid
          uid: "",  //用户id
          mobile: '',   //联系手机号
          price: 00.0,    //票价
          fromStationName: "",  //出发站
          fromStationCode: "",     //出发站code
          toStationName: "",     //到达站
          toStationCode: "",      //到达站code
          zwCode: "",               //坐席类型
          startDate: "",   //发车日期
          endDate: "",     //到达日期
          startTime: "",       //发车时间
          endTime: "",           //到达时间
          runTime: "",           //运行分钟
          checi: "",           //车次
          buyTicketPackageId: 3,    //出票套餐
          passengers: [] // 乘客信息
        },
        otherData: {
          table: '',
          startShow: '',
          arriveShow: '',
          runShow: '',
          passengers: []
        },
        loading: false,
        pack: false,
        skip: false
      }
    },
    beforeRouteEnter (to, from, next) {
      let sessionPreOrder = JSON.parse(sessionStorage.getItem('preOrder'));
      let sessionPreDate = JSON.parse(sessionStorage.getItem('preDate'));
      next(function (vm) {
        vm.$data.sureOrder.appId = to.query.appid;
        vm.$data.sureOrder.uid = to.query.uid;
        vm.$data.sureOrder.price = to.query.price;
        vm.$data.otherData.table = to.query.table;
        vm.$data.sureOrder.fromStationName = sessionPreOrder.from_station_name;
        vm.$data.sureOrder.fromStationCode = sessionPreOrder.from_station_code;
        vm.$data.sureOrder.toStationName = sessionPreOrder.to_station_name;
        vm.$data.sureOrder.toStationCode = sessionPreOrder.to_station_code;
        vm.$data.sureOrder.zwCode = zwCode[to.query.table];
        vm.$data.sureOrder.startTime = sessionPreOrder.start_time;
        vm.$data.sureOrder.endTime = sessionPreOrder.arrive_time;
        vm.$data.sureOrder.runTime = sessionPreOrder.run_time_minute;
        vm.$data.sureOrder.checi = sessionPreOrder.train_code;
        vm.$data.sureOrder.startDate = sessionPreDate.startTime.post;
        vm.$data.sureOrder.endDate = sessionPreDate.arriveTime.post;
        vm.$data.otherData.startShow = sessionPreDate.startTime.show;
        vm.$data.otherData.arriveShow = sessionPreDate.arriveTime.show;
        vm.$data.otherData.runShow = sessionPreDate.runTime;
      });
    },
    methods: {
      login: function () {
        this.$router.push({path: '/ticket/login',query:{appid:this.$data.sureOrder.appId,uid:this.$data.sureOrder.uid}});
      },
      packShow: function () {
        this.$data.pack = true;
      },
      packHide: function (num) {
        this.$data.pack = false;
        this.$data.sureOrder.buyTicketPackageId = num;
      },
      skipHide: function(){
        this.$data.skip = false;
      },
      submit: function (event,skip) {
        // 判断是否选择了套餐
        if(this.$data.sureOrder.buyTicketPackageId==1&&!!skip){
          console.log('请选择抢票套餐');
          this.$data.skip = true;
          return false;
        }
        this.$data.skip = false;
        var polling = '';
        const pollFun = (id)=>{
          this.$http.post('/order/queryById', {orderFormId: id}).then(function (res) {
            if (res.data.code == 1 && res.data.data.status == 3) {
              clearInterval(polling);
              this.$router.push({
                path: '/ticket/pay-order',
                query: {appid: this.$data.sureOrder.appId, uid: this.$data.sureOrder.uid, id: res.data.data.orderFormId}
              });
            } else if (res.data.code == 1 && res.data.data.status == 4) {
              clearInterval(polling);
              console.log("占座失败");
            }
          })
        }
        this.$http.post('/ticket/submitOrder', this.$data.sureOrder).then(function (res) {
          if (res.data.code == 1) {
            // 这里 轮询 等待回调
            this.$data.loading = true;
            polling = setInterval(function () {
              pollFun(res.data.data.orderFormId);
            }, 1500);
          } else {
            console.log(res.data.message);
          }
        }, function (err) {
          console.log(err);
        });
      },
      contact: function () {
        this.$store.commit("CONTACT_OPEN", {
          ctrl: true
        });
      },
      station: function () {
        this.$store.commit('STATION_CTRL', true);
      }
    },
    computed: {
      price: function () {
        console.log(this.$data.sureOrder.passengers);
        return this.$data.sureOrder.passengers;
      },
      passengers: function () {
        let storeDate = this.$store.state.contact.info;
        let data = [];
        this.$data.sureOrder.passengers = [];
        let type = {
          '成人': 1, '儿童': 2, '学生': 3, '伤残军人': 4
        };
        for (let i in storeDate) {
          if (storeDate[i]) {
            data.push(storeDate[i]);
            this.$data.sureOrder.passengers.push({
              id: storeDate[i].id,
              piaoType: type[storeDate[i].personType]
            })
          }
        }
        return data;
      },
      pageInfo () {
        return {
          table: this.$data.otherData.table,
          price: this.$data.sureOrder.price
        }
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
    background-color: #f5f5f5;
  }

  .train-info {
    padding: 20px 15px;
    background-color: #FFF;
    margin-bottom: 5px;
    overflow: hidden;
    position: relative;
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
      .ipt {
        float: left;
        height: 54px;
        border: none;
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

  .loading {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.7);
    font-size: 15px;
    color: #FFF;
    text-align: center;
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
    }
  }
  .skip{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 88;
    background: rgba(0, 0, 0, 0.5);
    .show{
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
      .btn{
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 50px;
        div{
          float: left;
          width: 50%;
          height: 50px;
          line-height: 50px;
          text-align: center;
          &.sub{
            font-size: 15px;
            color: #4ab9f1;
            border-top: 1px solid #f5f5f5;
          }
          &.close{
            font-size: 15px;
            color: #FFF;
            background-color: #4ab9f1;
          }
        }
      }
    }
  }
</style>
