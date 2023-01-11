<template>
  <el-card v-if="!user.is_login">
    <el-button @click="store.state.loginDialogVisible = true">
      尚未登录， 请先登录
    </el-button>
  </el-card>
  <div v-else>
    <el-card class="box-card">
      <div class="is-flex justify-content-between" style="height: 250px">
        <el-image :src="user.info.photo" style="border-radius: 15px; width: 250px; height: 250px"/>
        <div style="height: 250px; align-items: center;" class="is-flex">
          <el-descriptions
              direction="vertical"
              :column="3"
              size="large"
          >
            <el-descriptions-item label="关注" label-align="center" align="center" width="100px">123</el-descriptions-item>
            <el-descriptions-item label="粉丝" label-align="center" align="center" width="100px">123456</el-descriptions-item>
            <el-descriptions-item label="发布" label-align="center" align="center" width="100px">12</el-descriptions-item>
          </el-descriptions>
        </div>
        <el-descriptions
            style="width: 400px;"
            class="margin-top"
            title="用户"
            :column="1"
            border
        >
          <template #extra>
            <el-button type="danger" plain @click="logout">退出登录</el-button>
          </template>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <User />
                </el-icon>
                用户名
              </div>
            </template>
            {{ user.info.username }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <iphone />
                </el-icon>
                电话号码
              </div>
            </template>
            {{ user.info.tel }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <Calendar />
                </el-icon>
                生日
              </div>
            </template>
            {{ user.info.birth }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <tickets />
                </el-icon>
                性别
              </div>
            </template>
            <el-tag v-if="user.info.sex" :type="user.info.sex==='男'?'primary':'danger'">
              {{ user.info.sex }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <office-building />
                </el-icon>
                注册时间
              </div>
            </template>
            {{ user.info.createTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <el-card class="mt-4">
      <template #header>
        <div class="card-header">
          <span>我的评论</span>
        </div>
      </template>
      <div>
        <el-table :data="data.evaluation" stripe table-layout="auto">
          <el-table-column fixed type="index" align="center"/>
          <el-table-column prop="appName" label="应用" align="center" />
          <el-table-column prop="score" label="评分" align="center"/>
          <el-table-column prop="content" label="内容" align="center" />
          <el-table-column prop="liked" label="获赞" align="center" />
          <el-table-column prop="createTime" label="日期" align="center" />
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button link type="danger" @click="deleteEvl(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import store from "@/store";
import router from "@/router";
import {reactive} from "vue";
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage, ElMessageBox} from "element-plus";

const user = store.state.user
const data = reactive({
  iconStyle: 'marginRight: 6px',
  evaluation: [],
  page: 1,
  pageSize: 10,
})

const getAppEvlList = () => {
  AjaxUtils.getAppEvlListByLoginUser({page: data.page, pageSize: data.pageSize}).then(resp => {
    if (resp.msg !== 'success') return ElMessage.error("获取评论记录失败！" + resp.msg)
    data.evaluation = resp.data.list
    data.total = resp.data.total
  }).catch(() => { ElMessage.error("获取评论记录失败！") })
}
const pageInit = () => {
  getAppEvlList()
}
pageInit()

const logout = () => {
  store.commit("logout")
  router.push({name: "home"})
}

const deleteEvl = (id) => {
  ElMessageBox.confirm("确认删除？", "提示").then(() => {
    console.log("delete:", id)
  }).catch(() => {
    console.log("cancel")
  })
}

</script>

<style scoped>
.cell-item {
  display: flex;
  align-items: center;
}
.is-flex {
  display: flex;
}

</style>