package com.blue.girl.server.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_user", schema = "blue_girl", catalog = "")
public class UserEntity {
    @Transient
    private static final long serialVersionUID = 1L;

    private int id;
    private Byte hasEmoji;
    private String nickname;
    private String openId;
    private String unionId;
    private String headImgUrl;
    private Integer sex;
    private String city;
    private Integer subscribe;
    private Timestamp createTime;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "has_emoji")
    public Byte getHasEmoji() {
        return hasEmoji;
    }

    public void setHasEmoji(Byte hasEmoji) {
        this.hasEmoji = hasEmoji;
    }

    @Basic
    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "open_id")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Basic
    @Column(name = "union_id")
    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Basic
    @Column(name = "head_img_url")
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    @Basic
    @Column(name = "sex")
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "subscribe")
    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id && Objects.equals(hasEmoji, that.hasEmoji) && Objects.equals(nickname, that.nickname) && Objects.equals(openId, that.openId) && Objects.equals(unionId, that.unionId) && Objects.equals(headImgUrl, that.headImgUrl) && Objects.equals(sex, that.sex) && Objects.equals(city, that.city) && Objects.equals(subscribe, that.subscribe) && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hasEmoji, nickname, openId, unionId, headImgUrl, sex, city, subscribe, createTime);
    }
}
