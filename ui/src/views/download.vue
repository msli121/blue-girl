<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/download.jpg" />
    <div v-if="downloadSrc" class="img_bg_download">
      <img :src="downloadSrc" alt="" class="download_img" />
      <span class="text1">扫描二维码</span>
      <span class="text2"><br />即可保存至手机</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "download",
  components: {},
  data() {
    return {
      downloadSrc: "",
    };
  },
  created() {
    this.downloadSrc = this.$route.query.src;
    console.log(this.downloadSrc);
  },
  methods: {},
};
</script>
<style lang="less">
.img_bg_download {
  display: block;
  position: absolute;
  top: 600px;
  left: 50%;
  img {
    //   position: absolute;
    float: left;
    // margin-left: 50%;
    transform: translateX(-50%);
  }
  .text1 {
      //   position: absolute;
    float: left;
    // margin-left: 50%;
    transform: translateX(-50%);
    color: #ffffff;
    text-shadow: 1px 1px #000, -1px -1px #000, 1px -1px #000, -1px 1px #000;
    font-size: 60px;
    font-family: "Microsoft YaHei";
  }
  .text2 {
    float: left;
    transform: translate(-200%,50%);
    color: #ffffff;
    font-size: 30px;
    font-family: "Microsoft YaHei";
  }
}
</style>
