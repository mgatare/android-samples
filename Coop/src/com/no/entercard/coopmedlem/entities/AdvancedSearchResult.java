package com.no.entercard.coopmedlem.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class AdvancedSearchResult implements Parcelable {
	private String header;
	private String desc;

	public AdvancedSearchResult(Parcel in) {
		readFromParcel(in);
	}

	public AdvancedSearchResult() {
		super();
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(header);
		dest.writeString(desc);
	}

	private void readFromParcel(Parcel in) {
		header = in.readString();
		desc = in.readString();
	}

	public static final Parcelable.Creator<AdvancedSearchResult> CREATOR = new Parcelable.Creator<AdvancedSearchResult>() {
		@Override
		public AdvancedSearchResult createFromParcel(Parcel in) {
			return new AdvancedSearchResult(in);
		}

		@Override
		public AdvancedSearchResult[] newArray(int size) {
			return new AdvancedSearchResult[size];
		}
	};
}
