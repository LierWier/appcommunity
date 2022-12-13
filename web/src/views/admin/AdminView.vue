<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="200px">
        <el-menu
            default-active="1"
            class="el-menu-vertical-demo"
            :router="true"
        >
          <el-menu-item index="1" :route="{name: 'user_manage'}">
            <el-icon><icon-menu /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="2" :route="{name: 'blog_manage'}">
            <el-icon><document /></el-icon>
            <span>博客管理</span>
          </el-menu-item>
          <el-menu-item index="3" :route="{name: 'news_manage'}">
            <el-icon><document /></el-icon>
            <span>资讯管理</span>
          </el-menu-item>
          <el-menu-item index="4" :route="{name: 'app_manage'}">
            <el-icon><setting /></el-icon>
            <span>应用管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view v-slot="{ Component }">
          <transition mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import {Menu as IconMenu} from '@element-plus/icons-vue'
</script>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 0.2s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>