package mitso.volodymyr.tryintentservice.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Organization implements Parcelable, Comparable<Organization> {

    private int                 databaseId;
    private String              id;
    private String              name;
    private String              type;
    private String              region;
    private String              city;
    private String              address;
    private String              phone;
    private String              link;
    private List<Currency>      currencies;
    private Date                date;

    public Organization() {

    }

    @Override
    public int compareTo(@NonNull Organization _that) {

        return this.getName().toLowerCase().compareTo(_that.getName().toLowerCase());
    }

    @Override
    public String toString() {

        return "Organization{" +
                "databaseId=" + databaseId +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", link='" + link + '\'' +
                ", currencies=" + currencies +
                ", date=" + date +
                '}';
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Organization(Parcel _in) {

        databaseId = _in.readInt();
        id = _in.readString();
        name = _in.readString();
        type = _in.readString();
        region = _in.readString();
        city = _in.readString();
        address = _in.readString();
        phone = _in.readString();
        link = _in.readString();
        if (_in.readByte() == 0x01) {
            currencies = new ArrayList<>();
            _in.readList(currencies, Currency.class.getClassLoader());
        } else {
            currencies = null;
        }
        long tmpDate = _in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel _dest, int _flags) {

        _dest.writeInt(databaseId);
        _dest.writeString(id);
        _dest.writeString(name);
        _dest.writeString(type);
        _dest.writeString(region);
        _dest.writeString(city);
        _dest.writeString(address);
        _dest.writeString(phone);
        _dest.writeString(link);
        if (currencies == null) {
            _dest.writeByte((byte) (0x00));
        } else {
            _dest.writeByte((byte) (0x01));
            _dest.writeList(currencies);
        }
        _dest.writeLong(date != null ? date.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Organization> CREATOR = new Parcelable.Creator<Organization>() {

        @Override
        public Organization createFromParcel(Parcel in) {
            return new Organization(in);
        }

        @Override
        public Organization[] newArray(int size) {
            return new Organization[size];
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
