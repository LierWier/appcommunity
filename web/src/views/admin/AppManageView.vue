<template>
  <div>
    <el-form :inline="true" :model="queryApp" class="demo-form-inline">
      <el-form-item label="应用名称">
        <el-input v-model="queryApp.appName" clearable style="width: 150px;"/>
      </el-form-item>
      <el-form-item label="开发者">
        <el-input v-model="queryApp.author" clearable style="width: 150px;"/>
      </el-form-item>
      <el-form-item label="类别">
        <el-select v-model="queryApp.category" style="width: 80px;">
          <el-option label="全部" value=""/>
          <el-option v-for="(item,index) in category" :key="index" :label="item" :value="item"/>
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryApp.status" style="width: 80px;">
          <el-option label="全部" value=""/>
          <el-option label="正常" value="1"/>
          <el-option label="下架" value="0"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getAppList">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row style="margin-bottom: 10px">
      <el-col :span="12">
        <el-button-group>
          <el-button type="primary" plain @click="appDialogVisible = true; isAdd = true">添加</el-button>
          <el-button type="danger" plain @click="deleteApp">删除</el-button>
        </el-button-group>
        <el-button-group class="ml-4">
          <el-button type="primary" plain @click="changeShelf(true)">上架</el-button>
          <el-button type="danger" plain @click="changeShelf(false)">下架</el-button>
        </el-button-group>
      </el-col>
      <el-col :span="12">
        <el-pagination
            v-model:current-page="page"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100, 200]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="dataTotal"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            style="float: right"
        />
      </el-col>
    </el-row>
    <el-table
        :data="tableData"
        border style="width: 100%"
        stripe
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection"/>
      <el-table-column fixed type="index" align="center"/>
      <el-table-column fixed prop="appName" label="应用名称" align="center" width="100" show-overflow-tooltip/>
      <el-table-column prop="id" label="ID" align="center" width="50"/>
      <el-table-column prop="description" label="描述" align="center" width="200" show-overflow-tooltip/>
      <el-table-column prop="author" label="开发者" align="center" width="150" show-overflow-tooltip/>
      <el-table-column prop="category" label="类别" align="center" width="100"/>
      <el-table-column prop="score" label="评分" align="center"/>
      <el-table-column prop="downloads" label="下载量" align="center" width="100" sortable
                       :formatter="(_,__,cell) => formatterUtils.appDownloadsFmt(cell)"/>
      <el-table-column prop="postTime" label="发布时间" align="center" sortable width="120"/>
      <el-table-column prop="updatePostTime" label="上次更新" align="center" sortable width="120"/>
      <el-table-column fixed="right" prop="status" label="状态" align="center" sortable
                       :formatter="(_,__,cell) => formatterUtils.appStatusFmt(cell)"/>
      <el-table-column fixed="right" label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="updateApp(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="appDialogVisible" title="应用信息" width="600px" @close="appFormRef.resetFields()">
      <el-form :model="appForm" ref="appFormRef" :rules="rules" v-loading="loading">
        <el-form-item label="id" prop="id" v-show="false"></el-form-item>
        <el-form-item label="应用名称" :label-width="formLabelWidth" prop="appName">
          <el-input v-model.trim="appForm.appName" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="开发者" :label-width="formLabelWidth" prop="author">
          <el-input v-model.trim="appForm.author" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="描述" :label-width="formLabelWidth" prop="description">
          <el-input v-model.trim="appForm.description" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="类别" :label-width="formLabelWidth" prop="category">
          <el-input v-model.trim="appForm.category" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="下载量" :label-width="formLabelWidth" prop="downloads">
          <el-input v-model="appForm.downloads" autocomplete="off" type="number"/>
        </el-form-item>
        <el-form-item label="发布日期" :label-width="formLabelWidth" prop="postTime">
          <el-date-picker
              v-model="appForm.postTime"
              type="date"
              placeholder="未选择"
              :disabled-date="disabledDate"
              :shortcuts="shortcuts"
          />
        </el-form-item>
        <el-form-item label="上次更新" :label-width="formLabelWidth" prop="updatePostTime">
          <el-date-picker
              v-model="appForm.updatePostTime"
              type="date"
              placeholder="未选择"
              :disabled-date="disabledDate"
              :shortcuts="shortcuts"
          />
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="appDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit(appFormRef)">
          确认
        </el-button>
      </span>
      </template>
    </el-dialog>
  </div>
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

const handleSelectionChange = (val) => selection.value = val
// 分页配置
const page = ref(1)  // 当前页
const pageSize = ref(20)  // 分页大小
const dataTotal = ref(0)  // 数据条数
const handleCurrentChange = () => getAppList();
const handleSizeChange = () => {
  page.value = 1;
  getAppList()
}
// 弹框配置
const appDialogVisible = ref(false)
const loading = ref(false)
const isAdd = ref(true)
const formLabelWidth = ref("160px")
const appFormRef = ref({})
const appForm = reactive({
  id: null,
  appName: "",
  description: "",
  author: "",
  category: "",
  downloads: "",
  postTime: null,
  updatePostTime: null
})
const shortcuts = [{text: '今天', value: new Date()}]
const disabledDate = (time) => {
  return time.getTime() > Date.now()
}
const rules = reactive({
  appName: [
    {required: true, message: '应用名称不能为空', trigger: 'blur'},
  ],
  postTime: [
    {required: true, message: '不能为空', trigger: 'blur'}
  ],
  updatePostTime: [
    {required: true, message: '不能为空', trigger: 'blur'}
  ]
})
const submit = (formRef) => {
  if (isAdd.value) {
    formRef.validate(vld => {
      if (vld) {
        AjaxUtils.addApp(appForm).then(resp => {
          if (resp.msg === "success") {
            pageInit()
            ElMessage.success("添加成功！")
            appDialogVisible.value = false
          } else ElMessage.error(resp.msg)
        }).catch(() => ElMessage.error("error"))
      }
    })
  } else {
    AjaxUtils.updateApp(appForm).then(resp => {
      if (resp.msg === "success") {
        pageInit()
        ElMessage.success("修改成功！")
        appDialogVisible.value = false
      } else ElMessage.error(resp.msg)
    }).catch(() => ElMessage.error("error"))
  }
}

const getAppList = () => {
  AjaxUtils.getAppList({
    ...queryApp,
    page: page.value,
    pageSize: pageSize.value
  }).then((resp) => {
    if (resp.msg === "success") {
      tableData.value = resp.data.apps
      dataTotal.value = resp.data.total
    } else ElMessage.error(resp.msg)
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
    AjaxUtils.deleteAppByList({ids}).then(resp => {
      if (resp.msg === "success") {
        pageInit()
        ElMessage.success("删除成功！")
      } else ElMessage.error("删除失败！")
    })
  } else ElMessage.info("请选择至少1条数据！");
}

const updateApp = (row) => {
  isAdd.value = false
  appDialogVisible.value = loading.value = true
  setTimeout(() => {
    for (const item in appForm) appForm[item] = row[item]
    appForm["postTime"] = new Date(row["postTime"])
    appForm["updatePostTime"] = new Date(row["updatePostTime"])
    loading.value = false
  }, 1000)
}
</script>

<style scoped>
.ml-4 {
  margin-left: 1.5rem;
}

.el-input {
  width: 300px;
}
</style>