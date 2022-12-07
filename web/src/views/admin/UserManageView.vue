<template>
  <el-form :inline="true" :model="queryUser" class="demo-form-inline">
    <el-form-item label="用户名">
      <el-input v-model="queryUser.username" clearable style="width: 150px;"/>
    </el-form-item>
    <el-form-item label="电话">
      <el-input v-model="queryUser.tel" type="tel" clearable style="width: 150px;"/>
    </el-form-item>
    <el-form-item label="性别">
      <el-select v-model="queryUser.sex" style="width: 80px;">
        <el-option label="全部" value="" />
        <el-option label="男" value="男" />
        <el-option label="女" value="女" />
      </el-select>
    </el-form-item>
    <el-form-item label="状态">
      <el-select v-model="queryUser.status" style="width: 80px;">
        <el-option label="全部" value="" />
        <el-option label="正常" value="1" />
        <el-option label="封禁" value="0" />
        <el-option label="注销" value="-1" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="getUserList">查询</el-button>
    </el-form-item>
  </el-form>
  <el-button type="primary" plain @click="addUserDialogVisible = true">添加</el-button>
  <el-table
      :data="tableData"
      border style="width: 100%"
      table-layout="auto"
      :row-class-name="tableRowClassName"
  >
    <el-table-column type="index" align="center" />
    <el-table-column prop="id" label="UID" align="center" />
    <el-table-column prop="username" label="用户名" align="center" />
    <el-table-column prop="tel" label="电话" align="center" />
    <el-table-column prop="birth" label="生日" align="center" sortable />
    <el-table-column prop="sex" label="性别" align="center" sortable />
    <el-table-column prop="createTime" label="注册时间" align="center" sortable />
    <el-table-column prop="isManager" label="管理员" sortable :formatter="(_,__,cell) => formatterUtils.isManagerFmt(cell)" align="center" />
    <el-table-column prop="status" label="状态" sortable :formatter="(_,__,cell) => formatterUtils.userStatusFmt(cell)" align="center" />
    <el-table-column label="操作" align="center" >
      <template #default="scope">
        <el-button link type="primary" @click="banOp(scope.row.id, scope.row.status)">{{ scope.row.status==1?"封禁":(scope.row.status==0?"解禁":"") }}</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog v-model="addUserDialogVisible" title="添加用户" @close="addUserFormRef.resetFields()">
    <el-form :model="addUserForm" ref="addUserFormRef" :rules="rules">
      <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
        <el-input v-model="addUserForm.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
        <el-input v-model="addUserForm.password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="管理员" :label-width="formLabelWidth" prop="isManager">
        <el-switch v-model="addUserForm.isManager" active-value="1" inactive-value="0" />
      </el-form-item>
      <el-form-item label="电话" :label-width="formLabelWidth" prop="tel">
        <el-input v-model="addUserForm.tel" autocomplete="off" type="tel"/>
      </el-form-item>
      <el-form-item label="生日" :label-width="formLabelWidth" prop="birth">
        <el-date-picker
            v-model="addUserForm.birth"
            type="date"
            placeholder="未选择"
            :disabled-date="disabledDate"
            :shortcuts="shortcuts"
        />
      </el-form-item>
      <el-form-item label="性别" :label-width="formLabelWidth" prop="sex">
        <el-radio-group v-model="addUserForm.sex" class="ml-4">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
          <el-radio :label="null">暂不选择</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addUserDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addUser(addUserFormRef)">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage} from "element-plus";
import {ref, reactive} from "vue";
import formatterUtils from "@/assets/utils/formatterUtils";

let tableData = ref([])
const queryUser = reactive({username: "", tel: "", sex: "", status: ""})
const addUserDialogVisible = ref(false)
const formLabelWidth = "180px"
const addUserForm = reactive({username: "", password: "", isManager: null, tel: null, birth: null, sex: null})
const addUserFormRef = ref({})
const shortcuts = [
  {
    text: 'Today',
    value: new Date(),
  },
  {
    text: 'Yesterday',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() - 3600 * 1000 * 24)
      return date
    },
  },
  {
    text: 'A week ago',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
      return date
    },
  },
]
const disabledDate = (time) => {
  return time.getTime() > Date.now()
}
const rules = reactive({
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 3, max: 10, message: '用户名长度应大于3小于10', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应大于6小于20', trigger: 'blur' },
  ]
})

const getUserList = () => {
  AjaxUtils.getUserList(queryUser).then((resp) => {
    if (resp.msg === "success") tableData.value = resp.users
    else ElMessage.error(resp.msg)
  })
}
getUserList()

const tableRowClassName = ({row}) => {
  const status = row.status;
  if (status == 0) return 'danger-row'
  if (status == -1) return 'dark-row'
  return ''
}

const banOp = (id, st) => {
  const status = st == 0 ? 1 : 0
  AjaxUtils.updateUserStatus({id, status}).then((resp) => {
    if (resp.msg === "success") {
      ElMessage.success((status == 1 ? "解禁" : "封禁") + "成功")
      getUserList()
    }
    else ElMessage.error("操作失败")
  })
}

const addUser = (form) => {
  form.validate((valid) => {
    if (valid) {
      if (addUserForm.birth == null) delete addUserForm.birth
      AjaxUtils.addUser(addUserForm).then((resp) => {
        if (resp.msg === "success") {
          ElMessage.success("添加成功！");
          getUserList()
          addUserDialogVisible.value = false
        } else ElMessage.error(resp.msg);
      })
    }
  })
}

</script>

<style scoped>
.el-table >>> .danger-row {
  --el-table-tr-bg-color: var(--el-color-danger-light-5);
}
.el-table >>> .dark-row {
  --el-table-tr-bg-color: var(--el-color-info-light-5);
}
.el-input {
  width: 300px;
}
</style>