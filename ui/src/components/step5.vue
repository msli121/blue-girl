<template>
  <div class="step-4-bg">
    <el-image @click="handleStepClick"
              style="width: 100%; height: 100%"
              :src="imgUrl"
              :fit="bgFit">
    </el-image>
    <div style="z-index: 999">
      <!--      ;position: absolute;top: 40%-->
      <vue-drag-resize v-for="item in existUrlList" :key="item.tempID" :x="item.leftStart" :y="item.topStart" @dragstop="(event) => doEnd(event,item)">
        <el-image style="position:absolute;width: 300px;height: 300px;padding: 5px"

                  :src="item.url"
                  :fit="imageFit"
        ></el-image>
<!--        :style="item.style"-->
      </vue-drag-resize>
<!--      <draggable v-model="existUrlList" group="people" @start="drag=true" @end="doEnd">-->
<!--        <el-image v-for="item in existUrlList" :key="item.tempID"-->
<!--                  style="position:absolute;width: 300px;height: 300px;padding: 5px"-->
<!--                  :style="item.style"-->
<!--                  :src="item.url"-->
<!--                  :fit="imageFit"-->
<!--        ></el-image>-->
<!--      </draggable>-->
    </div>
    <div style="z-index:999;background-color: #111721;position: absolute;bottom: 0;width: 100%;opacity: .85">
      <div style="width: 100%;position: relative;" class="title">
        <span class="reset" style="position: absolute;left: 20px;top: 15px;" @click="doReset">重置</span>
        <h2>贴    纸</h2>
        <span class="save" style="position: absolute;right: 20px;top: 15px;" @click="doSave">保存</span>
      </div>
<!--      <div>-->
<!--        <el-button>保存</el-button>-->
<!--      </div>-->
      <el-image v-for="item in stickUrlList" :key="item.tempID"
                @click="handleStickerClick(item)"
                style="width: 150px;height: 150px;padding: 5px;opacity: 1"
                :src="item.url"
                :fit="imageFit"
      ></el-image>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import draggable from 'vuedraggable'
import VueDragResize from 'vue-drag-resize'
import {saveSticker} from "@/server/api";

export default {

  name: "step6",

  components:{
    draggable,
    VueDragResize
  },

  created() {
    console.log(this.$route)
    this.fileId = this.$route.query.mergedId
    this.imgUrl = this.$route.query.mergedUrl
    this.fileUrl = this.$route.query.qrCodeUrl
  },

  data() {
    return {
      // url: require("../assets/step-6-bg.jpg"),
      fileId:'',
      imgUrl:'',
      fileUrl:'',
      fit: "fill",
      url: require("../assets/bg10.png"),
      bgFit:'fill',
      imageFit: "contain",
      stickUrlList:[
        {
          tagKey:'sticker1.png',
          leftStart:250,
          topStart:250,
          urlString:'../assets/sticker1.png',
          url:require("../assets/sticker1.png"),
          tempID:Math.random(),
          style:'top:250px;left:250px',
        },
        {
          tagKey:'sticker2.png',
          leftStart:120,
          topStart:1300,
          urlString:'../assets/sticker2.png',
          url:require("../assets/sticker2.png"),
          tempID:Math.random(),
          style:'top:1300px;left:120px',
        },
        {
          tagKey:'sticker3.png',
          leftStart:800,
          topStart:700,
          urlString:'../assets/sticker3.png',
          url:require("../assets/sticker3.png"),
          tempID:Math.random(),
          style:'top:700px;left:800px',
        },
        {
          tagKey:'sticker4.png',
          leftStart:100,
          topStart:600,
          urlString:'../assets/sticker4.png',
          url:require("../assets/sticker4.png"),
          tempID:Math.random(),
          style:'top:600px;left:100px',
        },
        {
          tagKey:'sticker5.png',
          leftStart:700,
          topStart:250,
          urlString:'../assets/sticker5.png',
          url:require("../assets/sticker5.png"),
          tempID:Math.random(),
          style:'top:250px;left:700px',
        },
        {
          tagKey:'sticker6.png',
          leftStart:0,
          topStart:550,
          urlString:'../assets/sticker6.png',
          url:require("../assets/sticker6.png"),
          tempID:Math.random(),
          style:'top:550px;left:0',
        },
      ],
      existUrlList:[],
      moveList:[],
    };
  },
  methods: {
    handleStepClick() {
      this.$router.replace({path: "/step1"})
    },
    handleStickerClick(val) {
      let imgInfo = val
      let deleteFlag = false
      let delIndex = 0
      this.existUrlList.forEach((item,index) => {
        if (item.urlString === imgInfo.urlString) {
          deleteFlag = true
          delIndex = index
        }
      })
      if (deleteFlag) {
        this.existUrlList.splice(delIndex,1)
      }
      else {
        imgInfo.tempID = Math.random()
        this.existUrlList.push(imgInfo)
      }
    },
    doSave() {
      if (this.existUrlList.length > 0) {
        let saveForm = {
          fileId:parseInt(this.fileId),
          tags:[]
        }
        this.existUrlList.forEach(item => {
          let flag = true
          this.moveList.forEach(moveItem => {
            if (item.tagKey === moveItem.tagKey) {
              saveForm.tags.push({
                tagKey:item.tagKey,
                leftStart:moveItem.leftStart,
                topStart:moveItem.topStart,
              })
              flag = false
            }
          })
          if (flag) {
            console.log(9999)
            saveForm.tags.push({
              tagKey:item.tagKey,
              leftStart:item.leftStart,
              topStart:item.topStart,
            })
          }
        })
        console.log(saveForm)
        axios.post("https://www.performercn.com/api/file/tag", saveForm, {
              "Content-Type": "application/json;charset=UTF-8",
            }).then(json => {
             /* console.log("json", json);*/
              this.$router.push({ path:'/step6', query: { fileUrl: json.data.data.qrCode.fileUrl }});
        })
      }
      else {
        this.$router.push({ path:'/step6', query: {fileUrl: this.fileUrl}});
      }
    },
    doReset() {
      this.existUrlList = []
    },
    doEnd(event,item) {
      let flag = true
      this.moveList.forEach(moveItem => {
        if (moveItem.tagKey === item.tagKey) {
          moveItem.leftStart = event.left
          moveItem.topStart = event.top
          flag = false
        }
      })
      if (flag) {
        this.moveList.push({
          tagKey:item.tagKey,
          leftStart:event.left,
          topStart:event.top,
          urlString:item.urlString,
          url:item.url,
          tempID:Math.random(),
          style:item.style,
        })
      }
      // console.log(this.moveList)
      // console.log(this.existUrlList)
    }
  }
}
</script>

<style scoped lang="less">
.step-4-bg {
  position: relative;
  height: 1920px;
}
.title {
  text-align: center;
  color: yellow;

  h2 {
    display: inline-block;
    margin-top: 10px;
  }

  .save{
    span {

    }
  }
  .reset{
    span {
      position: absolute;
      left: 20px;
      top: 15px;
    }
  }

}
</style>
