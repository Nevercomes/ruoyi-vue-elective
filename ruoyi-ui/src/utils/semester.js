import {
  parseSemester
} from '@/utils/index.js'

export function inTime(label) {
  try {
    let today = new Date()
    let startTime = new Date('1997-1-1')
    let endTime = new Date('1997-1-1')
    const semList = parseSemester(label)
    const year1 = semList['year1']
    const year2 = semList['year2']
    const half = semList['half']

    if (half == '上期') {
      // 上学期是 8月到来年1月
      startTime = new Date(Date.parse(year1 + '-8-1 00:00:00'))
      endTime = new Date(Date.parse(year2 + '-1-31 23:59:59'))
    } else if (half == '下期') {
      // 下学期是 2月到7月
      startTime = new Date(Date.parse(year2 + '-2-1 00:00:00'))
      endTime = new Date(Date.parse(year2 + '-7-31 23:59:59'))
    }
    if (startTime < today && endTime > today) {
      return true
    }
    return false
  } catch (e) {
    //TODO handle the exception
    return false
  }
}
