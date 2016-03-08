package com.dvsnier.crashmonitor.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * the abstract base bean,that is describe system interaction masthead code
 *
 * @author dovsnier
 * @version 1.0.0
 * @date 2015/12/15.
 * @since JDK 1.7
 */
public class AbstractBaseBean implements Parcelable {

    /* the current element id */
    private long id;
    /* the current element version code */
    private int version;
    /* the current element permission describe check digit  */
    private String seqToken;
    /* the current element node flags */
    private String flag;


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.version);
        dest.writeString(this.seqToken);
        dest.writeString(this.flag);
    }

    public AbstractBaseBean() {}

    protected AbstractBaseBean(Parcel in) {
        this.id = in.readLong();
        this.version = in.readInt();
        this.seqToken = in.readString();
        this.flag = in.readString();
    }

}
