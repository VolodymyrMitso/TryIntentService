package mitso.volodymyr.tryintentservice.models.json;

import java.io.Serializable;

public class JsonOrganization implements Serializable {

    private String          id;
    private int             oldId;
    private int             orgType;
    private boolean         branch;
    private String          title;
    private String          regionId;
    private String          cityId;
    private String          phone;
    private String          address;
    private String          link;

    @Override
    public String toString() {
        return "JsonOrganization{" +
                "id='" + id + '\'' +
                ", oldId=" + oldId +
                ", orgType=" + orgType +
                ", branch=" + branch +
                ", title='" + title + '\'' +
                ", regionId='" + regionId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOldId() {
        return oldId;
    }

    public void setOldId(Integer oldId) {
        this.oldId = oldId;
    }

    public Integer getOrgType() {
        return orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public Boolean getBranch() {
        return branch;
    }

    public void setBranch(Boolean branch) {
        this.branch = branch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public boolean isBranch() {
        return branch;
    }

    public void setBranch(boolean branch) {
        this.branch = branch;
    }
}