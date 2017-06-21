package com.dvsnier.monitor.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * the abstrct pma model bean
 *
 * @author dovsnier
 * @version 1.0.0
 * @since JDK 1.7
 */
public class AbstrctPMABean extends AbstractBaseBean implements Parcelable {

    /* the current element bean channel code */
    protected long channel;
    /* the current element bean that is describe system code version */
    protected String platform;
    /* the current element bean that is describe logical code version */
    protected String modules;
    /* the current element bean that is describe action type: message,event,state,control,command and so on */
    protected String action;
    /* the current element bean that is hold submitted to the body object */
    protected Parcelable content;
    /* the current element bean container that maybe has sub element node object*/
    protected AbstractBaseBean[] node;

    public long getChannel() {
        return channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Parcelable getContent() {
        return content;
    }

    public void setContent(Parcelable content) {
        this.content = content;
    }

    public AbstractBaseBean[] getNode() {
        return node;
    }

    public void setNode(AbstractBaseBean[] node) {
        this.node = node;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.channel);
        dest.writeString(this.platform);
        dest.writeString(this.modules);
        dest.writeString(this.action);
        dest.writeParcelable(this.content, flags);
        dest.writeTypedArray(this.node, flags);
    }

    public AbstrctPMABean() {
    }

    protected AbstrctPMABean(Parcel in) {
        super(in);
        this.channel = in.readLong();
        this.platform = in.readString();
        this.modules = in.readString();
        this.action = in.readString();
        this.content = in.readParcelable(Parcelable.class.getClassLoader());
        this.node = in.createTypedArray(AbstractBaseBean.CREATOR);
    }

    public static final Creator<AbstrctPMABean> CREATOR = new Creator<AbstrctPMABean>() {
        @Override
        public AbstrctPMABean createFromParcel(Parcel source) {
            return new AbstrctPMABean(source);
        }

        @Override
        public AbstrctPMABean[] newArray(int size) {
            return new AbstrctPMABean[size];
        }
    };
}
