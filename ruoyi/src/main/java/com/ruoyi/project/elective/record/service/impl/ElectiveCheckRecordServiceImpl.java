package com.ruoyi.project.elective.record.service.impl;

import com.ruoyi.common.constant.ElectiveDict;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.elective.course.domain.ElectiveCourse;
import com.ruoyi.project.elective.course.service.IElectiveCourseService;
import com.ruoyi.project.elective.record.domain.ElectiveApplyRecord;
import com.ruoyi.project.elective.record.domain.ElectiveCheckRecord;
import com.ruoyi.project.elective.record.domain.ElectiveSelectRecord;
import com.ruoyi.project.elective.record.mapper.ElectiveCheckRecordMapper;
import com.ruoyi.project.elective.record.service.IElectiveApplyRecordService;
import com.ruoyi.project.elective.record.service.IElectiveCheckRecordService;
import com.ruoyi.project.elective.record.service.IElectiveSelectRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * checkService业务层处理
 *
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveCheckRecordServiceImpl implements IElectiveCheckRecordService {
    @Autowired
    private ElectiveCheckRecordMapper electiveCheckRecordMapper;
    @Autowired
    private IElectiveCourseService courseService;
    @Autowired
    private IElectiveApplyRecordService applyRecordService;
    @Autowired
    private IElectiveSelectRecordService selectRecordService;

    /**
     * 查询check
     *
     * @param id checkID
     * @return check
     */
    @Override
    public ElectiveCheckRecord selectElectiveCheckRecordById(Long id) {
        return electiveCheckRecordMapper.selectElectiveCheckRecordById(id);
    }

    /**
     * 查询check列表
     *
     * @param electiveCheckRecord check
     * @return check
     */
    @Override
    public List<ElectiveCheckRecord> selectElectiveCheckRecordList(ElectiveCheckRecord electiveCheckRecord) {
        return electiveCheckRecordMapper.selectElectiveCheckRecordList(electiveCheckRecord);
    }

    /**
     * 新增check
     *
     * @param electiveCheckRecord check
     * @return 结果
     */
    @Override
    @Transactional
    public int insertElectiveCheckRecord(ElectiveCheckRecord electiveCheckRecord) {
        // 更新课程和课程状态
        ElectiveCourse course = electiveCheckRecord.getCourse();
        course.setStatus(electiveCheckRecord.getResult());
        courseService.updateElectiveCourse(course);
        // 如果状态为退回则删除所有的已选课
        if (ElectiveDict.COURSE_STATUS_DENY.equals(electiveCheckRecord.getResult())) {
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
        // 更新申请状态
        ElectiveApplyRecord applyRecord = new ElectiveApplyRecord();
        applyRecord.setId(electiveCheckRecord.getApplyId());
        applyRecord.setStatus(electiveCheckRecord.getResult());
        applyRecordService.updateElectiveApplyRecord(applyRecord);
        // 插入审核记录
        electiveCheckRecord.preInsert();
        return electiveCheckRecordMapper.insertElectiveCheckRecord(electiveCheckRecord);
    }

    /**
     * 修改check
     *
     * @param electiveCheckRecord check
     * @return 结果
     */
    @Override
    public int updateElectiveCheckRecord(ElectiveCheckRecord electiveCheckRecord) {
        electiveCheckRecord.setUpdateTime(DateUtils.getNowDate());
        return electiveCheckRecordMapper.updateElectiveCheckRecord(electiveCheckRecord);
    }

    /**
     * 批量删除check
     *
     * @param ids 需要删除的checkID
     * @return 结果
     */
    @Override
    public int deleteElectiveCheckRecordByIds(Long[] ids) {
        return electiveCheckRecordMapper.deleteElectiveCheckRecordByIds(ids);
    }

    /**
     * 删除check信息
     *
     * @param id checkID
     * @return 结果
     */
    @Override
    public int deleteElectiveCheckRecordById(Long id) {
        return electiveCheckRecordMapper.deleteElectiveCheckRecordById(id);
    }
}
