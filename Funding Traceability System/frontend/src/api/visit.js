import request from '@/utils/request'

// 分页查询回访记录
export function listVisits(params) {
  return request({
    url: '/visits',
    method: 'get',
    params
  })
}

// 查询单条回访记录
export function getVisit(id) {
  return request({
    url: '/visits/' + id,
    method: 'get'
  })
}

// 新增回访记录
export function addVisit(data) {
  return request({
    url: '/visits',
    method: 'post',
    data
  })
}

// 修改回访记录
export function updateVisit(id, data) {
  return request({
    url: '/visits/' + id,
    method: 'put',
    data
  })
}

// 删除回访记录
export function deleteVisit(id) {
  return request({
    url: '/visits/' + id,
    method: 'delete'
  })
}

// 导出回访记录 Excel
export function exportVisits() {
  return request({
    url: '/visits/export',
    method: 'get',
    responseType: 'blob'
  })
}
