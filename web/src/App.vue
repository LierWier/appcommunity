<template>
  <div class="common-layout">
    <el-container>
      <el-header class="has-sidebar">
        <nav-bar class="container" />
      </el-header>
      <el-main class="container">
        <router-view/>
      </el-main>
      <el-footer>@LIERLIER</el-footer>
    </el-container>
  </div>

  <el-config-provider :locale="locale"/>
</template>

<script setup>
import NavBar from "@/components/NavBar";
import {computed} from "vue";
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
// 语言配置
const locale = computed(() => zhCn)
</script>

<style>
</style>
