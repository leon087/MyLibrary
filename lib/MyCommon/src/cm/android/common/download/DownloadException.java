package cm.android.common.download;

public class DownloadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6595770718569581057L;

	/**
	 * 异常信息:HttpURLConnection
	 */
	public static final String HTTP_URL_CONNECTION = "exception_httpurlconnection";

	/**
	 * 异常信息:下载文件不存在
	 */
	public static final String FILE_NOT_FOUND = "exception_file_notexists";

	/**
	 * 异常信息:网络异常
	 */
	public static final String NETWORK_ERROR = "exception_network_error";

	/**
	 * 异常信息:空间不足
	 */
	public static final String SDCARD_NOSPACE = "exception_sdcard_nospace";

	/**
	 * 异常信息
	 */
	public static final String URL_INVALID = "url_invalid";

	/**
	 * 异常信息
	 */
	public static final String REQUEST_TIMEOUT = "error_request_timeout";

	/**
	 * 下载地址失效
	 */
	public static final String DOWNLOAD_URL_INVALID = "error_download_invalid";
	public static final String MD5 = "ERROR_MD5";

	public static final String CODE_EXCEPTION = "ERROR_CODE_EXCEPTION";
	public static final String OTHER = "ERROR_OTHER";
	public static final String FILE_SIZE_ERROR = "FILE_SIZE_ERROR";

	public DownloadException(String message) {
		super(message);
	}

	public DownloadException(String message, Throwable cause) {
		super(message, cause);
	}

}
