import request from '@/utils/request'

// 查询配置模板下内容的label与value列表
export function listValue(query) {
  return request({
    url: '/elective/config/value/list',
    method: 'get',
    params: query
  })
}

// 查询当前启用的学期模板的值
export function listSemester() {
	return request({
		url: '/elective/config/value/list/semester',
		method: 'get'
	})
}

// 查询当前启用的上课时间模板下的值
export function listClassTime() {
	return request({
		url: '/elective/config/value/list/classTime',
		method: 'get'
	})
}

// 查询配置模板下内容的label与value详细
export function getValue(id) {
  return request({
    url: '/elective/config/value/' + id,
    method: 'get'
  })
}

// 新增配置模板下内容的label与value
export function addValue(data) {
  return request({
    url: '/elective/config/value',
    method: 'post',
    data: data
  })
}

// 修改配置模板下内容的label与value
export function updateValue(data) {
  return request({
    url: '/elective/config/value',
    method: 'put',
    data: data
  })
}

// 删除配置模板下内容的label与value
export function delValue(id) {
  return request({
    url: '/elective/config/value/' + id,
    method: 'delete'
  })
}

// 导出配置模板下内容的label与value
export function exportValue(query) {
  return request({
    url: '/elective/config/value/export',
    method: 'get',
    params: query
  })
}

export function listInUse(type) {
  return request({
    url: '/elective/config/value/list/inUserType/' + type,
    method: 'get'
  })
}
