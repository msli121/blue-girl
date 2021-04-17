<template>
  <div>
    <iframe id="flash22"
            align="center" width="338" height="564"
            src="http://sda.4399.com/4399swf/upload_swf/ftp31/gamehwq/20200310/07/index.htm"
            frameborder="no"
            border="0" marginwidth="0" marginheight="0" scrolling="no" style="width: 714px; height: 1191px;"></iframe>
  </div>
</template>

<script>
export default {
  data() {
    return {

    };
  },
  methods: {
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    }
  }
}
</script>

<style>

</style>