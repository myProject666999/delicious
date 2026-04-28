<template>
  <div class="notice-detail-page">
    <Header />
    
    <div class="main-content container">
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/notice' }">系统公告</el-breadcrumb-item>
        <el-breadcrumb-item>{{ notice.title || '公告详情' }}</el-breadcrumb-item>
      </el-breadcrumb>
      
      <div v-if="loading" class="loading-container">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      
      <template v-else-if="notice">
        <div class="detail-card card">
          <div class="detail-header">
            <h1 class="title">{{ notice.title }}</h1>
            <div class="meta">
              <span v-if="notice.author"><i class="el-icon-edit-outline"></i> {{ notice.author }}</span>
              <span><i class="el-icon-time"></i> {{ formatDate(notice.createTime) }}</span>
              <span><i class="el-icon-view"></i> {{ notice.viewCount || 0 }} 次浏览</span>
            </div>
          </div>
          
          <div class="detail-body" v-html="formatContent(notice.content)"></div>
          
          <div class="detail-footer">
            <el-button @click="$router.back()" icon="el-icon-arrow-left">返回列表</el-button>
          </div>
        </div>
      </template>
      
      <el-empty v-else description="公告不存在或已被删除"></el-empty>
    </div>
    
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getNoticeDetail } from '@/api/notice'

export default {
  name: 'NoticeDetail',
  components: { Header, Footer },
  data() {
    return {
      loading: false,
      notice: null
    }
  },
  created() {
    this.loadDetail()
  },
  watch: {
    '$route'(to, from) {
      if (to.params.id !== from.params.id) {
        this.loadDetail()
      }
    }
  },
  methods: {
    async loadDetail() {
      this.loading = true
      const id = this.$route.params.id
      
      try {
        const res = await getNoticeDetail(id)
        this.notice = res.data
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`
    },
    formatContent(content) {
      if (!content) return ''
      return content.replace(/\n/g, '<br>')
    }
  }
}
</script>

<style scoped lang="scss">
.notice-detail-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.breadcrumb {
  padding: 20px 0;
  font-size: 14px;
}

.loading-container {
  text-align: center;
  padding: 100px 0;
  color: #999;
  font-size: 16px;
  
  i {
    margin-right: 8px;
  }
}

.main-content {
  padding-bottom: 60px;
}

.detail-card {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px;
  
  .detail-header {
    text-align: center;
    padding-bottom: 30px;
    border-bottom: 1px solid #f0f0f0;
    margin-bottom: 30px;
    
    .title {
      font-size: 26px;
      color: #333;
      margin-bottom: 20px;
      font-weight: 600;
      line-height: 1.5;
    }
    
    .meta {
      display: flex;
      justify-content: center;
      gap: 30px;
      font-size: 14px;
      color: #999;
      
      span {
        display: flex;
        align-items: center;
        gap: 5px;
      }
    }
  }
  
  .detail-body {
    font-size: 16px;
    color: #666;
    line-height: 2;
    min-height: 200px;
    
    img {
      max-width: 100%;
      display: block;
      margin: 20px auto;
    }
  }
  
  .detail-footer {
    margin-top: 40px;
    padding-top: 30px;
    border-top: 1px solid #f0f0f0;
    text-align: center;
  }
}
</style>
