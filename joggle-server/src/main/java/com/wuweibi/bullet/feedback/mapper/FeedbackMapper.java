package com.wuweibi.bullet.feedback.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.feedback.domain.FeedbackListVO;
import com.wuweibi.bullet.feedback.domain.FeedbackParam;
import com.wuweibi.bullet.feedback.entity.Feedback;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 意见反馈(Feedback)表数据库访问层
 *
 * @author marker
 * @since 2022-04-01 17:32:35
 */
public interface FeedbackMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Feedback queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Feedback> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param feedback 实例对象
     * @return 对象列表
     */
    List<Feedback> queryAll(Feedback feedback);

    /**
     * 新增数据
     *
     * @param feedback 实例对象
     * @return 影响行数
     */
    int insert(Feedback feedback);

    /**
     * 修改数据
     *
     * @param feedback 实例对象
     * @return 影响行数
     */
    int update(Feedback feedback);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    /**
     * 分页查询意见反馈
     * @param pageInfo
     * @param params
     * @return
     */
    Page<FeedbackListVO> selectAdminList(Page pageInfo,@Param("params") FeedbackParam params);
}