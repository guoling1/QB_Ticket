<template lang="html">
  <div id="app" class="flex-box-column flexBox">
    <div class="title">
      <div class="back" @click='back'></div>
      <h1>{{title}}</h1>
    </div>
    <router-view></router-view>
  </div>
</template>

<script lang="babel">
  // 根节点一级路由,负责维护顶部title
  export default {
    name: 'title',
    data: function () {
      return {
        title: this.$store.commit('TITLE_CHANGE', this.$route.name)
      }
    },
    watch: {
      // 监听路由变化 触发title变更
      $route: function (val) {
        this.$store.commit('TITLE_CHANGE', val.name);
      }
    },
    methods: {
      back: function (event) {
        console.log('返回');
      }
    },
    computed: {
      title () {
        return this.$store.state.title.title
      }
    }
  }
</script>

<style lang="less">
  .title {
    width: 100%;
    height: 64px;
    color: #fefefe;
    background-color: #4ab9f1;
    padding: 30px 10px 0;
    .back {
      width: 19px;
      height: 24px;
      background: url("./assets/back.png") no-repeat center;
      background-size: 9px 14px;
      float: left;
      padding: 5px;
    }
    h1 {
      font-size: 18px;
      line-height: 24px;
    }
  }

  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    position: relative;
    width: 100%;
    height: 100%;
  }

  .flex-box-column {
    flex-direction: column;
  }

  .flexBox {
    display: box;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    display: -webkit-flex;
  }

  html, body {
    width: 100%;
    height: 100%;
  }

  /* Reset.less
* Props to Eric Meyer (meyerweb.com) for his CSS reset file. We're using an adapted version here that cuts out some of the reset HTML elements we will never need here (i.e., dfn, samp, etc).
 * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- */

  /*https://github.com/fankay/bootstrap/blob/master/lib/reset.less

  http://www.lesscss.net/

   ERIC MEYER RESET
   --------------------------------------------------*/
  html, body {
    margin: 0;
    padding: 0;
  }

  h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, cite, code, del, dfn, em, img, q,
  s, small, strike, strong, sub, sup, tt, var, dd, dl, dt, li, ol, ul, fieldset, form,
  label, legend, button, table, caption, tbody, tfoot, thead, tr, th, td {
    margin: 0;
    padding: 0;
    border: 0;
    font-weight: normal;
    font-style: normal;
    line-height: 1;
    font-family: 'Microsoft YaHei', 'SimHei', sans-serif;
  }

  table {
    border-collapse: collapse;
    border-spacing: 0;
  }

  ol, ul {
    list-style: none;
  }

  q:before, q:after, blockquote:before, blockquote:after {
    content: "";
  }

  // Normalize.css
  // Pulling in select resets form the normalize.css project
  // --------------------------------------------------

  // Display in IE6-9 and FF3
  // -------------------------
  // Source: http://github.com/necolas/normalize.css
  html {
    overflow-y: scroll;
    font-size: 100%;
    -webkit-text-size-adjust: 100%;
    -ms-text-size-adjust: 100%;
  }

  // Focus states
  a:focus {
    outline: none 0;
  }

  // Hover & Active
  a:hover,
  a:active {
    outline: none 0;
  }

  * {
    -webkit-box-sizing: border-box;
    -ms-box-sizing: border-box;
    -moz-box-sizing: border-box;
    -o-box-sizing: border-box;
    box-sizing: border-box;
  }

  a {
    text-decoration: none;
    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
    -moz-tap-highlight-color: rgba(0, 0, 0, 0);
    -ms-tap-highlight-color: rgba(0, 0, 0, 0);
    -o-tap-highlight-color: rgba(0, 0, 0, 0);
    tap-highlight-color: rgba(0, 0, 0, 0);
  }

  // 让 IE6-9 与 FF3支持HTML5的新标签
  // -------------------------
  // Source: http://github.com/necolas/normalize.css
  article,
  aside,
  details,
  figcaption,
  figure,
  footer,
  header,
  nav,
  section {
    display: block;
  }

  //  让 IE6-9 与 FF3支持HTML5的新标签 inline-block
  // -------------------------
  // Source: http://github.com/necolas/normalize.css
  audio,
  canvas,
  video {
    display: inline-block;
    *display: inline;
    *zoom: 1;
  }

  // Prevents modern browsers from displaying 'audio' without controls
  // -------------------------
  // Source: http://github.com/necolas/normalize.css
  audio:not([controls]) {
    display: none;
  }

  // Prevents sub and sup affecting line-height in all browsers
  // -------------------------
  // Source: http://github.com/necolas/normalize.css
  sub,
  sup {
    font-size: 75%;
    line-height: 0;
    position: relative;
    vertical-align: baseline;
  }

  sup {
    top: -0.5em;
  }

  sub {
    bottom: -0.25em;
  }

  // Img border in a's and image quality
  // -------------------------
  // Source: http://github.com/necolas/normalize.css
  img {
    border: 0;
    -ms-interpolation-mode: bicubic;
  }

  // Forms
  // -------------------------
  // Source: http://github.com/necolas/normalize.css

  // Font size in all browsers, margin changes, misc consistency
  button,
  input,
  select,
  textarea {
    font-size: 100%;
    margin: 0;
    vertical-align: baseline;
    *vertical-align: middle;
  }

  button,
  input {
    line-height: normal; // FF3/4 have !important on line-height in UA stylesheet
    *overflow: visible; // Inner spacing ie IE6/7
  }

  button::-moz-focus-inner,
  input::-moz-focus-inner { // Inner padding and border oddities in FF3/4
    border: 0;
    padding: 0;
  }

  button,
  input[type="button"],
  input[type="reset"],
  input[type="submit"] {
    cursor: pointer; // Cursors on all buttons applied consistently
    -webkit-appearance: button; // Style clicable inputs in iOS
  }

  input[type="search"] { // Appearance in Safari/Chrome
    -webkit-appearance: textfield;
    -webkit-box-sizing: content-box;
    -moz-box-sizing: content-box;
    box-sizing: content-box;
  }

  input[type="search"]::-webkit-search-decoration {
    -webkit-appearance: none; // Inner-padding issues in Chrome OSX, Safari 5
  }

  textarea {
    overflow: auto; // Remove vertical scrollbar in IE6-9
    vertical-align: top; // Readability and alignment cross-browser
  }
</style>
