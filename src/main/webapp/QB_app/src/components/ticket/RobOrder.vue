<template lang="html">
  <div class="main">
    <div class="from">
      <div class="space">
        <div class="goto">
          <div class="side show">
            <div class="left">出发城市</div>
            <div class="right">到达城市</div>
          </div>
          <div class="side write">
            <div class="left">{{$submitInfo.fromStationName}}</div>
            <img class="middle" src="../../assets/exchange-blue.png">

            <div class="right">{{$submitInfo.toStationName}}</div>
          </div>
        </div>
        <div class="group">
          <div class="prompt">已选车次</div>
          <div class="write no-prompt right">{{$submitInfo.trainCodes}}</div>
        </div>
        <div class="group no-border">
          <div class="prompt">已选坐席</div>
          <div class="write no-prompt right">{{$submitInfo.seatTypes}}</div>
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
            <span class="info">{{passenger.piaoType}}票</span>
          </div>
        </div>
        <div class="group no-border" v-for="child in childs">
          <div class="list"></div>
          <div class="write no-prompt">
            <span class="name">{{child.name}}</span>
            <span class="info">{{child.piaoType}}票</span>
          </div>
        </div>
      </div>
      <div class="space no-padding">
        <div class="handle">
          <div class="btn" @click="contact">添加/编辑乘客</div>
          <div class="line"></div>
          <div class="btn" @click="addChild">添加儿童</div>
        </div>
      </div>
      <div class="space">
        <div class="group no-border" @click="packShow">
          <div class="prompt">套餐类型</div>
          <div class="write empty" v-if="submitInfo.buyTicketPackageId==1">不购买</div>
          <div class="write empty" v-if="submitInfo.buyTicketPackageId==2">￥20/人 极速出票</div>
          <div class="write empty" v-if="submitInfo.buyTicketPackageId==3">￥30/人 优先出票</div>
        </div>
      </div>
      <div class="space">
        <div class="group no-border">
          <div class="prompt">联系手机</div>
          <input type="text" class="ipt" placeholder="通知出票信息" v-model="submitInfo.phone">
        </div>
      </div>
      <div class="submit" @click="submit">立即抢票</div>
    </div>
    <div class="pack" v-show="pack">
      <div class="select">
        <div class="xx"></div>
        <ul>
          <li @click="packHide(2)" v-bind:class="{active:submitInfo.buyTicketPackageId==2}"><span>¥ 20/人套餐</span>
            极速出票，赠送78万保险
          </li>
          <li @click="packHide(3)" v-bind:class="{active:submitInfo.buyTicketPackageId==3}"><span>¥ 30/人套餐</span>
            优先出票，赠送300万保险
          </li>
          <li @click="packHide(1)" v-bind:class="{active:submitInfo.buyTicketPackageId==1}">不购买 出票慢，失败的可能性增加</li>
        </ul>
      </div>
    </div>
    <div class="bank" v-show="payInfo.bank">
      <div class="bankList" v-show="payInfo.list">
        <div class="xx"></div>
        <ul>
          <li v-for="(card,index) in payInfo.cardList" v-bind:class="{active:payInfo.index==index}"
              @click="select($event,card,index)">
            <div class="logo">logo</div>
            <div class="word">这是银行</div>
            <div class="word">{{card.cardNo}}</div>
            <div class="small">储蓄卡</div>
          </li>
        </ul>
        <div class="new" @click="newCard">使用新卡支付</div>
        <div class="btn" @click="choose">确认付款 ￥{{$payInfo.price}}</div>
      </div>
      <div class="checkout" v-show="!payInfo.list">
        <div class="xx"></div>
        <div class="ul">
          <div class="logo">logo</div>
          <div class="word">这是银行</div>
          <div class="word">{{payInfo.checkout.cardNo}}</div>
          <div class="small">储蓄卡</div>
        </div>
        <div class="ul">
          <input type="text" placeholder="请输入验证码" v-model="payInfo.checkCode">
          <button v-show="payInfo.sendCtrl" @click="send">获取验证码</button>
          <button v-show="!payInfo.sendCtrl">{{payInfo.timer}}</button>
        </div>

        <div class="btn" @click="pay">确认付款 ￥{{$payInfo.price}}</div>
      </div>
    </div>
    <contacts></contacts>
    <!-- 添加儿童 -->
    <div class="content" v-if="show">
      <ul>
        <li>
          <label for="name">乘客姓名</label>
          <input type="text" name="name" id='name' value="">
        </li>
        <li>
          <label for="sex">乘客性别</label>
          <label style="margin-left:10px;color:#999"><input type="radio" name="sex" value="0" checked="checked">男</label>
          <label style="margin-left:20px;color:#999"><input type="radio" name="sex" value="1">女</label>
        </li>
        <li class="typeLi">
          <label for="birthday">出生日期</label>
          <input type="text" name="birthday" id='birthday' placeholder="出生年月日，如：20160101">
        </li>
      </ul>
      <div class="sure" @click="sev">保存</div>
    </div>
  </div>
</template>

<script lang="babel">
  import Contacts from './Contacts.vue'
  import Vue from 'vue'

  export default {
    name: 'menu',
    components: {
      Contacts
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
        pack: false,
        payInfo: {
          orderId: '',
          price: 00.0,
          bank: false,
          list: true,
          sendCtrl: true,
          timer: 60,
          cardList: '',
          peopleInfo: '',
          checkout: {},
          index: 0,
          checkCode: '',
          sn: ''
        },
        show:false,
        childs:[]
      }
    },
    beforeRouteEnter (to, from, next) {
      next(function (vm) {
        vm.$data.submitInfo = JSON.parse(sessionStorage.getItem('robOrder'));
      });
    },
    methods: {
      addChild:function(){
        if(this.$data.submitInfo.grabPassengers.length==0){
          console.log("请先添加成人")
        }else{
          this.$data.show=!this.$data.show
        }

      },
      sev:function(){
        var addPerson={
          uid:1,
          appid:1,
          name:document.querySelector('#name').value,
          sex:document.querySelector(':checked').value,
          birthday:document.querySelector('#birthday').value,
          personType:2
        }
        Vue.http.post('/contactInfo/add',JSON.stringify(addPerson))
          .then((res)=>{
            console.log(res);
            if(res.data.code==1){
              this.$data.show=!this.$data.show
              addPerson.id=res.data.data;
              this.$data.childs.push(addPerson);
          }
        })
        .catch(function(err){
          console.log(err);
        })
      },
      login: function () {
        this.$router.push({
          path: '/ticket/login',
          query: {appid: this.$data.submitInfo.appId, uid: this.$data.submitInfo.uid}
        });
      },
      select: function (event, card, index) {
        this.$data.payInfo.checkout = card;
        this.$data.payInfo.index = index;
      },
      choose: function () {
        this.$data.payInfo.list = false;
      },
      send: function () {
        this.$http.post('/authen/getCode', {
          phone: this.$data.payInfo.checkout.phone,//手机号
          amount: this.$data.payInfo.price, //支付金额
          uid: this.$data.submitInfo.uid, //三方商户用户id
          appid: this.$data.submitInfo.appId //三方商户唯一标示appid
        }).then(function (res) {
          if (res.data.code == 1) {
            this.$data.payInfo.sendCtrl = false;
            this.$data.payInfo.sn = res.data.data;
            let polling = '';
            const pollFun = ()=>{
              this.$data.payInfo.timer--;
              if (this.$data.payInfo.timer < 0) {
                this.$data.payInfo.timer = 60;
                this.$data.payInfo.sendCtrl = true;
                clearInterval(polling);
              }
            }
            polling = setInterval(pollFun, 1000);
          } else {
            console.log(res.data.message);
          }
        }, function (err) {
          console.log(err);
        })
      },
      pay: function () {
        let data = new Date(),
          year = data.getFullYear() + '',
          month = data.getMonth() + 1 + '',
          day = data.getDate() + '',
          hour = data.getHours() + '',
          min = data.getMinutes() + '',
          ss = data.getSeconds() + '';
        let random = parseInt(Math.random() * 89999 + 10000) + '';
        let nonceStr = year + month + day + hour + min + ss + random;
        this.$http.post('/authen/toPayGrabByCid', {
          uid: this.$data.submitInfo.uid,
          appid: this.$data.submitInfo.appId,
          orderId: this.$data.payInfo.orderId, //订单编号，不是订单号，是金开门系统唯一id
          nonceStr: nonceStr, //随机字符串，每次请求都不一样，生成规则（yyyyMMddHHmmssSSS+5个随机数字）
          cId: this.$data.payInfo.checkout.id,//银行卡id
          vCode: this.$data.payInfo.checkCode, //手机验证码
          sn: this.$data.payInfo.sn //调用短信接口返回的值
        }).then(function (res) {
          if (res.data.code == 1) {
            this.$router.push({
              path: '/ticket/rob-detail',
              query: {
                appid: this.$data.submitInfo.appId,
                uid: this.$data.submitInfo.uid,
                orderid: this.$data.payInfo.orderId
              }
            });
          } else {
            console.log(res);
          }
        }, function (err) {
          console.log(err);
        })
      },
      newCard: function () {
        this.$router.push({
          path: '/pay/second-add', query: {
            appid: this.$data.submitInfo.appId,
            uid: this.$data.submitInfo.uid,
            orderid: this.$data.payInfo.orderId,
            price: this.$data.payInfo.price,
            name: this.$data.payInfo.peopleInfo.accountName,
            card: this.$data.payInfo.peopleInfo.cardId,
            payType: 1
          }
        });
      },
      packShow: function () {
        this.$data.pack = true;
      },
      packHide: function (num) {
        this.$data.pack = false;
        this.$data.submitInfo.buyTicketPackageId = num;
      },
      contact: function () {
        this.$store.commit("CONTACT_OPEN", {
          ctrl: true
        });
      },
      submit: function () {
        this.$data.submitInfo.grabPassengers.concat(this.$data.child)
        let data = this.$data.submitInfo;
        if (data.seatTypes == '1,3,4,O,M,9') {
          data.seatTypes = '全部坐席';
        } else {
          //data.seatTypes = data.seatTypes.replace('无座', '0');
          data.seatTypes = data.seatTypes.replace('硬座', '1');
          //data.seatTypes = data.seatTypes.replace('软座', '2');
          data.seatTypes = data.seatTypes.replace('硬卧', '3');
          data.seatTypes = data.seatTypes.replace('软卧', '4');
          //data.seatTypes = data.seatTypes.replace('高级软卧', '6');
          data.seatTypes = data.seatTypes.replace('二等座', 'O');
          data.seatTypes = data.seatTypes.replace('一等座', 'M');
          //data.seatTypes = data.seatTypes.replace('特等座', 'P');
          data.seatTypes = data.seatTypes.replace('商务座', '9');
        }
        this.$http.post('/ticket/grab', data).then(function (res) {
          if (res.data.code == 1) {
            this.$data.payInfo.price = res.data.data.price;
            this.$data.payInfo.orderId = res.data.data.grabTicketFormId;
            this.$http.post('/card/list', {
              appid: this.$data.submitInfo.appId,
              uid: this.$data.submitInfo.uid
            }).then(function (rs) {
              if (rs.data.code == 1) {
                if (rs.data.data.cardList) {
                  this.$data.payInfo.cardList = rs.data.data.cardList;
                  this.$data.payInfo.checkout = rs.data.data.cardList[0];
                  this.$data.payInfo.peopleInfo = rs.data.data.userCardInfo;
                  this.$data.payInfo.bank = true;
                } else {
                  this.$router.push({
                    path: '/pay/first-add',
                    query: {
                      appid: this.$data.submitInfo.appId,
                      uid: this.$data.submitInfo.uid,
                      id: this.$data.payInfo.orderId,
                      price: res.data.payInfo.price,
                      payType: 1
                    }
                  });
                }
              } else {
                console.log(res.data.message);
              }
            }, function (err) {
              console.log(err);
            });
          } else {
            console.log(res.data.message);
          }
        }, function (err) {
          console.log(err);
        })
      }
    },
    computed: {
      $payInfo: function () {
        return this.$data.payInfo;
      },
      passengers: function () {
        let storeDate = this.$store.state.contact.info;
        console.log(storeDate);
        let data = [];
        this.$data.submitInfo.grabPassengers = [];
        let type = {
          '成人': 1, '儿童': 2, '学生': 3, '伤残军人': 4
        };
        for (let i in storeDate) {
          if (storeDate[i]) {
            data.push(storeDate[i]);
            this.$data.submitInfo.grabPassengers.push({
              id: storeDate[i].id,
              name: storeDate[i].name,
              piaoType: type[storeDate[i].personType]
            })
          }
        }
        return data;
      },
      $submitInfo: function () {
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

  .main {
    width: 100%;
    height: 100%;
    overflow: hidden;
    .flexItem(1, 100%);
    background-color: #f5f5f5;
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

  .submit {
    display: block;
    width: 100%;
    height: 49px;
    line-height: 49px;
    background-color: #4ab9f1;
    font-size: 18px;
    font-weight: bold;
    color: #FFF;
    position: fixed;
    bottom: 0;
    left: 0;
  }

  .bank {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 88;
    background: rgba(0, 0, 0, 0.5);
    .bankList, .checkout {
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
      .logo {
        float: left;
        margin-right: 15px;
      }
      .word {
        float: left;
        margin-right: 15px;
        font-size: 15px;
        color: #000;
      }
      .small {
        float: left;
        font-size: 12px;
        color: #999;
      }
      .ul {
        width: 100%;
        height: 45px;
        line-height: 45px;
        text-align: left;
        padding-left: 15px;
        color: #999;
        border-bottom: 1px solid #f5f5f5;
        button {
          float: right;
          height: 44px;
          padding: 0 15px;
          font-size: 12px;
          color: #37abe5;
          background-color: #FFF;
        }
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
      .new {
        width: 100%;
        height: 45px;
        padding-left: 50px;
        line-height: 45px;
        text-align: left;
        background: url("../../assets/new.png") no-repeat 15px 10px;
        background-size: 25px 25px;
        border-bottom: 1px solid #f5f5f5;
        font-size: 15px;
        color: #4ab9f1;
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
        text-align: center;
      }
    }
  }

  .content{
    width: 100%;
    height: 100%;
    background: #fff;
    position: absolute;;
    top: 64px;
    left: 0;
    ul{
      li{
        width: 100%;
        height: 48px;
        line-height: 48px;
        font-size: 15px;
        text-align: left;
        padding:0 15px;
        background: #fff;
        border-bottom: 1px solid #ebebeb;

        &.typeLi{
          background: url("../../assets/prompt-arrow.png") no-repeat 96%;
          background-size: 8px 11px;
        }

        input{
          border: none;
          color:#999;
          &:focus{
            outline: none;
          }
        }
      }
    }
    .sure{
      position: fixed;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 50px;
      line-height: 50px;
      font-size: 15px;
      background: #4ab9f1;
      color: #fff;
    }
    }

</style>
