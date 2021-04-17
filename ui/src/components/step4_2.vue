<template>
  <div class="p-step4_2">
<!--    <img :src="imgSrc" alt="">-->
    <el-image :v-loading="loading"
        style="width: 100%; height: 100%"
        :src="imgSrc"
        @load="handleImageLoad"
        fit="fill"></el-image>
    <div class="confirm-panel">
      <div class="main">
        <!-- 重拍 -->
        <div
            @click="goStep3()"
        >
          <span>重拍</span>
          <br/>
          <span class="span-en">REMAKE</span>
        </div>
        <!-- 保存 -->
        <div
            @click="go()"
        >
          <span>确认</span>
          <br/>
          <span class="span-en">UPLOAD</span>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "step4_2",
  data() {
    return {
      loading: true,
      imgSrc: '',
      queryInfo: ''
    }
  },
  created() {
    this.queryInfo = {...this.$route.query}
    console.log(21, this.queryInfo)
    this.imgSrc = this.queryInfo.mergedUrl
  },
  mounted() {
    this.loading = true;
  },
  methods: {
    handleImageLoad() {
      this.loading = false;
      console.log("loading", this.loading);
    },
    goStep3() {
      this.$router.push({path:'/step3'})
    },
    go() {
      this.$router.push({
        path:'/step5',
        query: this.queryInfo
      })
    },
  },
};
</script>

<style lang="less" scoped>
.p-step4_2{
  position: relative;
  .confirm-panel {
    text-align: center;
    position: absolute;
    bottom: 0;
    height: 200px;
    font-size: 72px;
    z-index: 11;
    width: 100%;
    background-color: rgba(0,0,0, .5);
    display: flex;
    justify-content: center;
    align-items: center;
    .main{
      width: 70%;
      display: flex;
      justify-content: space-between;
      color: yellow;
    }
    .span-en{
      color: black;
      //-webkit-text-stroke: 1px yellow;
      text-shadow: 0 1px yellow, 1px 0 yellow, -1px 0 yellow, 0 -1px yellow;
    }
  }
}
</style>
