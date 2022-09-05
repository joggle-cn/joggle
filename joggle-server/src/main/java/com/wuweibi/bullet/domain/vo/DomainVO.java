package com.wuweibi.bullet.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DomainVO {

 private Long    id;
 private String domain;
 private String domainFull;
 private Integer type;
 private Integer status;
 private Date dueDateTime;
 private String dueTime;
}
