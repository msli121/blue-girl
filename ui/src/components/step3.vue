<template>
  <el-carousel :interval="5000"
               :autoplay="true"
               arrow="always"
               trigger="click">
    <el-carousel-item
        v-for="item in cardList"
                      :key="item.id">
      <img
          @click="handleStepClick(item)"
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
        name: "scene_1.png",
        value: require("@/assets/scene_origin/scene_1.png")
      },
      {
        id: 2,
        name: "scene_2.png",
        value: require("@/assets/scene_origin/scene_2.png")
      },
      {
        id: 3,
        name: "scene_3.png",
        value: require("@/assets/scene_origin/scene_3.png")
      }
    ];
  },
  methods: {
    imgLoad() {
      console.log("图像加载完毕");
      this.$nextTick(() => {
        this.imgHeight = `${this.$refs.imgH[0].height}px`;
        // console.log(this.imgHeight)
      });
    },
    handleStepClick(item) {
      this.$router.push({
        path: '/step4_1',
        query: {name: item.name}});
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

/deep/.el-carousel__container {
  height: 100%;
}
.el-carousel__container {
  position: relative;
  height: 100%;
}
.el-carousel{
  height: 100% !important;
  .el-carousel__container{
    height: 100% !important;
    img {
      height: 100%!important;
    }
    .el-carousel__arrow{
      //background: #8664ed;
      //box-shadow: #00489a 0 0 10px;
      color: #0178ff;
      background-color: rgba(255,255,255,0);
      width: 76px;
      height: 76px;
      >i{
        font-size: 72px;
        font-weight: bolder;
        /*&:before{
          box-shadow: #00489a 0 0 10px;
        }*/
      }
    }
  }
}

.el-carousel__container {
  position: relative;
  height: 100% !important;
}
</style>
