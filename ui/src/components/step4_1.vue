<template>
  <div class="camera_outer">
    <el-image class="bg-class"
      :src="this.bgSrc"
      fit="fill">
    </el-image>
    <video id="videoCamera" :width="videoWidth" :height="videoHeight" autoplay></video>
    <canvas style="display:none;" id="canvasCamera" :width="videoWidth" :height="videoHeight" ></canvas>
    <!--底部按钮-->
    <div class="confirm-panel">
      <!-- 拍照 -->
      <div
          style="color:yellow;"
          @click="setImage()"
      >
        <span>确认</span>
        <br/>
        <span class="span-en">CONFIRM</span>
      </div>
    </div>
  </div>

</template>

<script>
import apiBaseUrl from "../server/baseUrl";
import axios from "axios";
import { Loading } from 'element-ui';

export default {
  name: "step4",
  data() {
    return {
      videoWidth: 1080,
      videoHeight: 1920,
      imgSrc: "",
      bgSrc: "",
      canvas: null,
      context: null,
      video: null,
      backgroundImg: "",
      bgName: "",
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
    this.bgSrc = require("@/assets/scene_show/" + this.bgName);
    console.log("bgName", this.bgName);
    console.log("bgSrc", this.bgSrc);
  },
  mounted() {
    this.getCompetence();
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
      let that = this;
      this.canvas = document.getElementById("canvasCamera");
      this.context = this.canvas.getContext("2d");
      this.video = document.getElementById("videoCamera");
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
          let getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia;
          // 有些浏览器不支持，会返回错误信息
          // 保持接口一致
          if (!getUserMedia) {
            return Promise.reject(
              new Error("getUserMedia is not implemented in this browser")
            );
          }
          // 否则，使用Promise将调用包装到旧的 navigator.getUserMedia
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject);
          });
        };
      }
      let constraints = {
        audio: false,
        video: {
          width: {ideal: 540},
          height: {ideal: 960}
        },
      };
      navigator.mediaDevices.getUserMedia(constraints)
        .then(function (stream) {
          // 旧的浏览器可能没有srcObject
          if ("srcObject" in that.video) {
            that.video.srcObject = stream;
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            that.video.src = window.URL.createObjectURL(stream);
          }
          that.video.onloadedmetadata = function (e) {
            console.log(e);
            that.video.play();
          };
        })
        .catch((err) => {
          console.log(err);
        });
    },


    //  绘制图片（拍照功能）
    setImage() {
      let that = this;
      // 点击，canvas 画图
      this.context.drawImage(
          this.video,
          0,
          0,
          this.videoWidth,
          this.videoHeight
      );
      // 获取图片base64链接
      let image = this.canvas.toDataURL("image/png");
      that.imgSrc = image;
      this.dataURLtoFile(that.imgSrc, "groupPhoto.png");
      this.uploadFile()
    },
    // base64 转文件
    dataURLtoFile(dataurl, filename) {
      let arr = dataurl.split(",");
      let mime = arr[0].match(/:(.*?);/)[1];
      let bstr = atob(arr[1]);
      let n = bstr.length;
      let u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      this.file = new File([u8arr], filename, { type: mime });
      console.log(this.file);
    },
    // 关闭摄像头
    stopNavigator() {
      this.video.srcObject.getTracks()[0].stop();
    },

    // 上传图片
    uploadFile() {
      let formData = new FormData();
      let name = this.bgName.replace('png', 'jpg');
      formData.append("file", this.file);
      formData.append("other", name);
      console.log("file", this.file);
      console.log("other", name);
      // 屏幕 loading
      this.loadingInstance = Loading.service(
          { fullscreen: true },
      );
      // 发起调用请求
      let url = "";
      if(apiBaseUrl.indexOf("ai") > 0) {
        url = apiBaseUrl + "/file/photo";
      } else {
        url = apiBaseUrl + "/file/photo2";
      }
      console.log("url ", url);
      axios.post(url, formData, {
          "Content-Type": "multipart/form-data",
        }).then((res) => {
          if (res.data.flag === "T") {
            this.loadingInstance.close();
            this.$notify({
              title: '成功',
              message: '拍摄成功',
              type: 'success'
            })
            this.queryInfo.mergedUrl = res.data.data.mergedPhoto.fileUrl
            this.queryInfo.mergedId = res.data.data.mergedPhoto.id
            this.queryInfo.qrCodeUrl = res.data.data.qrCode.fileUrl
            this.backgroundImg = this.queryInfo.mergedUrl
            this.$router.push({
              path:'/step4_2',
              query: this.queryInfo
            })
            console.log("queryInfo", this.queryInfo)
          } else {
            this.loadingInstance.close();
            this.$notify.error({
              title: "错误",
              message: `出错啦！重新尝试`,
            });
          }
        }).catch((err) => {
          this.loadingInstance.close();
          console.log(err);
        });
    },

    go(){
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
  height: 100%;
  width: 100%;
  position: relative;
  overflow: hidden;
  video {
    position: relative;
    top: 0;
    left: 0;
    z-index: 1;
  }
}
.bg-class {
  height: 100%;
  width: 100%;
  position: fixed;
  overflow: hidden;
  z-index: 10;
}
.confirm-panel {
  text-align: center;
  position: absolute;
  bottom: 0;
  height: 200px;
  font-size: 72px;
  z-index: 11;
  width: 100%;
  background-color: rgba(0,0,0, .5);
  .span-en{
    color: black;
    //-webkit-text-stroke: 1px yellow;
    text-shadow: 0 1px yellow, 1px 0 yellow, -1px 0 yellow, 0 -1px yellow;
  }
}
.p-confirm-photo{
  margin-left: calc(50% - 80px);
}
.p-step4{
  height: 100%;
}
</style>
