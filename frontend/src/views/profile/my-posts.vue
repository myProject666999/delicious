<template>
  <div class="profile-list">
    <div class="content-card card">
      <div class="content-header flex-between">
        <h2>我的帖子</h2>
      </div>
      
      <div class="empty-box" v-if="list.length === 0">
        <i class="el-icon-chat-dot-round"></i>
        <p>暂无帖子</p>
      </div>
      
      <div class="list-wrapper" v-else>
        <div class="list-item" v-for="item in list" :key="item.id">
          <div class="item-info">
            <h3 class="item-title">{{ item.title }}</h3>
            <p class="item-content">{{ item.content || '暂无内容' }}</p>
            <div class="item-meta">
              <span><i class="el-icon-view"></i> {{ item.viewCount || 0 }}</span>
              <span><i class="el-icon-star-on"></i> {{ item.likeCount || 0 }}</span>
              <span><i class="el-icon-chat-dot-round"></i> {{ item.commentCount || 0 }}</span>
              <span class="time">{{ formatDate(item.createTime) }}</span>
            </div>
          </div>
          <div class="item-actions">
            <el-button type="text" @click="handleEdit(item)">查看</el-button>
            <el-button type="text" @click="handleDelete(item)">删除</el-button>
          </div>
        </div>
      </div>
      
      <div class="pagination-wrapper">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { getForumPage } from '@/api/forum'
import { mapState } from 'vuex'

export default {
  name: 'MyPosts',
  data() {
    return {
      list: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  created() {
    this.loadData()
  },
  methods: {
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          userId: this.userInfo?.id
        }
        const res = await getForumPage(params)
        this.list = res.data.records || []
        this.total = res.data.total || 0
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    handleEdit(item) {
      this.$router.push(`/forum/detail/${item.id}`)
    },
    handleDelete(item) {
      this.$confirm('确定要删除该帖子吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loadData()
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadData()
    }
  }
}
</script>

<style scoped lang="scss">
.profile-list {
  .content-card {
    padding: 30px;
    
    .content-header {
      margin-bottom: 30px;
      padding-bottom: 20px;
      border-bottom: 1px solid #f0f0f0;
      
      h2 {
        font-size: 18px;
        color: #333;
        font-weight: 500;
      }
    }
  }
  
  .empty-box {
    text-align: center;
    padding: 60px 20px;
    
    i {
      font-size: 60px;
      color: #ddd;
    }
    
    p {
      margin-top: 15px;
      color: #999;
    }
  }
  
  .list-wrapper {
    .list-item {
      display: flex;
      padding: 20px 0;
      border-bottom: 1px solid #f0f0f0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .item-info {
        flex: 1;
        min-width: 0;
        
        .item-title {
          font-size: 16px;
          color: #333;
          margin-bottom: 8px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          cursor: pointer;
          
          &:hover {
            color: #ff6b6b;
          }
        }
        
        .item-content {
          font-size: 14px;
          color: #666;
          line-height: 1.6;
          margin-bottom: 10px;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
        
        .item-meta {
          font-size: 12px;
          color: #999;
          
          span {
            margin-right: 15px;
            
            i {
              margin-right: 3px;
            }
            
            &.time {
              float: right;
              margin-right: 0;
            }
          }
        }
      }
      
      .item-actions {
        flex-shrink: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
      }
    }
  }
  
  .pagination-wrapper {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
