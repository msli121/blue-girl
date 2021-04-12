<template>
  <div class="step-4-bg">
    <el-image @click="handleStepClick"
              style="width: 100%; height: 100%"
              :src="url"
              :fit="fit"></el-image>
  </div>
</template>

<script>
export default {
  name: "step4",
  data() {
    return {
      url: require("../assets/step-4-bg.jpg"),
      fit: "fill"
    };
  },
  methods: {
    handleStepClick() {
      this.$router.replace({path: "/step6"})
    }
  }
}
</script>

<style>

</style>