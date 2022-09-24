package com.wuweibi.bullet.domain2.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DomainReleaseVO {

 private Long    id;
 private Long userId;
 private String domain;
 private String userEmail;
 private String domainFull;
 private Integer type;
 private Integer status;
 private Date dueDateTime;
 private Date dueTime;
}
