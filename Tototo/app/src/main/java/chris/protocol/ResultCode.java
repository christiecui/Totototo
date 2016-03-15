package chris.protocol;

public final class ResultCode {
	public static final int Code_OK = 0; 						// 正常结束，httpCode 200

	public static final int Code_Network_Unavaiable = -800; 	// 当前没有网络
	public static final int Code_Canceled = -801; 				// 请求被cancel
	public static final int Code_Request_ParamErr = -802; 		// 请求参数错误
	public static final int Code_Received_Html = -803; 			// 被劫持

	// ---- Http异常
	public static final int Code_Http_MalformedURLErr = -820; 	// url错误
	public static final int Code_Http_Client_ProtocolErr = -821; // http协议错误
	public static final int Code_Http_Connect_TimeOut = -822; 	// 连接超时
	public static final int Code_Http_Socket_Timeout = -823; 	// socket超时
	public static final int Code_Http_ConnectErr = -824; 		// 连接异常
	public static final int Code_Http_SocketErr = -825; 		// socket异常
	public static final int Code_Http_IOErr = -826; 			// IO异常
	public static final int Code_Http_Err = -827; 				// 其他Http异常

	public static final int Code_Http_EntityNull = -840; 		// 回包包体为空
	public static final int Code_Http_ResponseNull = -841; 		// 没有得到Response

	// -- JCE解包异常
	public static final int Code_JceErr_Response = -861; 		// Response jce 数据包错误
	public static final int Code_JceErr_Body = -862; 			// Body jce 数据包错误

	public static final int Code_UnzipErr = -871; 				// 解压失败
	public static final int Code_DecryptErr = -872; 			// 解密失败
}
