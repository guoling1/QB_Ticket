<template lang="html">
  <div class="main">
    <div class="banner con">
      <div class="bannerLeft">新增联系人</div>
      <div class="bannerRight">导入12306联系人</div>
    </div>
    <ul class="con">
      <li></li>
    </ul>
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
  //background: #fff;
  height: 50px;
  //width: 100%;
}
</style>
