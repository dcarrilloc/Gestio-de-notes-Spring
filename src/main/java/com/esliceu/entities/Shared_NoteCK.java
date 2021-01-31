package com.esliceu.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Shared_NoteCK implements Serializable {
    @Column(name = "noteid")
    Long noteid;

    @Column(name = "userid")
    Long userid;

    public Shared_NoteCK() {}

    public Shared_NoteCK(Long noteid, Long userid) {
        this.noteid = noteid;
        this.userid = userid;
    }

    public Long getNoteid() {
        return noteid;
    }

    public void setNoteid(Long noteid) {
        this.noteid = noteid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shared_NoteCK that = (Shared_NoteCK) o;
        return noteid.equals(that.noteid) &&
                userid.equals(that.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteid, userid);
    }
}
