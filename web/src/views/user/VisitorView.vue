<template>
  <el-card v-if="!user.is_login">
    <el-button @click="store.state.loginDialogVisible = true">
      尚未登录， 请先登录
    </el-button>
  </el-card>
  <div v-else>
    <el-card class="box-card">
      <div class="is-flex justify-content-between" style="height: 250px">
        <el-image :src="data.user.photo" style="border-radius: 15px; width: 250px; height: 250px"/>
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
            :column="1"
            border
        >
          <template #extra>
            <el-button :type="data.isFollow && 'danger' || 'primary'" plain @click="followUser(!data.isFollow)">
              {{ data.isFollow && '取消关注' || '关注' }}
            </el-button>
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
            {{ data.user.username }}
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
            {{ data.user.tel }}
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
            {{ data.user.birth }}
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
            <el-tag v-if="data.user.sex" :type="data.user.sex==='男'?'primary':'danger'">
              {{ data.user.sex }}
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
            {{ data.user.createTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <el-card class="mt-4">
      <template #header>
        <div class="card-header">
          <span>他的博客</span>
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
        </el-table>
        <el-pagination
            class="mt-3"
            v-model:current-page="data.page"
            layout="->, prev, pager, next, total"
            :total="data.total"
            @current-change="getBlogList"
            hide-on-single-page
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import store from "@/store";
import {reactive} from "vue";
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useRoute} from "vue-router/dist/vue-router";

const user = store.state.user
const route = useRoute()
const id = route.params.id
console.log(id)
if (id == user.info.id) router.push({name: 'user'})
const data = reactive({
  user: {
    photo: ""
  },
  follows: {},
  blogs: [],
  iconStyle: 'marginRight: 6px',
  imageUrl: user.info.photo,
  page: 1,
  pageSize: 10,
})
const visitUser = () => {
  AjaxUtils.visitUser({id}).then(resp => {
    if (resp.msg !== "success") return ElMessage.error("获取用户失败！" + resp.msg)
    data.user = resp.data
  }).catch(() => ElMessage.error("网络连接失败！"))
}
const getUserFollow = () => {
  AjaxUtils.getUserFollow({userId: id}).then(resp => {
    if (resp.msg !== "success") return ElMessage.error("获取关注和粉丝失败！")
    data.follows = resp.data
  })
}
const isFollow = () => {
  AjaxUtils.isFollow({id}).then(resp => {
    data.isFollow = resp
  })
}
const getBlogList = () => {
  AjaxUtils.getBlogList({
    authorId: id,
    page: data.page,
    pageSize: data.pageSize
  }).then(resp => {
    if (resp.msg !== "success") return ElMessage.error("我的博客获取失败！")
    data.blogs = resp.data.blogs
    data.total = resp.data.total
  }).catch(() => ElMessage.error("网络连接失败！"))
}
const pageInit = () => {
  visitUser()
  getUserFollow()
  getBlogList()
  isFollow()
}
pageInit()

const followUser = (x) => {
  if (x) {
    AjaxUtils.followUser({id}).then(resp => {
      if (resp.msg !== "success") return ElMessage.error(resp.msg)
      ElMessage.success("关注成功！")
      pageInit()
    })
  } else {
    AjaxUtils.cancelFollowUser({id}).then(resp => {
      if (resp.msg !== "success") return ElMessage.error(resp.msg)
      ElMessage.success("取消关注！")
      pageInit()
    })
  }
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