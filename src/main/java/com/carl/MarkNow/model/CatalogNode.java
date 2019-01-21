package com.carl.MarkNow.model;

public	class CatalogNode {	
	private int level;
	private int lineNO;
	private String title;
	
	public CatalogNode(int lv, int lN, String t) {
		level = lv;
		lineNO = lN;
		title = t;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getLineNO() {
		return lineNO;
	}

	public String getTitle() {
		return title;
	}
	@Override
	public String toString() {
		return title;
	}
}

