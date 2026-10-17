<template>
  <div class="page-container">
    <div class="filter-bar">
      <el-select v-model="listQuery.studentId" placeholder="选择学生筛选" clearable filterable style="width:180px" @change="handleFilter">
        <el-option v-for="item in studentOptions" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-input v-model="listQuery.keyword" placeholder="用途 / 备注" clearable style="width:220px" @keyup.enter.native="handleFilter" />
      <el-button type="primary" icon="el-icon-search" @click="handleFilter">查询</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      <div class="filter-actions">
        <el-button v-permission="['ADMIN']" type="success" icon="el-icon-plus" @click="handleCreate">新增资金</el-button>
        <el-button v-permission="['ADMIN']" type="warning" icon="el-icon-download" :loading="exportLoading" @click="handleExport">导出</el-button>
      </div>
    </div>

    <el-table v-loading="listLoading" :data="list" border stripe style="width:100%">
      <el-table-column type="index" label="序号" width="55" align="center" />
      <el-table-column prop="studentName" label="学生" min-width="90" />
      <el-table-column prop="sponsorName" label="资助人" min-width="90" />
      <el-table-column prop="amount" label="金额(元)" min-width="110" align="right">
        <template slot-scope="{ row }">{{ formatAmount(row.amount) }}</template>
      </el-table-column>
      <el-table-column prop="fundDate" label="资助日期" min-width="110" />
      <el-table-column prop="purpose" label="用途" min-width="140" show-overflow-tooltip />
      <el-table-column prop="remark" label="备注" min-width="140" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" min-width="150" />
      <el-table-column v-if="isAdmin" label="操作" width="150" align="center" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" icon="el-icon-edit" @click="handleUpdate(row)">编辑</el-button>
          <el-button type="text" class="danger-text" icon="el-icon-delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-bar">
      <el-pagination
        background
        :current-page="listQuery.page"
        :page-size="listQuery.size"
        :page-sizes="[10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="560px" :close-on-click-modal="false">
      <el-form ref="dataForm" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="学生" prop="studentId">
          <el-select v-model="form.studentId" placeholder="请选择学生" filterable style="width:100%" @change="onStudentChange">
            <el-option v-for="item in studentOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="资助人" prop="sponsorId">
          <el-select v-model="form.sponsorId" placeholder="请选择资助人" filterable style="width:100%" @change="onSponsorChange">
            <el-option v-for="item in sponsorOptions" :key="item.id" :label="item.realName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额(元)" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :precision="2" :step="100" controls-position="right" style="width:100%" />
        </el-form-item>
        <el-form-item label="资助日期" prop="fundDate">
          <el-date-picker v-model="form.fundDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
        </el-form-item>
        <el-form-item label="用途" prop="purpose">
          <el-input v-model="form.purpose" placeholder="请输入用途" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFunds, getFund, addFund, updateFund, deleteFund, exportFunds } from '@/api/fund'
import { studentOptions } from '@/api/student'
import { userOptions } from '@/api/user'
import { downloadFile } from '@/utils/download'

export default {
  name: 'Fund',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: { page: 1, size: 10, studentId: undefined, keyword: '' },
      dialogVisible: false,
      submitLoading: false,
      exportLoading: false,
      dialogType: 'create',
      studentOptions: [],
      sponsorOptions: [],
      form: this.getEmptyForm(),
      rules: {
        studentId: [{ required: true, message: '请选择学生', trigger: 'change' }],
        sponsorId: [{ required: true, message: '请选择资助人', trigger: 'change' }],
        amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
        fundDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
        purpose: [{ required: true, message: '请输入用途', trigger: 'blur' }]
      }
    }
  },
  computed: {
    isAdmin() { return this.$store.getters.role === 'ADMIN' },
    dialogTitle() { return this.dialogType === 'create' ? '新增资金' : '编辑资金' }
  },
  created() {
    this.getList()
    this.loadOptions()
  },
  methods: {
    getEmptyForm() {
      return { id: null, studentId: undefined, studentName: '', sponsorId: undefined, sponsorName: '', amount: 0, fundDate: '', purpose: '', remark: '' }
    },
    getList() {
      this.listLoading = true
      listFunds(this.listQuery).then(data => {
        this.list = (data && data.records) || []
        this.total = (data && data.total) || 0
      }).finally(() => { this.listLoading = false })
    },
    loadOptions() {
      studentOptions().then(data => { this.studentOptions = data || [] }).catch(() => {})
      userOptions('SPONSOR').then(data => { this.sponsorOptions = data || [] }).catch(() => {})
    },
    handleFilter() { this.listQuery.page = 1; this.getList() },
    handleReset() { this.listQuery.studentId = undefined; this.listQuery.keyword = ''; this.listQuery.page = 1; this.getList() },
    handleSizeChange(size) { this.listQuery.size = size; this.listQuery.page = 1; this.getList() },
    handleCurrentChange(page) { this.listQuery.page = page; this.getList() },
    onStudentChange(val) {
      const t = this.studentOptions.find(s => s.id === val)
      this.form.studentName = t ? t.name : ''
    },
    onSponsorChange(val) {
      const t = this.sponsorOptions.find(s => s.id === val)
      this.form.sponsorName = t ? t.realName : ''
    },
    handleCreate() {
      this.dialogType = 'create'
      this.form = this.getEmptyForm()
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },
    handleUpdate(row) {
      this.dialogType = 'update'
      getFund(row.id).then(data => {
        this.form = Object.assign(this.getEmptyForm(), data || {})
        this.dialogVisible = true
        this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
      })
    },
    submitForm() {
      this.$refs.dataForm.validate(valid => {
        if (!valid) return
        this.submitLoading = true
        const payload = { studentId: this.form.studentId, sponsorId: this.form.sponsorId, amount: this.form.amount, fundDate: this.form.fundDate, purpose: this.form.purpose, remark: this.form.remark }
        const action = this.dialogType === 'create' ? addFund(payload) : updateFund(this.form.id, payload)
        action.then(() => {
          this.$message.success(this.dialogType === 'create' ? '新增成功' : '修改成功')
          this.dialogVisible = false
          this.getList()
        }).finally(() => { this.submitLoading = false })
      })
    },
    handleDelete(row) {
      this.$confirm(`确认删除该资金记录吗？`, '提示', { type: 'warning' }).then(() => {
        deleteFund(row.id).then(() => {
          this.$message.success('删除成功')
          if (this.list.length === 1 && this.listQuery.page > 1) this.listQuery.page--
          this.getList()
        })
      }).catch(() => {})
    },
    handleExport() {
      this.exportLoading = true
      exportFunds().then(blob => downloadFile(blob, '资金记录.xlsx')).then(() => {
        this.$message.success('导出成功')
      }).catch(err => {
        if (err && err.message) this.$message.error(err.message)
      }).finally(() => { this.exportLoading = false })
    },
    formatAmount(val) {
      if (val === undefined || val === null) return '0.00'
      return Number(val).toFixed(2)
    }
  }
}
</script>

<style scoped>
.page-container { background:#fff; padding:16px; border-radius:4px; }
.filter-bar { display:flex; align-items:center; gap:8px; margin-bottom:16px; }
.filter-actions { margin-left:auto; display:flex; gap:8px; }
.pagination-bar { margin-top:16px; text-align:right; }
.danger-text { color:#F56C6C; }
</style>
