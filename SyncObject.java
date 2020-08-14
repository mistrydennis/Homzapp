package com.example.mbg;

import java.io.Serializable;

public class SyncObject implements Serializable {
private String tag;
private byte[] bytes;
public SyncObject(String string, byte[] serialize) {
	// TODO Auto-generated constructor stub
	this.tag=string;
	bytes = serialize;
}
public String getTag() {
	return tag;
}
public void setTag(String tag) {
	this.tag = tag;
}
public byte[] getBytes() {
	return bytes;
}
public void setBytes(byte[] bytes) {
	this.bytes = bytes;
}
}
