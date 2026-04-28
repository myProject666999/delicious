<template>
  <div class="forum-detail-page">
    <Header />
    
    <div class="main-content container">
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/forum' }">美食论坛</el-breadcrumb-item>
        <el-breadcrumb-item>{{ forum.title || '帖子详情' }}</el-breadcrumb-item>
      </el-breadcrumb>
      
      <div v-if="loading" class="loading-container">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      
      <template v-else-if="forum">
        <div class="detail-content">
          <div class="forum-main">
            <div class="forum-card card">
              <div class="forum-header">
                <div class="user-info">
                  <el-avatar :size="50" :src="forum.userAvatar" icon="el-icon-user-solid"></el-avatar>
                  <div class="user-detail">
                    <span class="user-name">{{ forum.userName || '匿名用户' }}</span>
                    <span class="create-time">{{ formatDate(forum.createTime) }}</span>
                  </div>
                </div>
                <div class="forum-stats">
                  <span><i class="el-icon-view"></i> {{ forum.viewCount || 0 }} 浏览</span>
                  <span><i class="el-icon-chat-dot-round"></i> {{ forum.replyCount || 0 }} 回复</span>
                </div>
              </div>
              
              <div class="forum-body">
                <h1 class="forum-title">
                  <el-tag v-if="forum.isTop" type="danger" size="small" effect="dark">置顶</el-tag>
                  {{ forum.title }}
                </h1>
                <div class="forum-content-text" v-html="formatContent(forum.content)"></div>
              </div>
            </div>
            
            <div class="reply-section">
              <h3 class="section-title">全部回复 ({{ forum.replyCount || 0 }})</h3>
              
              <div class="reply-form">
                <el-form :model="replyForm" :rules="replyRules" ref="replyForm">
                  <el-form-item prop="content">
                    <el-input
                      v-model="replyForm.content"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入回复内容"
                      maxlength="1000"
                      show-word-limit>
                    </el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="handleReply" :loading="replyLoading" :disabled="!isLoggedIn">
                      {{ isLoggedIn ? '发表回复' : '登录后回复' }}
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
              
              <div class="reply-list">
                <el-empty v-if="forum.replyList?.length === 0" description="暂无回复，快来抢沙发吧！"></el-empty>
                <div v-else class="reply-items">
                  <div v-for="reply in forum.replyList" :key="reply.id" class="reply-item">
                    <div class="reply-avatar">
                      <el-avatar :size="40" :src="reply.userAvatar" icon="el-icon-user-solid"></el-avatar>
                    </div>
                    <div class="reply-content">
                      <div class="reply-header">
                        <span class="user-name">{{ reply.userName || '匿名用户' }}</span>
                        <span v-if="reply.replyUserName" class="reply-to">
                          回复 <span class="reply-user">{{ reply.replyUserName }}</span>
                        </span>
                        <span class="create-time">{{ formatDate(reply.createTime) }}</span>
                        <el-button v-if="isLoggedIn" type="text" size="small" @click="replyTo(reply)">回复</el-button>
                      </div>
                      <div class="reply-text">{{ reply.content }}</div>
                      
                      <div v-if="reply.children?.length > 0" class="child-replies">
                        <div v-for="child in reply.children" :key="child.id" class="child-reply-item">
                          <div class="child-avatar">
                            <el-avatar :size="30" :src="child.userAvatar" icon="el-icon-user-solid"></el-avatar>
                          </div>
                          <div class="child-content">
                            <span class="user-name">{{ child.userName || '匿名用户' }}</span>
                            <span v-if="child.replyUserName" class="reply-to">
                              回复 <span class="reply-user">{{ child.replyUserName }}</span>
                            </span>
                            <span class="create-time">{{ formatDate(child.createTime) }}</span>
                            <div class="child-text">{{ child.content }}</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
      
      <el-empty v-else description="帖子不存在或已被删除"></el-empty>
    </div>
    
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getForumDetail, saveReply } from '@/api/forum'
import { mapGetters, mapState } from 'vuex'

export default {
  name: 'ForumDetail',
  components: { Header, Footer },
  data() {
    return {
      loading: false,
      forum: null,
      replyLoading: false,
      replyForm: {
        content: '',
        parentId: 0,
        replyUserId: null
      },
      replyRules: {
        content: [{ required: true, message: '请输入回复内容', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapGetters(['isLoggedIn']),
    ...mapState(['userInfo'])
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
        const res = await getForumDetail(id)
        this.forum = res.data
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
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
    formatContent(content) {
      if (!content) return ''
      return content.replace(/\n/g, '<br>')
    },
    replyTo(reply) {
      this.replyForm.parentId = reply.id
      this.replyForm.replyUserId = reply.userId
      this.$nextTick(() => {
        const textarea = document.querySelector('.reply-form textarea')
        if (textarea) {
          textarea.focus()
        }
      })
    },
    async handleReply() {
      if (!this.isLoggedIn) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }
      
      const valid = await this.$refs.replyForm.validate().catch(() => false)
      if (!valid) return
      
      this.replyLoading = true
      try {
        await saveReply({
          forumId: this.forum.id,
          userId: this.userInfo.id,
          content: this.replyForm.content,
          parentId: this.replyForm.parentId || 0,
          replyUserId: this.replyForm.replyUserId
        })
        this.$message.success('回复成功')
        this.replyForm = {
          content: '',
          parentId: 0,
          replyUserId: null
        }
        this.loadDetail()
      } catch (e) {
        console.error(e)
      } finally {
        this.replyLoading = false
      }
    }
  }
}
</script>

<style scoped lang="scss">
.forum-detail-page {
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

.forum-main {
  max-width: 800px;
  margin: 0 auto;
}

.forum-card {
  padding: 25px;
  margin-bottom: 20px;
}

.forum-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20px;
  
  .user-info {
    display: flex;
    gap: 12px;
    align-items: center;
    
    .user-detail {
      display: flex;
      flex-direction: column;
      
      .user-name {
        font-size: 15px;
        font-weight: 500;
        color: #333;
      }
      
      .create-time {
        font-size: 12px;
        color: #999;
      }
    }
  }
  
  .forum-stats {
    font-size: 13px;
    color: #999;
    
    span {
      margin-left: 20px;
    }
  }
}

.forum-body {
  .forum-title {
    font-size: 22px;
    color: #333;
    margin-bottom: 20px;
    font-weight: 600;
    line-height: 1.5;
    
    .el-tag {
      margin-right: 10px;
    }
  }
  
  .forum-content-text {
    font-size: 15px;
    color: #666;
    line-height: 2;
    white-space: pre-wrap;
  }
}

.reply-section {
  .section-title {
    font-size: 18px;
    color: #333;
    font-weight: 600;
    margin-bottom: 20px;
  }
  
  .reply-form {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
  }
  
  .reply-items {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .reply-item {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    display: flex;
    gap: 12px;
    
    .reply-content {
      flex: 1;
      min-width: 0;
      
      .reply-header {
        margin-bottom: 10px;
        font-size: 13px;
        
        .user-name {
          font-weight: 500;
          color: #667eea;
          margin-right: 8px;
        }
        
        .reply-to {
          color: #999;
          margin-right: 8px;
          
          .reply-user {
            color: #667eea;
          }
        }
        
        .create-time {
          color: #999;
          margin-right: 8px;
        }
      }
      
      .reply-text {
        font-size: 14px;
        color: #666;
        line-height: 1.6;
      }
      
      .child-replies {
        margin-top: 15px;
        padding-left: 15px;
        border-left: 2px solid #f0f0f0;
        
        .child-reply-item {
          display: flex;
          gap: 10px;
          padding: 10px 0;
          border-bottom: 1px solid #f5f5f5;
          
          &:last-child {
            border-bottom: none;
          }
          
          .child-content {
            flex: 1;
            font-size: 13px;
            
            .user-name {
              color: #667eea;
              margin-right: 8px;
            }
            
            .reply-to {
              color: #999;
              margin-right: 8px;
              
              .reply-user {
                color: #667eea;
              }
            }
            
            .create-time {
              color: #999;
            }
            
            .child-text {
              margin-top: 5px;
              color: #666;
              line-height: 1.5;
            }
          }
        }
      }
    }
  }
}
</style>
