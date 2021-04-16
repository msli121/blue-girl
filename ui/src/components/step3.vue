<template>
  <el-carousel :interval="3000"
               :autoplay="false"
               arrow="always"
               trigger="click">
    <el-carousel-item :v-loading="loading"
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
  import imgPreloaderList from "../config/imgPreloaderList.js";
export default {
  name: "step3",
  data() {
    return {
      loading: true,
      cardList: [],
    };
  },
  created() {
    this.cardList = [
      {
        id: 1,
        name: "scene_1.jpg",
        photoIndex: 3,
        value: imgPreloaderList[3]
      },
      {
        id: 2,
        name: "scene_2.jpg",
        photoIndex: 4,
        value: imgPreloaderList[4]
      },
      {
        id: 3,
        name: "scene_3.jpg",
        photoIndex: 5,
        value: imgPreloaderList[5]
      }
    ];
  },
  methods: {
    imgLoad() {
      console.log("图像加载完毕");
      this.loading = false;
      // this.$nextTick(() => {
      //   this.imgHeight = `${this.$refs.imgH[0].height}px`;
      //   // console.log(this.imgHeight)
      // });
    },
    handleStepClick: function(item) {
      this.$router.push({
        path: '/step4',
        query: {name: item.name, photoIndex: item.photoIndex }});
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
  height: 100%;
  .el-carousel__container{
    height: 100% !important;
    img {
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

.el-carousel__container {
  position: relative;
  height: 100% !important;
}
</style>
