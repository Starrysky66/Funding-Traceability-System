import request from '@/utils/request'

// 分页查询用户
export function listUsers(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

// 修改用户
export function updateUser(id, data) {
  return request({
    url: '/users/' + id,
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: '/users/' + id,
    method: 'delete'
  })
}

// 资助人下拉选项
export function userOptions(role) {
  return request({
    url: '/users/options',
    method: 'get',
    params: { role }
  })
}
