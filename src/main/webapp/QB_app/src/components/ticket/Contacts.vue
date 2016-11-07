<template lang="html">
  <div class="main">
    <div class="banner con">
      <div class="bannerLeft" @click="add">新增联系人</div>
      <div class="bannerRight">导入12306联系人</div>
    </div>
    <ul id="list">
      <li v-for="(massage,index) in massages" @click="change(index)">
        <span class="option"></span>
        <div class="passen">
          <span class="name">{{massage.name}}</span>
          <span class="type">({{massage.personType}})</span>
          <p>{{massage.identy}}</p>
        </div>
        <span class="edit" @click="show(index)"></span>
      </li>
    </ul>
    <div class="bottom">
      确定
    </div>
    <div class="mask" id="mask">
      <div class="content">
        <div class="sub">
          <span class="del" >删除联系人</span>
          <router-link class="btn" to="/ticket/contacts">
            <span class="sev" @click="sev">保存</span>
          </router-link>

        </div>
        <ul>
          <li>
            <label for="name">乘客姓名</label>
            <input type="text" name="name" id='name' :value="people==undefined?'':people.name" v-model='people.name'>
          </li>
          <li class="typeLi">
            <label for="type">证件类型</label>
            <input type="text" name="type" id='type' :value="people==undefined?'':people.identyType" v-model='people.identyType'>
          </li>
          <li>
            <label for="identy">证件号码</label>
            <input type="text" name="identy" id='identy' :value="people==undefined?'':people.identy" v-model='people.identy'>
          </li>
          <li style="border:none">
            <label for="personType">乘客类型</label>
            <input type="text" name="personType" id='personType' :value="people==undefined?'':people.personType" v-model='people.personType'>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  import Vue from 'vue';
  export default {
    name: 'menu',
    data () {
      return {
        name:'',
        massages:[
          {
            "name":"刘思思",
            "personType":1,
            "identyType":1,
            "identy":"411082198605163634"
          },
          {
            "name":"刘思思",
            "personType":2,
            "identyType":1,
            "identy":"411082198605163634"
          },
          {
            "name":"刘思思",
            "personType":3,
            "identyType":1,
            "identy":"411082198605163634"
          }
        ],
        selected: '',
        people:''
      }
    },
    /*beforeRouteEnter (to, from, next) {
      Vue.http.get('/card/list',{'uid':1,'appid':1})
        .then(function (response) {
          next(vm=> {
            vm.$data.massages = response.data;
            var personType=vm.$data.massages.personType;
            if(personType==1){
              personType='二代身份证';
            }else if(personType==2){
              personType='一代身份证';
            }else if(personType=='C'){
              personType='港澳通行证';
            }else if(personType=='G'){
              personType='台湾通行证';
            }else if(personType=='B'){
              personType='护照'
            }
            var identyType=vm.$data.massages.personType;
            if(identyType==1){
              identyType='成人';
            }else if(identyType==2){
              identyType='儿童';
            }else if(identyType==3){
              identyType='学生';
            }else if(identyType==4){
              identyType='伤残军人';
            }
          })
        })
        .catch(function (response) {
          next(false)
        })
    },*/
    methods:{
      change:function(index){
        var oUl=document.getElementById("list");
        var aLi=oUl.getElementsByTagName("li");
        for (var i = 0; i < aLi.length; i++) {
          aLi[i].className = "";
        }
        aLi[index].className = "select";
      },
      show:function(index){
        var mask=document.getElementById("mask");
        if(mask.style.display=="block"){
          //Vue.$route.route.go('/card/list')
          mask.style.display="none";
        }else {
          mask.style.display="block";
          this.$data.people=Number(index) ==NaN?'':this.$data.massages[index];
        }
      },
      add:function(){
        var mask=document.getElementById("mask");
          mask.style.display="block";
          //this.$data.people=Number(index) ==NaN?'':this.$data.massages[index];
        },
        //input中的值
        //var name=document.querySelector('#name').value;
        //var type=document.querySelector('#type').value;
      //  var personType=document.querySelector('#personType').value;
        //var typeID=document.querySelector('#typeID').value;
        //Vue.http.post('/contactInfo/add',{"name":name,"type":type,"personType":personType,"identy":identy})
        //  .then(function (res) {
        //    if(res.result=='true'){
        //      Vue.$route.route.go('/card/list?uid=1&appid=1')
        //    }
        //  })
        sev:function () {
          var name=document.querySelector('#name').value;
          var type=document.querySelector('#type').value;

          var personType=document.querySelector('#personType').value;
          var typeID=document.querySelector('#identy').value;
          console.log(type);
          this.$data.massages.push({"name":name,"type":type,"personType":personType,"identy":identy})
          var mask=document.getElementById("mask");
            mask.style.display="none";
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
  overflow: hidden;
  .flexItem(1, 100%);
  background-color: #f5f5f5;
}
.con{
  padding: 0 15px;
  background: #fff;
  width: 100%;
}
.banner{
  height: 45px;
  font-size: 15px;
  color: #1ca0e2;
  padding: 10px 0;
  margin-bottom: 5px;
  .bannerLeft{
    float: left;
    width: 50%;
    border-right: 1px solid #dcdcdc;
  }
  .bannerLeft,.bannerRight{
    height: 25px;
    line-height: 25px;
  }
}
ul{

  li{
    background: #fff;
    height: 65px;
    margin-bottom: 1px;
    padding:14px 15px 14px 15px;

    &.select .option{
      background: url("../../assets/option-active.png") no-repeat 0px 4px;
      background-size: 23px 23px;
    }
    .option{
      float: left;
      width: 23px;
      height: 30px;
      background: url("../../assets/option.png") no-repeat 0px 4px;
      background-size: 23px 23px;
    }

    .passen{
      float: left;
      padding-left: 14px;
      text-align: left;

        .name{
          font-size: 15px;
          font-weight: bold;
          margin-right: 10px;
        }
        .type{
          font-size: 15px;
          color: #999;

        }
        p{
          font-size: 12px;
          position: relative;
          top: 9px;
          left: 3px;
        }
    }
    .edit{
      float: right;
      width: 16px;
      height: 30px;
      background: url("../../assets/edit.png") no-repeat 0 10px;
      background-size: 16px 16px;
    }
  }
}
.bottom{
  height: 50px;
  line-height: 50px;
  width:100%;
  text-align: center;
  background: #4ab9f1;
  color: #fff;
  font-size: 15px;
  position: fixed;
  bottom: 0;
  left:0;
}
.mask{
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);

  .content{
    width: 100%;
    position: fixed;
    bottom: 0;
    left: 0;
    background: #fff;
    .sub{
      padding: 8px 15px 0;
      width: 100%;
      height: 42.5px;
      line-height:42.5px;

      .del{
        position: relative;
        right: 140px;
        font-size: 15px;
        color: #b9b9b9;

      }
      .sev{
        float: right;
        font-size: 15px;
        color: #4ab9f1;
      }
    }
    ul{
      padding-bottom: 22px;
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
  }
}
</style>
