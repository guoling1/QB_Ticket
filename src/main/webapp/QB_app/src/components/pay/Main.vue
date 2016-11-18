<template lang="html">
  <transition name="fade">
    <div class="main" v-if="$$show.pay">
      <div v-if="$$show.methodPayment">
        支付入口:选择支付方式
      </div>
      <!-- 子组件的调用 根据不同的支付方式 -->
      <quickPayment></quickPayment>
    </div>
  </transition>
</template>

<script lang="babel">
  import QuickPayment from './QuickPayment.vue'

  export default {
    name: 'pay',
    components: {
      QuickPayment
    },
    data () {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },
    created: function () {
      // 跳过支付选择 直接唤起快捷支付
      this.$store.commit('QUICK_PAYMENT_CALL');
    },
    computed: {
      $$show: function () {
        return {
          pay: this.$store.state.pay.show,
          methodPayment: this.$store.state.pay.methodPayment
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
    position: absolute;
    top: 0;
    left: 0;
    z-index: 888;
    background: rgba(0, 0, 0, 0.5);
  }
</style>
