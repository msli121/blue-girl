<template>
  <div class="step-3-bg">
    <el-image @click="handleStepClick"
        style="width: 100%; height: 100%"
        :src="url"
        :fit="fit"></el-image>
  </div>
</template>

<script>
export default {
  name: "step3",
  data() {
    return {
      url: require("../assets/step-3-bg.jpg"),
      fit: "fill"
    };
  },
  methods: {
    handleStepClick() {
      this.$router.replace({path: "/step4"})
    }
  }
}
</script>

<style>

</style>