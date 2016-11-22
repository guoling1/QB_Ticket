/**
 * Created by administrator on 16/11/22.
 */

// 这样引用可以使用 类似 this.$store
import Store from '../store'

export default{
  install(Vue){
    /*
     * 二次封装ajax请求
     * 参数 url data success 全部必传
     * error 不传 默认会弹窗提示错误 如果不想提示 传 none !注意是字符串*/
    const resource = {
      post: function (url, data, success, error) {
        Vue.http.post(url, data).then(function (res) {
          if (res.body.code == 1) {
            success(res);
          } else {
            if (typeof error == 'function') {
              error(res.body.message);
            } else if (!error) {
              console.log(res.body.message);
            } else {
              Store.commit('MESSAGE_DELAY_SHOW', {
                text: res.body.message
              });
            }
          }
        }, function (err) {
          if (typeof error == 'function') {
            error(err);
          } else if (!error) {
            console.log(err);
          } else {
            Store.commit('MESSAGE_DELAY_SHOW', {
              text: err
            });
          }
        });
      }
    };

    Vue.__http = resource;

    Vue.prototype._$http = resource;

  }
}
