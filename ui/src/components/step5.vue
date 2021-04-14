<template>
  <div class="step-5-bg">
    <video id="videoCamera" :width="videoWidth" :height="videoHeight" autoplay></video>
    <canvas style="display:none;" id="canvasCamera" :width="videoWidth" :height="videoHeight" ></canvas>

    <div v-if="imgSrc" class="img_bg_camera">
      <img :src="imgSrc" alt="" class="tx_img">
    </div>
    <button @click="getCompetence">打开摄像头</button>
    <button @click="stopNavigator">关闭摄像头</button>
    <button @click="setImage">拍照</button>
  </div>
</template>

<script>
export default {
  name: "step5",
  data() {
    return {
      videoWidth: 720,
      videoHeight: 1280,
      imgSrc: '',
      thisCanvas: null,
      thisContext: null,
      thisVideo: null,
    };
  },
  methods: {
    // 打开摄像头功能
    getCompetence() {
      var _this = this
      this.thisCancas = document.getElementById('canvasCamera')
      this.thisContext = this.thisCancas.getContext('2d')
      this.thisVideo = document.getElementById('videoCamera')

      // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {}
      }

      // 使用getUserMedia，因为它会覆盖现有的属性。
      // 这里，如果缺少getUserMedia属性，就添加它。
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
          // 首先获取现存的getUserMedia(如果存在)
          var getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia
          // 有些浏览器不支持，会返回错误信息
          // 保持接口一致
          if (!getUserMedia) {
            return Promise.reject(new Error('getUserMedia is not implemented in this browser'))
          }
          // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject)
          })
        }
      }

      // 摄像头大小配置
      var constraints = {
        audio: false,
        video: {
          width: this.videoWidth,
          height: this.videoHeight,
          transform: 'scaleX(-1)' }
      }

      navigator.mediaDevices.getUserMedia(constraints).then(function (stream) {
        // 旧的浏览器可能没有srcObject
        if ('srcObject' in _this.thisVideo) {
          _this.thisVideo.srcObject = stream
        } else {
          // 避免在新的浏览器中使用它，因为它正在被弃用。
          _this.thisVideo.src = window.URL.createObjectURL(stream)
        }
        _this.thisVideo.onloadedmetadata = function (e) {
          console.log(e);
          _this.thisVideo.play()
        }
      }).catch(err => {
        this.$notify({
          title: '警告',
          message: '没有开启摄像头权限或浏览器版本不兼容.',
          type: 'warning'
        });
        console.log(err)
      })
    },

    // 拍照功能
    setImage() {
      var _this = this
      // 点击，canvas画图
      _this.thisContext.drawImage(_this.thisVideo, 0, 0, _this.videoWidth, _this.videoHeight)
      // 获取图片base64链接
      var image = this.thisCancas.toDataURL('image/jpg')
      _this.imgSrc = image

    },

    // 关闭摄像头

    stopNavigator () {
      this.thisVideo.srcObject.getTracks()[0].stop()
    }

  }
}
</script>

<style lang="less" scoped>
.step-5-bg {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  background: url("../assets/step-5-bg.jpg") no-repeat center;
  background-size: 100%;
  video,canvas,.tx_img {
    -moz-transform:scaleX(-1);
    -webkit-transform:scaleX(-1);
    -o-transform:scaleX(-1);
    transform:scaleX(-1);
  }
}
</style>
