package com.tech.ivant.qapp.entities;

import com.tech.ivant.qapp.dao.ServiceDao;

/**
 * Created by matthew on 7/22/15.
 *
 * Service entity
 *
 * Current implemented fields:
 * -id
 * -name
 * -notes
 * -startNumber
 * -endNumber
 *
 * Fields not yet implemented:
 * -logo
 * -online
 * -priorityNumber
 *
 */
public class Service {

    public long id;
    public String name;
    public String notes;

    public int startNumber;
    public int endNumber;

    public Service(){}

    public Service(String name, String notes) {
        this.name = name;
        this.notes = notes;
        this.id = -1;
        this.startNumber = 1;
        this.endNumber = 0;
    }

    public Service(long id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.startNumber = 1;
        this.endNumber = 0;
    }

    public Service(long id, String name, String notes, int startNumber, int endNumber) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.startNumber = startNumber;
        this.endNumber = endNumber;
    }

    public static void createNoShowService(){
        Service noShow;

        String noShowServiceName = "No Show";

        Service[] serviceList = ServiceDao.where(ServiceDao.ServiceEntry.COLUMN_NAME_SNAME, noShowServiceName);

        if(serviceList.length <= 0 ){
            noShow = new Service();
            noShow.name = noShowServiceName;
            ServiceDao.save(noShow);

        }


    }
}
