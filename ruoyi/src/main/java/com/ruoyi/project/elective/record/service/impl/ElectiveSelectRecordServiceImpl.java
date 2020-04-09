package com.ruoyi.project.elective.record.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.elective.course.mapper.ElectiveCoursePeopleMapper;
import com.ruoyi.project.elective.record.domain.ElectiveSelectStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.record.mapper.ElectiveSelectRecordMapper;
import com.ruoyi.project.elective.record.domain.ElectiveSelectRecord;
import com.ruoyi.project.elective.record.service.IElectiveSelectRecordService;
import org.springframework.transaction.annotation.Transactional;

/**
 * selectService业务层处理
 *
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveSelectRecordServiceImpl implements IElectiveSelectRecordService {
    @Autowired
    private ElectiveSelectRecordMapper electiveSelectRecordMapper;
    @Autowired
    private ElectiveCoursePeopleMapper coursePeopleMapper;

    /**
     * 查询select
     *
     * @param id selectID
     * @return select
     */
    @Override
    public ElectiveSelectRecord selectElectiveSelectRecordById(Long id) {
        return electiveSelectRecordMapper.selectElectiveSelectRecordById(id);
    }

    /**
     * 查询select列表
     *
     * @param electiveSelectRecord select
     * @return select
     */
    @Override
    public List<ElectiveSelectRecord> selectElectiveSelectRecordList(ElectiveSelectRecord electiveSelectRecord) {
        return electiveSelectRecordMapper.selectElectiveSelectRecordList(electiveSelectRecord);
    }

    /**
     * 新增select
     *
     * @param electiveSelectRecord select
     * @return 结果
     */
    @Override
    @Transactional
    public int insertElectiveSelectRecord(ElectiveSelectRecord electiveSelectRecord) {
        electiveSelectRecord.preInsert();
        int res = electiveSelectRecordMapper.insertElectiveSelectRecord(electiveSelectRecord);
        // 插入记录成功 则设置select数+1
//        coursePeopleMapper.addOneSelectNum(electiveSelectRecord);
        return res;
    }

    /**
     * 修改select
     *
     * @param electiveSelectRecord select
     * @return 结果
     */
    @Override
    public int updateElectiveSelectRecord(ElectiveSelectRecord electiveSelectRecord) {
        electiveSelectRecord.setUpdateTime(DateUtils.getNowDate());
        return electiveSelectRecordMapper.updateElectiveSelectRecord(electiveSelectRecord);
    }

    /**
     * 批量删除select
     *
     * @param ids 需要删除的selectID
     * @return 结果
     */
    @Override
    public int deleteElectiveSelectRecordByIds(Long[] ids) {
        return electiveSelectRecordMapper.deleteElectiveSelectRecordByIds(ids);
    }

    /**
     * 删除select信息
     *
     * @param id selectID
     * @return 结果
     */
    @Override
    public int deleteElectiveSelectRecordById(Long id) {
        return electiveSelectRecordMapper.deleteElectiveSelectRecordById(id);
    }

    /**
     * 查询学生可选的课程
     * @param electiveSelectRecord
     * @return
     */
    @Override
    public List<ElectiveSelectRecord> listCanSelect(ElectiveSelectRecord electiveSelectRecord) {
        List<ElectiveSelectRecord> list = electiveSelectRecordMapper.listCanSelect(electiveSelectRecord);
        return list;
    }

    /**
     * 统计选课结果
     * @param electiveSelectStatistic
     * @return
     */
    @Override
    public List<ElectiveSelectStatistic> listStatistic(ElectiveSelectStatistic electiveSelectStatistic) {
        return electiveSelectRecordMapper.listStatistic(electiveSelectStatistic);
    }

    @Override
    public List<ElectiveSelectRecord> checkGrade(ElectiveSelectRecord electiveSelectRecord) {
        return electiveSelectRecordMapper.checkGrade(electiveSelectRecord);
    }
}
