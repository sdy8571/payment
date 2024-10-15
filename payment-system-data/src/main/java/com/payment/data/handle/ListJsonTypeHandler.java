package com.payment.data.handle;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @author sdy
 * @description
 * @date 2024/10/15
 */
@Slf4j
@MappedTypes({Object.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListJsonTypeHandler extends AbstractJsonTypeHandler<List<Long>> {

    @Override
    protected List<Long> parse(String json) {
        return JSONUtil.parseArray(json).toList(Long.class);
    }

    @Override
    protected String toJson(List<Long> obj) {
        return JSONUtil.toJsonStr(obj);
    }

}
