<template>
  <div class="notice-page">
    <Header />
    
    <div class="page-banner">
      <div class="container">
        <h1>系统公告</h1>
        <p>了解最新公告和活动信息</p>
      </div>
    </div>
    
    <div class="main-content container">
      <div class="notice-list">
        <el-empty v-if="noticeList.length === 0" description="暂无公告"></el-empty>
        <div v-else class="notice-items">
          <div v-for="notice in noticeList" :key="notice.id" class="notice-item card" @click="goToDetail(notice.id)">
            <div class="notice-date">
              <span class="day">{{ formatDay(notice.createTime) }}</span>
              <span class="month">{{ formatMonth(notice.createTime) }}</span>
            </div>
            <div class="notice-content">
              <h3 class="title text-ellipsis">{{ notice.title }}</h3>
              <p class="summary text-ellipsis-2">{{ notice.content }}</p>
              <div class="meta">
                <span v-if="notice.author"><i class="el-icon-edit-outline"></i> {{ notice.author }}</span>
                <span><i class="el-icon-view"></i> {{ notice.viewCount || 0 }} 浏览</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getNoticeFrontList } from '@/api/notice'

export default {
  name: 'Notice',
  components: { Header, Footer },
  data() {
    return {
      noticeList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.loadNoticeList()
  },
  methods: {
    async loadNoticeList() {
      try {
        const res = await getNoticeFrontList({
          pageNum: this.currentPage,
          pageSize: this.pageSize
        })
        this.noticeList = res.data.records || []
        this.total = res.data.total || 0
      } catch (e) {
        console.error(e)
      }
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadNoticeList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadNoticeList()
    },
    formatDay(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.getDate().toString().padStart(2, '0')
    },
    formatMonth(date) {
      if (!date) return ''
      const months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
      const d = new Date(date)
      return months[d.getMonth()]
    },
    goToDetail(id) {
      this.$router.push(`/notice/detail/${id}`)
    }
  }
}
</script>

<style scoped lang="scss">
.notice-page {
  min-height: 100vh;
}

.page-banner {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  padding: 60px 0;
  text-align: center;
  
  h1 {
    font-size: 36px;
    color: #fff;
    margin-bottom: 10px;
  }
  
  p {
    color: rgba(255, 255, 255, 0.9);
    font-size: 16px;
  }
}

.main-content {
  padding: 30px 0 60px;
}

.notice-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.notice-item {
  display: flex;
  gap: 20px;
  padding: 25px;
  cursor: pointer;
  
  .notice-date {
    width: 80px;
    height: 80px;
    background: linear-gradient(135deg, #ff6b6b, #ff8e53);
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    
    .day {
      font-size: 28px;
      font-weight: bold;
      color: #fff;
      line-height: 1;
    }
    
    .month {
      font-size: 12px;
      color: rgba(255, 255, 255, 0.9);
      margin-top: 5px;
    }
  }
  
  .notice-content {
    flex: 1;
    min-width: 0;
    
    .title {
      font-size: 18px;
      color: #333;
      margin-bottom: 10px;
      font-weight: 500;
    }
    
    .summary {
      font-size: 14px;
      color: #666;
      line-height: 1.6;
      margin-bottom: 12px;
    }
    
    .meta {
      display: flex;
      gap: 20px;
      font-size: 13px;
      color: #999;
      
      span {
        display: flex;
        align-items: center;
        gap: 3px;
      }
    }
  }
  
  &:hover {
    transform: translateX(5px);
    
    .title {
      color: #ff6b6b;
    }
  }
}

.pagination-section {
  margin-top: 40px;
  text-align: center;
}
</style>
