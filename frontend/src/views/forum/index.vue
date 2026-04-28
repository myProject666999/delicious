<template>
  <div class="forum-page">
    <Header />
    
    <div class="page-banner">
      <div class="container flex-between">
        <div class="banner-left">
          <h1>美食论坛</h1>
          <p>分享美食故事，交流美食心得</p>
        </div>
        <div class="banner-right">
          <el-button type="primary" size="large" @click="goToPublish" :disabled="!isLoggedIn">
            <i class="el-icon-edit"></i> 发布帖子
          </el-button>
          <p v-if="!isLoggedIn" class="login-tip">登录后可发布帖子</p>
        </div>
      </div>
    </div>
    
    <div class="main-content container">
      <div class="search-section">
        <el-form :inline="true" :model="searchForm">
          <el-form-item>
            <el-input v-model="searchForm.title" placeholder="搜索帖子" clearable @keyup.enter.native="handleSearch">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch"><i class="el-icon-search"></i> 搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <div class="forum-list">
        <el-empty v-if="forumList.length === 0" description="暂无帖子"></el-empty>
        <div v-else class="forum-items">
          <div v-for="forum in forumList" :key="forum.id" class="forum-item card" @click="goToDetail(forum.id)">
            <div class="forum-avatar">
              <el-avatar :size="50" :src="forum.userAvatar" icon="el-icon-user-solid"></el-avatar>
            </div>
            <div class="forum-content">
              <h3 class="title">
                <el-tag v-if="forum.isTop" type="danger" size="mini" effect="dark">置顶</el-tag>
                <span>{{ forum.title }}</span>
              </h3>
              <p class="summary text-ellipsis-2">{{ forum.content }}</p>
              <div class="meta">
                <span class="author">{{ forum.userName || '匿名用户' }}</span>
                <span class="time">{{ formatDate(forum.createTime) }}</span>
                <span class="view"><i class="el-icon-view"></i> {{ forum.viewCount || 0 }}</span>
                <span class="reply"><i class="el-icon-chat-dot-round"></i> {{ forum.replyCount || 0 }}</span>
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
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
    
    <el-dialog title="发布帖子" :visible.sync="publishVisible" width="600px">
      <el-form :model="publishForm" :rules="publishRules" ref="publishForm">
        <el-form-item label="帖子标题" prop="title">
          <el-input v-model="publishForm.title" placeholder="请输入帖子标题" maxlength="200"></el-input>
        </el-form-item>
        <el-form-item label="帖子内容" prop="content">
          <el-input
            v-model="publishForm.content"
            type="textarea"
            :rows="8"
            placeholder="请输入帖子内容"
            maxlength="5000"
            show-word-limit>
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="publishVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePublish" :loading="publishLoading">发布</el-button>
      </span>
    </el-dialog>
    
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getForumFrontList, publishForum } from '@/api/forum'
import { mapGetters, mapState } from 'vuex'

export default {
  name: 'Forum',
  components: { Header, Footer },
  data() {
    return {
      forumList: [],
      searchForm: {
        title: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      publishVisible: false,
      publishLoading: false,
      publishForm: {
        title: '',
        content: ''
      },
      publishRules: {
        title: [{ required: true, message: '请输入帖子标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入帖子内容', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapGetters(['isLoggedIn']),
    ...mapState(['userInfo'])
  },
  created() {
    this.loadForumList()
  },
  methods: {
    async loadForumList() {
      try {
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          title: this.searchForm.title
        }
        const res = await getForumFrontList(params)
        this.forumList = res.data.records || []
        this.total = res.data.total || 0
      } catch (e) {
        console.error(e)
      }
    },
    handleSearch() {
      this.currentPage = 1
      this.loadForumList()
    },
    resetSearch() {
      this.searchForm.title = ''
      this.currentPage = 1
      this.loadForumList()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadForumList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadForumList()
    },
    goToDetail(id) {
      this.$router.push(`/forum/detail/${id}`)
    },
    goToPublish() {
      if (!this.isLoggedIn) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }
      this.publishVisible = true
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const now = new Date()
      const diff = now.getTime() - d.getTime()
      
      if (diff < 60000) {
        return '刚刚'
      } else if (diff < 3600000) {
        return `${Math.floor(diff / 60000)}分钟前`
      } else if (diff < 86400000) {
        return `${Math.floor(diff / 3600000)}小时前`
      } else if (diff < 604800000) {
        return `${Math.floor(diff / 86400000)}天前`
      } else {
        return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`
      }
    },
    async handlePublish() {
      const valid = await this.$refs.publishForm.validate().catch(() => false)
      if (!valid) return
      
      this.publishLoading = true
      try {
        await publishForum({
          ...this.publishForm,
          userId: this.userInfo.id
        })
        this.$message.success('发布成功')
        this.publishVisible = false
        this.publishForm = { title: '', content: '' }
        this.loadForumList()
      } catch (e) {
        console.error(e)
      } finally {
        this.publishLoading = false
      }
    }
  }
}
</script>

<style scoped lang="scss">
.forum-page {
  min-height: 100vh;
}

.page-banner {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  padding: 50px 0;
  
  h1 {
    font-size: 36px;
    color: #fff;
    margin-bottom: 10px;
  }
  
  p {
    color: rgba(255, 255, 255, 0.9);
    font-size: 16px;
  }
  
  .login-tip {
    font-size: 12px;
    color: rgba(255, 255, 255, 0.9);
    margin-top: 8px;
    text-align: center;
  }
}

.main-content {
  padding: 30px 0 60px;
}

.search-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.forum-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.forum-item {
  display: flex;
  gap: 15px;
  padding: 20px;
  cursor: pointer;
  
  .forum-avatar {
    flex-shrink: 0;
  }
  
  .forum-content {
    flex: 1;
    min-width: 0;
    
    .title {
      font-size: 18px;
      color: #333;
      margin-bottom: 10px;
      font-weight: 500;
      
      .el-tag {
        margin-right: 8px;
      }
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
      
      .author {
        color: #667eea;
      }
    }
  }
  
  &:hover {
    transform: translateX(5px);
  }
}

.pagination-section {
  margin-top: 30px;
  text-align: center;
}
</style>
