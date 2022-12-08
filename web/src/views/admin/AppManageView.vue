<template>
  <el-form :inline="true" :model="queryApp" class="demo-form-inline">
    <el-form-item label="应用名称">
      <el-input v-model="queryApp.appName" clearable style="width: 150px;"/>
    </el-form-item>
    <el-form-item label="开发者">
      <el-input v-model="queryApp.author" clearable style="width: 150px;"/>
    </el-form-item>
    <el-form-item label="类别">
      <el-select v-model="queryApp.category" style="width: 80px;">
        <el-option label="全部" value="" />
        <el-option v-for="(item,index) in category" :key="index" :label="item" :value="item" />
      </el-select>
    </el-form-item>
    <el-form-item label="状态">
      <el-select v-model="queryApp.status" style="width: 80px;">
        <el-option label="全部" value="" />
        <el-option label="正常" value="1" />
        <el-option label="下架" value="0" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="getAppList">查询</el-button>
    </el-form-item>
  </el-form>
  <div>
    <el-button-group>
      <el-button type="primary" plain @click="addAppDialogVisible = true">添加</el-button>
      <el-button type="danger" plain @click="deleteApp">删除</el-button>
    </el-button-group>
    <el-button-group class="ml-4">
      <el-button type="primary" plain @click="changeShelf(true)">上架</el-button>
      <el-button type="danger" plain @click="changeShelf(false)">下架</el-button>
    </el-button-group>
  </div>
  <el-table
      :data="tableData"
      border style="width: 100%"
      stripe
      @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" />
    <el-table-column fixed type="index" align="center" />
    <el-table-column fixed prop="appName" label="应用名称" align="center" width="100" show-overflow-tooltip/>
    <el-table-column prop="id" label="ID" align="center" width="50" />
    <el-table-column prop="description" label="描述" align="center" width="200" show-overflow-tooltip/>
    <el-table-column prop="author" label="开发者" align="center" width="150" show-overflow-tooltip/>
    <el-table-column prop="category" label="类别" align="center" width="100"/>
    <el-table-column prop="score" label="评分" align="center" />
    <el-table-column prop="downloads" label="下载量" align="center" width="100" sortable :formatter="(_,__,cell) => formatterUtils.appDownloadsFmt(cell)"/>
    <el-table-column prop="postTime" label="发布时间" align="center" sortable width="120"/>
    <el-table-column prop="updatePostTime" label="上次更新" align="center" sortable width="120"/>
    <el-table-column fixed="right" prop="status" label="状态" align="center" sortable :formatter="(_,__,cell) => formatterUtils.appStatusFmt(cell)" />
    <el-table-column fixed="right" label="操作" align="center" >
      <template #default>
        <el-button link type="primary">修改</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import {reactive, ref} from "vue";
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage} from "element-plus";
import formatterUtils from "@/assets/utils/formatterUtils";

const tableData = ref([])
const queryApp = reactive({appName: "", author: "", category: "", status: ""})
const category = ref([])
const selection = ref([])

const handleSelectionChange = (val) => {
  selection.value = val
  // AjaxUtils.text()
}

const getAppList = () => {
  AjaxUtils.getAppList(queryApp).then((resp) => {
    if (resp.msg === "success") tableData.value = resp.data
    else ElMessage.error(resp.msg)
  })
}

const getAppCategory = () => {
  AjaxUtils.getAppCategory().then(resp => {
    if (resp.msg === "success") category.value = resp.data;
    else ElMessage.error("拉取应用分类错误:" + resp.msg);
  }).catch(() => ElMessage.error("拉取应用分类错误！"))
}

const pageInit = () => {
  getAppList();
  getAppCategory();
}
pageInit();

const changeShelf = (v) => {
  const ids = []
  if (selection.value.length > 0) {
    for (const i of selection.value) ids.push(i["id"])
    AjaxUtils.updateAppStatus({ids, status: v ? 1 : 0}).then(resp => {
      if (resp.msg === "success") ElMessage.success("状态更新成功！")
      pageInit();
    })
  } else ElMessage.info("请选择至少1条数据！");
}

const deleteApp = () => {
  const ids = []
  if (selection.value.length > 0) {
    for (const i of selection.value) ids.push(i["id"])
  } else ElMessage.info("请选择至少1条数据！");
}
</script>

<style scoped>
.ml-4 {
  margin-left: 1.5rem;
}
</style>