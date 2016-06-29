package com.gonali.task.entityModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@Entity
public class CrawlerUser implements Serializable {

    @Id
    @Column(nullable = false)
    private String userId;
    private String userAppkey;
    private String userDescription;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAppkey() {
        return userAppkey;
    }

    public void setUserAppkey(String userAppkey) {
        this.userAppkey = userAppkey;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
}
