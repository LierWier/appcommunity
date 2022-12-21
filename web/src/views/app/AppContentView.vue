<template>
  <el-card class="box-card">
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"> 应用详情 </span>
      </template>
    </el-page-header>
    <el-divider />
    <div style="height: 130px">
      <div class="app_info">
        <el-image style="width: 130px; height: 130px" :src="app.appIcon" fit="fill" />
      </div>
      <div class="app_info">
        <h1 class="app_info_name">{{ app.appName }}</h1>
        <div class="description mt">{{ app.description || "&nbsp;" }}</div>
        <div class="description mt">{{ app.author || "&nbsp;" }}</div>
        <div class="description mt">{{ app.downloads + ' 次下载' }}</div>
      </div>
      <div class="app_info_end">
        <el-rate
            class="app_info_content"
            v-model="app.score"
            disabled
            show-score
            text-color="#ff9900"
            size="large"
            style="height: 130px; margin-right: 20px;"
        />
      </div>
    </div>
    <el-divider />
    <div>
      <h3>应用介绍</h3>
      <div>
        <pre>{{ app.content || "暂无介绍" }}</pre>
      </div>
    </div>
    <el-divider />
    <div>
      <h3>评论</h3>
      <div style="height: 100px;">
        <div class="evl_rate_box evl_rate_box_left">
          <div class="count">{{ app.score }}</div>
          <div class="commentators">{{ data.total }}人评价</div>
        </div>
        <div class="evl_rate_box evl_rate_box_right" style="height: 100%;">
          <div v-for="i in 5" :key="i">
            <div class="rate-bar" style="width: 100px">
              <el-icon v-for="j in 6-i" :key="j"><Star /></el-icon>
            </div>
            <el-progress class="rate-bar" :show-text="false" :percentage="data.ratePct[6-i]" />
          </div>
        </div>
      </div>
      <el-divider />
      <div>
        <el-card v-if="!loginUser.is_login" class="evl-card" style="margin: 20px 0">
          <template #header>
            <div class="card-header">
              <span>我的评论</span>
            </div>
          </template>
          尚未登陆
        </el-card>
        <el-card v-else-if="myEvl" class="evl-card my-evl" style="margin: 20px 0">
          <template #header>
            <div class="card-header">
              <span>我的评论</span>
              <el-popconfirm title="确认删除吗?" @confirm="deleteAppEvl">
                <template #reference>
                  <el-button class="button" text>删除</el-button>
                </template>
              </el-popconfirm>
            </div>
          </template>
          <div class="evl-box" style="display: flex">
            <div class="evl-box-left" style="width: 50px">
              <el-avatar :size="50" src="https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png" />
              <el-button style="width: 50px; margin-top: 10px;" :icon="Star" round>赞</el-button>
            </div>
            <div class="evl-box-right" style="margin-left: 20px">
              <el-rate
                  v-model="myEvl.score"
                  disabled
                  size="small"
              />
              <div>{{ loginUser.info.username }}</div>
              <div class="description" style="font-size: 12px">{{ myEvl.createTime }}</div>
              <div>{{ myEvl.content }}</div>
            </div>
          </div>
        </el-card>
        <el-card v-else class="evl-card" style="margin: 20px 0">
          <template #header>
            <div class="card-header">
              <span>我的评论</span>
              <el-button class="button" text @click="onSubmit">发布</el-button>
            </div>
          </template>
          <el-form :model="form" label-width="120px" label-position="top">
            <el-form-item label="评分">
              <el-rate v-model="form.score" />
            </el-form-item>
            <el-form-item label="评语">
              <el-input v-model="form.content" type="textarea" />
            </el-form-item>
          </el-form>
        </el-card>
        <el-card v-for="item in appEvls" :key="item.id" class="evl-card" shadow="hover" style="margin: 20px 0">
          <div class="evl-box" style="display: flex">
            <div class="evl-box-left" style="width: 50px">
              <el-avatar :size="50" src="https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png" />
              <el-button style="width: 50px; margin-top: 10px;" :icon="Star" round>赞</el-button>
            </div>
            <div class="evl-box-right" style="margin-left: 20px">
              <el-rate
                  v-model="item.score"
                  disabled
                  size="small"
              />
              <div>{{ item.username }}</div>
              <div class="description" style="font-size: 12px">{{ item.createTime }}</div>
              <div>{{ item.content }}</div>
            </div>
          </div>
        </el-card>
      </div>
      <div style="height: 40px;margin: 20px">
        <el-pagination
            v-model:current-page="data.page"
            v-model:page-size="data.pageSize"
            :page-sizes="[10, 20, 50, 100, 200]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.total"
            @size-change="getAppEvlList"
            @current-change="getAppEvlList"
            style="float: right"
        />
      </div>
    </div>
  </el-card>
</template>

<script setup>
import {reactive, ref} from "vue";
import {useRoute} from "vue-router";
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage} from "element-plus";
import router from "@/router";
import {Star} from '@element-plus/icons-vue'
import store from "@/store";

const route = useRoute()
const app = ref({appName: "微信"})
const appEvls = ref([])
const myEvl = ref(null)
const form = reactive({score: 5, content: ""})
const loginUser = store.state.user
const data = reactive({
  page: 1,
  pageSize: 10,
  total: 0,
  rateCount: {},
  ratePct: {}
})

const getAppInfo = (id) => {
  AjaxUtils.getAppInfo({id}).then(resp => {
    if (resp.msg === 'success') app.value = resp.data
    else ElMessage.error("error")
  })
}
getAppInfo(route.params.id)
const getAppEvlList = () => {
  AjaxUtils.getAppEvlList({
    appId: route.params.id, page: data.page, pageSize: data.pageSize
  }).then(resp => {
    if (resp.msg === "success") {
      appEvls.value = resp.data.appEvls
      data.total = resp.data.total
      data.rateCount = rateCountFunc(appEvls.value)
      data.ratePct = ratePctFunc(data.rateCount)
    } else ElMessage.error("获取评论失败！")
  }).catch(() => ElMessage.error("获取评论失败！"))
}

const getAppEvlByLoginUser = () => {
  if (!loginUser.is_login) return;
  AjaxUtils.getAppEvlByLoginUser({appId: route.params.id}).then(resp => {
    if (resp.msg === "success") myEvl.value = resp.data
  })
}
const getAppEvlsInit = () => {
  myEvl.value = null
  getAppEvlByLoginUser()
  getAppEvlList()
}
getAppEvlsInit()

const onSubmit = () => {
  const data = {...form, appId: route.params.id, userId: loginUser.info.id}
  AjaxUtils.postAppEvl(data).then(resp => {
    if (resp.msg !== "success") {
      ElMessage.error("评论失败！" + resp.msg)
      return;
    }
    ElMessage.success("评论成功！")
    getAppEvlsInit()
  })
}
const deleteAppEvl = () => {
  AjaxUtils.deleteAppEvl({id: myEvl.value.id}).then(resp => {
    if (resp.msg !== "success") {
      ElMessage.error("删除失败！" + resp.msg)
      return;
    }
    ElMessage.success("删除成功！")
    getAppEvlsInit()
  })
}

const rateCountFunc = (appEvls) => {
  const rateCount = {"5": 0, "4": 0, "3": 0, "2": 0, "1": 0}
  for (const item of appEvls) rateCount[item.score]++
  return rateCount
}
const ratePctFunc = (rateCount) => {
  const ratePct = {"5": 0, "4": 0, "3": 0, "2": 0, "1": 0}
  let total = 0
  for (const i in rateCount) total += rateCount[i]
  for (const i in rateCount) ratePct[i] = rateCount[i] / total * 100
  return ratePct
}

const goBack = () => {
  router.go(-1)
}
</script>

<style scoped>
.app_info {
  display: inline-block;
  margin-left: 20px;
  height: 130px;
  float: left;
}
.app_info_end {
  float: right;
}
.app_info h1 {
  margin: 0;
}
.mt {
  margin: 10px 0;
}
.description {
  font-size: 16px;
  color: #999;
}
pre {
  font-size: 15px;
  font-family: "system-ui";
  overflow: auto;
}
.evl_rate_box_left .count {
  width: 80px;
  height: 80px;
  line-height: 80px;
  text-align: center;
  font-size: 60px
}
.evl_rate_box_left .commentators {
  width: 80px;
  line-height: 17px;
  opacity: .5;
  font-size: 12px;
  color: #000;
  text-align: center;
}
.evl_rate_box_right {
  margin-left: 50px;
  width: 500px;
}
.evl_rate_box {
  display: inline-block;
  float: left;
}
.el-progress {
  width: 200px;
}
.rate-bar {
  display: inline-block;
}
.my-evl {
  /*border: 1px solid #409EFF;*/
  background: #ecf5ff;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}
</style>