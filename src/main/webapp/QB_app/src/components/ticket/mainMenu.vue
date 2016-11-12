<template lang="html">
  <div class="menu flex-box-column flexBox">
    <router-view></router-view>

    <div class="bottom">
      <router-link :to="{path:'/ticket/main-menu/reserve',query: common}" class="btn"
                   :class="pathName=='ticketReserve'?'active':''">
        <div class="icon icon-ticket"></div>
        <p>车票预定</p>
      </router-link>
      <router-link :to="{path:'/ticket/main-menu/rob',query: common}" class="btn"
                   :class="pathName=='ticketRob'?'active':''">
        <div class="icon icon-rob"></div>
        <p>抢票</p>
      </router-link>
      <!--<router-link to="/ticket/main-menu/private" class="btn" :class="pathName=='ticketPrivate'?'active':''">-->
      <!--<div class="icon icon-private"></div>-->
      <!--<p>私人定制</p>-->
      <!--</router-link>-->
      <router-link :to="{path:'/ticket/main-menu/order',query: common}" class="btn"
                   :class="pathName=='ticketOrder'?'active':''">
        <div class="icon icon-order"></div>
        <p>我的订单</p>
      </router-link>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'menu',
    data () {
      return {
        common: {
          appid: '',
          uid: ''
        },
        pathName: this.$route.name
      }
    },
    beforeRouteEnter (to, from, next) {
      next(function (vm) {
        vm.$data.common.appid = to.query.appid;
        vm.$data.common.uid = to.query.uid;
      });
    },
    watch: {
      $route: function (n, o) {
        this.$data.pathName = n.name;
        this.$data.common.appid = o.query.appid;
        this.$data.common.uid = o.query.uid;
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

  .menu {
    .flexItem(1, 100%);
  }

  .bottom {
    width: 100%;
    height: 49px;
    background-color: #FFF;
    border-top: 1px solid #ebebeb;
    .btn {
      float: left;
      width: 33.33%;
      height: 49px;
      text-align: center;
      display: block;
      .icon {
        margin: 10px auto 0;
        width: 16px;
        height: 16px;
        background-size: 16px 16px;
        &.icon-ticket {
          background-image: url("../../assets/ticket.png");
        }
        &.icon-rob {
          background-image: url("../../assets/rob.png");
        }
        &.icon-private {
          background-image: url("../../assets/private.png");
        }
        &.icon-order {
          background-image: url("../../assets/order.png");
        }
      }
      p {
        font-size: 11px;
        color: #898989;
        margin-top: 4px;
      }
      &.active {
        .icon-ticket {
          background-image: url("../../assets/ticket-active.png");
        }
        .icon-rob {
          background-image: url("../../assets/rob-active.png");
        }
        .icon-private {
          background-image: url("../../assets/private-active.png");
        }
        .icon-order {
          background-image: url("../../assets/order-active.png");
        }
        p {
          color: #1ca0e2;
        }
      }
    }
  }
</style>
