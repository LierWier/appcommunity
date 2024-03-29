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
public class AppEvaluation {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    @TableField(exist = false)
    private String userName;
    private Integer appId;
    @TableField(exist = false)
    private String appName;
    private Integer score;
    private Integer liked;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
    private Integer status;
}
