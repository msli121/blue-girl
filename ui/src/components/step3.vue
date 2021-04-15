<template>
  <el-carousel :interval="3000"
               :autoplay="false"
               arrow="always"
               trigger="click">
    <el-carousel-item
        v-for="item in cardList"
                      :key="item.id">
      <img
          @click="handleStepClick(item.name)"
          ref="imgH"
          style="width: 100%;"
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
  name: "step3",
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
        name: "dzq_2.png",
        value: require("../assets/step3_overall/dzq_2.jpg")
      },
      {
        id: 2,
        name: "dzq_1.jpg",
        value: require("../assets/step3_overall/dzq_1.jpg")
      },
      {
        id: 3,
        name: "dzq_3.png",
        value: require("../assets/step3_overall/dzq_3.jpg")
      },
      {
        id: 4,
        name: "dzq_4.jpg",
        value: require("../assets/step3_overall/dzq_4.jpg")
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
    handleStepClick(name) {
      this.$router.push({ path:'/step4', query: {"name": name}});
    },
  },
  mounted() {
    this.imgLoad();
    window.addEventListener("resize", this.imgLoad, false);
  },
  destroyed() {
    window.removeEventListener("resize", this.imgLoad, false);
  },
}
</script>

<style lang="less">
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

/deep/ .el-carousel__container {
  height: 100%;
}
.el-carousel{
  height: 100%;
  .el-carousel__container{
    height: 100%;
    img{
      height: 100%!important;
    }
    .el-carousel__arrow{
      background: #8664ed;
      box-shadow: white 0 0 10px;
      width: 76px;
      height: 76px;
      font-size: 36px;
    }
  }
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
