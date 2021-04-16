<template>
  <div class="step-6-bg" @click="handleStepClick">
    <img
        class="qr-code"
        :src="url"
        @error="imgLoadError"
        @load="imgLoad"/>
  </div>
</template>

<script>
  import { Loading } from 'element-ui'
export default {
  name: "step6",
  data() {
    return {
      url: require("@/assets/step_6_bg.jpg"),
      fit: "fill",
      loading: true,
      loadingInstance: null,
    };
  },
  mounted() {
    if(this.$route.query.fileUrl) {
      this.url = this.$route.query.fileUrl;
    } else {
      this.$notify({
        title: "出错啦",
        type: "error",
        message: "出错啦，照片不存在"
      });
      this.$router.replace({path: "/step1"});
    }
    // 等待
    this.loadingInstance = Loading.service(
        { fullscreen: true }
    )
    // this.drawQrCodePhoto(this.url);
  },
  methods: {
    imgLoad() {
      this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
        this.loadingInstance.close();
      });
    },
    imgLoadError() {
      this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
        this.loadingInstance.close();
      });
      this.$notify.warning("网络有点糟糕，再试一次吧");
    },
    handleStepClick() {
      this.$router.replace({path: "/step1"})
    },
    // async drawQrCodePhoto(qrCodeUrl) {
    //   // 二维码图
    //   const qrImage = await loadImage(qrCodeUrl);
    //   // const bgImage = await loadImage(require("../assets/step_6_bg.jpg"));
    //   console.log("qrImage ",qrImage.width, qrImage.height);
    //   this.loading = false;
    //   // console.log("w: ", bgImage.width, "h: ", bgImage.height);
    //   // let canvas = document.createElement("canvas");
    //   // canvas.width = bgImage.width;
    //   // canvas.height = bgImage.height;
    //   // let ctx = canvas.getContext("2d");
    //   // // 380 524
    //   // ctx.drawImage(bgImage, 0, 0, bgImage.width, bgImage.height);
    //   // ctx.drawImage(qrImage, 380, 524, 388, 388);
    // }
  }
}
</script>

<style lang="less" scoped>
  .step-6-bg {
    height: 100%;
    width: 100%;
    background: url('../assets/step_6_bg.jpg') no-repeat;
    background-size: cover;
    position: absolute;
}
  .qr-code {
    z-index: 10;
    position: relative;
    top: 29.34%;
    left: 31%;
    width: 38%;
}
</style>