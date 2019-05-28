package com.mohe.fastdevpro.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xiePing on 2019/5/24 0024.
 * Description:
 */
@Entity
public class CloudPayBean {
    @Id private long id;

    @Generated(hash = 999812870)
    public CloudPayBean(long id) {
        this.id = id;
    }

    @Generated(hash = 220246944)
    public CloudPayBean() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
