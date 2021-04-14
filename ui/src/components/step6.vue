<template>
  <div class="step-6-bg" :v-loading="loading">
<!--    <el-image @click="handleStepClick"-->
<!--              style="width: 100%; height: 100%"-->
<!--              :src="url"-->
<!--              :fit="fit"></el-image>-->
    <img
        @click="handleStepClick"
        class="qr-code"
        :src="url"
        @load="imgLoad"/>
<!--    <el-image class="qr-code"-->
<!--      :src="url"-->
<!--      :fit="fit">-->
<!--    </el-image>-->
  </div>
</template>

<script>
  import {loadImage} from "@/server/func"
export default {
  name: "step6",
  data() {
    return {
      url: require("../assets/step-6-bg.jpg"),
      fit: "fill",
      loading: true
    };
  },
  mounted() {
    if(this.$route.query.fileUrl) {
      this.url = this.$route.query.fileUrl;
    } else {
      this.url = "https://www.performercn.com/api/file/download/141";
    }
    this.drawQrCodePhoto(this.url);
  },
  methods: {
    imgLoad() {
      this.loading = false;
    },
    handleStepClick() {
      this.$router.replace({path: "/step1"})
    },
    async drawQrCodePhoto(qrCodeUrl) {
      // 二维码图
      const qrImage = await loadImage(qrCodeUrl);
      // const bgImage = await loadImage(require("../assets/step-6-bg.jpg"));
      console.log("qrImage ",qrImage.width, qrImage.height);
      this.loading = false;
      // console.log("w: ", bgImage.width, "h: ", bgImage.height);
      // let canvas = document.createElement("canvas");
      // canvas.width = bgImage.width;
      // canvas.height = bgImage.height;
      // let ctx = canvas.getContext("2d");
      // // 380 524
      // ctx.drawImage(bgImage, 0, 0, bgImage.width, bgImage.height);
      // ctx.drawImage(qrImage, 380, 524, 388, 388);
    }
  }
}
</script>

<style lang="less" scoped>
  .step-6-bg {
    height: 100%;
    width: 100%;
    background: url('../assets/step-6-bg.jpg') no-repeat;
    background-size: cover;
    position: absolute;
    //background-image: 'url(' + require("../assets/step-6-bg.jpg") + ')';
}
  .qr-code {
    z-index: 10;
    position: relative;
    top: 29.34%;
    left: 31%;
    width: 38%;
}
</style>