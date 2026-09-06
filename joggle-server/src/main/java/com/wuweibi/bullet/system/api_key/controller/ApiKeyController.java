package com.wuweibi.bullet.system.api_key.controller;

import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.system.api_key.domain.dto.ApiKeyCreateDTO;
import com.wuweibi.bullet.system.api_key.domain.dto.ApiKeyVO;
import com.wuweibi.bullet.system.api_key.entity.UserApiKey;
import com.wuweibi.bullet.system.api_key.service.UserApiKeyService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "API Key 管理")
@WebApi
@RestController
@RequestMapping("/api/user/api-key")
public class ApiKeyController {

    @Resource
    private UserApiKeyService userApiKeyService;

    private static final int MAX_API_KEY_COUNT = 100;

    @Operation(summary = "创建 API Key")
    @PostMapping
    public R<ApiKeyVO> create(@JwtUser Session session, @RequestBody @Valid ApiKeyCreateDTO dto) {
        long count = userApiKeyService.lambdaQuery()
                .eq(UserApiKey::getUserId, session.getUserId())
                .count();
        if (count >= MAX_API_KEY_COUNT) {
            return R.fail("最多只能创建 " + MAX_API_KEY_COUNT + " 个 API Key");
        }
        UserApiKey key = userApiKeyService.generateKey(session.getUserId(), dto.getName());
        ApiKeyVO vo = toVo(key);
        vo.setApiKey(key.getApiKey());
        return R.ok(vo);
    }

    @Operation(summary = "我的 API Key 列表")
    @GetMapping
    public R<List<ApiKeyVO>> list(@JwtUser Session session) {
        List<UserApiKey> list = userApiKeyService.lambdaQuery()
                .eq(UserApiKey::getUserId, session.getUserId())
                .orderByDesc(UserApiKey::getCreateTime)
                .list();
        List<ApiKeyVO> voList = list.stream().map(this::toVo).collect(Collectors.toList());
        return R.ok(voList);
    }

    @Operation(summary = "删除 API Key")
    @DeleteMapping("/{id}")
    public R<Void> delete(@JwtUser Session session, @PathVariable Long id) {
        UserApiKey key = userApiKeyService.getById(id);
        if (key == null || !key.getUserId().equals(session.getUserId())) {
            return R.fail("API Key 不存在");
        }
        userApiKeyService.removeById(id);
        return R.success();
    }

    private ApiKeyVO toVo(UserApiKey key) {
        ApiKeyVO vo = new ApiKeyVO();
        vo.setId(key.getId());
        vo.setName(key.getName());
        vo.setApiKey(key.getApiKey());
        vo.setEnabled(key.getEnabled());
        vo.setLastUsedAt(key.getLastUsedAt());
        vo.setCreateTime(key.getCreateTime());
        return vo;
    }
}
