package net.zn.ddxj.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import net.zn.ddxj.utils.CmsUtils;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-04-23
 */
public class ProblemLib implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 194505198911862999L;

	private Integer id;

    /**
     * 问题标题
     */
    private String problemTitle;

    /**
     * 问题内容
     */
    private  byte[] problemContent;

    /**
     * 问题难度(最大到5颗星)
     */
    private Integer problemDifficulty;

    /**
     * 作者
     */
    private String author;

    private Date createTime;

    private Date updateTime;

    private String flag;
    
    private String infoContent;
    
    public String getInfoContent() {
    	if(!CmsUtils.isNullOrEmpty(problemContent) && problemContent.length > 0)
    	{
    		try {
    			infoContent = new String(problemContent,"UTF-8");
			} 
    		catch (UnsupportedEncodingException e) 
    		{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public byte[] getProblemContent() {
		return problemContent;
	}

	public void setProblemContent(byte[] problemContent) {
		this.problemContent = problemContent;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle == null ? null : problemTitle.trim();
    }

    public Integer getProblemDifficulty() {
        return problemDifficulty;
    }

    public void setProblemDifficulty(Integer problemDifficulty) {
        this.problemDifficulty = problemDifficulty;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }
}