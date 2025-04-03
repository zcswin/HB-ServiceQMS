package com.ww.boengongye.entity;

/**
 * 缺陷图片类（记录DfAoiDefect类数据）
 */
public class Defectimages {
    /**
     * 缺陷ID
     */
    private String defectid;

    /**
     * 检测通道
     */
    private String channelkey;

    /**
     * 缺陷小图ID
     */
    private String imageid;

    /**
     * 图片base64
     */
    private String base64str;

    public String getDefectid() {
        return defectid;
    }

    public void setDefectid(String defectid) {
        this.defectid = defectid;
    }

    public String getChannelkey() {
        return channelkey;
    }

    public void setChannelkey(String channelkey) {
        this.channelkey = channelkey;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getBase64str() {
        return base64str;
    }

    public void setBase64str(String base64str) {
        this.base64str = base64str;
    }

    @Override
    public String toString() {
        return "Defectimages{" +
                "defectid='" + defectid + '\'' +
                ", channelkey='" + channelkey + '\'' +
                ", imageid='" + imageid + '\'' +
                ", base64str='" + base64str + '\'' +
                '}';
    }
}
