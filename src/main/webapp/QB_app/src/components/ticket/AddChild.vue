<template lang="html">
  <div class="main">

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
          personType:2,
          identy:311082198605163634,
          identyType:1
        }
        Vue.http.post('/contactInfo/add',JSON.stringify(addPerson))
          .then((res)=>{
            console.log(res);
            if(res.data.code==1){
              addPerson.id=res.data.data;
              this.$data.massage=addPerson;
              console.log(this.$data.massage);
              this.$store.commit("CONTACT_CLOSE", {
                ctrl: false,
                info: this.$data.massage
              });
          }
        })
        .catch(function(err){
          console.log(err);
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
