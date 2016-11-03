<template lang="html">
  <div class="main">
    <div class="reserve show">预订单</div>
    <div class="rob" >抢票单</div>
    <ul>
      <li v-for="massage in massages">
        <div class="top">
          <span class="date">{{massage.date}}出发</span><span class="checi">{{massage.checi}}</span>
        </div>
        <div class="bottom">
          <div>
            <span class="form">{{massage.fromstation}}</span>
            <span class="icon"></span>
            <span class="line">></span>
            <i></i>
            <b></b>
            <span class="to">{{massage.tostation}}</span>
          </div>
          <span class="name">{{massage.name}}</span>
          <span class="price">￥{{massage.price}}</span>
          <p class="static">{{massage.static}}<p>
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
        getPost: null
      }
    },
    beforeRouteEnter (to, from, next) {
      Vue.http.get('/static/test.json')
        .then(function (response) {
          next(vm=> {
            vm.$data.massages = response.data
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

            .form{
              float: left;
            }

            .to{
              float: right;
            }

            .icon{
              vertical-align: middle;
              display: inline-block;
              width: 45px;
              height: 0;
              border-bottom: 2px solid #000;
            }
            .line{
              float: left;
              position: relative;
              top: 0px;
              left: 67px;

            }

            b{
              width: 10px;
              height:5px;
              background: #FFF;
              float: left;
              position: absolute;
              top: 29px;
              right: 227px;
            }
          }

          .name{
            float: left;
            color: #999;
          }

          .price{
            color: #f14242;
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
            color: #fff;
            text-align: center;
            background: #f14242
          }
        }
      }
    }

  }
</style>
