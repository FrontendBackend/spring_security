package com.backend.security.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// import com.code.geocampo.models.entity.Modulo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

    /*
     * Identificador interno del usuario. Secuencia: PRMI_SEQ_USUARIO
     */
    private Long idUsuario;
   
    /*
     * Nombre de usuario
     */
    private String noUsuario;

    /*
     * Password de usuario
     */
    private String dePassword;

    /*
     * Flag que indica si está activo o no. Posibles valores: "1" = Sí y "0" = No
     */
    private String flActivo;

    /*
     * Nombres de la persona natural
     */
    private String noPersona;

    /*
     * Apellido paterno de la persona natural
     */
    private String apPaterno;

    /*
     * Apellido materno de la persona natural
     */
    private String apMaterno;

    /*
     * Número de documento de identidad
     */
    private String coDocumentoIdentidad;

    /*
     * Correo electrónico
     */
    private String deCorreo;

    private String coEmpresa;

    /*
     * Estado del registro. Los posibles valores son: "1" = Activo y "0" = Inactivo
     */
    private String esRegistro;

    /*
     * Usuario creador
     */
    private String usCreacion;

    /*
     * Terminal de creación
     */
    private String ipCreacion;

    /*
     * Fecha y hora de creación
     */
    private Date feCreacion;

    /*
     * Usuario modificador
     */
    private String usActualizacion;

    /*
     * Terminal de modificación
     */
    private String ipActualizacion;

    /*
     * Fecha y hora de modificación
     */
    private Date feActualizacion;
    
    // private List<FuncionalidadDTO> roles;

    // private List<Modulo> listaModulos;

    /*
     * Texto utilizado para una búsqueda genérica.
     */
    private String textoBusqueda;

    /**
     * Nombres completos de la persona usuaria
     */
    private String descNoPersonaCompleto;

    /**
     * Perfil de usuario para filtro
     */
    private Long descIdPerfil;

    /**
     * Lista de perfiles del usuario
     */
    // private List<PerfilDTO> lstPerfilDTO;

    /**
     * Contraseña actual del usuario (auxiliar)
     */
    private String descDePasswordActual;    
    private ArrayList<String>  authorities;
    
}
