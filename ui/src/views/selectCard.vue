<template>
  <el-carousel :interval="10000" trigger="click" :height="imgHeight">
    <el-carousel-item v-for="item in cardList" :key="item.id">
      <img
        @click="select(item.name)"
        ref="imgH"
        style="width: 100%"
        class="medium"
        :src="item.value"
        alt=""
        @load="imgLoad"
      />
    </el-carousel-item>
  </el-carousel>
</template>
<script>
export default {
  name: "selectCard",
  data() {
    return {
      imgHeight: "0px",
      cardList: [],
    };
  },
  created() {
    this.cardList = [
      {
        id: 1,
        name: "blue_girl_home.jpg",
        value: require("../assets/blue_girl_home.jpg")
      },
      {
        id: 2,
        name: "dzq_1.png",
        value: require("../assets/dzq_1.png")
      },
      {
        id: 3,
        name: "blue_girl_home.jpg",
        value: require("../assets/blue_girl_home.jpg")
      }
    ];
  },
  methods: {
    imgLoad() {
      this.$nextTick(() => {
        this.imgHeight = `${this.$refs.imgH[0].height}px`;
        // console.log(this.imgHeight)
      });
    },
    select(name) {
      this.$router.push({ path:'/photo', query: {"name": name}});
    },
  },
  mounted() {
    this.imgLoad();
    window.addEventListener("resize", this.imgLoad, false);
  },
  destroyed() {
    window.removeEventListener("resize", this.imgLoad, false);
  },
};
</script>

<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>