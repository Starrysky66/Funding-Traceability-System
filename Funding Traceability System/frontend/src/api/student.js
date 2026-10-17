import request from '@/utils/request'

// 分页查询学生列表
export function listStudents(params) {
  return request({
    url: '/students',
    method: 'get',
    params
  })
}

// 查询单个学生详情
export function getStudent(id) {
  return request({
    url: '/students/' + id,
    method: 'get'
  })
}

// 新增学生
export function addStudent(data) {
  return request({
    url: '/students',
    method: 'post',
    data
  })
}

// 修改学生
export function updateStudent(id, data) {
  return request({
    url: '/students/' + id,
    method: 'put',
    data
  })
}

// 删除学生
export function deleteStudent(id) {
  return request({
    url: '/students/' + id,
    method: 'delete'
  })
}

// 导出学生 Excel
export function exportStudents() {
  return request({
    url: '/students/export',
    method: 'get',
    responseType: 'blob'
  })
}

// 学生下拉选项
export function studentOptions() {
  return request({
    url: '/students/options',
    method: 'get'
  })
}
