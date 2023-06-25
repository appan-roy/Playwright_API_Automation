package utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // this is for default constructor
@AllArgsConstructor // this is for all arg constructor
@Data // this is for all the getter and setter
@Builder // this is for building the object of this class
public class Users {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;

}
