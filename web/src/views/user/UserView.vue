<template>
  <el-card v-if="!user.is_login">
    <el-button @click="store.state.loginDialogVisible = true">
      尚未登录， 请先登录
    </el-button>
  </el-card>
  <el-card v-else class="box-card">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-image :src="user.info.photo" style="border-radius: 15px"/>
      </el-col>
      <el-col :span="18">
        <h1>{{ user.info.username }}{{user.info.isManager&&"&nbsp;&nbsp;&nbsp;&nbsp;管理员"||""}}</h1>
        <div>手机：{{ user.info.tel }}</div>
        <div>年龄：{{ user.info.age }}</div>
        <div>性别：{{ user.info.sex }}</div>
        <div>注册时间：{{ user.info.createTime }}</div>
        <el-button @click="logout">
          退出登录
        </el-button>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import store from "@/store";
import router from "@/router";

const user = store.state.user

const logout = () => {
  store.commit("logout")
  router.push({name: "home"})
}
</script>

<style scoped>

</style>