package com.auction.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long totalCount;
	private int totalPages;
	private int page;
	private int count;
	private List<T> items = new ArrayList<T>();
	
	public Page() {
		super();
	}
	public Page(List<T> items) {
		super();
		this.totalCount = null!=items? items.size():0L;
		this.items = items;
	}
	public Page(Long totalCount, int totalPages, int page, int count, List<T> items) {
		super();
		this.totalCount = totalCount;
		this.totalPages = totalPages;
		this.page = page;
		this.count = count;
		this.items = items;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Page [totalCount=" + totalCount + ", totalPages=" + totalPages + ", page=" + page + ", count=" + count
				+ ", items=" + items + "]";
	}
}
