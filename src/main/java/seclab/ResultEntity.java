package seclab;

import java.util.List;

/**
 * Created by Harlan1994 on 2018/4/9.
 * 联合第一个表和第二第三个表的结果
 */
public class ResultEntity {

    private String business_id;
    private String city;
    private float latitude;
    private float longitude;

    private List<String> categories;
    private List<String> reviews;

    @Override
    public String toString() {
        return "ResultEntity{" +
                "business_id='" + business_id + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", categories=" + categories +
                ", reviews=" + reviews +
                ", count=" + count +
                '}';
    }

    private Integer count;

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
