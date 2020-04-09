package com.ruoyi.project.elective.course.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.constant.ElectiveDict;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.elective.course.domain.ElectiveCoursePeople;
import com.ruoyi.project.elective.course.domain.ElectiveCourseTime;
import com.ruoyi.project.elective.course.mapper.ElectiveCourseTimeMapper;
import com.ruoyi.project.elective.course.service.IElectiveCoursePeopleService;
import com.ruoyi.project.elective.record.domain.ElectiveApplyRecord;
import com.ruoyi.project.elective.record.domain.ElectiveSelectRecord;
import com.ruoyi.project.elective.record.service.IElectiveApplyRecordService;
import com.ruoyi.project.elective.record.service.IElectiveSelectRecordService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.course.mapper.ElectiveCourseMapper;
import com.ruoyi.project.elective.course.domain.ElectiveCourse;
import com.ruoyi.project.elective.course.service.IElectiveCourseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课程Service业务层处理
 *
 * @author Sunss
 * @date 2020-02-12
 */
@Service
public class ElectiveCourseServiceImpl implements IElectiveCourseService {
    @Autowired
    private ElectiveCourseMapper electiveCourseMapper;
    @Autowired
    private IElectiveCoursePeopleService electiveCoursePeopleService;
    @Autowired
    private IElectiveApplyRecordService applyRecordService;
    @Autowired
    private IElectiveCoursePeopleService coursePeopleService;
    @Autowired
    private IElectiveSelectRecordService selectRecordService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ElectiveCourseTimeMapper courseTimeMapper;

    /**
     * 查询课程
     *
     * @param id 课程ID
     * @return 课程
     */
    @Override
    public ElectiveCourse selectElectiveCourseById(Long id) {
        ElectiveCourse course = electiveCourseMapper.selectElectiveCourseById(id);
        if (course != null) {
            course.setPeopleList(coursePeopleService.selectElectiveCoursePeopleList(new ElectiveCoursePeople(id)));
            course.setTimeList(courseTimeMapper.selectElectiveCourseTimeList(new ElectiveCourseTime(id)));
        }
        return course;
    }

    /**
     * 查询课程列表
     *
     * @param electiveCourse 课程
     * @return 课程
     */
    @Override
    public List<ElectiveCourse> selectElectiveCourseList(ElectiveCourse electiveCourse) {
        List<ElectiveCourse> list = electiveCourseMapper.selectElectiveCourseList(electiveCourse);
        for (ElectiveCourse course : list) {
            course.setPeopleList(coursePeopleService.selectElectiveCoursePeopleList(new ElectiveCoursePeople(course.getId())));
            course.setTimeList(courseTimeMapper.selectElectiveCourseTimeList(new ElectiveCourseTime(course.getId())));
        }
        return list;
    }

    /**
     * 新增课程
     *
     * @param electiveCourse 课程
     * @return 结果
     */
    @Override
    @Transactional
    public int insertElectiveCourse(ElectiveCourse electiveCourse) {
        electiveCourse.preInsert();
        int res = electiveCourseMapper.insertElectiveCourse(electiveCourse);
        // 插入招生人数
        List<ElectiveCoursePeople> peopleList = electiveCourse.getPeopleList();
        for (ElectiveCoursePeople people : peopleList) {
            people.setCourseId(electiveCourse.getId());
            electiveCoursePeopleService.insertElectiveCoursePeople(people);
        }
        // 插入上课时间
        List<ElectiveCourseTime> timeList = electiveCourse.getTimeList();
        for (ElectiveCourseTime time : timeList) {
            time.setCourseId(electiveCourse.getId());
            courseTimeMapper.insertElectiveCourseTime(time);
        }
        // 插入申请记录
        ElectiveApplyRecord applyRecord = new ElectiveApplyRecord();
        applyRecord.setCourseId(electiveCourse.getId());
        applyRecord.setTeacherId(electiveCourse.getTeacherId());
        applyRecordService.insertElectiveApplyRecord(applyRecord);
        return res;
    }

    /**
     * 修改课程
     *
     * @param electiveCourse 课程
     * @return 结果
     */
    @Override
    @Transactional
    public int updateElectiveCourse(ElectiveCourse electiveCourse) {
        electiveCourse.preUpdate();
        int res = electiveCourseMapper.updateElectiveCourse(electiveCourse);
        List<ElectiveCoursePeople> peopleNew = electiveCourse.getPeopleList();
        List<ElectiveCoursePeople> peopleOld = electiveCoursePeopleService.selectElectiveCoursePeopleListByCourseId(electiveCourse.getId());
        List<ElectiveCourseTime> timeNew = electiveCourse.getTimeList();
        List<ElectiveCourseTime> timeOld = courseTimeMapper.selectElectiveCourseTimeList(new ElectiveCourseTime(electiveCourse.getId()));
        // 获取新增的
        List<ElectiveCoursePeople> add = getAdd(peopleNew, peopleOld);
        List<ElectiveCourseTime> addTime = getAddTime(timeNew, timeOld);
        for (ElectiveCoursePeople p : add) {
            p.setCourseId(electiveCourse.getId());
            electiveCoursePeopleService.insertElectiveCoursePeople(p);
        }
        for (ElectiveCourseTime t : addTime) {
            t.setCourseId(electiveCourse.getId());
            courseTimeMapper.insertElectiveCourseTime(t);
        }
        // 获取删除的
        List<ElectiveCoursePeople> del = getDel(peopleNew, peopleOld);
        List<ElectiveCourseTime> delTime = getDelTime(timeNew, timeOld);
        for (ElectiveCoursePeople p : del) {
            // 如果已经有人选课则不可以删除该年级
            if (p.getSelectNum() != null && p.getSelectNum() != 0) {
                SysDept grade = sysDeptService.selectDeptById(p.getGradeId());
                throw new CustomException(grade.getDeptName() + "已经有人选课，禁止删除该年级招生人数");
            }
            electiveCoursePeopleService.deleteElectiveCoursePeopleById(p.getId());
        }
        for (ElectiveCourseTime t : delTime) {
            courseTimeMapper.deleteElectiveCourseTimeById(t.getId());
        }
        // 获取修改的
        List<ElectiveCoursePeople> update = getUpdate(peopleNew, peopleOld);
        List<ElectiveCourseTime> updateTime = getUpdateTime(timeNew, timeOld);
        for (ElectiveCoursePeople p : update) {
            electiveCoursePeopleService.updateElectiveCoursePeople(p);
        }
        for (ElectiveCourseTime t : updateTime) {
            courseTimeMapper.updateElectiveCourseTime(t);
        }
        return res;
    }

    /**
     * 批量删除课程
     * 新的业务逻辑为可以删除课程，但会删除相关的选课记录
     *
     * @param ids 需要删除的课程ID
     * @return 结果
     */
    public String deleteElectiveCourseByIds_old(Long[] ids) {
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Long id : ids) {
            ElectiveCourse course = this.selectElectiveCourseById(id);
            // 检查课程是否已经选课
            ElectiveSelectRecord q = new ElectiveSelectRecord();
            q.setCourseId(course.getId());
            List<ElectiveSelectRecord> selectRecordList = selectRecordService.selectElectiveSelectRecordList(q);
            if (selectRecordList != null && selectRecordList.size() > 0) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、课程").append(course.getName()).append(" 删除失败，已有学生选课");
            } else {
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、课程 ").append(course.getName()).append(" 删除成功");
                electiveCourseMapper.deleteElectiveCourseById(id);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，删除失败！共 " + failureNum + " 课程不允许删除：");
            return failureMsg.toString();
        } else {
            successMsg.insert(0, "课程删除成功！共 " + successNum + " 条，数据如下：");
            return successMsg.toString();
        }
    }

    /**
     * 批量删除课程
     *
     * @param ids 需要删除的课程ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteElectiveCourseByIds(Long[] ids) {
        for (Long id : ids) {
            ElectiveCourse course = this.selectElectiveCourseById(id);
            // 检查课程是否已经选课
            ElectiveSelectRecord q = new ElectiveSelectRecord();
            q.setCourseId(course.getId());
            List<ElectiveSelectRecord> selectRecordList = selectRecordService.selectElectiveSelectRecordList(q);
            if (selectRecordList != null && selectRecordList.size() > 0) {
//                selectRecordService.deleteElectiveSelectRecordByIds((Long[]) selectRecordList.stream().map(ElectiveSelectRecord::getId).toArray());
                Long[] sIds = new Long[selectRecordList.size()];
                for (int i = 0; i < selectRecordList.size(); i++) {
                    sIds[i] = selectRecordList.get(i).getId();
                }
                selectRecordService.deleteElectiveSelectRecordByIds(sIds);
            }
        }
        return electiveCourseMapper.deleteElectiveCourseByIds(ids);
    }

    /**
     * 删除课程信息
     *
     * @param id 课程ID
     * @return 结果
     */
    @Override
    public int deleteElectiveCourseById(Long id) {
        return electiveCourseMapper.deleteElectiveCourseById(id);
    }

    @Override
    public List<ElectiveCourse> selectPlainList(ElectiveCourse electiveCourse) {
        return electiveCourseMapper.selectPlainList(electiveCourse);
    }


    /**
     * 重新申请课程
     * @param electiveCourse
     * @return
     */
    @Override
    @Transactional
    public int reApply(ElectiveCourse electiveCourse) {
        // 插入申请记录
        ElectiveApplyRecord applyRecord = new ElectiveApplyRecord();
        applyRecord.setCourseId(electiveCourse.getId());
        applyRecord.setTeacherId(electiveCourse.getTeacherId());
        applyRecordService.insertElectiveApplyRecord(applyRecord);
        electiveCourse.setStatus(ElectiveDict.COURSE_STATUS_APPLYING);
        return this.updateElectiveCourse(electiveCourse);
    }

    private List<ElectiveCoursePeople> getUpdate(List<ElectiveCoursePeople> peopleNew, List<ElectiveCoursePeople> peopleOld) {
        List<ElectiveCoursePeople> res = new ArrayList<>();
        for (ElectiveCoursePeople n : peopleNew) {
            for (ElectiveCoursePeople o : peopleOld) {
                if (o.getId().equals(n.getId()) && (!o.getGradeId().equals(n.getGradeId()) || !o.getInitNum().equals(n.getInitNum()))) {
                    // 旧的已选课人数进行传递
                    n.setSelectNum(o.getSelectNum());
                    res.add(n);
                    break;
                }
            }
        }
        return res;
    }

    private List<ElectiveCourseTime> getUpdateTime(List<ElectiveCourseTime> timeNew, List<ElectiveCourseTime> timeOld) {
        List<ElectiveCourseTime> res = new ArrayList<>();
        for (ElectiveCourseTime n : timeNew) {
            for (ElectiveCourseTime o : timeOld) {
                if (o.getId().equals(n.getId()) && (!o.getWeekId().equals(n.getWeekId()) || !o.getTimeId().equals(n.getTimeId()))) {
                    res.add(n);
                    break;
                }
            }
        }
        return res;
    }

    private List<ElectiveCoursePeople> getDel(List<ElectiveCoursePeople> peopleNew, List<ElectiveCoursePeople> peopleOld) {
        List<ElectiveCoursePeople> res = new ArrayList<>();
        for (ElectiveCoursePeople o : peopleOld) {
            boolean flag = false;
            for (ElectiveCoursePeople n : peopleNew) {
                if (o.getId().equals(n.getId())) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                res.add(o);
        }
        return res;
    }
    private List<ElectiveCourseTime> getDelTime(List<ElectiveCourseTime> timeNew, List<ElectiveCourseTime> timeOld) {
        List<ElectiveCourseTime> res = new ArrayList<>();
        for (ElectiveCourseTime o : timeOld) {
            boolean flag = false;
            for (ElectiveCourseTime n : timeNew) {
                if (o.getId().equals(n.getId())) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                res.add(o);
        }
        return res;
    }

    private List<ElectiveCoursePeople> getAdd(List<ElectiveCoursePeople> peopleNew, List<ElectiveCoursePeople> peopleOld) {
        List<ElectiveCoursePeople> res = new ArrayList<>();
        for (ElectiveCoursePeople n : peopleNew) {
            boolean flag = false;
            for (ElectiveCoursePeople o : peopleOld) {
                if (o.getId().equals(n.getId())) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                res.add(n);
        }
        return res;
    }

    private List<ElectiveCourseTime> getAddTime(List<ElectiveCourseTime> timeNew, List<ElectiveCourseTime> timeOld) {
        List<ElectiveCourseTime> res = new ArrayList<>();
        for (ElectiveCourseTime n : timeNew) {
            boolean flag = false;
            for (ElectiveCourseTime o : timeOld) {
                if (o.getId().equals(n.getId())) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                res.add(n);
        }
        return res;
    }

}
