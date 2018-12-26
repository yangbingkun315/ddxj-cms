package net.zn.ddxj.utils.wechat;

public class WXException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = -2538270991284096465L;
    
    private String message;
    
    private int ret;
    
    private int errorCode;
    
    public int getErrorCode()
	{
		return errorCode;
	}
	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}
	public int getRet()
	{
		return ret;
	}
	public void setRet(int ret)
	{
		this.ret = ret;
	}
	public WXException(int ret)
    {
        this.ret = ret;
    }
	public WXException(int ret,int errorCode)
	{
		this.ret = ret;
		this.errorCode = errorCode;
	}
    public WXException(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
    
}
