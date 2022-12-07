package com.lierlier.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lierlier.backend.pojo.App;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppMapper extends BaseMapper<App> {
}
