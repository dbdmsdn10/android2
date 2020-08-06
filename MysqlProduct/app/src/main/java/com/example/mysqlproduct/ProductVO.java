package com.example.mysqlproduct;

public class ProductVO {
    String code,pname,price,image;

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String pname) {
        this.pname = pname;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return pname;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
