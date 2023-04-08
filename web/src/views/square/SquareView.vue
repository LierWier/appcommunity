<template>
  <el-table :data="data.blogs" stripe  >
<!--    <el-table-column type="index"/>-->
    <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip>
      <template #default="scope">
        <el-link type="primary" :href="`http://127.0.0.1:8080/blog/${scope.row.id}`" target="_blank" :underline="false">
          {{scope.row.title}}
        </el-link>
      </template>
    </el-table-column>
    <el-table-column prop="tag" label="标签" width="120" show-overflow-tooltip/>
    <el-table-column prop="authorName" label="作者" width="100" show-overflow-tooltip/>
    <el-table-column prop="description" label="描述"  show-overflow-tooltip/>
    <el-table-column prop="liked" label="赞" width="50"/>
    <el-table-column prop="unlike" label="踩" width="50"/>
    <el-table-column prop="updateTime" label="活跃时间" width="200"/>
  </el-table>
  <el-pagination
      class="mt-3"
      v-model:current-page="data.page"
      :page-size="10"
      layout="->, prev, pager, next, total"
      :total="data.total"
      @current-change="getBlogList"
  />
  <el-card class="box-card mt-3">
    <template #header>
      <div class="card-header">
        <span class="fw-bold">发表新帖</span>
        <el-button class="button" type="primary" @click="onSubmit">发表</el-button>
      </div>
    </template>
    <el-form :model="data.form" label-width="50px">
      <el-form-item label="标题">
        <el-input v-model="data.form.title" />
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="data.form.tag" multiple style="width: 100%">
          <el-option v-for="(item,index) in data.tags" :key="index" :label="item" :value="item"/>
        </el-select>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="data.form.description" />
      </el-form-item>
      <el-form-item label="内容">
        <el-input v-model="data.form.content" type="textarea" :rows="10" />
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage} from "element-plus";
import {reactive} from "vue";

const data = reactive({
  blogs: null,
  page: 1, pageSize: 10, total: 10,
  form: {}
})
// const form = reactive({})

const getBlogList = () => {
  AjaxUtils.getBlogList({page: data.page, pageSize: data.pageSize}).then(resp => {
    if (resp.msg !== "success") {
      ElMessage.error("获取失败！")
      return
    }
    data.blogs = resp.data.blogs
    data.total = resp.data.total
  }).catch(() => ElMessage.error("网络连接失败！"))
}
const getTagList = () => {
  AjaxUtils.getTagList().then(resp => {
    if (resp.msg !== "success") return ElMessage.error("标签获取失败！")
    data.tags = resp.data
  })
}
const getInit = () => {
  getBlogList()
  getTagList()
}
getInit()

const onSubmit = () => {
  const newForm = {...data.form, tag: data.form.tag.toString()}
  AjaxUtils.postBlog(newForm).then(resp => {
    if (resp.msg !== "success") return ElMessage.error(resp.msg)
    ElMessage.success("发表成功！")
    getInit()
    data.form = {}
  }).catch(() => ElMessage.error("网络连接失败！"))
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>