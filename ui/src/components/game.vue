<template>
  <div class="game-bg">
    <el-image
         style="height: 100%; width: 100%;z-index: 9"
        :src="bgUrl"
        :fit="fit">
    </el-image>
    <div class="confirm-panel">
      <!-- 拍照 -->
      <div style="color:yellow;" @click="handleExit()">
        <span>退出游戏</span>
        <br/>
        <span class="span-en">EXIT</span>
      </div>
    </div>
    <iframe id="flash22" class="game-window"
            align="center" width="338" height="564"
            src="https://sda.4399.com/4399swf/upload_swf/ftp31/gamehwq/20200310/07/index.htm"
            frameborder="no"
            border="0" marginwidth="0" marginheight="0" scrolling="no" style="width: 714px; height: 1191px;"></iframe>
  </div>
</template>

<script>
export default {
  name: "game",
  data() {
    return {
      bgUrl: require("@/assets/border.png"),
      fit: "fill"
    };
  },
  methods: {
    handleExit() {
      this.$router.push({path: "/step1"})
    }
  }
}
</script>

<style>
.game-bg {
  position: relative;
  height: 100%;
  width: 100%;
}
.game-window {
  position: absolute;
  left: 10.88%;
  top: 16.43%;
  width: 78% !important;
  height: 67% !important;
}
.confirm-panel {
  z-index: 99;
  text-align: center;
  position: absolute;
  bottom: 2%;
  height: 200px;
  font-size: 72px;
  width: 100%;
  background-color: rgba(0,0,0, .5);
  .span-en{
    color: black;
    text-shadow: 0 1px yellow, 1px 0 yellow, -1px 0 yellow, 0 -1px yellow;
  }
}
</style>