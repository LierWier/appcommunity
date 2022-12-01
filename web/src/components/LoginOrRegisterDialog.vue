<template>
  <el-dialog :title="isRegister && '注册' || '登录'" @close="onClose">
    <el-form :model="form">
      <el-form-item label="用户名" :label-width="formLabelWidth">
        <el-input v-model="form.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码" :label-width="formLabelWidth">
        <el-input v-model="form.password" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item v-if="isRegister" label="确认密码" :label-width="formLabelWidth">
        <el-input v-model="form.confirmedPassword" type="password" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-link v-if="!isRegister" @click="isRegister=true" style="margin-right: 20px;">去注册</el-link>
        <el-link v-if="isRegister" @click="isRegister=false" style="margin-right: 20px;">去登录</el-link>
        <el-button v-if="isRegister" type="primary" @click="register">
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
// import {AjaxUtils} from "@/assets/utils/ajaxUtils";

const formLabelWidth = ref('150px')
const form = reactive({
  username: '',
  password: '',
  confirmedPassword: ''
})

let isRegister = ref(false)

const login = () => {
  console.log("login: ", form.username, form.password)
}

const register = () => {
  console.log("register: ", form)
}

const onClose = () => {
  console.log("close")
  form.username = form.password = form.confirmedPassword = ''
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