<template>
  <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
      :router="true"
  >
    <el-menu-item index="1" :route="{name: 'home'}"><el-icon><House /></el-icon>首页</el-menu-item>
    <el-menu-item index="2" :route="{name: 'square'}"><el-icon><ChatSquare /></el-icon>广场</el-menu-item>
    <el-menu-item index="3" :route="{name: 'rank'}"><el-icon><Star /></el-icon>应用排行</el-menu-item>
    <el-menu-item index="4" :route="{name: 'app'}"><el-icon><CopyDocument /></el-icon>全部应用</el-menu-item>
    <el-menu-item v-if="$store.state.user.is_login" index="5" :route="{name: 'user'}"><el-icon><User /></el-icon>个人中心</el-menu-item>
    <el-button class="el-menu-item" v-else text @click="$store.commit('updateLoginDialogVisible', true)">
      <el-icon><User /></el-icon>登录
    </el-button>
    <el-menu-item index="6" :route="{name: 'admin'}" v-if="$store.state.user.is_login && true"><el-icon><Key /></el-icon>管理员</el-menu-item>
    <el-menu-item index="-1" :route="{name: 'text'}">测试页面</el-menu-item>
  </el-menu>

  <login-or-register-dialog v-model="$store.state.loginDialogVisible"/>
</template>

<script setup>
import { ref } from 'vue'
import LoginOrRegisterDialog from "@/components/LoginOrRegisterDialog";

const activeIndex = ref('1')
const handleSelect = () => {
}
</script>

<style scoped>
</style>