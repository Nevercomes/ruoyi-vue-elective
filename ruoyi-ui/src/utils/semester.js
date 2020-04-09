export function inTime(label) {
  try {
    let today = new Date()
    let startTime = new Date('1997-1-1')
    let endTime = new Date('1997-1-1')
    if (label.indexOf('上') != -1) {
      // 上学期是 9.1-1.31 2019-2020上学期
      let year = label.replace(/[^0-9]/ig, "")
      if (year.length == 4) {
        startTime = new Date(Date.parse(year + '-9-1'))
        endTime = new Date(Date.parse(Number(year) + 1 + '-1-31'))
      } else if (year.length == 8) {
        const year1 = year.substr(0, 4)
        const year2 = year.substr(4, 4)
        startTime = new Date(Date.parse(year1 + '-9-1'))
        endTime = new Date(Date.parse(year2 + '-1-31'))
      }
    } else if (label.indexOf('下') != -1) {
      // 下学期是 2.1-8.31 2019-2020下学期
      let year = label.replace(/[^0-9]/ig, "")
      if (year.length == 4) {
        startTime = new Date(Date.parse(year + '-2-1'))
        endTime = new Date(Date.parse(year + '-8-31'))
      } else if (year.length == 8) {
        const year1 = year.substr(0, 4)
        const year2 = year.substr(4, 4)
        startTime = new Date(Date.parse(year2 + '-2-1'))
        endTime = new Date(Date.parse(year2 + '-8-31'))
      }
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
