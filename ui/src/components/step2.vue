<template>
  <div class="step-2-bg">
    <el-image @click="handleStepClick"
        style="width: 100%; height: 100%"
        :src="url"
        :fit="fit"></el-image>
  </div>
</template>

<script>
export default {
  name: "step2",
  data() {
    return {
      url: require("../assets/step-2-bg.jpg"),
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
.step-2-bg {
  height: 100%;
  width: 100%;
}
</style>