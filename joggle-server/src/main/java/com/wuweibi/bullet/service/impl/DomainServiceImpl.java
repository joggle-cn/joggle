package com.wuweibi.bullet.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.vo.DomainVO;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.Domain;
import com.wuweibi.bullet.mapper.DeviceMappingMapper;
import com.wuweibi.bullet.mapper.DomainMapper;
import com.wuweibi.bullet.protocol.MsgUnMapping;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 域名与端口服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {


    @Resource
    private CoonPool coonPool;


    @Override
    public List<DomainVO> getListByUserId(Long userId) {
        return this.baseMapper.selectByUserId(userId);
    }

    @Override
    public boolean checkDomain(Long userId, Long domainId) {
        return this.baseMapper.existDomainUserId(userId, domainId);
    }

    @Override
    public List<JSONObject> getListNotBindByUserId(Long userId) {
        return this.baseMapper.selectListNotBindByUserId(userId);
    }

    @Override
    public void updateDueTime(Long domainId, Long dueTime) {
        this.baseMapper.updateDueTime(domainId, new Date(dueTime));
    }

    @Resource
    private DeviceMappingService deviceMappingService;
    @Resource
    private DeviceMappingMapper deviceMappingMapper;


    @Override
    public void checkStatus() {
        // 查询过期的域名
        List<JSONObject> list = this.baseMapper.selectDueDomain();
        if (list.size() == 0) {
            return;
        }
        log.debug("check domain status ....");
        // 强行停止ngrok客户端线程
        Iterator<JSONObject> it = list.iterator();
        while (it.hasNext()) {
            JSONObject item = it.next();
            Long mappingId = item.getLong("mappingId");
            String deviceNo = item.getString("deviceNo");

            DeviceMapping entity = deviceMappingService.getById(mappingId);
            BulletAnnotation annotation = coonPool.getByDeviceNo(deviceNo);
            if (annotation != null) {
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgUnMapping msg = new MsgUnMapping(data.toJSONString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    msg.write(outputStream);
                    // 包装了Bullet协议的
                    byte[] resultBytes = outputStream.toByteArray();
                    ByteBuffer buf = ByteBuffer.wrap(resultBytes);
                    annotation.getSession().getBasicRemote().sendBinary(buf);
                } catch (IOException e) {
                    log.error("", e);
                } finally {
                    IOUtils.closeQuietly(outputStream);
                }
            }
            // 更新Mapping状态
            deviceMappingMapper.updateStatusById(mappingId, 0);


        }

    }
}
