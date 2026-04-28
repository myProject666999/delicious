<template>
  <div class="recipe-page">
    <Header />
    
    <div class="page-banner">
      <div class="container">
        <h1>美食菜谱</h1>
        <p>探索世界各地的美味佳肴，学习制作方法</p>
      </div>
    </div>
    
    <div class="main-content container">
      <div class="search-section">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="分类">
            <el-select v-model="searchForm.categoryId" placeholder="全部分类" clearable @change="handleSearch">
              <el-option v-for="category in categoryList" :key="category.id" :label="category.name" :value="category.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="搜索">
            <el-input v-model="searchForm.title" placeholder="搜索菜谱名称" clearable @keyup.enter.native="handleSearch">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch"><i class="el-icon-search"></i> 搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <div class="recipe-list">
        <el-empty v-if="recipeList.length === 0" description="暂无菜谱数据"></el-empty>
        <div v-else class="recipe-grid">
          <div v-for="recipe in recipeList" :key="recipe.id" class="recipe-card card" @click="goToDetail(recipe.id)">
            <div class="recipe-image">
              <img :src="recipe.cover || defaultImage" :alt="recipe.title" />
              <div class="recipe-category">{{ recipe.categoryName }}</div>
            </div>
            <div class="recipe-info">
              <h3 class="text-ellipsis">{{ recipe.title }}</h3>
              <p class="intro text-ellipsis-2">{{ recipe.introduction }}</p>
              <div class="meta">
                <span v-if="recipe.userName" class="author">by {{ recipe.userName }}</span>
              </div>
              <div class="stats">
                <span><i class="el-icon-view"></i> {{ recipe.viewCount || 0 }}</span>
                <span><i class="el-icon-star-on"></i> {{ recipe.likeCount || 0 }}</span>
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
          :page-sizes="[8, 16, 24, 32]"
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
import { getRecipeFrontList, getRecipeCategoryList } from '@/api/recipe'

export default {
  name: 'Recipe',
  components: { Header, Footer },
  data() {
    return {
      categoryList: [],
      recipeList: [],
      searchForm: {
        title: '',
        categoryId: null
      },
      currentPage: 1,
      pageSize: 12,
      total: 0,
      defaultImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=delicious%20food%20placeholder&image_size=square'
    }
  },
  created() {
    this.loadCategoryList()
    this.loadRecipeList()
  },
  methods: {
    async loadCategoryList() {
      try {
        const res = await getRecipeCategoryList()
        this.categoryList = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    async loadRecipeList() {
      try {
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          title: this.searchForm.title,
          categoryId: this.searchForm.categoryId
        }
        const res = await getRecipeFrontList(params)
        this.recipeList = res.data.records || []
        this.total = res.data.total || 0
      } catch (e) {
        console.error(e)
      }
    },
    handleSearch() {
      this.currentPage = 1
      this.loadRecipeList()
    },
    resetSearch() {
      this.searchForm = {
        title: '',
        categoryId: null
      }
      this.currentPage = 1
      this.loadRecipeList()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadRecipeList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadRecipeList()
    },
    goToDetail(id) {
      this.$router.push(`/recipe/detail/${id}`)
    }
  }
}
</script>

<style scoped lang="scss">
.recipe-page {
  min-height: 100vh;
}

.page-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

.search-section {
  background: #fff;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
  
  .search-form {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-wrap: wrap;
    gap: 15px;
  }
}

.recipe-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 25px;
}

.recipe-card {
  cursor: pointer;
  
  .recipe-image {
    height: 180px;
    position: relative;
    overflow: hidden;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s;
    }
    
    &:hover img {
      transform: scale(1.05);
    }
    
    .recipe-category {
      position: absolute;
      top: 10px;
      left: 10px;
      background: linear-gradient(135deg, #ff6b6b, #ff8e53);
      color: #fff;
      padding: 4px 10px;
      border-radius: 4px;
      font-size: 12px;
    }
  }
  
  .recipe-info {
    padding: 15px;
    
    h3 {
      font-size: 16px;
      color: #333;
      margin-bottom: 8px;
      font-weight: 600;
    }
    
    .intro {
      font-size: 13px;
      color: #666;
      line-height: 1.5;
      margin-bottom: 10px;
    }
    
    .meta {
      margin-bottom: 8px;
      
      .author {
        font-size: 12px;
        color: #999;
      }
    }
    
    .stats {
      display: flex;
      gap: 15px;
      font-size: 12px;
      color: #999;
      
      span {
        display: flex;
        align-items: center;
        gap: 3px;
      }
    }
  }
}

.pagination-section {
  margin-top: 40px;
  text-align: center;
}

@media screen and (max-width: 1200px) {
  .recipe-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media screen and (max-width: 768px) {
  .recipe-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
