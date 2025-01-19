package com.johnboscoltd.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "customer")
@Data
@Accessors(chain = true)
public class Customer {
    @Id
    @Column
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid")
    private String id;

    @Column(name = "mobile_number",unique = true)
    private String mobileNumber;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "other_name")
    private String otherNames;

    @Column(unique = true)
    private String email;

    @Column
    private String address;

    @Column
    private String country;

    @Column
    private String bvn;

    @Column
    private String nin;

    @Column(name = "id_type")
    private String idType;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "account_number")
    private String accountNo;
}
