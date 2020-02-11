package com.ruoyi.project.elective.record.service;

import com.ruoyi.project.elective.record.domain.ElectiveApplyRecord;

import java.util.List;

/**
 * applyService接口
 *
 * @author Sunss
 * @date 2020-02-11
 */
public interface IElectiveApplyRecordService {
    /**
     * 查询apply
     *
     * @param id applyID
     * @return apply
     */
    public ElectiveApplyRecord selectElectiveApplyRecordById(Long id);

    /**
     * 查询apply列表
     *
     * @param electiveApplyRecord apply
     * @return apply集合
     */
    public List<ElectiveApplyRecord> selectElectiveApplyRecordList(ElectiveApplyRecord electiveApplyRecord);

    /**
     * 新增apply
     *
     * @param electiveApplyRecord apply
     * @return 结果
     */
    public int insertElectiveApplyRecord(ElectiveApplyRecord electiveApplyRecord);

    /**
     * 修改apply
     *
     * @param electiveApplyRecord apply
     * @return 结果
     */
    public int updateElectiveApplyRecord(ElectiveApplyRecord electiveApplyRecord);

    /**
     * 批量删除apply
     *
     * @param ids 需要删除的applyID
     * @return 结果
     */
    public int deleteElectiveApplyRecordByIds(Long[] ids);

    /**
     * 删除apply信息
     *
     * @param id applyID
     * @return 结果
     */
    public int deleteElectiveApplyRecordById(Long id);
}
