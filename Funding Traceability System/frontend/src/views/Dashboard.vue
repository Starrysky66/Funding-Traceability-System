<template>
  <div class="dashboard-container">
    <!-- 数据卡片 -->
    <el-row :gutter="16" class="card-row">
      <el-col :span="6" v-for="card in cards" :key="card.key">
        <div class="stat-card" :style="{ borderTopColor: card.color }">
          <div class="stat-icon" :style="{ background: card.color + '22', color: card.color }">
            <i :class="card.icon"></i>
          </div>
          <div class="stat-body">
            <div class="stat-value">{{ card.value }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">各模块数量</div>
          <div ref="moduleBar" class="chart-box"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">月度资金趋势</div>
          <div ref="monthlyLine" class="chart-box"></div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">资金用途分布</div>
          <div ref="purposePie" class="chart-box"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">各评分指标均值</div>
          <div ref="scoreAvgBar" class="chart-box"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardStats } from '@/api/dashboard'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {},
      charts: []
    }
  },
  computed: {
    cards() {
      const s = this.stats || {}
      return [
        { key: 'student', label: '学生总数', value: s.studentCount || 0, icon: 'el-icon-user', color: '#409EFF' },
        { key: 'fundAmount', label: '资助总金额(元)', value: this.formatAmount(s.fundTotalAmount), icon: 'el-icon-money', color: '#67C23A' },
        { key: 'fundCount', label: '资助次数', value: s.fundCount || 0, icon: 'el-icon-wallet', color: '#E6A23C' },
        { key: 'visit', label: '回访次数', value: s.visitCount || 0, icon: 'el-icon-chat-line-round', color: '#F56C6C' }
      ]
    }
  },
  mounted() {
    this.fetchData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c && c.dispose())
    this.charts = []
  },
  methods: {
    formatAmount(val) {
      if (val === undefined || val === null) return 0
      return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    fetchData() {
      getDashboardStats().then(data => {
        this.stats = data || {}
        this.$nextTick(() => {
          this.renderModuleBar()
          this.renderMonthlyLine()
          this.renderPurposePie()
          this.renderScoreAvgBar()
        })
      }).catch(() => {})
    },
    safeChart(refName) {
      const dom = this.$refs[refName]
      if (!dom) return null
      let chart = echarts.getInstanceByDom(dom)
      if (!chart) {
        chart = echarts.init(dom)
        this.charts.push(chart)
      }
      return chart
    },
    renderModuleBar() {
      const chart = this.safeChart('moduleBar')
      if (!chart) return
      const mb = this.stats.moduleBar || {}
      const data = [
        { name: '学生', value: mb.student || 0 },
        { name: '资金', value: mb.fund || 0 },
        { name: '回访', value: mb.visit || 0 },
        { name: '评分', value: mb.score || 0 }
      ]
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 40, right: 20, top: 30, bottom: 30 },
        xAxis: { type: 'category', data: data.map(d => d.name) },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{
          type: 'bar',
          data: data.map(d => d.value),
          barWidth: '40%',
          itemStyle: { color: '#409EFF', borderRadius: [4, 4, 0, 0] }
        }]
      })
    },
    renderMonthlyLine() {
      const chart = this.safeChart('monthlyLine')
      if (!chart) return
      const list = this.stats.monthlyFunds || []
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 50, right: 20, top: 30, bottom: 30 },
        xAxis: { type: 'category', data: list.map(d => d.month), boundaryGap: false },
        yAxis: { type: 'value' },
        series: [{
          type: 'line',
          data: list.map(d => d.amount),
          smooth: true,
          areaStyle: { color: 'rgba(64,158,255,0.15)' },
          itemStyle: { color: '#409EFF' }
        }]
      })
    },
    renderPurposePie() {
      const chart = this.safeChart('purposePie')
      if (!chart) return
      const list = this.stats.purposeDistribution || []
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0 },
        series: [{
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['50%', '45%'],
          data: list.map(d => ({ name: d.name, value: d.value })),
          itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 },
          label: { formatter: '{b}\n{d}%' }
        }]
      })
    },
    renderScoreAvgBar() {
      const chart = this.safeChart('scoreAvgBar')
      if (!chart) return
      const list = this.stats.scoreAverages || []
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 50, right: 20, top: 30, bottom: 40 },
        xAxis: { type: 'category', data: list.map(d => d.itemName), axisLabel: { interval: 0, rotate: list.length > 4 ? 25 : 0 } },
        yAxis: { type: 'value', max: 100 },
        series: [{
          type: 'bar',
          data: list.map(d => Number(d.avg).toFixed(2)),
          barWidth: '40%',
          itemStyle: { color: '#67C23A', borderRadius: [4, 4, 0, 0] }
        }]
      })
    },
    handleResize() {
      this.charts.forEach(c => c && c.resize())
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 4px;
}
.card-row {
  margin-bottom: 16px;
}
.stat-card {
  background: #fff;
  border-radius: 6px;
  padding: 20px;
  display: flex;
  align-items: center;
  border-top: 3px solid #409EFF;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 14px;
}
.stat-body {
  flex: 1;
}
.stat-value {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
}
.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
.chart-row {
  margin-bottom: 16px;
}
.chart-card {
  background: #fff;
  border-radius: 6px;
  padding: 12px 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
.chart-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  border-left: 3px solid #409EFF;
  padding-left: 8px;
}
.chart-box {
  width: 100%;
  height: 280px;
}
</style>
