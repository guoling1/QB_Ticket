<template lang="html">
  <div>
    <slot>
      如果没有分发内容则显示我。
    </slot>
    <div v-show="false">{{$$validate}}</div>
    <message></message>
  </div>
</template>
<script lang="babel">
  /* 单输入框规则校验
   * 支持前置校验,前置数据必须为object,并且仅校验是否存在 PreData:object
   * 校验数据 Data
   * 校验类型 Type 类型对应 methods 中的校验方法
   * 校验时机 Time 父组件主动触发
   *
   *  <validate v-bind:Validate="validate_name" v-bind:Data="text">
        <input type="text" v-model="text" @blur="validate_name.Check=true">
      </validate>

      data:function(){
        return {
          validate_name: {
            Pre:{
              a:[data,msg],
              b:[data,msg]
            },
            Type:{
              a:[empty,msg]
            },
            Check: false
          },
          text: ''
        }
      }

     !! 特别注意 EMPTY 为校验保留字段,不可在外部使用
   * */

  const Methods = {
    pre_empty: function (object) {
      for(let text in object){
        console.log(text[i]);
        this.$store.commit('MESSAGE_DELAY_SHOW', {
          text: res.body.message
        })
      }
      return true;
    }
  };

  import Message from './Message.vue'

  export default {
    name: 'validate',
    props: ['Validate', 'Data'],
    data () {
      return {
        msg: 'validate'
      }
    },
    components: {
      Message
    },
    computed: {
      $$validate: function () {
        let Validate = this.Validate;
        let Data = this.Data;
        if (Validate.Check) {
          // 判断是否有前置校验,优先执行前置校验
          if (!!Validate.Pre) {
            if (Methods['pre_empty'](Validate.Pre))return;
          }
          // 执行校验,返回校验结果
          Methods[Validate.Type[0]](Data,Validate.Type[1]);
        }
        return {
          data: Data
        };
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">

</style>
