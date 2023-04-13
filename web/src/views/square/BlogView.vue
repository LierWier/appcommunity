<template>
  <div v-if="data.loaded >= 2">
    <el-card class="box-card" >
      <template #header>
        <div class="card-header">
          <div>
            <span class="fs-3 me-2">{{ data.blog.title }}</span>
            <el-tag v-for="(tag,index) in data.blog.tags" :key="index" class="ms-2">{{ tag }}</el-tag>
            <div style="display: flex; margin-top: 10px">
              <el-avatar :size="30" :src="data.blog.authorPhoto || 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'" />
              <div>
                <span class="ms-4 text-primary">{{ data.blog.authorName }}</span>
                <span class="ms-4 text-secondary">{{ data.blog.createTime }}</span>
                <span class="ms-4 text-secondary">{{ data.replyList.length + ' 回帖' }}</span>
              </div>
            </div>
          </div>
          <div v-if="data.likeOrUnlike >= 0">
            <div>
              <el-icon :color="data.likeOrUnlike && 'blue' || ''" size="25"><CaretTop /></el-icon>
            </div>
            {{ data.blog.liked - data.blog.unlike }}
            <div>
              <el-icon :color="!data.likeOrUnlike && 'blue' || ''" size="25"><CaretBottom /></el-icon>
            </div>
          </div>
          <div v-else>
            <div @click="likeOrUnlike(1)">
              <el-icon size="25"><CaretTop /></el-icon>
            </div>
            {{ data.blog.liked - data.blog.unlike }}
            <div @click="likeOrUnlike(0)">
              <el-icon size="25"><CaretBottom /></el-icon>
            </div>
          </div>
        </div>
      </template>
      <div>{{ data.blog.content }}</div>
    </el-card>
    <el-card class="box-card mt-3">
      <template #header>
        <div class="card-header">
          <span class="fw-bold">发表回复</span>
          <el-button class="button" type="primary" @click="comment(0)">发表</el-button>
        </div>
      </template>
      <el-input v-model="data.myReply[0]" type="textarea" :rows="4" />
    </el-card>
    <div>
      <el-select v-model="data.order" @change="getBlogReplyList" class="m-2">
        <el-option label="按时间倒序" :value="1"/>
        <el-option label="按时间顺序" :value="2"/>
      </el-select>
      <el-card class="box-card" v-for="reply in data.replyList" :key="reply.id">
        <template #header>
          <div class="card-header">
            <div style="display: flex">
              <el-avatar :size="30" :src="reply.userPhoto || 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'" />
              <div>
                <span class="ms-4 text-primary">{{ reply.userName }}</span>
                <span class="ms-4 text-secondary">{{ reply.createTime }}</span>
              </div>
            </div>
            <el-button class="button" text>-</el-button>
          </div>
        </template>
        <div>{{ reply.content }}</div>
        <el-card class="box-card mt-4">
          <el-scrollbar :height="data.replyReplyList.filter(i => i.replyId === reply.id).length <= 4 ? '' : '200px'">
            <p v-for="r in data.replyReplyList.filter(i => i.replyId === reply.id)" :key="r.id">
              <span class="text-secondary">{{ r.createTime }}</span>
              <span class="ms-2 text-primary">{{ r.userName }}</span>
              ：{{ r.content }}
            </p>
          </el-scrollbar>
          <el-input v-model="data.myReply[reply.id]" placeholder="输入你的评论~">
            <template #append>
              <el-button :icon="Promotion" @click="comment(reply.id)">发送</el-button>
            </template>
          </el-input>
        </el-card>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import {useRoute} from "vue-router/dist/vue-router";
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import {ElMessage} from "element-plus";
import {reactive} from "vue";
import {Promotion} from '@element-plus/icons-vue'

const route = useRoute()
const data = reactive({loaded: 0, order: 1, myReply: []})

const getBlog = () => {
  AjaxUtils.getBlog({id: route.params.id}).then(resp => {
    if (resp.msg !== "success") return ElMessage.error("获取失败！")
    data.blog = resp.data
    data.blog.tags = data.blog.tag && data.blog.tag.split(',') || []
    data.loaded++
  }).catch(() => ElMessage.error("网络连接失败！"))
}
const getBlogReplyList = () => {
  AjaxUtils.getBlogReplyList({blogId: route.params.id, order: data.order}).then(resp => {
    if (resp.msg !== "success") return ElMessage.error("获取失败！")
    data.replyList = resp.data.filter(i => i.replyId === 0)
    data.replyReplyList = resp.data.filter(i => i.replyId > 0)
    data.loaded++
  }).catch(() => ElMessage.error("网络连接失败！"))
}
const getBlogLikeOrUnlike = () => {
  AjaxUtils.getBlogLikeOrUnlike({blogId: route.params.id}).then(resp => {
    if (resp.msg !== "success") return
    data.likeOrUnlike = resp.data
  })
}
const getInit = () => {
  getBlog()
  getBlogReplyList()
  getBlogLikeOrUnlike()
}
getInit()

const likeOrUnlike = (likeOrUnlike) => {
  AjaxUtils.blogLikeOrUnlike({
    blogId: route.params.id,
    likeOrUnlike,
  }).then(resp => {
    if (resp.msg !== "success") return ElMessage.error(resp.msg)
    getInit()
  })
}

const comment = (id) => {
  console.log(id + data.myReply[id])
  const params = {
    blogId: route.params.id,
    replyId: id,
    content: data.myReply[id]
  }
  AjaxUtils.postReply(params).then(resp => {
    if (resp.msg !== "success") return ElMessage.error(resp.msg)
    ElMessage.success("发表成功！")
    data.myReply = []
    getBlogReplyList()
  }).catch(() => ElMessage.error("网络连接失败！"))
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.box-card {
  margin-bottom: 10px;
}
</style>