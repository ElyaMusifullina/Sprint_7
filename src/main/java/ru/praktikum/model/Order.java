package ru.praktikum.model;

import lombok.Data;

@Data
public class Order {
  private String firstName;
  private String lastName;
  private String address;
  private String metroStation;
  private String phone;
  private Number rentTime;
  private String deliveryDate;
  private String comment;
  private String[] color;
  private Integer track;
}
