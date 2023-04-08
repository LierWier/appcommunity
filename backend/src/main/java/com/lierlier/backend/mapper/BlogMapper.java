package com.lierlier.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lierlier.backend.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
