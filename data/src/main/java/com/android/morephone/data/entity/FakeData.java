package com.android.morephone.data.entity;

import java.util.List;

/**
 * Created by Ethan on 3/17/17.
 */

public class FakeData {

    public List<NumberEntity> list_number;
    public List<Message> message;
    public List<CallEntity> call_log;

    public FakeData(List<NumberEntity> list_number, List<Message> message, List<CallEntity> call_log) {
        this.list_number = list_number;
        this.message = message;
        this.call_log = call_log;
    }

    public class Message {
        public String sid;
        public String to;
        public String from;
        public String date_created;
        public String status;
        public String body;

        public Message(String sid, String to, String from, String date_created, String status, String body) {
            this.sid = sid;
            this.to = to;
            this.from = from;
            this.date_created = date_created;
            this.status = status;
            this.body = body;
        }
    }

}
