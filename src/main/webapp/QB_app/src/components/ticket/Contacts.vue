<template lang="html">
  <transition name="fade">
    <div class="main flex-box-column flexBox" v-if="showModule">
      <div class="header">
        <div class="close" @click='close'></div>
        <h1>常用联系人</h1>
      </div>
      <div class="contact flex-box-column flexBox">
        <div class="banner con">
          <div class="bannerLeft" @click="mask=true">新增联系人</div>
          <div class="bannerRight" @click="importCon">导入12306联系人</div>
        </div>
        <ul id="list">
          <li v-for="(people,index) in peoples" @click="change(index,people)" :class="people.selected?'select':''">
            <span class="option"></span>

            <div class="passen">
              <span class="name">{{people.name}}</span>
              <span class="type">({{people.piaoType}})</span>

              <p>{{people.identy}}</p>
            </div>
            <span class="edit" @click="show(index)"></span>
          </li>
        </ul>
        <div class="bottom" @click="close">确定</div>
      </div>
      <div class="mask" id="mask" v-show="mask">
        <div class="content">
          <div class="sub">
            <span class="del" @click="del(index)">删除联系人</span>
            <span class="sev" @click="sev(index)">保存</span>
          </div>
          <ul style="padding:0">
            <li>
              <label for="name">乘客姓名</label>
              <input type="text" name="name" id='name' placeholder="必填" style="width: 80%">
            </li>
            <li>
              <label for="sex">乘客性别</label>
              <label style="margin-left:10px;color:#999"><input type="radio" name="sex" value="男"
                                                                checked="checked">男</label>
              <label style="margin-left:20px;color:#999"><input type="radio" name="sex" value="女">女</label>
            </li>
            <li class="typeLi">
              <label for="identyType">证件类型</label>
              <input type="text" name="identyType" id='identyType' readOnly="true" value="二代身份证">
            </li>
            <li>
              <label for="identy">证件号码</label>
              <input type="text" name="identy" id='identy' placeholder="必填" style="width: 80%">
            </li>
            <li>
              <label for="personType">乘客类型</label>
              <input type="text" name="personType" id='personType' readOnly="true" value="成人">
            </li>
            <li style="border:none">
              <label for="tel">手机号码</label>
              <input type="text" name="tel" id='tel' style="width: 80%">
            </li>
          </ul>
        </div>
      </div>
    </div>
  </transition>
</template>
<script lang="babel">
  import Vue from 'vue';
  export default {
    name: 'menu',
    data () {
      return {
        name: '',
        massages: [],
        selected: {},
        people: '',
        $index: '',
        keepID: [],
        uid: '',
        appid: '',
        mask: false
      }
    },
    computed: {
      showModule: function () {
        if (this.$store.state.contact.ctrl) {
          let type = {
            1: '成人', 2: '儿童', 3: '学生', 4: '伤残军人'
          };
          this.$data.uid = this.$route.query.uid;
          this.$data.appid = this.$route.query.appid;
          this.$http.post('/contactInfo/list', {
            appid: this.$route.query.appid,
            uid: this.$route.query.uid
          }).then(function (res) {
            let massages = res.data;
            for (var i = 0; i < massages.length; i++) {
              massages[i].piaoType = type[massages[i].personType];
              massages[i].sex = massages[i].sex == 0 ? "男" : "女";
              massages[i].selected = false;
              if (this.$store.state.contact.keepID.length != 0) {
                for (var j = 0; j < this.$store.state.contact.keepID.length; j++) {
                  if (massages[i].id == this.$store.state.contact.keepID[j]) {
                    massages[i].selected = true;
                  }
                }
              }
              continue
            }
            this.$data.massages = massages;
          }, function () {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '获取联系人列表失败'
            })
          });
        }
        // 每次打开都去重新获取联系人列表
        return this.$store.state.contact.ctrl
      },
      peoples: function () {
        return this.$data.massages;
      },
      index: function () {
        return this.$data.$index
      }
    },
    methods: {
      importCon: function () {
        this.$http.post('/website/importContacts', {
          appid: this.$route.query.appid,
          uid: this.$route.query.uid
        }).then(function () {
          this.$http.post('/contactInfo/list', {
            appid: this.$route.query.appid,
            uid: this.$route.query.uid
          }).then(function (res) {
            let massages = res.data;
            let type = {
              1: '成人', 2: '儿童', 3: '学生', 4: '伤残军人'
            };
            for (var i = 0; i < massages.length; i++) {
              massages[i].piaoType = type[massages[i].personType];
              massages[i].sex = massages[i].sex == 0 ? "男" : "女";
              massages[i].selected = false;
              if (this.$store.state.contact.keepID.length != 0) {
                for (var j = 0; j < this.$store.state.contact.keepID.length; j++) {
                  if (massages[i].id == this.$store.state.contact.keepID[j]) {
                    massages[i].selected = true;
                  }
                }
              }
              continue
            }
            this.$data.massages = massages;
          }, function () {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '获取联系人列表失败'
            })
          });
        }, function () {
          this.$router.push({
            path: '/ticket/login',
            query: {
              appid: this.$route.query.appid,
              uid: this.$route.query.uid
            }
          })
        });
      },
      close: function () {
        var ary = [];
        for (var i = 0; i < this.$data.massages.length; i++) {
          if (this.$data.massages[i].selected == true) {
            ary.push(this.$data.massages[i])
          }
        }
        this.$store.commit("CONTACT_CLOSE", {
          ctrl: false,
          info: ary
        });
      },
      change: function (index, people) {
        var oUl = document.getElementById("list");
        var aLi = oUl.getElementsByTagName("li");
        this.$data.massages[index].selected = !this.$data.massages[index].selected
        if (aLi[index].className == "select") {
          this.$data.selected[index] = null;
        } else {
          this.$data.selected[index] = people;
        }
      },
      show: function (idx) {
        var mask = document.getElementById("mask");
        if (isNaN(idx)) {
          mask.style.display = "block";
        } else {
          mask.style.display = "block";
          this.$data.$index = idx;
          document.querySelector('#name').value = this.$data.massages[idx].name;
          document.querySelector('#identyType').value = "二代身份证";
          document.querySelector('#identy').value = this.$data.massages[idx].identy;
          document.querySelector('#personType').value = "成人";
          document.querySelector('#tel').value = this.$data.massages[idx].tel;
          var addPerson = {
            uid: this.$data.uid,
            appid: this.$data.appid,
            name: document.querySelector('#name').value,
            sex: document.querySelector(':checked').value,
            identyType: 1,
            identy: document.querySelector('#identy').value,
            tel: document.querySelector('#tel').value,
            personType: 1
          }
        }
      },
      del: function (index) {
        var delPerson = {
          uid: this.$data.uid,
          appid: this.$data.appid,
          id: this.$data.massages[index].id
        };
        this.$http.post('/contactInfo/delete', JSON.stringify(delPerson)).then(function (res) {
          this.$data.massages.splice(index, 1);
          document.querySelector("#mask").style.display = "none";
        }, function () {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '删除联系人失败'
          })
        });
      },
      sev: function (idx) {
        this.$data.mask = false;
        var addPerson = {
          uid: this.$data.uid,
          appid: this.$data.appid,
          name: document.querySelector('#name').value,
          sex: 1,
          identyType: 1,
          identy: document.querySelector('#identy').value,
          tel: document.querySelector('#tel').value,
          personType: 1,
          piaoType: "成人"
        };
        var reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/i;
        if ((typeof idx) !== 'number') {
          addPerson.sex = addPerson.sex == "男" ? 0 : 1;
          if (document.querySelector('#name').value == "") {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '请填写乘客姓名'
            })
          } else if (document.querySelector('#identy').value == '') {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '请填写身份证号'
            })
          } else if (document.querySelector('#identy').value != '' && !reg.test(document.querySelector('#identy').value)) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '请填写正确的身份证号'
            })
          } else {
            this.$http.post('/contactInfo/add', JSON.stringify(addPerson)).then(function (res) {
              addPerson.id = res.data.data;
              addPerson.selected = false;
              this.$data.massages.push(addPerson);
              document.querySelector("#mask").style.display = "none";
              this.$data.$index = "";
            }, function () {
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: '添加联系人失败'
              })
            });
          }
        } else {
          if (document.querySelector('#name').value == "") {
            this.$data.$err = true
            this.$data.errMsg = "请填写乘客姓名"
          } else if (!reg.test(document.querySelector('#identy').value)) {
            this.$data.$err = true
            this.$data.errMsg = "请填写正确的身份证号"
          }
          setTimeout(()=>{
            this.$data.$err = false
          },1000);
          addPerson.id = this.$data.massages[idx].id;
          addPerson.sex = addPerson.sex == "男" ? 0 : 1;
          if (document.querySelector('#name').value == "") {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '请填写乘客姓名'
            })
          } else if (document.querySelector('#identy').value == '') {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '请填写身份证号'
            })
          } else if (document.querySelector('#identy').value != '' && !reg.test(document.querySelector('#identy').value)) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '请填写正确的身份证号'
            })
          } else {
            this.$http.post('/contactInfo/update', JSON.stringify(addPerson)).then(function (res) {
              addPerson.selected = false;
              for (var i in addPerson) {
                this.$set(this.$data.massages[idx], i, addPerson[i])
              }
              document.querySelector("#mask").style.display = "none";
              this.$data.$index = "";
            }, function () {
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: '修改联系人失败'
              })
            });
          }
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

  .fade-enter-active, .fade-leave-active {
    transition: opacity .3s;
  }

  .fade-enter, .fade-leave-active {
    opacity: 0;
  }

  .main {
    width: 100%;
    height: 100%;
    overflow: auto;
    .flexItem(1, 100%);
    background-color: #f5f5f5;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 99;
  }

  .contact {
    width: 100%;
    height: 100%;
    .flexItem(1, 100%);
  }

  .header {
    width: 100%;
    height: 50px;
    color: #fefefe;
    background-color: #4ab9f1;
    padding: 15px 10px 0;
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

  .banner {
    height: 45px;
    font-size: 15px;
    color: #1ca0e2;
    padding: 10px 0;
    margin-bottom: 5px;
    background-color: #FFF;
    .bannerLeft {
      float: left;
      width: 50%;
      border-right: 1px solid #dcdcdc;
    }
    .bannerLeft, .bannerRight {
      height: 25px;
      line-height: 25px;
    }
  }

  ul {
    overflow: auto;
    width: 100%;
    padding-bottom: 50px;
    -webkit-overflow-scrolling: touch;
    li {
      background: #fff;
      height: 65px;
      margin-bottom: 1px;
      padding: 14px 15px 14px 15px;

      &.select .option {
        background: url("../../assets/option-active.png") no-repeat 0px 4px;
        background-size: 23px 23px;
      }
      .option {
        float: left;
        width: 23px;
        height: 30px;
        background: url("../../assets/option.png") no-repeat 0px 4px;
        background-size: 23px 23px;
      }

      .passen {
        float: left;
        padding-left: 14px;
        text-align: left;

        .name {
          font-size: 15px;
          font-weight: bold;
          margin-right: 10px;
        }
        .type {
          font-size: 15px;
          color: #999;

        }
        p {
          font-size: 12px;
          margin-top: 9px;
        }
      }
      .edit {
        float: right;
        width: 16px;
        height: 30px;
        background: url("../../assets/edit.png") no-repeat 0 10px;
        background-size: 16px 16px;
      }
    }
  }

  .bottom {
    height: 50px;
    line-height: 50px;
    width: 100%;
    text-align: center;
    background: #4ab9f1;
    color: #fff;
    font-size: 15px;
    position: fixed;
    bottom: 0;
    left: 0;
  }

  .mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);

    .content {
      width: 100%;
      position: fixed;
      bottom: 0;
      left: 0;
      background: #fff;
      .sub {
        padding: 8px 15px 0;
        width: 100%;
        height: 42.5px;
        line-height: 42.5px;

        .del {
          height: 0;
          float: left;
          font-size: 15px;
          color: #b9b9b9;

        }
        .sev {
          float: right;
          font-size: 15px;
          color: #4ab9f1;
        }
      }
      ul {
        padding-bottom: 22px;
        width: 100%;
        li {
          width: 100%;
          height: 48px;
          line-height: 48px;
          font-size: 15px;
          text-align: left;
          padding: 0 15px;
          background: #fff;
          border-bottom: 1px solid #ebebeb;

          &.typeLi {
            background: url("../../assets/prompt-arrow.png") no-repeat 96%;
            background-size: 8px 11px;
          }

          input {
            border: none;
            color: #999;
            &:focus {
              outline: none;
            }
          }
        }
      }
    }
  }

</style>
