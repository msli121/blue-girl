<template>
  <div class="step-4-bg">
    <el-image @click="handleStepClick"
              style="width: 100%; height: 100%"
              :src="url"
              :fit="fit"></el-image>
  </div>
</template>

<script>
  import {loadImage} from "../server/func.js"
export default {
  name: "step6",
  data() {
    return {
      url: require("../assets/step-6-bg.jpg"),
      fit: "fill"
    };
  },
  mounted() {
    let qrCodeUrl = "https://www.performercn.com/api/file/download/141";
    this.drawQrCodePhoto(qrCodeUrl);
  },
  methods: {
    handleStepClick() {
      this.$router.replace({path: "/step1"})
    },
    async drawQrCodePhoto(qrCodeUrl) {
      // 二维码图
      const qrImage = await loadImage(qrCodeUrl);
      const bgImage = await loadImage(require("../assets/step-6-bg.jpg"));
      console.log("qrImage ",qrImage.width, qrImage.height);
      console.log("w: ", bgImage.width, "h: ", bgImage.height);
      let canvas = document.createElement("canvas");
      canvas.width = bgImage.width;
      canvas.height = bgImage.height;
      let ctx = canvas.getContext("2d");
      // 380 524
      ctx.drawImage(bgImage, 0, 0, bgImage.width, bgImage.height);
      ctx.drawImage(qrImage, 380, 524, 388, 388);
    }
  }
}
</script>

<style>

</style>