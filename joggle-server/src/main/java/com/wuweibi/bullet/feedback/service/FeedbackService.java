package com.wuweibi.bullet.feedback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.feedback.domain.FeedbackListVO;
import com.wuweibi.bullet.feedback.domain.FeedbackParam;
import com.wuweibi.bullet.feedback.entity.Feedback;
import java.util.List;

/**
 * 意见反馈(Feedback)表服务接口
 *
 * @author marker
 * @since 2022-04-01 17:32:37
 */
public interface FeedbackService   {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Feedback queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Feedback> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param feedback 实例对象
     * @return 实例对象
     */
    Feedback insert(Feedback feedback);

    /**
     * 修改数据
     *
     * @param feedback 实例对象
     * @return 实例对象
     */
    Feedback update(Feedback feedback);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);



    Page<FeedbackListVO> getAdminList(Page toMybatisPlusPage, FeedbackParam params);

}