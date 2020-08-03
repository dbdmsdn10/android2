package com.example.firebasememo;

public class Memoclass {
    String key;
    String content;
    String createDate;
    String update;

    public String getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdate() {
        return update;
    }



    public void setKey(String key) {
        this.key = key;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
