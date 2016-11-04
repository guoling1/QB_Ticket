<template lang="html">
  <div class="main">
    <div class="banner con">
      <div class="bannerLeft">新增联系人</div>
      <div class="bannerRight">导入12306联系人</div>
    </div>
    <ul>
      <li>
        <span class="option"></span>
        <div class="passen">
          <span class="name">刘思思</span>
          <span class="type">(成人票)</span>
          <p>132125198904040652</p>
        </div>
        <span class="edit"></span>
      </li>
      <li class="select">
        <span class="option"></span>
        <div class="passen">
          <span class="name">刘思思</span>
          <span class="type">(成人票)</span>
          <p>132125198904040652</p>
        </div>
        <span class="edit"></span>
      </li><li>
        <span class="option"></span>
        <div class="passen">
          <span class="name">刘思思</span>
          <span class="type">(成人票)</span>
          <p>132125198904040652</p>
        </div>
        <span class="edit"></span>
      </li>
    </ul>
    <div class="bottom">
      确定
    </div>
    <div class="mask">
      <div class="content">
        <div class="sub">
          <span class="del">删除联系人</span>
          <span class="sev">保存</span>
        </div>
        <ul>
          <li>
            <label for="name">乘客姓名</label>
            <input type="text" name="name" value="刘思思">
          </li>
          <li class="typeLi">
            <label for="type">证件类型</label>
            <input type="text" name="type" value="身份证">
          </li><li>
            <label for="typeID">证件号码</label>
            <input type="text" name="typeID" value="1321251198904040652">
          </li><li style="border:none">
            <label for="name">乘客类型</label>
            <input type="text" name="name" value="成人">
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default {
    name: 'menu',
    data () {
      return {
        msg: '可以了'
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
        }
      }
    }
  }
}
</style>
