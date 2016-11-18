<template lang="html">
  <transition name="fade">
    {{$$show.quickPayment}}
    <div v-if="$$show.quickPayment">
      <div class="space" v-if="$$show.submitSpace">
        快捷支付-支付模块
      </div>
      <div class="space" v-if="$$show.cardListSpace">
        快捷支付-选卡模块
      </div>
      <div class="space" v-if="$$show.firstAddSpace">
        快捷支付-首次绑卡模块
      </div>
      <div class="space" v-if="$$show.secondAddSpace">
        快捷支付-二次绑卡模块
      </div>
    </div>
  </transition>
</template>

<script lang="babel">
  export default {
    name: 'menu',
    data () {
      return {
        submit_from: {

        },
        card_list: {

        },
        first_bind_from: {

        },
        second_bind_from: {

        }
      }
    },
    computed: {
      $$show: function () {
        return {
          quickPayment: this.$store.state.quickPayment.show,
          submitSpace: false,
          cardListSpace: false,
          firstBindSpace: false,
          secondBindSpace: false
        }
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">

</style>
