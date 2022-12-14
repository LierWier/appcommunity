<template>
  <div class="common-layout">
    <el-container>
      <el-header class="has-sidebar">
        <nav-bar />
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
      <el-footer>@LIERLIER</el-footer>
    </el-container>
  </div>
</template>

<script setup>
import NavBar from "@/components/NavBar";
</script>

<style>
</style>
