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
            <span class="info">{{perType[passenger.personType]}}票</span>
          </div>
        </div>
        <div class="group no-border" v-for="(child,index) in childs">
          <div class="list" @click="minusChild($event,index)"></div>
          <div class="write no-prompt">
            <span class="name">{{child.name}}</span>
            <span>请使用{{passengers.data[0].name}}的身份证取票</span>
            <span class="info">{{child.personType}}票</span>
          </div>
        </div>
      </div>
      <div class="state" v-show="childs.length>0||passengers.child">
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
        <div class="group no-border">
          <div class="prompt">联系手机</div>
          <input type="text" class="ipt" placeholder="通知出票信息" v-model="sureOrder.mobile">
        </div>
      </div>
      <div class="space">
        <div class="group no-border" @click="packShow">
          <div class="prompt">套餐类型</div>
          <div class="write" v-if="sureOrder.buyTicketPackageId==1">不购买</div>
          <div class="write" v-if="sureOrder.buyTicketPackageId==2">￥20/人 极速出票</div>
          <div class="write" v-if="sureOrder.buyTicketPackageId==3">￥30/人 优先出票</div>
        </div>
      </div>
      <div class="submit">
        <div class="detail" v-bind:class="{active:detail}">
          <div class="tt">金额详情</div>
          <div class="tt_detail">
            <div class="left">火车票</div>
            <div class="right">x{{$amountDetail.number}}人</div>
            <div class="right margin">￥{{$amountDetail.ticketPrice}}</div>
          </div>
          <div class="tt_detail">
            <div class="left">出票套餐</div>
            <div class="right">x{{$amountDetail.number}}人</div>
            <div class="right margin">￥{{$amountDetail.packagePrice}}</div>
          </div>
        </div>
        <div class="btn">
          <div class="left" @click="detailShow">
            <div class="amount">实付款<span class="red">￥</span><span class="red big">{{$amountDetail.totalPrice}}</span>
            </div>
            <div class="i" v-bind:class="{active:detail}"></div>
          </div>
          <div class="right" @click="submit($event,true)">提交订单</div>
        </div>
      </div>
    </div>
    <transition name="fade">
      <div class="pack" v-show="pack" @click="packBGHide($event)">
        <div class="select">
          <div class="space_t">
            <div class="xx" @click="pack=false"></div>
            <div class="word">选择出票套餐</div>
          </div>
          <ul>
            <li @click="packHide(2)" v-bind:class="{active:sureOrder.buyTicketPackageId==2}"><span>¥ 20/人套餐</span>
              极速出票，赠送80万保险<span class="know" @click.stop="insurance=true"></span>
            </li>
            <li @click="packHide(3)" v-bind:class="{active:sureOrder.buyTicketPackageId==3}"><span>¥ 30/人套餐</span>
              优先出票，赠送160万保险<span class="know" @click.stop="insurance=true"></span>
            </li>
            <li @click="packHide(1)" v-bind:class="{active:sureOrder.buyTicketPackageId==1}">不购买 出票慢，失败的可能性增加</li>
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
    <transition name="fade">
      <div class="skip" v-show="skip">
        <div class="show">
          <div class="space_t">
            <div class="xx" @click="skip=false"></div>
            <div class="word">提示</div>
          </div>
          <div class="content">
            <div class="image"></div>
            <div class="word">您没有选择“出票套餐”</div>
            <div class="small">出票失败率提高，安全保障降低</div>
          </div>
          <div class="btn">
            <div class="sub" @click="submit($event)">放弃</div>
            <div class="close" @click="skipHide">增加保障</div>
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
          <input type="text" name="name" id='name' placeholder="必填" value="">
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
      <div class="prote">儿童票按成人价收款。差价在购票成功后的1-3个工作日退回原支付银行卡。部分银行处理缓慢，不会超过7个工作日。</div>
      <div class="sure" @click="sev">保存</div>
    </div>
    <message></message>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue'
  import Contacts from './Contacts.vue'
  import Message from '../Message.vue'

  const zwCode = {
    '商务座': '9',
    '特等座': 'P',
    '一等座': 'M',
    '二等座': 'O',
    '高级软卧': '6',
    '软卧': '4',
    '硬卧': '3',
    '软座': '2',
    '硬座': '1',
    '无座': '0'
  };

  export default {
    name: 'menu',
    components: {
      Contacts,
      Message
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
        detail: false,
        pack: false,
        skip: false,
        show: false,
        childs: [],
        perType: ["", "成人", "儿童"],
        isLogin: true,
        insurance: false
      }
    },
    created: function () {
      let sessionPreOrder = JSON.parse(sessionStorage.getItem('preOrder'));
      let sessionPreDate = JSON.parse(sessionStorage.getItem('preDate'));
      let query = this.$route.query;
      this.$data.sureOrder.fromStationName = sessionPreOrder.from_station_name;
      this.$data.sureOrder.fromStationCode = sessionPreOrder.from_station_code;
      this.$data.sureOrder.toStationName = sessionPreOrder.to_station_name;
      this.$data.sureOrder.toStationCode = sessionPreOrder.to_station_code;
      this.$data.sureOrder.zwCode = zwCode[query.table];
      this.$data.sureOrder.startTime = sessionPreOrder.start_time;
      this.$data.sureOrder.endTime = sessionPreOrder.arrive_time;
      this.$data.sureOrder.runTime = sessionPreOrder.run_time_minute;
      this.$data.sureOrder.checi = sessionPreOrder.train_code;
      this.$data.sureOrder.startDate = sessionPreDate.startTime.post;
      this.$data.sureOrder.endDate = sessionPreDate.arriveTime.post;
      this.$data.otherData.startShow = sessionPreDate.startTime.show;
      this.$data.otherData.arriveShow = sessionPreDate.arriveTime.show;
      this.$data.otherData.runShow = sessionPreDate.runTime;
      this.$data.sureOrder.appId = query.appid;
      this.$data.sureOrder.uid = query.uid;
      this.$data.sureOrder.price = query.price;
      this.$data.otherData.table = query.table;

      this.$http.post('/userInfo/findPhone', {
        appid: query.appid,
        uid: query.uid
      }).then(function (res) {
        this.$data.sureOrder.mobile = res.data;
      }, function () {
        console.log('获取手机号失败');
      });
      this.$http.post('/userInfo/isLogin', {
        appid: query.appid,
        uid: query.uid
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
      login: function () {
        this.$router.push({
          path: '/ticket/login',
          query: {appid: this.$data.sureOrder.appId, uid: this.$data.sureOrder.uid}
        });
      },
      addChild: function () {
        if (this.$data.sureOrder.passengers.length == 0) {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '请先添加随行成人'
          })
        } else {
          this.$data.show = !this.$data.show
        }
      },
      sev: function () {
        var addPerson = {
          uid: this.$data.sureOrder.uid,
          appid: this.$data.sureOrder.appId,
          name: document.querySelector('#name').value,
          sex: document.querySelector(':checked').value,
          birthday: document.querySelector('#birthday').value,
          personType: 2
        };
        var reg = /^[1-9][0-9]{3}(0[1-9]|1[0-2])([0-2][1-9]|3[0-1])$/;
        if (document.querySelector('#name').value == "") {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '请填写儿童姓名'
          })
        } else if (document.querySelector('#birthday').value == "") {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '请填写出生日期'
          })
        } else if (!reg.test(document.querySelector('#birthday').value)) {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '请填写正确的出生日期'
          })
        } else {
          this.$http.post('/contactInfo/add', JSON.stringify(addPerson)).then(function (res) {
            this.$data.show = !this.$data.show;
            addPerson.id = res.data;
            if (addPerson.personType == 2) {
              addPerson.personType = "儿童"
            }
            this.$data.childs.push(addPerson);
          }, function () {
            this.$store.commit('MESSAGE_PROMPT_SHOW', {
              text: '添加联系人失败'
            })
          });
        }
      },
      detailShow: function () {
        this.$data.detail = !this.$data.detail;
      },
      packShow: function () {
        this.$data.pack = true;
      },
      packHide: function (num) {
        this.$data.pack = false;
        this.$data.sureOrder.buyTicketPackageId = num;
      },
      skipHide: function () {
        this.$data.skip = false;
      },
      submit: function (event, skip) {
        var ary = [];
        for (var i = 0; i < this.$data.childs.length; i++) {
          ary.push({
            id: this.$data.childs[i].id,
            piaoType: 2
          })
        }
        this.$data.sureOrder.passengers = this.$data.sureOrder.passengers.concat(ary);
        // 判断是否选择了套餐
        if (this.$data.sureOrder.buyTicketPackageId == 1 && !!skip) {
          this.$data.skip = true;
          return false;
        }
        this.$data.skip = false;
        var reg = /^1(3|4|5|7|8)\d{9}$/;
        if (this.$data.sureOrder.passengers == "") {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '请添加乘客'
          })
        } else if (!reg.test(this.$data.sureOrder.mobile)) {
          this.$store.commit('MESSAGE_PROMPT_SHOW', {
            text: '请填写正确的手机号'
          })
        } else {
          this.$http.post('/ticket/submitOrder', JSON.stringify(this.$data.sureOrder)).then(function (res) {
            this.$router.push({
              path: '/ticket/pay-order',
              query: {appid: this.$data.sureOrder.appId, uid: this.$data.sureOrder.uid, id: res.data.orderFormId}
            });
          }, function () {
            this.$store.commit('MESSAGE_PROMPT_SHOW', {
              text: '订单提交失败'
            })
          });
        }
      },
      contact: function () {
        var ary = [];
        for (var i = 0; i < this.$data.sureOrder.passengers.length; i++) {
          ary.push(this.$data.sureOrder.passengers[i].id)
        }
        this.$store.commit("CONTACT_OPEN", {
          ctrl: true,
          keepID: ary
        });
      },
      station: function () {
        this.$store.commit('STATION_CTRL', true);
      }
    },
    computed: {
      $amountDetail: function () {
        let number = this.$data.sureOrder.passengers.length + this.$data.childs.length;
        let ticketPrice = this.$data.sureOrder.price;
        let packagePrice = ['', '0', '20', '30'];
        let packageId = this.$data.sureOrder.buyTicketPackageId;
        return {
          number: number,
          ticketPrice: ticketPrice,
          packagePrice: packagePrice[packageId],
          totalPrice: (ticketPrice * number) + (packagePrice[packageId] * number)
        }
      },
      price: function () {
        return this.$data.sureOrder.passengers;
      },
      passengers: function () {
        let storeDate = this.$store.state.contact.info;
        let data = [];
        let hasChild = false;
        this.$data.sureOrder.passengers = [];
        for (let i = 0; i < storeDate.length; i++) {
          if (storeDate[i]) {
            if (storeDate[i].personType == 2) {
              hasChild = true;
            }
            data.push(storeDate[i]);
            this.$data.sureOrder.passengers.push({
              id: storeDate[i].id,
              piaoType: storeDate[i].personType
            });
          }
        }
        return {
          data: data,
          child: hasChild
        };
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

  .fade-enter-active, .fade-leave-active {
    transition: opacity .3s
  }

  .fade-enter, .fade-leave-active {
    opacity: 0
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

  .main {
    width: 100%;
    height: 100%;
    overflow: auto;
    .flexItem(1, 100%);
    background-color: #f5f5f5;
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
    padding-bottom: 50px;
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
      top: 0;
      left: 0;
      padding: 15px;
      transition: all .5s;
      &.active {
        top: -88px;
      }
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
          transform: rotate(180deg);
          &.active {
            transform: rotate(0deg);
          }
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

  .skip {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 88;
    background: rgba(0, 0, 0, 0.5);
    .show {
      position: absolute;
      width: 100%;
      height: 368px;
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
      .content {
        .image {
          width: 82px;
          height: 82px;
          margin: 50px auto 0;
          background: url("../../assets/no-package.png") no-repeat center;
          background-size: 82px 82px;
        }
        .word {
          font-size: 18px;
          color: #000;
          font-weight: bold;
          text-align: center;
          margin-top: 20px;
        }
        .small {
          font-size: 12px;
          color: #000;
          text-align: center;
          margin-top: 5px;
        }
      }
      .btn {
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
        height: 50px;
        div {
          float: left;
          width: 50%;
          height: 50px;
          line-height: 50px;
          text-align: center;
          &.sub {
            font-size: 15px;
            color: #4ab9f1;
            border-top: 1px solid #f5f5f5;
          }
          &.close {
            font-size: 15px;
            color: #FFF;
            background-color: #4ab9f1;
          }
        }
      }
    }
  }

  .content {
    width: 100%;
    height: 100%;
    background: #fff;
    position: absolute;;
    top: 0px;
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
