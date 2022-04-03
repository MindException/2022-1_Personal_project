package com.lastproject.used_item_market;

//실제 서버에 저장되는 중고물품이다.
public class Product {

    public String seller = "";          //판매자
    public String university = "";      //대학 이름
    public String title = "";           //게시글 제목
    public int cost;                    //가격
    public String purpose;              //거래 목적 (판매와 무료나눔만 존재)
    public String category;             //카테고리
    public String destination;          //거래 장소
    public String text;                 //내용

    Product(){}


}
