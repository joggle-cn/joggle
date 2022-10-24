package com.wuweibi.bullet.feedback.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.feedback.domain.FeedbackListVO;
import com.wuweibi.bullet.feedback.domain.FeedbackParam;
import com.wuweibi.bullet.feedback.entity.Feedback;
import com.wuweibi.bullet.feedback.mapper.FeedbackMapper;
import com.wuweibi.bullet.feedback.service.FeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 意见反馈(Feedback)表服务实现类
 *
 * @author marker
 * @since 2022-04-01 17:32:37
 */
@Service("feedbackService")
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Feedback queryById(Integer id) {
        return this.feedbackMapper.selectById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Feedback> queryAllByLimit(int offset, int limit) {
        return this.feedbackMapper.queryAllByLimit(offset, limit);
    }


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.feedbackMapper.deleteById(id) > 0;
    }

    @Override
    public Page<FeedbackListVO> getAdminList(Page pageInfo, FeedbackParam params) {
        return this.feedbackMapper.selectAdminList(pageInfo, params);
    }
}