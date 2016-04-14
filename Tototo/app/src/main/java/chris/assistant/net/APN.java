package chris.assistant.net;

//todo 删除无用代码
public enum APN
{

	UN_DETECT, WIFI, CMWAP, CMNET, UNIWAP, UNINET, WAP3G, NET3G, CTWAP, CTNET, UNKNOWN, UNKNOW_WAP, NO_NETWORK;

	public byte getIntValue()
	{
		switch (this)
		{
		case UN_DETECT:
			return 0;
		case WIFI:
			return 1;
		case CMWAP:
			return 2;
		case CMNET:
			return 3;
		case UNIWAP:
			return 4;
		case UNINET:
			return 5;
		case WAP3G:
			return 6;
		case NET3G:
			return 7;
		case CTWAP:
			return 8;
		case CTNET:
			return 9;
		case UNKNOWN:
			return 10;
		case UNKNOW_WAP:
			return 11;
		case NO_NETWORK:
			return 12;
		}
		return 10;
	}

}
