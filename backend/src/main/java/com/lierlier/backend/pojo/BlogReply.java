package com.lierlier.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogReply {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer blogId;
    private Integer replyId;
    private Integer userId;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String userPhoto;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
    private Integer status;
}