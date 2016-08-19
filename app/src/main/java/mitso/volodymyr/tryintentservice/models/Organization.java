package mitso.volodymyr.tryintentservice.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Organization implements Parcelable, Comparable<Organization> {

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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Organization(Parcel in) {

        id = in.readString();
        name = in.readString();
        type = in.readString();
        region = in.readString();
        city = in.readString();
        address = in.readString();
        phone = in.readString();
        link = in.readString();
        if (in.readByte() == 0x01) {
            currencies = new ArrayList<>();
            in.readList(currencies, Currency.class.getClassLoader());
        } else {
            currencies = null;
        }
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(region);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(link);
        if (currencies == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(currencies);
        }
        dest.writeLong(date != null ? date.getTime() : -1L);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "Organization{" +
                "id='" + id + '\'' +
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

    @Override
    public boolean equals(Object _object) {

        if (this == _object) return true;
        if (!(_object instanceof Organization)) return false;

        final Organization that = (Organization) _object;

        return getId().equals(that.getId()) && getDate().equals(that.getDate());
    }

    @Override
    public int hashCode() {

        int result = getId().hashCode();
        result = 31 * result + getDate().hashCode();
        return result;
    }

    @Override
    public int compareTo(@NonNull Organization _that) {

        return this.getName().toLowerCase().compareTo(_that.getName().toLowerCase());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
