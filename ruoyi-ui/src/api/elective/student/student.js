import request from '@/utils/request'

// 查询student列表
export function listStudent(query) {
  return request({
    url: '/elective/student/list',
    method: 'get',
    params: query
  })
}

// 查询student详细
export function getStudent(id) {
  return request({
    url: '/elective/student/' + id,
    method: 'get'
  })
}

// 新增student
export function addStudent(data) {
  return request({
    url: '/elective/student',
    method: 'post',
    data: data
  })
}

// 修改student
export function updateStudent(data) {
  return request({
    url: '/elective/student',
    method: 'put',
    data: data
  })
}

// 删除student
export function delStudent(id) {
  return request({
    url: '/elective/student/' + id,
    method: 'delete'
  })
}

// 导出student
export function exportStudent(query) {
  return request({
    url: '/elective/student/export',
    method: 'get',
    params: query
  })
}