<template>
  <div class="p-step-3">
    <el-carousel
        :autoplay="false"
        :interval="10000" trigger="click">
      <el-carousel-item
          style="width: 100%;height: 100%"
          v-for="item in cardList"
          :key="item.id">
        <img
          :src="item.value"
          style="height: 100%;width: 100%"
        >
<!--        <img
            @click="handleStepClick(item.name)"
            ref="imgH"
            style="width: 100%;"
            class="medium"
            :src="item.value"
            alt=""
            @load="imgLoad"
        />-->
      </el-carousel-item>
    </el-carousel>
  </div>
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
        name: "dzq_1.jpg",
        value: require("../assets/step3/dzq_1.jpg")
      },
      {
        id: 2,
        name: "dzq_2.jpg",
        value: require("../assets/step3/dzq_2.png")
      },
      {
        id: 3,
        name: "dzq_3.jpg",
        value: require("../assets/step3/dzq_3.png")
      },
      {
        id: 4,
        name: "dzq_4.jpg",
        value: require("../assets/step3/dzq_4.jpg")
      }
    ];
    this.cardList.forEach(i=>{
      console.log(i.name)
    })
  },
  methods: {
    imgLoad() {
      this.$nextTick(() => {
        this.imgHeight = `${this.$refs.imgH[0].height}px`;
        // console.log(this.imgHeight)
      });
    },
    handleStepClick(name) {
      //this.$router.push({ path:'/step4', query: {"name": name}});
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
.p-step-3{
  //border: solid 1px red;
  height: 100%;
  .el-carousel{
    //margin-top: 16%;
   /* height: 68%;
    width: 78%;*/
    width: 100%;
    height: 100%;
    /*background: url("../assets/step-3-bg.jpg") 100% 100% no-repeat;
    background-size: cover;*/
    display: flex;
    justify-content: center;
    align-items: center;
    border: solid 2px yellow;
    .el-carousel__container{
      height: 100%;
      width: 100%;
      >img{
        height: 68%;
        width: 78%;
      }
    }
  }
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

/*.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}*/
</style>
