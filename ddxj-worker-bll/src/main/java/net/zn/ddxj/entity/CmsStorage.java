package net.zn.ddxj.entity;

import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-08-23
 */
public class CmsStorage {
    private Integer id;

    /**
     * 存储用户ID
     */
    private Integer storageUserId;

    /**
     * 存储KEY
     */
    private String storageKey;

    /**
     * 存储hash
     */
    private String storageHash;

    /**
     * 存储大小
     */
    private String storageSize;

    /**
     * 存储类型
     */
    private String storageType;

    /**
     * 存储时间
     */
    private Date storageTime;

    /**
     * 创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStorageUserId() {
        return storageUserId;
    }

    public void setStorageUserId(Integer storageUserId) {
        this.storageUserId = storageUserId;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey == null ? null : storageKey.trim();
    }

    public String getStorageHash() {
        return storageHash;
    }

    public void setStorageHash(String storageHash) {
        this.storageHash = storageHash == null ? null : storageHash.trim();
    }

    public String getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(String storageSize) {
        this.storageSize = storageSize == null ? null : storageSize.trim();
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType == null ? null : storageType.trim();
    }

    public Date getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}