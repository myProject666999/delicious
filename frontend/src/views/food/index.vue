<template>
  <div class="food-page">
    <Header />
    
    <div class="page-banner">
      <div class="container">
        <h1>美食信息</h1>
        <p>发现各地特色美食，了解美食文化</p>
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
            <el-input v-model="searchForm.name" placeholder="搜索美食名称" clearable @keyup.enter.native="handleSearch">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch"><i class="el-icon-search"></i> 搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <div class="food-list">
        <el-empty v-if="foodList.length === 0" description="暂无美食数据"></el-empty>
        <div v-else class="food-grid">
          <div v-for="food in foodList" :key="food.id" class="food-card card" @click="goToDetail(food.id)">
            <div class="food-image">
              <img :src="food.cover || defaultImage" :alt="food.name" />
              <div class="food-origin" v-if="food.origin">{{ food.origin }}</div>
            </div>
            <div class="food-info">
              <h3 class="text-ellipsis">{{ food.name }}</h3>
              <p class="intro text-ellipsis-2">{{ food.introduction }}</p>
              <div class="meta">
                <span v-if="food.categoryName" class="category">{{ food.categoryName }}</span>
              </div>
              <div class="stats">
                <span><i class="el-icon-view"></i> {{ food.viewCount || 0 }}</span>
                <span><i class="el-icon-star-on"></i> {{ food.likeCount || 0 }}</span>
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
import { getFoodFrontList, getFoodCategoryList } from '@/api/food'

export default {
  name: 'Food',
  components: { Header, Footer },
  data() {
    return {
      categoryList: [],
      foodList: [],
      searchForm: {
        name: '',
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
    this.loadFoodList()
  },
  methods: {
    async loadCategoryList() {
      try {
        const res = await getFoodCategoryList()
        this.categoryList = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    async loadFoodList() {
      try {
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          name: this.searchForm.name,
          categoryId: this.searchForm.categoryId
        }
        const res = await getFoodFrontList(params)
        this.foodList = res.data.records || []
        this.total = res.data.total || 0
      } catch (e) {
        console.error(e)
      }
    },
    handleSearch() {
      this.currentPage = 1
      this.loadFoodList()
    },
    resetSearch() {
      this.searchForm = {
        name: '',
        categoryId: null
      }
      this.currentPage = 1
      this.loadFoodList()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadFoodList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadFoodList()
    },
    goToDetail(id) {
      this.$router.push(`/food/detail/${id}`)
    }
  }
}
</script>

<style scoped lang="scss">
.food-page {
  min-height: 100vh;
}

.page-banner {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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

.food-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 25px;
}

.food-card {
  cursor: pointer;
  
  .food-image {
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
    
    .food-origin {
      position: absolute;
      top: 10px;
      left: 10px;
      background: linear-gradient(135deg, #667eea, #764ba2);
      color: #fff;
      padding: 4px 10px;
      border-radius: 4px;
      font-size: 12px;
    }
  }
  
  .food-info {
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
      
      .category {
        font-size: 12px;
        color: #667eea;
        background: #f0f2ff;
        padding: 2px 8px;
        border-radius: 4px;
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
  .food-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media screen and (max-width: 768px) {
  .food-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
