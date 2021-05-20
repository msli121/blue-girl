<template>
  <div class="p-step4">
    <div class="camera_outer">
      <video
        id="videoCamera"
        :width="videoWidth"
        :height="videoHeight"
        autoplay
      ></video>
      <canvas
        style="display: none"
        id="canvasCamera"
        :width="videoWidth"
        :height="videoHeight"
      ></canvas>

<!--      <div v-if="imgSrc" class="img_bg_camera">
        <img :src="imgSrc" alt="" class="tx_img" />
      </div>-->
    </div>
    <div class="dzq_bg" style="width:100%; height:90%; display: none; position: fixed; top: 0; left: 0; bottom: 0; right: 0;">
      <img style="float: right; width:100%; height:100%;"
           :src="backgroundImg"
           class="dzq_img" />
    </div>
    <div class="confirm"
         :v-loading="true"
         :class="confirmPhoto?'p-confirm-photo':''"
         v-if="confirmPhoto">
      <!-- 重拍 -->
      <div
        style="font-size:20px; color:yellow; float:left;"
        @click="goStep3()"
        >
        <span>重拍</span>
        <br/>
        <span>REMAKE</span>
      </div>
      <!-- 保存 -->
      <div
        style="font-size:20px; color:white; float:right; margin-left:15px;"
        @click="go()"
        >
        <span>确认</span>
        <br/>
        <span>UPLOAD</span>
      </div>
    </div>
    <div class="confirm" v-else>
      <!-- 拍照 -->
      <div
        style="font-size:20px; color:yellow;"
        @click="setImage()"
        >
        <span>确认</span>
        <br/>
        <span>CONFIRM</span>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { Loading } from 'element-ui';
import imgPreloaderList from "../config/imgPreloaderList.js";
export default {
  name: "step4",
  data() {
    return {
      videoWidth: 1080,
      videoHeight: 1920,
      imgSrc: "",
      thisCancas: null,
      thisContext: null,
      thisVideo: null,
      backgroundImg: "",
      bgName: "",
      confirmPhoto: false,
      file: "",
      loadingInstance: null,
      queryInfo: {
        mergedUrl: '',
        mergedId: '',
        qrCodeUrl: ''
      }
    };
  },
  created() {
    this.bgName = this.$route.query.name;
  },
  mounted() {
    /*setTimeout(_=>{
      this.loadingInstance.close();
    },2000)*/

    this.init();
  },
  methods: {
    goStep3() {
      this.$router.push({path:'/step3'})
    },

    init() {
      this.getCompetence();
    },
    // 调用权限（打开摄像头功能）
    getCompetence() {
      this.confirmPhoto = false;
      console.log("this.bgName", "../assets/scene_show/"+this.bgName);
      this.backgroundImg = require("../assets/scene_show/"+this.bgName);
      document.getElementsByClassName("dzq_bg")[0].style.display = "inline";
      // console.log(document.getElementsByClassName("dzq_bg")[0].style.display);

      this.imgSrc = "";

      var _this = this;
      this.thisCancas = document.getElementById("canvasCamera");
      this.thisContext = this.thisCancas.getContext("2d");
      this.thisVideo = document.getElementById("videoCamera");
      // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
      }
      // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象
      // 使用getUserMedia，因为它会覆盖现有的属性。
      // 这里，如果缺少getUserMedia属性，就添加它。
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
          // 首先获取现存的getUserMedia(如果存在)
          var getUserMedia =
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.getUserMedia;
          // 有些浏览器不支持，会返回错误信息
          // 保持接口一致
          if (!getUserMedia) {
            return Promise.reject(
              new Error("getUserMedia is not implemented in this browser")
            );
          }
          // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject);
          });
        };
      }
      var constraints = {
        audio: false,
        video: {
          width: this.videoWidth,
          height: this.videoHeight,
          transform: "scaleX(-1)",
        },
      };
      navigator.mediaDevices
        .getUserMedia(constraints)
        .then(function (stream) {
          // 旧的浏览器可能没有srcObject
          if ("srcObject" in _this.thisVideo) {
            _this.thisVideo.srcObject = stream;
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            _this.thisVideo.src = window.URL.createObjectURL(stream);
          }
          _this.thisVideo.onloadedmetadata = function (e) {
            console.log(e);
            _this.thisVideo.play();
          };
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //  绘制图片（拍照功能）

    setImage() {
      this.confirmPhoto = true;
      var _this = this;
      // 点击，canvas画图
      _this.thisContext.drawImage(
        _this.thisVideo,
        0,
        0,
        _this.videoWidth,
        _this.videoHeight
      );
      // 获取图片base64链接
      var image = this.thisCancas.toDataURL("image/png");
      _this.imgSrc = image;
      this.dataURLtoFile(_this.imgSrc, "groupPhoto.png");
      this.$emit("refreshDataList", this.imgSrc);
      this.uploadFile()
    },
    // base64转文件

    dataURLtoFile(dataurl, filename) {
      var arr = dataurl.split(",");
      var mime = arr[0].match(/:(.*?);/)[1];
      var bstr = atob(arr[1]);
      var n = bstr.length;
      var u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      this.file = new File([u8arr], filename, { type: mime });
      console.log(this.file);
    },
    // 关闭摄像头

    stopNavigator() {
      this.thisVideo.srcObject.getTracks()[0].stop();
    },

    // 上传图片
    uploadFile() {
      let formData = new FormData();
      console.log(this.file);
      console.log(211, this.bgName);
      formData.append("file", this.file);
      //formData.append("other", this.bgName)
      formData.append("other", this.bgName.replace('png', 'jpg'))

      this.loadingInstance = Loading.service(
          { fullscreen: true },
      )

      axios
        .post("https://www.performercn.com/api/file/photo", formData, {
          "Content-Type": "multipart/form-data",
        })
        .then((res) => {
          if (res.data.flag === "T") {
            /*this.$router.push({
              path: "/step5",
              query: { src: res.data.mergedPhoto.fileUrl },
            });*/
            this.loadingInstance.close()
            this.queryInfo.mergedUrl = res.data.data.mergedPhoto.fileUrl
            this.queryInfo.mergedId = res.data.data.mergedPhoto.id
            this.queryInfo.qrCodeUrl = res.data.data.qrCode.fileUrl
            this.$notify({
              title: '成功',
              message: '拍摄成功',
              type: 'success'
            })
            this.backgroundImg = this.queryInfo.mergedUrl
            console.log(233, this.queryInfo)
          } else {
            this.loadingInstance.close();
            this.$notify.error({
              title: "错误",
              message: `出错啦！重新尝试`,
            });
          }
        })
        .catch((err) => {
          this.loadingInstance.close();
          console.log(err);
        });
    },

    go() {
      this.$router.push({
        path:'/step5',
        query: this.queryInfo
      })
    },
  },
};
</script>

<style lang="less">
.camera_outer {
  position: relative;
  overflow: hidden;
  background-size: 100%;
  video,
  canvas,
  .tx_img {
    width: 100%;
    height: 100%;
    // -moz-transform: scaleX(-1);
    // -webkit-transform: scaleX(-1);
    // -o-transform: scaleX(-1);
    // transform: scaleX(-1);
  }
  .img_bg_camera {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    top: 0;
    img {
      width: 490px;
      height: 1000px;
    }
  }
}
.confirm {
  text-align: center;
  position: absolute;
  bottom: 0;
  margin-left: calc(50% - 33px);
}
.p-confirm-photo{
  margin-left: calc(50% - 80px);
}
.p-step4{
  height: 100%;
}
</style>
