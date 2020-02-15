package com.ruoyi.project.elective.course.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.elective.course.domain.ElectiveCoursePeople;
import com.ruoyi.project.elective.course.service.IElectiveCoursePeopleService;
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

    /**
     * 查询课程
     *
     * @param id 课程ID
     * @return 课程
     */
    @Override
    public ElectiveCourse selectElectiveCourseById(Long id) {
        return electiveCourseMapper.selectElectiveCourseById(id);
    }

    /**
     * 查询课程列表
     *
     * @param electiveCourse 课程
     * @return 课程
     */
    @Override
    public List<ElectiveCourse> selectElectiveCourseList(ElectiveCourse electiveCourse) {
        return electiveCourseMapper.selectElectiveCourseList(electiveCourse);
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
        // 获取新增的
        List<ElectiveCoursePeople> add = getAdd(peopleNew, peopleOld);
        for (ElectiveCoursePeople p : add) {
            p.setCourseId(electiveCourse.getId());
            electiveCoursePeopleService.insertElectiveCoursePeople(p);
        }
        // 获取删除的
        List<ElectiveCoursePeople> del = getDel(peopleNew, peopleOld);
        for (ElectiveCoursePeople p : del) {
            electiveCoursePeopleService.deleteElectiveCoursePeopleById(p.getId());
        }
        // 获取修改的
        List<ElectiveCoursePeople> update = getUpdate(peopleNew, peopleOld);
        for (ElectiveCoursePeople p : update) {
            electiveCoursePeopleService.updateElectiveCoursePeople(p);
        }
        return res;
    }

    /**
     * 批量删除课程
     *
     * @param ids 需要删除的课程ID
     * @return 结果
     */
    @Override
    public int deleteElectiveCourseByIds(Long[] ids) {
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

    private List<ElectiveCoursePeople> getUpdate(List<ElectiveCoursePeople> peopleNew, List<ElectiveCoursePeople> peopleOld) {
        List<ElectiveCoursePeople> res = new ArrayList<>();
        for (ElectiveCoursePeople n : peopleNew) {
            for (ElectiveCoursePeople o : peopleOld) {
                if (o.getId().equals(n.getId()) && (!o.getGradeId().equals(n.getGradeId()) || !o.getInitNum().equals(n.getInitNum()))) {
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

}
