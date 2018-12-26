package net.zn.ddxj.common.upload;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;


public class UploadUtils
{
    /**
     * 日期格式化对象
     */
    public static final DateFormat MONTH_FORMAT = new SimpleDateFormat("/yyyyMM/ddHHmmss");

    public static String generateFilename(String path, String ext)
    {
        return path + MONTH_FORMAT.format(new Date()) + RandomStringUtils.random(4, Num62.N36_CHARS) + "." + ext;
    }

    protected static final Pattern ILLEGAL_CURRENT_FOLDER_PATTERN = Pattern
            .compile("^[^/]|[^/]$|/\\.{1,2}|\\\\|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}");

    public static InputStream downloadFile(String urlString) throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(urlString);
		client.executeMethod(getMethod);
		return getMethod.getResponseBodyAsStream();
	}
    /**
     * Sanitizes a filename from certain chars.<br />
     * 
     * This method enforces the <code>forceSingleExtension</code> property and
     * then replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;, &gt;,
     * control chars by _ (underscore).
     * 
     * @param filename
     *            a potentially 'malicious' filename
     * @return sanitized filename
     */
    public static String sanitizeFileName(final String filename)
    {

        if (StringUtils.isBlank(filename))
            return filename;

        String name = forceSingleExtension(filename);

        // Remove \ / | : ? * " < > 'Control Chars' with _
        return name.replaceAll("\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
    }

    /**
     * Sanitizes a folder name from certain chars.<br />
     * 
     * This method replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;,
     * &gt;, control chars by _ (underscore).
     * 
     * @param folderName
     *            a potentially 'malicious' folder name
     * @return sanitized folder name
     */
    public static String sanitizeFolderName(final String folderName)
    {

        if (StringUtils.isBlank(folderName))
            return folderName;

        // Remove . \ / | : ? * " < > 'Control Chars' with _
        return folderName.replaceAll("\\.|\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
    }

    /**
     * Checks whether a path complies with the FCKeditor File Browser <a href=
     * "http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide/Server_Side_Integration#File_Browser_Requests"
     * target="_blank">rules</a>.
     * 
     * @param path
     *            a potentially 'malicious' path
     * @return <code>true</code> if path complies with the rules, else
     *         <code>false</code>
     */
    public static boolean isValidPath(final String path)
    {
        if (StringUtils.isBlank(path))
            return false;

        if (ILLEGAL_CURRENT_FOLDER_PATTERN.matcher(path).find())
            return false;

        return true;
    }

    /**
     * Replaces all dots in a filename with underscores except the last one.
     * 
     * @param filename
     *            filename to sanitize
     * @return string with a single dot only
     */
    public static String forceSingleExtension(final String filename)
    {
        return filename.replaceAll("\\.(?![^.]+$)", "_");
    }

    /**
     * Checks if a filename contains more than one dot.
     * 
     * @param filename
     *            filename to check
     * @return <code>true</code> if filename contains severals dots, else
     *         <code>false</code>
     */
    public static boolean isSingleExtension(final String filename)
    {
        return filename.matches("[^\\.]+\\.[^\\.]+");
    }

    /**
     * Checks a directory for existence and creates it if non-existent.
     * 
     * @param dir
     *            directory to check/create
     */
    public static void checkDirAndCreate(File dir)
    {
        if (!dir.exists())
            dir.mkdirs();
    }

    /**
     * Iterates over a base name and returns the first non-existent file.<br />
     * This method extracts a file's base name, iterates over it until the first
     * non-existent appearance with <code>basename(n).ext</code>. Where n is a
     * positive integer starting from one.
     * 
     * @param file
     *            base file
     * @return first non-existent file
     */
    public static File getUniqueFile(final File file)
    {
        if (!file.exists())
            return file;

        File tmpFile = new File(file.getAbsolutePath());
        File parentDir = tmpFile.getParentFile();
        int count = 1;
        String extension = FilenameUtils.getExtension(tmpFile.getName());
        String baseName = FilenameUtils.getBaseName(tmpFile.getName());
        do
        {
            tmpFile = new File(parentDir, baseName + "(" + count++ + ")." + extension);
        }
        while (tmpFile.exists());
        return tmpFile;
    }

    public static void main(String[] args)
    {
        System.out.println(generateFilename("/base", "gif"));
    }

    public static boolean isFlashType(String contentType)
    {
    	return "application/x-shockwave-flash".equals(contentType);
    }
    public static boolean isImageExtension(String extension)
    {
    	if(extension == null)
    	{
    		return false;
    	}
		if (".gif".equals(extension) || ".jpg".equals(extension) || ".png".equals(extension)
				|| ".bmp".equals(extension) || ".jpeg".equals(extension))
    	{
    		return true;
    	}
		return false;
    }
    public static boolean isImageType(String contentType)
    {
    	String[] types =
		{
				"image/pjpeg", "image/jpeg", "application/x-bmp", "image/bmp", "image/gif", "image/png", "image/x-png"
		};
		boolean isImage = false;
		for (String type : types) 
		{
			if(type.equals(contentType))
			{
				isImage = true;
				break;
			}
		}
		return isImage;
    }
    
    public static byte[] File2byte(File file)
	{
		byte[] buffer = null;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1)
			{
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return buffer;
	}

	public static void byte2File(byte[] buf, String filePath, String fileName)
	{
		byte2File(buf,filePath + File.separator + fileName);
	}
	public static void byte2File(byte[] buf, String filePath)
	{
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try
		{
			file = new File(filePath);
			
			checkDirAndCreate(file.getParentFile());
			
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (bos != null)
			{
				try
				{
					bos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	/**
	* 获取视频大小
	* @param source
	* @return
	*/
	private String getVideoSize(File source) {
		FileChannel fc = null;
		String size = "";
		try {
			@SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream(source);
			fc = fis.getChannel();
			BigDecimal fileSize = new BigDecimal(fc.size());
			size = fileSize.divide(new BigDecimal(1048576), 2,
					RoundingMode.HALF_UP) + "MB";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fc) {
				try {
					fc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return size;
	}
}
