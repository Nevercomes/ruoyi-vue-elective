import request from '@/utils/request'

// 查询课程列表
export function listCourse(query) {
  return request({
    url: '/elective/course/list',
    method: 'get',
    params: query
  })
}

// 查询课程详细
export function getCourse(id) {
  return request({
    url: '/elective/course/' + id,
    method: 'get'
  })
}

// 新增课程
export function addCourse(data) {
  return request({
    url: '/elective/course',
    method: 'post',
    data: data
  })
}

// 修改课程
export function updateCourse(data) {
  return request({
    url: '/elective/course',
    method: 'put',
    data: data
  })
}

// 删除课程
export function delCourse(id) {
  return request({
    url: '/elective/course/' + id,
    method: 'delete'
  })
}

// 导出课程
export function exportCourse(query) {
  return request({
    url: '/elective/course/export',
    method: 'get',
    params: query
  })
}

export function listForSelect(query) {
  return request({
    url: '/elective/course/select/list',
    method: 'get',
    params: query
  })
}

export function listStatistic(query) {
  return request({
    url: '/elective/people/statistic',
    method: 'get',
    params: query
  })
}

export function exportStatistic(query) {
  return request({
    url: '/elective/people/export',
    method: 'get',
    params: query
  })
}

export function listCoursePlainList() {
  return request({
    url: '/elective/course/list/plain',
    method: 'get'
  })
}
