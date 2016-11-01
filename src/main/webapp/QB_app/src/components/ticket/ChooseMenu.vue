<template lang="html">
  <div class="menu flex-box-column flexBox">
    <router-view></router-view>

    <div class="bottom">
      <router-link to="/ticket/main-menu/reserve" class="btn" :class="pathName=='ticketReserve'?'active':''">
        <div class="icon icon-ticket"></div>
        <p>车票预定123123</p>
      </router-link>
      <router-link to="/ticket/main-menu/order" class="btn" :class="pathName=='ticketOrder'?'active':''">
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
        pathName: this.$route.name
      }
    },
    watch: {
      $route: function (val) {
        this.$data.pathName = val.name;
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
      width: 50%;
      height: 49px;
      text-align: center;
      display: block;
      .icon {
        margin: auto;
        &.icon-ticket {
          width: 19px;
          height: 22px;
          margin-top: 6px;
          background-image: url("../../assets/ticket.png");
          background-size: 19px 22px;
        }
        &.icon-order {
          width: 24px;
          height: 20px;
          margin-top: 8px;
          background-image: url("../../assets/order.png");
          background-size: 24px 20px;
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
