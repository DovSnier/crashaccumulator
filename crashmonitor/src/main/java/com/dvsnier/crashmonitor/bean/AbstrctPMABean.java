package com.dvsnier.crashmonitor.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * the abstrct pma model bean
 *
 * @author dovsnier
 * @version 1.0.0
 * @date 2015/12/15.
 * @since JDK 1.7
 */
public class AbstrctPMABean extends AbstractBaseBean implements Parcelable {

    /* the current element bean channel code */
    private long channel;
    /* the current element bean that is describe system code version */
    private String platform;
    /* the current element bean that is describe logical code version */
    private String modules;
    /* the current element bean that is describe action type: message,event,state,control,command and so on */
    private String action;
    /* the current element bean that is hold submitted to the body object */
    private Parcelable content;
    /* the current element bean container that maybe has sub element node object*/
    private AbstractBaseBean[] node;


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.channel);
        dest.writeString(this.platform);
        dest.writeString(this.modules);
        dest.writeString(this.action);
        dest.writeParcelable(this.content, 0);
        dest.writeParcelableArray(this.node, 0);
    }

    public AbstrctPMABean() {}

    protected AbstrctPMABean(Parcel in) {
        super(in);
        this.channel = in.readLong();
        this.platform = in.readString();
        this.modules = in.readString();
        this.action = in.readString();
        this.content = in.readParcelable(Parcelable.class.getClassLoader());
        this.node = (AbstractBaseBean[]) in.readParcelableArray(AbstractBaseBean.class.getClassLoader());
    }

    public static final Creator<AbstrctPMABean> CREATOR = new Creator<AbstrctPMABean>() {

        public AbstrctPMABean createFromParcel(Parcel source) {return new AbstrctPMABean(source);}

        public AbstrctPMABean[] newArray(int size) {return new AbstrctPMABean[size];}
    };
}
