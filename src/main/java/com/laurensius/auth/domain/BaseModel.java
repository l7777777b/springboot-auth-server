package com.laurensius.auth.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@MappedSuperclass
public class BaseModel {
	private long id;
    private String createBy;
    
	@JsonSerialize(using=com.laurensius.auth.util.CustomDateSerializer.class)
	@JsonDeserialize(using=com.laurensius.auth.util.CustomDateDeserializer.class)
    private Date createTime;
	
    private String updateBy;
    
	@JsonSerialize(using=com.laurensius.auth.util.CustomDateSerializer.class)
	@JsonDeserialize(using=com.laurensius.auth.util.CustomDateDeserializer.class)
    private Date updateTime;
	
    private boolean deleted = false;
    
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
		return id;
	}
    
    public void setId(long id) {
		this.id = id;
	}
    
	@Column(name="deleted")
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Column(name="createtime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="updatetime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name="createby")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	@Column(name="updateby")
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	
}
