package com.backend.security.dtos;

import java.util.ArrayList;
import java.util.List;


import lombok.Data;

@Data
public class UserAuthDTO {

    private Long id;

    private String nombres;
    
    private String dependencia;
    
    private String compania;       	

    private String user_name;  

    private ArrayList<String>  authorities;

    private String client_id;   

    private String[] scope;  

    private String exp;    

    private String jti;

    private String email;

    // private List<Modulo> listaModulos;

}
