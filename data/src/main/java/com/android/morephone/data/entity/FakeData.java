package com.android.morephone.data.entity;

import java.util.List;

/**
 * Created by Ethan on 3/17/17.
 */

public class FakeData {

    public List<ListNumber> list_number;
    public List<Message> message;

    public FakeData(List<ListNumber> list_number, List<Message> message) {
        this.list_number = list_number;
        this.message = message;
    }

    public class Message {
        public String sid;
        public String to;
        public String from;
        public String date_created;
        public String body;

        public Message(String sid, String to, String from, String date_created, String body) {
            this.sid = sid;
            this.to = to;
            this.from = from;
            this.date_created = date_created;
            this.body = body;
        }
    }


    public class ListNumber {
        public String sid;
        public String phone_number;

        public ListNumber(String sid, String phone_number) {
            this.sid = sid;
            this.phone_number = phone_number;
        }
    }

}
