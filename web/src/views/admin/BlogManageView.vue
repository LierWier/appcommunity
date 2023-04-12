<template>
  <div>
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">
      <el-form-item label="标题">
        <el-input v-model="queryForm.title"/>
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="queryForm.authorName"/>
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="queryForm.tag" multiple collapse-tags collapse-tags-tooltip style="width: 100%">
          <el-option v-for="(item,index) in data.tags" :key="index" :label="item" :value="item"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getBlogList">搜索</el-button>
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 10px">
      <el-pagination
          v-model:current-page="data.page"
          v-model:page-size="data.pageSize"
          :page-sizes="[10, 20, 50, 100, 200]"
          layout="->, total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @size-change="getBlogList"
          @current-change="getBlogList"
      />
    </div>
    <el-table :data="data.blogs" stripe border>
      <el-table-column fixed type="index"/>
      <el-table-column fixed prop="title" label="标题" width="200" show-overflow-tooltip>
        <template #default="scope">
          <el-link type="primary" :href="`http://127.0.0.1:8080/blog/${scope.row.id}`" target="_blank" :underline="false">
            {{scope.row.title}}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="tag" label="标签" width="120" show-overflow-tooltip/>
      <el-table-column prop="authorId" label="作者ID" width="100" show-overflow-tooltip/>
      <el-table-column prop="authorName" label="作者" width="100" show-overflow-tooltip/>
      <el-table-column prop="description" label="描述" width="200" show-overflow-tooltip/>
      <el-table-column prop="reply" label="回复" width="80"/>
      <el-table-column prop="liked" label="赞" width="50"/>
      <el-table-column prop="unlike" label="踩" width="50"/>
      <el-table-column prop="createTime" label="发布时间" width="180"/>
      <el-table-column prop="updateTime" label="活跃时间" width="180"/>
      <el-table-column fixed="right" label="操作" align="center" width="180">
        <template #default="scope">
          <el-button link type="danger" @click="deleteAndBan(scope.row)">封禁作者并删除</el-button>
          <el-button link type="danger" @click="deleteBlog(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  page: 1,
  pageSize: 10,
  total: 10
})
const queryForm = reactive({
  title: "",
  tag: [],
  authorName: ""
})

const getBlogList = () => {
  AjaxUtils.getBlogList({
    ...queryForm,
    tag: queryForm.tag.toString(),
    page: data.page,
    pageSize: data.pageSize
  }).then(resp => {
    if (resp.msg !== "success") return ElMessage.error("获取失败！")
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

const deleteBlog = (id) => {
  ElMessageBox.confirm("确认删除？", "提示").then(() => {
    AjaxUtils.deleteBlog({id}).then(resp => {
      if (resp.msg !== "success") return ElMessage.error("删除失败！" + resp.msg)
      ElMessage.success("删除成功！")
      getBlogList()
    }).catch(() => { ElMessage.error("删除失败！") })
  })
}

const deleteAndBan = (row) => {
  ElMessageBox.confirm("确认封禁作者？", "提示").then(() => {
    AjaxUtils.updateUserStatus({id: row.authorId, status: 0}).then(resp => {
      if (resp.msg !== "success") return ElMessage.error("封禁失败")
      ElMessage.success( "封禁成功!")
      deleteBlog(row.id)
    })
  })
}
</script>

<style scoped>

</style>