package com.hoot.proto;

import org.apache.http.HttpStatus;

public class ResultCode {
	public static final int Code_Ok = HttpStatus.SC_OK;
	public static final int Not_Found = HttpStatus.SC_NOT_FOUND;
	
	public static final int Code_Network_Unavaiable = -800; 	// ��ǰû������
	public static final int Code_Canceled = -801; 				// ����cancel
	public static final int Code_Request_ParamErr = -802; 		// �����������
	public static final int Code_Received_Html = -803; 			// ���ٳ�

	// ---- Http�쳣
	public static final int Code_Http_MalformedURLErr = -820; 	// url����
	public static final int Code_Http_Client_ProtocolErr = -821; // httpЭ�����
	public static final int Code_Http_Connect_TimeOut = -822; 	// ���ӳ�ʱ
	public static final int Code_Http_Socket_Timeout = -823; 	// socket��ʱ
	public static final int Code_Http_ConnectErr = -824; 		// �����쳣
	public static final int Code_Http_SocketErr = -825; 		// socket�쳣
	public static final int Code_Http_IOErr = -826; 			// IO�쳣
	public static final int Code_Http_Err = -827; 				// ����Http�쳣
	
	public static final int Code_Http_EntityNull = -840; 		// �ذ�����Ϊ��
	public static final int Code_Http_ResponseNull = -841; 		// û�еõ�Response
	
	// -- json����쳣
	public static final int Code_Json_Body = -862; 			// Body json ���ݰ�����

	public static final int Code_UnzipErr = -871; 				// ��ѹʧ��
}
