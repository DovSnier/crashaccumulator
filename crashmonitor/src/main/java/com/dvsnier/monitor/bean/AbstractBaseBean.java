package com.dvsnier.monitor.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * the abstract base bean,that is describe system interaction masthead code
 *
 * @author dovsnier
 * @version 1.0.0
 * @since JDK 1.7
 */
public class AbstractBaseBean implements Parcelable {

    /* the current element id */
    protected long id;
    /* the current element version code */
    protected int version;
    /* the current element permission describe check digit  */
    protected String seqToken;
    /* the current element node flags */
    protected String flag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSeqToken() {
        return seqToken;
    }

    public void setSeqToken(String seqToken) {
        this.seqToken = seqToken;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.version);
        dest.writeString(this.seqToken);
        dest.writeString(this.flag);
    }

    public AbstractBaseBean() {
    }

    protected AbstractBaseBean(Parcel in) {
        this.id = in.readLong();
        this.version = in.readInt();
        this.seqToken = in.readString();
        this.flag = in.readString();
    }

    public static final Creator<AbstractBaseBean> CREATOR = new Creator<AbstractBaseBean>() {
        @Override
        public AbstractBaseBean createFromParcel(Parcel source) {
            return new AbstractBaseBean(source);
        }

        @Override
        public AbstractBaseBean[] newArray(int size) {
            return new AbstractBaseBean[size];
        }
    };
}
