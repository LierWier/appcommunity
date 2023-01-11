<template>
  <el-card v-if="!user.is_login">
    <el-button @click="store.state.loginDialogVisible = true">
      尚未登录， 请先登录
    </el-button>
  </el-card>
  <el-card v-else class="box-card">
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
</template>

<script setup>
import store from "@/store";
import router from "@/router";
import {reactive} from "vue";

const user = store.state.user
const data = reactive({
  iconStyle: 'marginRight: 6px'
})

const logout = () => {
  store.commit("logout")
  router.push({name: "home"})
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