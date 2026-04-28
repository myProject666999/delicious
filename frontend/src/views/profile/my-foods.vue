<template>
  <div class="profile-list">
    <div class="content-card card">
      <div class="content-header flex-between">
        <h2>我的美食</h2>
      </div>
      
      <div class="empty-box" v-if="list.length === 0">
        <i class="el-icon-goods"></i>
        <p>暂无美食</p>
      </div>
      
      <div class="list-wrapper" v-else>
        <div class="list-item" v-for="item in list" :key="item.id">
          <div class="item-img">
            <el-image :src="item.image" fit="cover"></el-image>
          </div>
          <div class="item-info">
            <h3 class="item-title">{{ item.title }}</h3>
            <p class="item-desc">
              <span><i class="el-icon-view"></i> {{ item.viewCount || 0 }}</span>
              <span><i class="el-icon-star-on"></i> {{ item.likeCount || 0 }}</span>
              <span><i class="el-icon-chat-dot-round"></i> {{ item.commentCount || 0 }}</span>
            </p>
            <div class="item-tags">
              <el-tag v-if="item.categoryName" size="mini" type="info">{{ item.categoryName }}</el-tag>
            </div>
          </div>
          <div class="item-actions">
            <el-button type="text" @click="handleEdit(item)">编辑</el-button>
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
import { getFoodPage } from '@/api/food'
import { mapState } from 'vuex'

export default {
  name: 'MyFoods',
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
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          userId: this.userInfo?.id
        }
        const res = await getFoodPage(params)
        this.list = res.data.records || []
        this.total = res.data.total || 0
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    handleEdit(item) {
      this.$router.push(`/food/detail/${item.id}`)
    },
    handleDelete(item) {
      this.$confirm('确定要删除该美食吗？', '提示', {
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
      
      .item-img {
        width: 160px;
        height: 100px;
        flex-shrink: 0;
        overflow: hidden;
        border-radius: 8px;
        
        >>> .el-image {
          width: 100%;
          height: 100%;
        }
      }
      
      .item-info {
        flex: 1;
        padding: 0 20px;
        min-width: 0;
        
        .item-title {
          font-size: 16px;
          color: #333;
          margin-bottom: 10px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          cursor: pointer;
          
          &:hover {
            color: #ff6b6b;
          }
        }
        
        .item-desc {
          font-size: 13px;
          color: #999;
          margin-bottom: 10px;
          
          span {
            margin-right: 15px;
            
            i {
              margin-right: 3px;
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
