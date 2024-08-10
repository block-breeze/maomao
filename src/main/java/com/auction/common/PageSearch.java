package com.auction.common;

import java.io.Serializable;

public class PageSearch implements Serializable{

	private static final long serialVersionUID = 1L;
	int start;
	int count;
	String sortname;
	String sortorder;
	
	public PageSearch() {
	}

	public PageSearch(int start, int count, String sortname, String sortorder) {
		super();
		this.start = start;
		this.count = count;
		this.sortname = sortname;
		this.sortorder = sortorder;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}
}
