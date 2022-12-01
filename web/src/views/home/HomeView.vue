<template>
  <el-carousel :interval="4000" type="card" height="200px">
    <el-carousel-item v-for="item in 6" :key="item">
      <el-image :src="require(`@/assets/images/index_photo/${item}.png`)" fit="cover"/>
    </el-carousel-item>
  </el-carousel>

  <el-row>
    <el-col :span="24">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>今日推荐</span>
            <el-button class="button" text>more >></el-button>
          </div>
        </template>
        <div class="text item">
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <p v-for="item in 50" :key="item" class="scrollbar-demo-item">
                {{ item }}
              </p>
            </div>
          </el-scrollbar>
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="16">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>热点资讯</span>
            <el-button class="button" text>more >></el-button>
          </div>
        </template>
        <div class="text item">
          List item
        </div>
      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>热门应用</span>
            <el-button class="button" text>more >></el-button>
          </div>
        </template>
        <div class="text item">List item</div>
      </el-card>
    </el-col>
  </el-row>

</template>

<script setup>

</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.scrollbar-flex-content {
  display: flex;
}
.scrollbar-demo-item {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100px;
  height: 100px;
  margin: 10px;
  text-align: center;
  border-radius: 15px;
  background: var(--el-color-danger-light-9);
  color: var(--el-color-danger);
}
</style>
