<template>
  <el-card v-if="!user.is_login">
    <el-button @click="store.state.loginDialogVisible = true">
      尚未登录， 请先登录
    </el-button>
  </el-card>
  <div v-else>
    <el-card class="box-card">
      <div class="is-flex justify-content-between" style="height: 250px">
        <el-image :src="user.info.photo" style="border-radius: 15px; width: 250px; height: 250px"/>
        <div style="height: 250px; align-items: center;" class="is-flex">
          <el-descriptions
              direction="vertical"
              :column="3"
              size="large"
          >
            <el-descriptions-item label="关注" label-align="center" align="center" width="100px">
              {{ data.follows.follow }}
            </el-descriptions-item>
            <el-descriptions-item label="粉丝" label-align="center" align="center" width="100px">
              {{ data.follows.fans }}
            </el-descriptions-item>
            <el-descriptions-item label="发布" label-align="center" align="center" width="100px">
              {{ data.blogs.length }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <el-descriptions
            style="width: 400px;"
            class="margin-top"
            :title="`尊敬的${user.info.isManager?'管理员':'用户'}，您好！`"
            :column="1"
            border
        >
          <template #extra>
            <el-button type="primary" plain @click="data.dialogVisible = true">修改信息</el-button>
            <el-button type="danger" plain @click="logout">退出登录</el-button>
          </template>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <User />
                </el-icon>
                用户名
              </div>
            </template>
            {{ user.info.username }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <iphone />
                </el-icon>
                电话号码
              </div>
            </template>
            {{ user.info.tel }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <Calendar />
                </el-icon>
                生日
              </div>
            </template>
            {{ user.info.birth }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <tickets />
                </el-icon>
                性别
              </div>
            </template>
            <el-tag v-if="user.info.sex" :type="user.info.sex==='男'?'primary':'danger'">
              {{ user.info.sex }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon :style="data.iconStyle">
                  <office-building />
                </el-icon>
                注册时间
              </div>
            </template>
            {{ user.info.createTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <el-card class="mt-4">
      <template #header>
        <div class="card-header">
          <span>我的评论</span>
        </div>
      </template>
      <div>
        <el-table :data="data.evaluation" stripe table-layout="auto">
          <el-table-column fixed type="index" align="center"/>
          <el-table-column prop="appName" label="应用" align="center" />
          <el-table-column prop="score" label="评分" align="center"/>
          <el-table-column prop="content" label="内容" show-overflow-tooltip />
          <el-table-column prop="liked" label="获赞" align="center" />
          <el-table-column prop="createTime" label="日期" align="center" />
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button link type="danger" @click="deleteEvl(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
            class="mt-3"
            v-model:current-page="data.page"
            layout="->, prev, pager, next, total"
            :total="data.total"
            @current-change="getAppEvlList"
            hide-on-single-page
        />
      </div>
    </el-card>
    <el-card class="mt-4">
      <template #header>
        <div class="card-header">
          <span>我的博客</span>
        </div>
      </template>
      <div>
        <el-table :data="data.blogs" stripe table-layout="auto">
          <el-table-column fixed type="index" align="center"/>
          <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip>
            <template #default="scope">
              <el-link type="primary" :href="`http://127.0.0.1:8080/blog/${scope.row.id}`" target="_blank" :underline="false">
                {{scope.row.title}}
              </el-link>
            </template>
          </el-table-column>
          <el-table-column prop="tag" label="标签" width="120" show-overflow-tooltip/>
          <el-table-column prop="description" label="描述"  show-overflow-tooltip/>
          <el-table-column prop="reply" label="回复" width="80"/>
          <el-table-column prop="liked" label="赞" width="50"/>
          <el-table-column prop="unlike" label="踩" width="50"/>
          <el-table-column prop="updateTime" label="活跃时间" width="200"/>
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button link type="danger" @click="deleteBlog(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
            class="mt-3"
            v-model:current-page="data.page2"
            layout="->, prev, pager, next, total"
            :total="data.total2"
            @current-change="getMyBlogList"
            hide-on-single-page
        />
      </div>
    </el-card>
  </div>

  <el-dialog v-model="data.dialogVisible" title="个人信息" width="600px" >
    <el-form :model="data.form" ref="formRef" >
      <el-form-item label="头像" :label-width="data.formLabelWidth" prop="photo">
        <el-upload
            class="avatar-uploader"
            action="http://127.0.0.1:3001/api/common/upload/cache"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="data.imageUrl" :src="data.imageUrl" class="avatar" style="width: 100px; height: 100px;" alt=""/>
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="电话号码" :label-width="data.formLabelWidth" prop="tel"
                    :rules="[{type: 'number', message: '电话号码必须是数字'}]"
      >
        <el-input type="tel" v-model.number="data.form.tel" />
      </el-form-item>
      <el-form-item label="生日" :label-width="data.formLabelWidth">
        <el-date-picker
            v-model="data.form.birth"
            type="date"
            placeholder="请选择日期"
        />
      </el-form-item>
      <el-form-item label="性别" :label-width="data.formLabelWidth">
        <el-radio-group v-model="data.form.sex">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
          <el-radio label="">不选择</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.dialogVisible = false;data.form = {...utils.deepClone(user.info), tel: ~~user.info.tel || ''}">取消</el-button>
        <el-button type="primary" @click="onSubmit(formRef)">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import store from "@/store";
import router from "@/router";
import {reactive} from "vue";
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage, ElMessageBox} from "element-plus";
import utils from "@/assets/utils/utils";

const user = store.state.user
const data = reactive({
  follows: {},
  blogs: [],
  iconStyle: 'marginRight: 6px',
  dialogVisible: false,
  formLabelWidth: '160px',
  form: {...utils.deepClone(user.info), tel: ~~user.info.tel || ''},
  imageUrl: user.info.photo,
  evaluation: [],
  page: 1,
  pageSize: 10,
  page2: 1,
  pageSize2: 10
})
const formRef = reactive({})

const getUserFollow = () => {
  AjaxUtils.getUserFollow({userId: user.info.id}).then(resp => {
    if (resp.msg !== "success") return ElMessage.error("获取关注和粉丝失败！")
    data.follows = resp.data
  })
}
const getAppEvlList = () => {
  AjaxUtils.getAppEvlListByLoginUser({page: data.page, pageSize: data.pageSize}).then(resp => {
    if (resp.msg !== 'success') return ElMessage.error("获取评论记录失败！" + resp.msg)
    data.evaluation = resp.data.list
    data.total = resp.data.total
  }).catch(() => { ElMessage.error("获取评论记录失败！") })
}
const getMyBlogList = () => {
  AjaxUtils.getBlogList({
    authorId: user.info.id,
    page: data.page2,
    pageSize: data.pageSize2
  }).then(resp => {
    if (resp.msg !== "success") return ElMessage.error("我的博客获取失败！")
    data.blogs = resp.data.blogs
    data.total2 = resp.data.total
  }).catch(() => ElMessage.error("网络连接失败！"))
}
const pageInit = () => {
  getUserFollow()
  getAppEvlList()
  getMyBlogList()
}
pageInit()

const onSubmit = (formRef) => {
  if (!formRef) return
  formRef.validate(valid => {
    if (!valid) return false
    if (data.form["birth"]) data.form["birth"] = new Date(data.form["birth"])
    else delete data.form["birth"]
    delete data.form["createTime"]
    AjaxUtils.updateUser(data.form).then(resp => {
      if (resp.msg !== "success") return ElMessage.error("修改失败！" + resp.msg)
      ElMessage.success("修改成功！")
      setTimeout(() => {
        router.go(0)
      }, 1000)
      data.dialogVisible = false
    }).catch(() => ElMessage.error("修改失败！"))
  })
}

const handleAvatarSuccess = (resp) => {
  data.imageUrl = resp
  data.form.photo = resp
}

const logout = () => {
  store.commit("logout")
  router.push({name: "home"})
}

const deleteEvl = (id) => {
  ElMessageBox.confirm("确认删除？", "提示").then(() => {
    AjaxUtils.deleteAppEvl({id}).then(resp => {
      if (resp.msg !== "success") return ElMessage.error("删除失败！" + resp.msg)
      ElMessage.success("删除成功！")
      pageInit()
    }).catch(() => { ElMessage.error("删除失败！") })
  })
}

const deleteBlog = (id) => {
  ElMessageBox.confirm("确认删除？", "提示").then(() => {
    AjaxUtils.deleteBlog({id}).then(resp => {
      if (resp.msg !== "success") return ElMessage.error("删除失败！" + resp.msg)
      ElMessage.success("删除成功！")
      pageInit()
    }).catch(() => { ElMessage.error("删除失败！") })
  })
}

</script>

<style scoped>
.cell-item {
  display: flex;
  align-items: center;
}
.is-flex {
  display: flex;
}
.el-button--text {
  margin-right: 15px;
}
.el-input {
  width: 300px;
}
.dialog-footer button:first-child {
  margin-right: 10px;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
}
</style>