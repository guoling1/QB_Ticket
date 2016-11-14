<template lang="html">
  <div class="main" v-if="showModule">
    <div class="header">
      <div class="close" @click='close'></div>
      <h1>常用联系人</h1>
    </div>
    <div class="banner con">
      <div class="bannerLeft" @click="show">新增联系人</div>
      <div class="bannerRight">导入12306联系人</div>
    </div>
    <ul id="list">
      <li v-for="(people,index) in peoples" @click="change(index,people)">
        <span class="option"></span>
        <div class="passen">
          <span class="name">{{people.name}}</span>
          <span class="type">({{people.personType}})</span>
          <p>{{people.identy}}</p>
        </div>
        <span class="edit" @click="show(index)"></span>
      </li>
    </ul>
    <div class="bottom" @click="close">
      确定
    </div>
    <div class="mask" id="mask">
      <div class="content">
        <div class="sub">
            <span class="del" @click="del(index)">删除联系人</span>
            <span class="sev" @click="sev(index)">保存</span>
        </div>
        <ul>
          <li>
            <label for="name">乘客姓名</label>
            <input type="text" name="name" id='name' :value="people==undefined?'':people.name">
          </li>
          <li>
            <label for="sex">乘客性别</label>
            <label style="margin-left:10px;color:#999"><input type="radio" name="sex" value="男" checked="checked">男</label>
            <label style="margin-left:20px;color:#999"><input type="radio" name="sex" value="女">女</label>
          </li>
          <li class="typeLi">
            <label for="identyType">证件类型</label>
            <input type="text" name="identyType" id='identyType' value="二代身份证">
          </li>
          <li>
            <label for="identy">证件号码</label>
            <input type="text" name="identy" id='identy' :value="people==undefined?'':people.identy">
          </li>
          <li>
            <label for="personType">乘客类型</label>
            <input type="text" name="personType" id='personType' value="成人">
          </li>
          <li style="border:none">
            <label for="tel">手机号码</label>
            <input type="text" name="tel" id='tel' :value="people==undefined?'':people.tel">
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
        massages:[],
        selected: {},
        people:'',
        index:''
      }
    },
    computed:{
      showModule:function () {
        if(this.$store.state.contact.ctrl){
          this.$http.post('/contactInfo/list',{uid:this.$route.query.uid,appid:this.$route.query.appid})
            .then(function (response) {
                let massages = response.data.data;
                for (var i = 0; i < massages.length; i++) {
                  //设置乘客类型
                  var personType=massages[i].personType;
                  if(personType==1){
                    massages[i].personType='成人';
                  }else if(personType==2){
                    massages[i].personType='儿童';
                  }else if(personType==3){
                    massages[i].personType='学生';
                  }else if(personType==4){
                    massages[i].personType='伤残军人';
                  }
                  //设置证件
                  var identyType=massages[i].identyType;
                  if(identyType==1){
                    massages[i].identyType='二代身份证';
                  }else if(identyType==2){
                    massages[i].identyType='一代身份证';
                  }else if(identyType=='C'){
                    massages[i].identyType='港澳通行证';
                  }else if(identyType=='G'){
                    massages[i].identyType='台湾通行证';
                  }else if(identyType=='B'){
                    massages[i].identyType='护照'
                  }
                  //性别
                  massages[i].sex=massages[i].sex==0?"男":"女";
                }
              this.$data.massages = massages;
            })
            .catch(function (err) {
              console.log(err);
            })
        }
        // 每次打开都去重新获取联系人列表
        return this.$store.state.contact.ctrl
      },
      peoples:function(){
        return this.$data.massages;
      }
    },
    methods:{
      close: function(){
        this.$store.commit("CONTACT_CLOSE", {
          ctrl: false,
          info: this.$data.selected
        });
      },
      change:function(index,people){
        var oUl=document.getElementById("list");
        var aLi=oUl.getElementsByTagName("li");
        if(aLi[index].className == "select"){
          this.$data.selected[index] = null;
          aLi[index].className = "";
        }else {
          this.$data.selected[index] = people;
          aLi[index].className = "select"
        }
      },
      show:function(idx){
        var mask=document.getElementById("mask");
        if(isNaN(idx)){
          mask.style.display="block";
        }else {
          mask.style.display="block";
          this.$data.index=idx;
          document.querySelector('#name').value=this.$data.massages[idx].name;
          document.querySelector('#identyType').value=this.$data.massages[idx].identyType;
          document.querySelector('#identy').value=this.$data.massages[idx].identy;
          document.querySelector('#personType').value=this.$data.massages[idx].personType;
          var addPerson={
            uid:1,
            appid:1,
            name:document.querySelector('#name').value,
            sex:document.querySelector(':checked').value,
            identyType:document.querySelector('#identyType').value,
            identy:document.querySelector('#identy').value,
            tel:document.querySelector('#tel').value,
            personType:document.querySelector('#personType').value
          }
        }
      },
      del:function (index) {
          var delPerson={
            uid:1,
            appid:1,
            id:this.$data.massages[index].id
          }
          Vue.http.post('/contactInfo/delete',JSON.stringify(delPerson))
            .then((res)=>{
              if(res.data.result==true){
                this.$data.massages.splice(index,1);
                document.querySelector("#mask").style.display="none";
              }
            })
      },
      sev:function (idx) {
          var addPerson={
            uid:1,
            appid:1,
            name:document.querySelector('#name').value,
            sex:1,
            identyType:document.querySelector('#identyType').value,
            identy:document.querySelector('#identy').value,
            tel:document.querySelector('#tel').value,
            personType:document.querySelector('#personType').value
          }
          if((typeof idx)!=='number'){
            if(addPerson.personType=="成人"){
              addPerson.personType=1;
            }else if(addPerson.personType=="儿童"){
              addPerson.personType=2;
            }else if(addPerson.personType=="学生"){
              addPerson.personType=3;
            }else if(addPerson.personType=="伤残军人"){
              addPerson.personType=4;
            }
            if(addPerson.identyType=="二代身份证"){
              addPerson.identyType=1;
            }else if(addPerson.identyType=="一代身份证"){
              addPerson.identyType=2;
            }else if(addPerson.identyType=="港澳通行证"){
              addPerson.identyType='C';
            }else if(addPerson.identyType=="台湾通行证"){
              addPerson.identyType='G';
            }else  if(addPerson.identyType=="护照"){
              addPerson.identyType='B';
            }
            addPerson.sex=addPerson.sex=="男"?0:1;
            Vue.http.post('/contactInfo/add',JSON.stringify(addPerson))
              .then((res)=>{
                if(res.data.result==true){
                  addPerson.id=res.data.data;
                  this.$data.massages.push(addPerson);
                  document.querySelector("#mask").style.display="none";
                }
              })
          }else{
            addPerson.id=this.$data.massages[idx].id;
            if(addPerson.personType=="成人"){
              addPerson.personType=1;
            }else if(addPerson.personType=="儿童"){
              addPerson.personType=2;
            }else if(addPerson.personType=="学生"){
              addPerson.personType=3;
            }else if(addPerson.personType=="伤残军人"){
              addPerson.personType=4;
            }
            if(addPerson.identyType=="二代身份证"){
              addPerson.identyType=1;
            }else if(addPerson.identyType=="一代身份证"){
              addPerson.identyType=2;
            }else if(addPerson.identyType=="港澳通行证"){
              addPerson.identyType='C';
            }else if(addPerson.identyType=="台湾通行证"){
              addPerson.identyType='G';
            }else  if(addPerson.identyType=="护照"){
              addPerson.identyType='B';
            }
            addPerson.sex=addPerson.sex=="男"?0:1;
            Vue.http.post('/contactInfo/update',JSON.stringify(addPerson))
              .then((res)=>{
                if(res.data.result==true){
                  for(var i in addPerson){
                    this.$set(this.$data.massages[idx],i,addPerson[i])
                  }
                  document.querySelector("#mask").style.display="none";
                }
              })
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
  overflow: hidden;
  .flexItem(1, 100%);
  background-color: #f5f5f5;
  position: absolute;
  top:0;
  left: 0;
  z-index: 99;
}
.header {
  width: 100%;
  height: 64px;
  color: #fefefe;
  background-color: #4ab9f1;
  padding: 30px 10px 0;
  .close {
    width: 19px;
    height: 24px;
    background: url("../../assets/back.png") no-repeat center;
    background-size: 9px 14px;
    float: left;
    padding: 5px;
    position: absolute;
  }
  h1 {
    font-size: 18px;
    line-height: 24px;
  }
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
        height: 0;
        float: left;
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
