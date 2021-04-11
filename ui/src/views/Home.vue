<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/blue_girl_home.jpg" @click="photo"/>
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: "Home",
  components: {
  },
  methods: {
    photo() {
      this.$router.push({ path:'/selectCard'});
    }
  }
};
</script>
<style>
img{
  width: 100%;
}
</style>
