<template lang="html">
  <div class="main">
    <div class="reserve show">预订单</div>
    <div class="rob">抢票单</div>
    <ul>
      <li v-for="massage in massages">
        <div class="top">
          <span class="date">{{massage.startDate}} {{massage.startTime}}出发</span><span class="checi">{{massage.checi}}</span>
        </div>
        <div class="bottom">
          <div>
            <span class="form">{{massage.fromStationName}}</span>
            <img src="../../assets/jiantou.png" alt="" />
            <span class="to">{{massage.toStationName}}</span>
          </div>
          <span class="name" v-for="passenger in massage.passengers">{{passenger.name}}</span>
          <span class="price" v-for="passenger in massage.passengers">￥{{passenger.price}}</span>
          <router-link :to="{path:'/ticket/refund-detail',query:{orderFormId:massage.orderFormId}}">
            <p class="static"  v-for="passenger in massage.passengers" >{{passengerStatus[passenger.status-1]}}<p>
          </router-link>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="babel">
  import Vue from 'vue';
  export default {
    name: 'menu',
    data () {
      return {
        massages: [],
        passengerStatus:["票初始化","出票成功","出票失败","退票中","退票请求成功","退票成功","退票失败","订单取消"],
        orderStatus:["订单已删除"," ","订单初始化","占座申请中","占座成功","占座失败","支付中","客户付款成功","客户付款失败","确认出票请求失败","确认出票请求成功","出票成功","出票失败","订单已经退票","订单取消","退票中","退票成功","退票失败"],
      }
    },
    beforeRouteEnter (to, from, next) {
      Vue.http.post('/order/queryMyOrder',{"uid": "123456"})
        .then(function (response) {
          next(vm=> {
            if(response.data.code==1){
              vm.$data.massages = response.data.data;
            }
          })
        })
        .catch(function (response) {
          next(false)
        })
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
  }

  .main {
    width: 100%;
    height: 100%;
    overflow: hidden;
    background:#f5f5f5;
    .flexItem(1, 100 %);
    overflow: hidden;

    .reserve,.rob{
      width:50%;
      height:44px;
      color:#ccc;
      background: #fff;
      font-size:15px;
      text-align: center;
      line-height: 44px;
      float:left;
    }

    .show{
      color:#4ab9f1;
    }

    ul{
      margin-top: 49px;

      li{
        height: 113px;
        background: #fff;
        margin-top: 5px;

        .top{
          font-size: 14px;
          height: 37px;
          line-height: 37px;
          border-bottom: 1px solid #efefef;
          padding-left: 17px;
          padding-right: 17px;

          .date{
            float: left;
          }
          .checi{
            float: right;
          }
        }

        .bottom{
          font-size: 18px;
          padding: 17px 17px 10px;
          position: relative;
          overflow: hidden;

          div{
            width: 183px;
            margin-bottom: 15px;
            overflow: hidden;
            font-weight: bold;

            .form{
              float: left;
            }

            .to{
              float: right;
            }

            img{
              vertical-align: 5px;
            }
          }

          .name{
            float: left;
            font-size: 12px;
            color: #999;
            margin-right: 8px;
          }

          .price{
            color: #f14242;
            font-weight: bold;
            position:absolute;
            top: 18px;
            right: 17px;
          }

          .static{
            position: absolute;
            bottom: 10px;
            right: 15px;
            border-radius: 2px;
            width: 50px;
            height: 23px;
            line-height: 23px;
            font-size: 12px;
            color: #fff;
            text-align: center;
            background: #fe6969;
          }
        }
      }
    }

  }
</style>
