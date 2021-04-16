<template>
  <div class="step-1-bg">
    <el-image @click="handleStepClick"
        style="width: 100%; height: 100%"
        :src="url"
        :fit="fit"></el-image>
  </div>
</template>

<script>
  import imgPreloaderList from "../config/imgPreloaderList.js";
export default {
  name: "step1",
  data() {
    return {
      url: imgPreloaderList[1],
      fit: "fill"
    };
  },
  methods: {
    handleStepClick() {
      this.$router.replace({path: "/step3"})
    }
  }
}
</script>

<style>
.step-1-bg {
  width: 100%;
  height: 100%;
}
</style>
