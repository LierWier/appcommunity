<template>
  <el-dialog :title="isRegister && '注册' || '登录'" @close="onClose">
    <el-form :model="form" ref="formRef" :rules="rules" hide-required-asterisk v-loading="loading">
      <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
        <el-input v-model="form.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
        <el-input v-model="form.password" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item v-if="isRegister" label="确认密码" :label-width="formLabelWidth" prop="confirmedPassword">
        <el-input v-model="form.confirmedPassword" type="password" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-link v-if="!isRegister" @click="isRegister=true" style="margin-right: 20px;">去注册</el-link>
        <el-link v-if="isRegister" @click="isRegister=false" style="margin-right: 20px;">去登录</el-link>
        <el-button v-if="isRegister" type="primary" @click="register(formRef)">
          注册
        </el-button>
        <el-button v-else type="primary" @click="login">
          登录
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {reactive, ref} from "vue";
// import {FormInstance, FormRules} from 'element-plus'
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage, ElMessageBox} from "element-plus";
import store from "@/store";

const formLabelWidth = ref('150px')
const formRef = ref({});
const form = reactive({
  username: '',
  password: '',
  confirmedPassword: ''
})
const confirmedPasswordValid = (rule, value, callback) => {
  if (value !== form.password) callback(new Error("密码不一致"))
  else callback()
}
const rules = reactive({
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 3, max: 10, message: '用户名长度应大于3小于10', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应大于6小于20', trigger: 'blur' },
  ],
  confirmedPassword: [
    { validator: confirmedPasswordValid, trigger: 'blur' },
    { required: true, message: '密码不能为空', trigger: 'blur' },
  ]
})
const loading = ref(false)
let isRegister = ref(false)

const login = async () => {
  loading.value = true
  const resp = await AjaxUtils.login(form)
  if (resp.msg === "success") {
    store.commit("updateToken", resp.data)
    const res = await AjaxUtils.getInfo()
    if (res.msg === "success") {
      store.commit("updateInfo", res.data)
      store.commit("updateLogin", true)
      ElMessage.success("登录成功！")
      store.commit("updateLoginDialogVisible", false)
    } else {
      ElMessage.warning("获取个人信息失败！")
    }
  } else {
    ElMessage.error("用户名或密码错误，请重新登录！")
  }
  loading.value = false
}

const register = (formRef) => {
  loading.value = true
  formRef.validate(async (valid) => {
    if (valid) {
      const resp = await AjaxUtils.register(form)
      if (resp.msg === "success") {
        await ElMessageBox.alert("注册成功！", "提示", {
          callback: () => {
            loading.value = false
            isRegister.value = false
          }
        })
      } else {
        loading.value = false
        await ElMessageBox.alert(resp.msg || "注册失败！", "提示")
      }
    }
    loading.value = false
  })
}

const onClose = () => {
  form.username = form.password = form.confirmedPassword = ''
  formRef.value.resetFields()
  isRegister.value = false
}

</script>

<style scoped>
.el-input {
  width: 300px;
}
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>