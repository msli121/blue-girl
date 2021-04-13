<template>
  <div class="step-4-bg">
    <el-image @click="handleStepClick"
              style="width: 100%; height: 100%"
              :src="url"
              :fit="bgFit">
    </el-image>
    <div style="z-index: 999;position: absolute;top: 40%">
      <el-image v-for="item in existUrlList" :key="item.tempID"
                style="width: 100px;height: 100px;padding: 5px"
                :src="item.url"
                :fit="imageFit"
      ></el-image>
    </div>
    <div style="z-index:999;background-color: cornflowerblue;position: absolute;bottom: 0;width: 100%;opacity: .85">
      <div style="width: 100%;" class="title">
        <h2>贴    纸</h2>
      </div>
      <el-image v-for="item in stickUrlList" :key="item.tempID"
                @click="handleStickerClick(item)"
                style="width: 100px;height: 100px;padding: 5px;opacity: 1"
                :src="item.url"
                :fit="imageFit"
      ></el-image>
    </div>
  </div>
</template>

<script>
export default {
  name: "step4",
  data() {
    return {
      url: require("../assets/bg10.png"),
      bgFit:'fill',
      imageFit: "contain",
      stickUrlList:[
        {
          urlString:'../assets/sticker1.png',
          url:require("../assets/sticker1.png"),
          tempID:Math.random()
        },
        {
          urlString:'../assets/sticker2.png',
          url:require("../assets/sticker2.png"),
          tempID:Math.random()
        },
        {
          urlString:'../assets/sticker3.png',
          url:require("../assets/sticker3.png"),
          tempID:Math.random()
        },
        {
          urlString:'../assets/sticker4.png',
          url:require("../assets/sticker4.png"),
          tempID:Math.random()
        },
        // {
        //   url:require("../assets/sticker5.png"),
        //   tempID:Math.random()
        // },
        // {
        //   url:require("../assets/sticker6.png"),
        //   tempID:Math.random()
        // },
      ],
      existUrlList:[],
  };
  },
  methods: {
    handleStepClick() {
      this.$router.replace({path: "/step6"})
    },
    handleStickerClick(val) {
      let item = val
      item.tempID = Math.random()
      this.existUrlList.push(item)
      console.log(this.existUrlList)
    }
  }
}
</script>

<style scoped lang="less">
.step-4-bg {
  position: relative;
}
.title {
  text-align: center;
  color: yellow;

  h2 {
    margin-top: 10px;
  }
}
</style>