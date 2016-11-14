<template lang="html">
  <div class="main">
    <div class="content">
      <ul>
        <li>
          <label for="name">乘客姓名</label>
          <input type="text" name="name" id='name' value="">
        </li>
        <li>
          <label for="sex">乘客性别</label>
          <label style="margin-left:10px;color:#999"><input type="radio" name="sex" value="男" checked="checked">男</label>
          <label style="margin-left:20px;color:#999"><input type="radio" name="sex" value="女">女</label>
        </li>
        <li class="typeLi">
          <label for="birthday">出生日期</label>
          <input type="text" name="birthday" id='birthday' placeholder="出生年月日，如：20160101">
        </li>
        <!-- <li class="typeLi" style="border:none">
          <label for="peer">同行成人</label>
          <input type="text" name="peer" id='peer' readOnly="true" value="二代身份证">
        </li> -->
      </ul>
      <div class="sure" @click="sev">保存</div>
    </div>
  </div>
</template>
<script lang="babel">
  import Vue from 'vue'
  export default {
    name: 'menu',
    data () {
      return {
        massage:{}
      }
    },
    methods:{
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
            if(res.data.code==1){
              addPerson.id=res.data.data;
              this.$data.massage=addPerson;
              this.$store.commit("CONTACT_CLOSE", {
                ctrl: false,
                info: this.$data.massage
              });
          }
        })
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
    background: #fff;
    .flexItem(1, 100%);
  }
  .content{
    width: 100%;
    background: #fff;
    }
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

</style>
