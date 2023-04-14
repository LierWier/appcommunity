<template>
  <div class="container">
    <el-card v-for="(app, index) in data.rankList" :key="app.id" :body-style="{ padding: '0px' }" class="box-card mt-3" shadow="never">
      <div class="d-flex">
        <el-image style="width: 100px; height: 100px" :src="app.appIcon || 'https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png'" fit="fill" />
        <div class="ms-4">
          <div class="p-1 fw-bold fs-5">{{ app.appName }}</div>
          <div class="p-1 fw-light">{{ app.description }}</div>
        </div>
        <div class="ms-auto text-end">
          <div>
            <el-tag :type="index + 1 <= 3 && 'primary' || 'info'" effect="dark">{{ index + 1 }}</el-tag>
          </div>
          <el-button @click="$router.push({name: 'app_content', params: {id: app.id}})" class="m-3">查看详情</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage} from "element-plus";
import {reactive} from "vue";

const data = reactive({})

const getAppRank = () => {
  AjaxUtils.getAppRank().then(resp => {
    if (resp.msg !== "success") return ElMessage.error("获取失败！")
    data.rankList = resp.data
  }).catch(() => ElMessage.error("网络连接失败！"))
}
getAppRank()
</script>

<style scoped>
.box-card {
  width: 600px;
  height: 100px;
  margin: 0 auto;
}
</style>