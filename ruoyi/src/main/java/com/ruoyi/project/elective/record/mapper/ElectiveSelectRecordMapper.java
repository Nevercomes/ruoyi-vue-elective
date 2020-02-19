package com.ruoyi.project.elective.record.mapper;

import com.ruoyi.project.elective.record.domain.ElectiveSelectStatistic;
import com.ruoyi.project.elective.record.domain.ElectiveSelectRecord;
import java.util.List;

/**
 * selectMapper接口
 * 
 * @author Sunss
 * @date 2020-02-11
 */
public interface ElectiveSelectRecordMapper 
{
    /**
     * 查询select
     * 
     * @param id selectID
     * @return select
     */
    public ElectiveSelectRecord selectElectiveSelectRecordById(Long id);

    /**
     * 查询select列表
     * 
     * @param electiveSelectRecord select
     * @return select集合
     */
    public List<ElectiveSelectRecord> selectElectiveSelectRecordList(ElectiveSelectRecord electiveSelectRecord);

    /**
     * 新增select
     * 
     * @param electiveSelectRecord select
     * @return 结果
     */
    public int insertElectiveSelectRecord(ElectiveSelectRecord electiveSelectRecord);

    /**
     * 修改select
     * 
     * @param electiveSelectRecord select
     * @return 结果
     */
    public int updateElectiveSelectRecord(ElectiveSelectRecord electiveSelectRecord);

    /**
     * 删除select
     * 
     * @param id selectID
     * @return 结果
     */
    public int deleteElectiveSelectRecordById(Long id);

    /**
     * 批量删除select
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveSelectRecordByIds(Long[] ids);

    /**
     * 根据学生查询可选课程
     * @param electiveSelectRecord
     * @return
     */
    List<ElectiveSelectRecord> listCanSelect(ElectiveSelectRecord electiveSelectRecord);

    /**
     * 统计选课结果
     * @param electiveSelectStatistic
     * @return
     */
    List<ElectiveSelectStatistic> listStatistic(ElectiveSelectStatistic electiveSelectStatistic);
}
