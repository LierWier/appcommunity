<template>
  <div>
    <el-form :model="queryForm" label-width="50px" label-position="left">
      <el-form-item label="类别">
        <div class="flex items-center text-sm">
          <el-radio-group @change="getAppList" v-model="queryForm.category" class="index-radio">
            <el-radio label="">全部</el-radio>
            <el-radio v-for="(item,index) in category" :key="index" :label="item">{{ item }}</el-radio>
          </el-radio-group>
        </div>
      </el-form-item>
      <el-form-item label="排序">
        <div class="flex items-center text-sm">
          <el-radio-group @change="getAppList" v-model="queryForm.order" class="index-radio">
            <el-radio label="">全部</el-radio>
            <el-radio label="downloads">下载最多</el-radio>
            <el-radio label="score">评分最高</el-radio>
            <el-radio label="post_time">最新发布</el-radio>
          </el-radio-group>
        </div>
      </el-form-item>
      <el-form-item label="搜索">
        <el-input v-model="queryForm.appName" size="small" clearable style="width: 200px;" />
        <el-button type="primary" plain size="small" @click="getAppList">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4" v-for="app in appList" :key="app.id" style="margin-bottom: 20px">
        <el-card shadow="hover" :body-style="{padding: '0px'}">
          <el-row>
            <el-col :span="6">
              <img :src="app.appIcon || 'https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png'"
                   class="image" alt="">
            </el-col>
            <el-col :span="18">
              <div style="padding: 10px">
                <span>{{ app.appName }}</span>
                <div class="app_evl">
                  <span class="description">
                    {{ formatterUtils.appDownloadsFmt(app.downloads) }} 次
                    <el-icon><Bottom /></el-icon>
                  </span>
                  <span
                      style="margin-left: 15px"
                      :class="app.score>=4 && 'fc-primary' || app.score>=3 && 'fc-warning' || 'fc-danger'"
                  >
                    {{ app.score }}
                  </span>
                </div>
                <div class="bottom">
                  <time class="description">{{ app.description }}</time>
                  <el-button text class="button" @click="$router.push({name: 'app_content', params: {id: app.id}})">查看</el-button>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage} from "element-plus";
import {reactive, ref} from "vue";
import formatterUtils from "@/assets/utils/formatterUtils";

const appList = ref([])
const queryForm = reactive({appName: "", category: "", order: ""})
const category = ref([])

const getAppList = () => {
  AjaxUtils.getAppList(queryForm).then((resp) => {
    if (resp.msg === "success") {
      appList.value = resp.data.apps
    } else ElMessage.error(resp.msg)
  })
}
getAppList()
const getAppCategory = () => {
  AjaxUtils.getAppCategory().then(resp => {
    if (resp.msg === "success") category.value = resp.data;
    else ElMessage.error("拉取应用分类错误:" + resp.msg);
  }).catch(() => ElMessage.error("拉取应用分类错误！"))
}
getAppCategory()
</script>

<style scoped>
.description {
  font-size: 12px;
  color: #999;
}
.bottom {
  /*margin-top: 13px;*/
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.image {
  width: 100%;
  display: block;
}
.el-card {
  border-radius: 20px;
}
.app_evl {
  /*font-size: 14px;*/
  float: right;
}
.fc-primary {
  color: #409EFF;
}
.fc-warning {
  color: #E6A23C;
}
.fc-danger {
  color: #F56C6C;
}
</style>

<style>
/* 去掉el-radio前面的小圆点 */
.index-radio .el-radio__input {
  display: none;
}
</style>