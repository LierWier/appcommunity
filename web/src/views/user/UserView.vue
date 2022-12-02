<template>
  <el-card v-if="!$store.state.user.is_login">
    <el-button @click="$store.state.loginDialogVisible = true">
      尚未登录， 请先登录
    </el-button>
  </el-card>
  <el-card v-else class="box-card">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-image :src="user.photo" style="border-radius: 15px"/>
      </el-col>
      <el-col :span="18">
        <h1>{{ user.username }}{{user.isManager&&"&nbsp;&nbsp;&nbsp;&nbsp;管理员"}}</h1>
        <div>手机：{{ user.tel }}</div>
        <div>年龄：{{ user.age }}</div>
        <div>性别：{{ user.sex }}</div>
        <div>注册时间：{{ user.createTime }}</div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import store from "@/store";

const user = store.state.user.user
</script>

<style scoped>

</style>