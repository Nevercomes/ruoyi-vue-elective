package com.ruoyi.project.elective.record.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.record.mapper.ElectiveApplyRecordMapper;
import com.ruoyi.project.elective.record.domain.ElectiveApplyRecord;
import com.ruoyi.project.elective.record.service.IElectiveApplyRecordService;

/**
 * applyService业务层处理
 *
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveApplyRecordServiceImpl implements IElectiveApplyRecordService {
    @Autowired
    private ElectiveApplyRecordMapper electiveApplyRecordMapper;

    /**
     * 查询apply
     *
     * @param id applyID
     * @return apply
     */
    @Override
    public ElectiveApplyRecord selectElectiveApplyRecordById(Long id) {
        return electiveApplyRecordMapper.selectElectiveApplyRecordById(id);
    }

    /**
     * 查询apply列表
     *
     * @param electiveApplyRecord apply
     * @return apply
     */
    @Override
    public List<ElectiveApplyRecord> selectElectiveApplyRecordList(ElectiveApplyRecord electiveApplyRecord) {
        return electiveApplyRecordMapper.selectElectiveApplyRecordList(electiveApplyRecord);
    }

    /**
     * 新增apply
     *
     * @param electiveApplyRecord apply
     * @return 结果
     */
    @Override
    public int insertElectiveApplyRecord(ElectiveApplyRecord electiveApplyRecord) {
        electiveApplyRecord.setCreateTime(DateUtils.getNowDate());
        return electiveApplyRecordMapper.insertElectiveApplyRecord(electiveApplyRecord);
    }

    /**
     * 修改apply
     *
     * @param electiveApplyRecord apply
     * @return 结果
     */
    @Override
    public int updateElectiveApplyRecord(ElectiveApplyRecord electiveApplyRecord) {
        electiveApplyRecord.setUpdateTime(DateUtils.getNowDate());
        return electiveApplyRecordMapper.updateElectiveApplyRecord(electiveApplyRecord);
    }

    /**
     * 批量删除apply
     *
     * @param ids 需要删除的applyID
     * @return 结果
     */
    @Override
    public int deleteElectiveApplyRecordByIds(Long[] ids) {
        return electiveApplyRecordMapper.deleteElectiveApplyRecordByIds(ids);
    }

    /**
     * 删除apply信息
     *
     * @param id applyID
     * @return 结果
     */
    @Override
    public int deleteElectiveApplyRecordById(Long id) {
        return electiveApplyRecordMapper.deleteElectiveApplyRecordById(id);
    }
}
