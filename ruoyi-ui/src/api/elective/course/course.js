import request from '@/utils/request'

// 查询course列表
export function listCourse(query) {
  return request({
    url: '/elective/course/list',
    method: 'get',
    params: query
  })
}

// 查询course详细
export function getCourse(id) {
  return request({
    url: '/elective/course/' + id,
    method: 'get'
  })
}

// 新增course
export function addCourse(data) {
  return request({
    url: '/elective/course',
    method: 'post',
    data: data
  })
}

// 修改course
export function updateCourse(data) {
  return request({
    url: '/elective/course',
    method: 'put',
    data: data
  })
}

// 删除course
export function delCourse(id) {
  return request({
    url: '/elective/course/' + id,
    method: 'delete'
  })
}

// 导出course
export function exportCourse(query) {
  return request({
    url: '/elective/course/export',
    method: 'get',
    params: query
  })
}