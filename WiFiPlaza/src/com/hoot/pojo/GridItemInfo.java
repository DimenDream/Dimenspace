package com.hoot.pojo;

public class GridItemInfo {
	public String iconUrl;
	public String itemName;
	public int actionType;
	public String actionUrl;// ÊÂ¼þurl
	public int imageName;

	public static final class ActionType {
		public static final int URL = 0;
		public static final int ACTIVITY = 1;
		public static final int ADS_GAME = 8;
		public static final int ADS_APP = 9;
		public static final int ADS_ALL = 10;
	}

	@Override
	public String toString() {
		return "GridItemInfo [iconUrl=" + iconUrl +",imageName" + imageName + ", itemName=" + itemName
				+ ", actionType=" + actionType + ", actionUrl=" + actionUrl
				+ "]";
	}
	
}
