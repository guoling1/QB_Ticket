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
      <div class="space" v-if="isLogin">
        <div class="group no-border" @click="login">
          <div class="logo"></div>
          <div class="write">使用12306账号登录</div>
        </div>
      </div>
      <div class="space no-border">
        <div class="group no-border" v-for="(passenger,index) in passengers.data">
          <div class="list" @click="minus($event,index)"></div>
          <div class="write no-prompt">
            <span class="name">{{passenger.name}}</span>
            <span v-if="passenger.personType==1">{{passenger.identy}}</span>
            <span v-if="passenger.personType==2">请使用{{passengers.data[0].name}}的身份证取票</span>
            <span class="info">{{passenger.piaoType}}票</span>
          </div>
        </div>
        <div class="group no-border" v-for="(child,index) in $$childs">
          <div class="list" @click="minusChild($event,index)"></div>
          <div class="write no-prompt">
            <span class="name">{{child.name}}</span>
            <span>请使用{{passengers.data[0].name}}的身份证取票</span>
            <span class="info">儿童票</span>
          </div>
        </div>
      </div>
      <div class="state" v-show="$$childs.length>0||passengers.child">
        儿童票按成人价收款,差价在购票成功后退回
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
          <div class="write" v-if="submitInfo.buyTicketPackageId==1">不购买</div>
          <div class="write" v-if="submitInfo.buyTicketPackageId==2">￥20/人 极速出票</div>
          <div class="write" v-if="submitInfo.buyTicketPackageId==3">￥30/人 优先出票</div>
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
    <transition name="fade">
      <div class="pack" v-show="pack" @click="packBGHide($event)">
        <div class="select">
          <div class="space_t">
            <div class="xx" @click="pack=false"></div>
            <div class="word">选择出票套餐</div>
          </div>
          <ul>
            <li @click="packHide(2)" v-bind:class="{active:submitInfo.buyTicketPackageId==2}"><span>¥ 20/人套餐</span>
              极速出票，赠送80万保险<span class="know" @click.stop="insurance=true"></span>
            </li>
            <li @click="packHide(3)" v-bind:class="{active:submitInfo.buyTicketPackageId==3}"><span>¥ 30/人套餐</span>
              优先出票，赠送160万保险<span class="know" @click.stop="insurance=true"></span>
            </li>
            <li @click="packHide(1)" v-bind:class="{active:submitInfo.buyTicketPackageId==1}">不购买 出票慢，失败的可能性增加</li>
          </ul>
          <div class="insurance" v-show="insurance">
            <div class="word">
              合众人寿20元火车意外伤害保险保额：<br>

              1). 成年火车意外险保额80万、火车意外伤害医疗保险责任保额15000元；<br>

              2). 未成年火车意外险保额最高赔付不超过￥10万人民币，意外医疗保险最高赔付不超过15000元；<br>

              合众人寿30元火车意外伤害保险保额：<br>

              成人交通工具意外伤害160万元，0-9周岁交通工具意外伤害20万，10-17周岁交通工具意外伤害50万，意外医疗0.4万元；<br>

              2. 投保年龄： 出生30天到80岁，且身体健康者；<br>

              3. 被保险人在本保险合同载明的保险责任生效当日当次以乘客身份乘坐合法商业运营的客运火车，并遵守承运人关于安全乘坐的规定，自通过车站安全检查时起至被保险人抵达火车票载明的终点走出火车车门时止；<br>

              4. 限购份数： 每人每次至多购买一份，多购无效；<br>

              5. 保障利益：<br>

              火车意外伤害：被保险人以乘客身份乘坐合法商业运营的客运火车，并遵守承运人关于安全乘坐的规定，在保险责任期限内遭受意外伤害所导致的意外伤害残疾和意外伤害身故保险责任。<br>

              火车意外伤害医疗：被保险人以乘客身份乘坐合法商业运营的客运火车，并遵守承运人关于安全乘坐的规定，在保险责期限内遭受意外伤害所导致的意外伤害医疗保险责任。<br>

              6. 如发生合同约定的保险事故，需在十日内通知本公司，报案电话：95515。<br>

              7. （以上说明未尽事宜，以《交通工具意外伤害保险条款（2014版）》为准。）<br>


              备注：<br>

              1、 护照，港澳台通行证可以不用出生日期购买。<br>

              2、 18岁以下儿童可以单独证件投保，无需监护人。<br>

              3、 18岁以下儿童也可以使用监护人证件投保。<br>

              4、 保险生效时间为火车发车时间，被保险人上车车门关闭时起。<br>

              5、 保险结束时间为火车到达时间，被保险人抵达火车票载明的终点走出火车车门时止。<br>

              6、 火车票发车时间之前1小时以上可以退保。<br>
            </div>

            <div class="x" @click="insurance=false">×</div>
          </div>
        </div>
      </div>
    </transition>
    <contacts></contacts>
    <!-- 添加儿童 -->
    <div class="content" v-if="show">
      <ul>
        <li>
          <label for="name">乘客姓名</label>
          <input type="text" name="name" id='name' value="" placeholder="必填">
        </li>
        <li>
          <label for="sex">乘客性别</label>
          <label style="margin-left:10px;color:#999"><input type="radio" name="sex" value="0"
                                                            checked="checked">男</label>
          <label style="margin-left:20px;color:#999"><input type="radio" name="sex" value="1">女</label>
        </li>
        <li class="typeLi">
          <label for="birthday">出生日期</label>
          <input type="text" name="birthday" id='birthday' placeholder="出生年月日，如：20160101">
        </li>
      </ul>
      <div class="prote">儿童票按成人价收款，差价在购票成功后的1-3个工作日退回原支付银行卡。部分银行处理缓慢，不会超过7个工作日。</div>
      <div class="sure" @click="sev">保存</div>
    </div>
    <message></message>
    <pay></pay>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  import Contacts from './Contacts.vue'
  import Message from '../Message.vue'
  import Pay from '../pay/Main.vue'

  export default {
    name: 'menu',
    components: {
      Contacts,
      Message,
      Pay
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
        show: false,
        childs: [],
        isLogin: true,
        insurance: false
      }
    },
    created: function () {
      this.$data.submitInfo = JSON.parse(sessionStorage.getItem('robOrder'));
      this.$http.post('/userInfo/findPhone', {
        appid: this.$data.submitInfo.appId,
        uid: this.$data.submitInfo.uid
      }).then(function (res) {
        this.$data.submitInfo.phone = res.data;
      }, function () {
        console.log('获取手机号失败');
      });
      this.$http.post('/userInfo/isLogin', {
        appid: this.$data.submitInfo.appId,
        uid: this.$data.submitInfo.uid
      }).then(function (res) {
        if (res.data == 1) {
          this.$data.isLogin = false;
        } else {
          this.$data.isLogin = true;
        }
      }, function () {
        console.log('获取12306登录信息失败');
      });
    },
    methods: {
      packBGHide: function (event) {
        if (event.target.className == "pack") {
          this.$data.pack = false;
        }
      },
      minusChild: function (event, index) {
        this.$data.childs.splice(index, 1);
      },
      minus: function (event, index) {
        this.$store.state.contact.info.splice(index, 1);
      },
      addChild: function () {
        if (this.$data.submitInfo.grabPassengers.length == 0) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请先添加随行成人'
          })
        } else {
          this.$data.show = !this.$data.show
        }
      },
      sev: function () {
        var addPerson = {
          uid: this.$data.submitInfo.uid,
          appid: this.$data.submitInfo.appId,
          name: document.querySelector('#name').value,
          sex: document.querySelector(':checked').value,
          birthday: document.querySelector('#birthday').value,
          personType: 2
        };
        var reg = /^[1-9][0-9]{3}(0[1-9]|1[0-2])([0-2][1-9]|3[0-1])$/;
        if (document.querySelector('#name').value == "") {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请填写儿童姓名'
          })
        } else if (document.querySelector('#birthday').value == "") {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请填写出生日期'
          })
        } else if (!reg.test(document.querySelector('#birthday').value)) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请填写正确的出生日期'
          })
        } else {
          this.$http.post('/contactInfo/add', JSON.stringify(addPerson)).then(function (res) {
            this.$data.show = !this.$data.show;
            let newPerson = {};
            newPerson.id = res.data.data;
            newPerson.name = document.querySelector('#name').value;
            newPerson.piaoType = 2;
            this.$data.childs.push(newPerson);
          }, function () {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '添加联系人失败'
            })
          });
        }
      },
      login: function () {
        this.$router.push({
          path: '/ticket/login',
          query: {appid: this.$data.submitInfo.appId, uid: this.$data.submitInfo.uid}
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
        var ary = [];
        for (var i = 0; i < this.$data.submitInfo.grabPassengers.length; i++) {
          ary.push(this.$data.submitInfo.grabPassengers[i].id)
        }
        this.$store.commit("CONTACT_OPEN", {
          ctrl: true,
          keepID: ary
        });
      },
      submit: function () {
        this.$data.submitInfo.grabPassengers = this.$data.submitInfo.grabPassengers.concat(this.$data.childs);
        let data = this.$data.submitInfo;
        if (data.seatTypes == '0,1,2,3,4,6,O,M,P,9') {
          data.seatTypes = '全部坐席';
        } else {
          data.seatTypes = data.seatTypes.replace('无座', '0');
          data.seatTypes = data.seatTypes.replace('硬座', '1');
          data.seatTypes = data.seatTypes.replace('软座', '2');
          data.seatTypes = data.seatTypes.replace('硬卧', '3');
          data.seatTypes = data.seatTypes.replace('软卧', '4');
          data.seatTypes = data.seatTypes.replace('高级软卧', '6');
          data.seatTypes = data.seatTypes.replace('二等座', 'O');
          data.seatTypes = data.seatTypes.replace('一等座', 'M');
          data.seatTypes = data.seatTypes.replace('特等座', 'P');
          data.seatTypes = data.seatTypes.replace('商务座', '9');
        }
        var reg = /^1(3|4|5|7|8)\d{9}$/;
        if (this.$data.submitInfo.grabPassengers == "") {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请添加乘客'
          })
        } else if (!reg.test(this.$data.submitInfo.phone)) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请填写正确的手机号'
          })
        } else {
          this.$http.post('/ticket/grab', data).then(function (res) {
            this.$store.commit('PAY_CALL', {
              appid: this.$data.submitInfo.appId,
              uid: this.$data.submitInfo.uid,
              orderId: res.data.grabTicketFormId,
              price: res.data.price,
              type: 'rob'
            });
          }, function () {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '订单提交失败'
            })
          });
        }
      }
    },
    computed: {
      $payInfo: function () {
        return this.$data.payInfo;
      },
      $$childs: function () {
        this.$data.childs.piaoType = '儿童';
        return this.$data.childs;
      },
      passengers: function () {
        let storeDate = this.$store.state.contact.info;
        let data = [];
        let hasChild = false;
        this.$data.submitInfo.grabPassengers = [];
        for (let i = 0; i < storeDate.length; i++) {
          if (storeDate[i]) {
            if (storeDate[i].personType == 2) {
              hasChild = true;
            }
            data.push(storeDate[i]);
            this.$data.submitInfo.grabPassengers.push({
              id: storeDate[i].id,
              name: storeDate[i].name,
              piaoType: storeDate[i].personType
            });
          }
        }
        return {
          data: data,
          child: hasChild
        };
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

  .state {
    width: 100%;
    height: auto;
    background-color: #fef0f0;
    border: 1px solid #fed4d4;
    line-height: 24px;
    font-size: 15px;
    color: #ff6565;
    text-align: center;
    padding: 15px;
  }

  .prote {
    font-size: 14px;
    color: #c1c1c1;
    float: left;
    padding: 15px;
    text-align: left;
  }

  #birthday {
    width: 200px;
  }

  .main {
    width: 100%;
    height: 100%;
    overflow: auto;
    padding-bottom: 50px;
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
      .insurance {
        position: fixed;
        top: 0;
        left: 0;
        z-index: 9999;
        width: 100%;
        height: 100%;
        padding: 25px 15px;
        background: rgba(255, 255, 255, 1);
        .word {
          height: 90%;
          text-align: left;
          overflow: auto;
        }
        .x {
          height: 10%;
          width: 100%;
          font-size: 30px;
          color: #999;
          text-align: center;
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
          span {
            color: #000;
          }
          .know {
            display: inline-block;
            width: 13px;
            height: 13px;
            margin-top: 5px;
            margin-left: 5px;
            background: url('../../assets/know.png') no-repeat;
            background-size: 13px 13px;
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

  .content {
    width: 100%;
    height: 100%;
    background: #fff;
    position: absolute;;
    top: 64px;
    left: 0;
    ul {
      li {
        width: 100%;
        height: 48px;
        line-height: 48px;
        font-size: 15px;
        text-align: left;
        padding: 0 15px;
        background: #fff;
        border-bottom: 1px solid #ebebeb;

        &.typeLi {
          background: url("../../assets/prompt-arrow.png") no-repeat 96%;
          background-size: 8px 11px;
        }

        input {
          border: none;
          color: #999;
          &:focus {
            outline: none;
          }
        }
      }
    }
    .sure {
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
