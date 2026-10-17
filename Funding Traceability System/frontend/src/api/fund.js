import request from '@/utils/request'

// 分页查询资金记录
export function listFunds(params) {
  return request({
    url: '/funds',
    method: 'get',
    params
  })
}

// 查询单条资金记录
export function getFund(id) {
  return request({
    url: '/funds/' + id,
    method: 'get'
  })
}

// 新增资金记录
export function addFund(data) {
  return request({
    url: '/funds',
    method: 'post',
    data
  })
}

// 修改资金记录
export function updateFund(id, data) {
  return request({
    url: '/funds/' + id,
    method: 'put',
    data
  })
}

// 删除资金记录
export function deleteFund(id) {
  return request({
    url: '/funds/' + id,
    method: 'delete'
  })
}

// 导出资金记录 Excel
export function exportFunds() {
  return request({
    url: '/funds/export',
    method: 'get',
    responseType: 'blob'
  })
}
