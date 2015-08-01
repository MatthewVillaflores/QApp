package com.tech.ivant.qapp.entities;

import com.tech.ivant.qapp.dao.QueueDao;
import com.tech.ivant.qapp.dao.ServiceDao;

/**
 * Created by matthew on 7/22/15.
 */
public class Queue {

    /*
     * calldate
     * customerName
     * formattedQueueNumber
     * mobileNumber
     * notes
     * queueCall
     * queueDate
     * queueNumber
     * status
     * serviceId
     */

    //public long callDate;
    public String customerName;
    public String notes;
    public String mobileNumber;
    public int queueNumber;
    public long queueDate;
    public long service_id;
    public long id;


    public Queue(){
        this.id = -1;
    }

    public Queue(String customerName, String notes, long service_id) {
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = "";
        this.service_id = service_id;
        this.id = -1;
        this.queueDate = System.currentTimeMillis();
    }

    public Queue(String customerName, String notes, String mobileNumber, long service_id){
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.service_id = service_id;
        this.id = -1;
        this.queueDate = System.currentTimeMillis();
    }

    public Queue(String customerName, String notes, long service_id, long id) {
        this.customerName = customerName;
        this.notes = notes;
        this.service_id = service_id;
        this.id = id;
        this.queueDate = System.currentTimeMillis();
    }

    public Queue(String customerName, String notes, String mobileNumber, long service_id, long id){
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.service_id = service_id;
        this.id = id;
    }

    public Queue(String customerName, String notes, long service_id, long id, int queueNumber, long queueDate) {
        this.customerName = customerName;
        this.notes = notes;
        this.service_id = service_id;
        this.id = id;
        this.queueNumber = queueNumber;
        this.queueDate = queueDate;
    }

    public Queue(String customerName, String notes, String mobileNumber, int queueNumber, long queueDate, long service_id, long id) {
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.queueNumber = queueNumber;
        this.queueDate = queueDate;
        this.service_id = service_id;
        this.id = id;
    }

    public static Queue enqueue(String customerName, String mobileNumber, String notes, long serviceId){

        Queue queue = new Queue();

        queue.customerName = customerName;
        queue.mobileNumber = mobileNumber;
        queue.notes = notes;
        queue.queueDate = System.currentTimeMillis();
        queue.service_id = serviceId;

        Service mService = ServiceDao.find(serviceId);
        try {
            mService.endNumber++;
            queue.queueNumber = mService.endNumber;

            Report.addQueue(queue);
            QueueDao.save(queue);
            ServiceDao.update(mService);
        } catch (NullPointerException er){}
        return queue;
    }

    public static void call(Queue queue){

        Report.addAveWait(queue);
        QueueDao.delete(queue);
    }

}

