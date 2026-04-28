<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card card1">
          <div class="stat-icon">
            <i class="el-icon-user"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount || 0 }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card card2">
          <div class="stat-icon">
            <i class="el-icon-notebook-2"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.recipeCount || 0 }}</div>
            <div class="stat-label">菜谱总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card card3">
          <div class="stat-icon">
            <i class="el-icon-goods"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.foodCount || 0 }}</div>
            <div class="stat-label">美食总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card card4">
          <div class="stat-icon">
            <i class="el-icon-chat-dot-round"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.forumCount || 0 }}</div>
            <div class="stat-label">论坛帖子</div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="content-section">
      <el-col :span="12">
        <div class="panel">
          <div class="panel-header">
            <h3>最新菜谱</h3>
            <el-button type="text" size="small" @click="$router.push('/admin/recipe')">查看全部</el-button>
          </div>
          <el-table :data="latestRecipes" style="width: 100%">
            <el-table-column prop="title" label="菜谱名称" show-overflow-tooltip></el-table-column>
            <el-table-column prop="categoryName" label="分类" width="100"></el-table-column>
            <el-table-column prop="viewCount" label="浏览量" width="80"></el-table-column>
            <el-table-column prop="createTime" label="发布时间" width="160">
              <template slot-scope="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      
      <el-col :span="12">
        <div class="panel">
          <div class="panel-header">
            <h3>最新帖子</h3>
            <el-button type="text" size="small" @click="$router.push('/admin/forum')">查看全部</el-button>
          </div>
          <el-table :data="latestForums" style="width: 100%">
            <el-table-column prop="title" label="帖子标题" show-overflow-tooltip></el-table-column>
            <el-table-column prop="userName" label="作者" width="100"></el-table-column>
            <el-table-column prop="viewCount" label="浏览" width="60"></el-table-column>
            <el-table-column prop="replyCount" label="回复" width="60"></el-table-column>
            <el-table-column prop="createTime" label="发布时间" width="160">
              <template slot-scope="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="content-section">
      <el-col :span="12">
        <div class="panel">
          <div class="panel-header">
            <h3>最新用户</h3>
            <el-button type="text" size="small" @click="$router.push('/admin/user')">查看全部</el-button>
          </div>
          <el-table :data="latestUsers" style="width: 100%">
            <el-table-column prop="username" label="用户名" width="120"></el-table-column>
            <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
            <el-table-column prop="phone" label="手机号" width="120"></el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="mini">
                  {{ scope.row.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="注册时间" width="160">
              <template slot-scope="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      
      <el-col :span="12">
        <div class="panel">
          <div class="panel-header">
            <h3>系统公告</h3>
            <el-button type="text" size="small" @click="$router.push('/admin/notice')">查看全部</el-button>
          </div>
          <el-table :data="latestNotices" style="width: 100%">
            <el-table-column prop="title" label="公告标题" show-overflow-tooltip></el-table-column>
            <el-table-column prop="author" label="作者" width="80"></el-table-column>
            <el-table-column prop="viewCount" label="浏览" width="60"></el-table-column>
            <el-table-column prop="createTime" label="发布时间" width="160">
              <template slot-scope="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserList } from '@/api/user'
import { getRecipeList } from '@/api/recipe'
import { getFoodList } from '@/api/food'
import { getForumList } from '@/api/forum'
import { getNoticeList } from '@/api/notice'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        userCount: 0,
        recipeCount: 0,
        foodCount: 0,
        forumCount: 0
      },
      latestRecipes: [],
      latestForums: [],
      latestUsers: [],
      latestNotices: []
    }
  },
  created() {
    this.loadStats()
    this.loadLatestData()
  },
  methods: {
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')} ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`
    },
    async loadStats() {
      try {
        const [userRes, recipeRes, foodRes, forumRes] = await Promise.all([
          getUserList({ pageNum: 1, pageSize: 1 }),
          getRecipeList({ pageNum: 1, pageSize: 1 }),
          getFoodList({ pageNum: 1, pageSize: 1 }),
          getForumList({ pageNum: 1, pageSize: 1 })
        ])
        
        this.stats.userCount = userRes.data.total || 0
        this.stats.recipeCount = recipeRes.data.total || 0
        this.stats.foodCount = foodRes.data.total || 0
        this.stats.forumCount = forumRes.data.total || 0
      } catch (e) {
        console.error(e)
      }
    },
    async loadLatestData() {
      try {
        const [recipeRes, forumRes, userRes, noticeRes] = await Promise.all([
          getRecipeList({ pageNum: 1, pageSize: 5 }),
          getForumList({ pageNum: 1, pageSize: 5 }),
          getUserList({ pageNum: 1, pageSize: 5 }),
          getNoticeList({ pageNum: 1, pageSize: 5 })
        ])
        
        this.latestRecipes = recipeRes.data.records || []
        this.latestForums = forumRes.data.records || []
        this.latestUsers = userRes.data.records || []
        this.latestNotices = noticeRes.data.records || []
      } catch (e) {
        console.error(e)
      }
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard {
  .stat-cards {
    margin-bottom: 20px;
    
    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      border-radius: 8px;
      color: #fff;
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28px;
        
        i {
          font-size: 32px;
        }
      }
      
      .stat-info {
        margin-left: 20px;
        
        .stat-value {
          font-size: 32px;
          font-weight: bold;
        }
        
        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-top: 5px;
        }
      }
    }
    
    .card1 {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    
    .card2 {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }
    
    .card3 {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }
    
    .card4 {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }
  }
  
  .content-section {
    margin-bottom: 20px;
  }
  
  .panel {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    
    .panel-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
      
      h3 {
        font-size: 16px;
        font-weight: 500;
        color: #333;
      }
    }
  }
}
</style>
