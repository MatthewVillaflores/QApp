package com.tech.ivant.qapp.entities;

/**
 * Created by matthew on 7/22/15.
 */
public class Service {

    /**
     * defaultWaitingTime
     * endNumber
     * logo
     * name
     * notes
     * online
     * priorityNumber
     * startNumber
     */

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
}
