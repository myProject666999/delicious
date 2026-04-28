<template>
  <div class="admin-forum">
    <div class="page-header">
      <h2>美食论坛管理</h2>
    </div>
    
    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="帖子标题">
          <el-input v-model="searchForm.title" placeholder="帖子标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="正常" :value="1"></el-option>
            <el-option label="隐藏" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="table-wrapper">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="帖子标题" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="作者" width="100"></el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="70"></el-table-column>
        <el-table-column prop="replyCount" label="回复" width="70"></el-table-column>
        <el-table-column prop="isTop" label="置顶" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isTop"
              :active-value="1"
              :inactive-value="0"
              @change="handleTopChange(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="mini">
              {{ scope.row.status === 1 ? '正常' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleStatus(scope.row)">
              {{ scope.row.status === 1 ? '隐藏' : '显示' }}
            </el-button>
            <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <div class="pagination-wrapper">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    
    <el-dialog title="帖子详情" :visible.sync="dialogVisible" width="700px">
      <div class="forum-detail" v-if="currentForum">
        <h3>{{ currentForum.title }}</h3>
        <div class="meta">
          <span>作者：{{ currentForum.userName || '匿名用户' }}</span>
          <span>发布时间：{{ formatDate(currentForum.createTime) }}</span>
        </div>
        <div class="content" v-html="formatContent(currentForum.content)"></div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getForumList, saveForum, deleteForum, updateForumTop } from '@/api/forum'

export default {
  name: 'AdminForum',
  data() {
    return {
      loading: false,
      tableData: [],
      searchForm: {
        title: '',
        status: null
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      currentForum: null
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')} ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`
    },
    formatContent(content) {
      if (!content) return ''
      return content.replace(/\n/g, '<br>')
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          title: this.searchForm.title,
          status: this.searchForm.status
        }
        const res = await getForumList(params)
        this.tableData = res.data.records || []
        this.total = res.data.total || 0
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        title: '',
        status: null
      }
      this.currentPage = 1
      this.loadData()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadData()
    },
    handleView(row) {
      this.currentForum = row
      this.dialogVisible = true
    },
    async handleTopChange(row) {
      try {
        await updateForumTop(row.id, row.isTop)
        this.$message.success(row.isTop ? '置顶成功' : '取消置顶成功')
      } catch (e) {
        console.error(e)
        row.isTop = row.isTop === 1 ? 0 : 1
      }
    },
    handleStatus(row) {
      const newStatus = row.status === 1 ? 0 : 1
      const action = newStatus === 1 ? '显示' : '隐藏'
      this.$confirm(`确定要${action}该帖子吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await saveForum({ id: row.id, status: newStatus })
          this.$message.success(`${action}成功`)
          this.loadData()
        } catch (e) {
          console.error(e)
        }
      }).catch(() => {})
    },
    handleDelete(row) {
      this.$confirm('确定要删除该帖子吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteForum(row.id)
          this.$message.success('删除成功')
          this.loadData()
        } catch (e) {
          console.error(e)
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped lang="scss">
.admin-forum {
  .page-header {
    margin-bottom: 20px;
    
    h2 {
      font-size: 20px;
      font-weight: 500;
      color: #333;
    }
  }
  
  .search-bar {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
  }
  
  .table-wrapper {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
  }
  
  .pagination-wrapper {
    margin-top: 20px;
    text-align: right;
  }
  
  .forum-detail {
    h3 {
      font-size: 18px;
      color: #333;
      margin-bottom: 15px;
    }
    
    .meta {
      font-size: 13px;
      color: #999;
      margin-bottom: 20px;
      
      span {
        margin-right: 20px;
      }
    }
    
    .content {
      font-size: 14px;
      color: #666;
      line-height: 2;
      background: #f5f5f5;
      padding: 15px;
      border-radius: 8px;
    }
  }
}
</style>
