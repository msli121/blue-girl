import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

// 导入图片预加载方法以及图片列表
// import { imgsPreloader } from './config/imgPreloader.js';
// import imgPreloaderList from './config/imgPreloaderList.js';


Vue.config.productionTip = false;
Vue.use(ElementUI);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");


// (async () => {
//   await imgsPreloader(imgPreloaderList);
//   // 关闭加载弹框
//   let homeLoading = document.getElementById("home-loading");
//   if(homeLoading) {
//     homeLoading.parentNode.removeChild(homeLoading);
//   }
//   // document.querySelector('.home-loading').style.display = 'none';
//   new Vue({
//     router,
//     store,
//     render: h => h(App)
//   }).$mount('#app');
// })();