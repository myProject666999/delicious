<template>
  <div class="recipe-detail-page">
    <Header />
    
    <div class="main-content container">
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/recipe' }">美食菜谱</el-breadcrumb-item>
        <el-breadcrumb-item>{{ recipe.title || '菜谱详情' }}</el-breadcrumb-item>
      </el-breadcrumb>
      
      <div v-if="loading" class="loading-container">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      
      <template v-else-if="recipe">
        <div class="detail-header">
          <h1 class="title">{{ recipe.title }}</h1>
          <div class="meta">
            <span class="category">
              <i class="el-icon-folder-opened"></i> {{ recipe.categoryName || '未分类' }}
            </span>
            <span class="author" v-if="recipe.userName">
              <i class="el-icon-user"></i> {{ recipe.userName }}
            </span>
            <span class="view-count">
              <i class="el-icon-view"></i> {{ recipe.viewCount || 0 }} 次浏览
            </span>
            <span class="create-time">
              <i class="el-icon-time"></i> {{ formatDate(recipe.createTime) }}
            </span>
          </div>
        </div>
        
        <div class="detail-content">
          <div class="cover-section">
            <img :src="recipe.cover || defaultImage" :alt="recipe.title" class="cover-image" />
            <div class="action-bar">
              <el-button :type="isLiked ? 'danger' : ''" :icon="isLiked ? 'el-icon-star-on' : 'el-icon-star-off'" @click="handleLike">
                {{ isLiked ? '已点赞' : '点赞' }} ({{ recipe.likeCount || 0 }})
              </el-button>
              <el-button icon="el-icon-share">分享</el-button>
            </div>
          </div>
          
          <div class="info-section">
            <div class="info-item" v-if="recipe.introduction">
              <h3 class="section-title">简介</h3>
              <div class="section-content">{{ recipe.introduction }}</div>
            </div>
            
            <div class="info-item" v-if="recipe.ingredients">
              <h3 class="section-title">食材</h3>
              <div class="section-content" v-html="formatContent(recipe.ingredients)"></div>
            </div>
            
            <div class="info-item" v-if="recipe.steps">
              <h3 class="section-title">制作步骤</h3>
              <div class="section-content" v-html="formatContent(recipe.steps)"></div>
            </div>
            
            <div class="info-item" v-if="recipe.tips">
              <h3 class="section-title">小贴士</h3>
              <div class="section-content">{{ recipe.tips }}</div>
            </div>
          </div>
        </div>
      </template>
      
      <el-empty v-else description="菜谱不存在或已被删除"></el-empty>
    </div>
    
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getRecipeDetail, toggleRecipeLike } from '@/api/recipe'
import { mapGetters, mapState } from 'vuex'

export default {
  name: 'RecipeDetail',
  components: { Header, Footer },
  data() {
    return {
      loading: false,
      recipe: null,
      isLiked: false,
      defaultImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=delicious%20food%20placeholder&image_size=square'
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
      const userId = this.userInfo ? this.userInfo.id : null
      
      try {
        const res = await getRecipeDetail(id, userId)
        this.recipe = res.data
        this.isLiked = this.recipe.isLiked || false
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
    },
    async handleLike() {
      if (!this.isLoggedIn) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }
      
      try {
        const res = await toggleRecipeLike(this.userInfo.id, this.recipe.id)
        this.isLiked = res.data
        if (this.isLiked) {
          this.recipe.likeCount = (this.recipe.likeCount || 0) + 1
          this.$message.success('点赞成功')
        } else {
          this.recipe.likeCount = Math.max(0, (this.recipe.likeCount || 0) - 1)
          this.$message.success('已取消点赞')
        }
      } catch (e) {
        console.error(e)
      }
    }
  }
}
</script>

<style scoped lang="scss">
.recipe-detail-page {
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

.detail-header {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  
  .title {
    font-size: 28px;
    color: #333;
    margin-bottom: 15px;
    font-weight: 600;
  }
  
  .meta {
    display: flex;
    gap: 25px;
    flex-wrap: wrap;
    font-size: 14px;
    color: #999;
    
    span {
      display: flex;
      align-items: center;
      gap: 5px;
    }
    
    .category {
      color: #ff6b6b;
    }
  }
}

.detail-content {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.cover-section {
  margin-bottom: 30px;
  
  .cover-image {
    width: 100%;
    max-height: 500px;
    object-fit: cover;
    border-radius: 8px;
  }
  
  .action-bar {
    margin-top: 20px;
    display: flex;
    gap: 15px;
    justify-content: center;
  }
}

.info-section {
  .info-item {
    margin-bottom: 30px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .section-title {
      font-size: 18px;
      color: #333;
      font-weight: 600;
      margin-bottom: 15px;
      padding-left: 10px;
      border-left: 3px solid #ff6b6b;
    }
    
    .section-content {
      font-size: 15px;
      color: #666;
      line-height: 2;
      padding: 0 13px;
    }
  }
}
</style>
