package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信自定义菜单
 * 
 * @author wcyong
 * 
 * @date 2018-06-22
 */
public class WechatMenu implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2429133562310837924L;

	/**
     * 主键
     */
    private Integer id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 执行事件类型 -1无触发 1-自定义链接 2-回复图片 3-回复文字
     */
    private Integer clickAction;

    /**
     * 排序
     */
    private Integer indexNumber;

    /**
     * 菜单链接
     */
    private String menuUrl;

    /**
     * 菜单key
     */
    private String menuKey;

    /**
     * 父级菜单
     */
    private Integer parentId;

    /**
     * 回复文字
     */
    private String replyText;

    /**
     * 回复图片
     */
    private String replyImage;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否有效
     */
    private Integer flag;
    
    private WechatMenu parentMenu; //上级菜单
    
	public WechatMenu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(WechatMenu parentMenu) {
		this.parentMenu = parentMenu;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public Integer getClickAction() {
        return clickAction;
    }

    public void setClickAction(Integer clickAction) {
        this.clickAction = clickAction;
    }

    public Integer getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(Integer indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey == null ? null : menuKey.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText == null ? null : replyText.trim();
    }

    public String getReplyImage() {
        return replyImage;
    }

    public void setReplyImage(String replyImage) {
        this.replyImage = replyImage == null ? null : replyImage.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}