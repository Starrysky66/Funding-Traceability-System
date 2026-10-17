import request from '@/utils/request'

// 查询全部评分指标
export function listScoreItems() {
  return request({
    url: '/score-items',
    method: 'get'
  })
}

// 新增评分指标
export function addScoreItem(data) {
  return request({
    url: '/score-items',
    method: 'post',
    data
  })
}

// 修改评分指标
export function updateScoreItem(id, data) {
  return request({
    url: '/score-items/' + id,
    method: 'put',
    data
  })
}

// 删除评分指标
export function deleteScoreItem(id) {
  return request({
    url: '/score-items/' + id,
    method: 'delete'
  })
}
