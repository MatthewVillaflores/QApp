package com.tech.ivant.qapp.entities;

import com.tech.ivant.qapp.dao.QueueDao;
import com.tech.ivant.qapp.dao.ServiceDao;

/**
 * Created by matthew on 7/22/15.
 *
 * Queue entity
 *
 * Current implemented fields:
 *  -customerName
 *  -notes
 *  -queueNumber
 *  -queueDate
 *  -service_id
 *  -id
 *
 *  Fields from entity map of iOS app:
 *  -callDate
 *  -customerName
 *  -formattedQueueNumber
 *  -mobileNumber
 *  -notes
 *  -queueCall
 *  -queueDate
 *  -queueNumber
 *  -status
 *  -serviceId
 *
 */
public class Queue {

    //public long callDate;
    public String customerName;
    public String notes;
    public String mobileNumber;
    public int queueNumber;
    public long queueDate;
    public long service_id;
    public long id;

    //Initialize an new empty Queue object
    public Queue(){
        this.id = -1;
    }

    //Initialize with a name, notes and foreign key(serviceId)
    public Queue(String customerName, String notes, long service_id) {
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = "";
        this.service_id = service_id;
        this.id = -1;
        this.queueDate = System.currentTimeMillis();
    }

    //Initialize with a name, notes, mobileNumber and foreign key(serviceId)
    public Queue(String customerName, String notes, String mobileNumber, long service_id){
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.service_id = service_id;
        this.id = -1;
        this.queueDate = System.currentTimeMillis();
    }

    //Initialize with a name, notes, foreign key(serviceId), and id
    public Queue(String customerName, String notes, long service_id, long id) {
        this.customerName = customerName;
        this.notes = notes;
        this.service_id = service_id;
        this.id = id;
        this.queueDate = System.currentTimeMillis();
    }

    //Initialize with a name, notes, mobileNumber, foreign key(serviceId), and id
    public Queue(String customerName, String notes, String mobileNumber, long service_id, long id){
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.service_id = service_id;
        this.id = id;
    }

    //Initialize with name, notes, foreign_key(serviceId), id, queueNumber, queueDate
    public Queue(String customerName, String notes, long service_id, long id, int queueNumber, long queueDate) {
        this.customerName = customerName;
        this.notes = notes;
        this.service_id = service_id;
        this.id = id;
        this.queueNumber = queueNumber;
        this.queueDate = queueDate;
    }

    //Initialize with name, notes, mobileNumber, foreign_key(serviceId), id, queueNumber, queueDate
    public Queue(String customerName, String notes, String mobileNumber, int queueNumber, long queueDate, long service_id, long id) {
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.queueNumber = queueNumber;
        this.queueDate = queueDate;
        this.service_id = service_id;
        this.id = id;
    }

    //Save a queue. Also adds the entry to the Reports database (iterates)
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

    //Method called when a person is "Called"
    public static void call(Queue queue){

        Report.addAveWait(queue);
        QueueDao.delete(queue);
    }

}

