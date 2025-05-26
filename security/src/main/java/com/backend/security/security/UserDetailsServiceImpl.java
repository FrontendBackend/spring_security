package com.backend.security.security;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.security.dtos.UserAuthDTO;
import com.backend.security.dtos.UsuarioDTO;
import com.backend.security.models.entity.User;
import com.backend.security.models.repository.UserRepository;
import com.backend.security.utils.CommonsUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
	private HttpServletRequest request;
    
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String refresh_token = request.getParameter("refresh_token");

        if (refresh_token != null) {
            try {
                return this.refrescarToken(refresh_token);
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username));

        return UserDetailsImpl.build(user);
    }

    // private UserDetails refrescarToken(String refresh_token) throws Exception {
		
	// 	UsuarioDTO usuarioDTO = new UsuarioDTO();
	// 	UserAuthDTO userAuthDTO = CommonsUtil.deserializarToken(refresh_token);
		
	// 	usuarioDTO.setIdUsuario(userAuthDTO.getId());
	// 	usuarioDTO.setNoUsuario(userAuthDTO.getUser_name());
	// 	usuarioDTO.setAuthorities(userAuthDTO.getAuthorities());
	// 	usuarioDTO.setDePassword("_");
	// 	// usuarioDTO.setListaModulos(userAuthDTO.getListaModulos()); // estamos ajustando
		
	// 	return UserPrincipal.build(usuarioDTO);
	// }

    private UserDetails refrescarToken(String refresh_token) throws Exception {
        // 1. Deserializa el token (debería darte al menos el ID o el username)
        UserAuthDTO userAuthDTO = CommonsUtil.deserializarToken(refresh_token);

        // 2. Busca directamente el usuario en la base de datos
        User usuario = userRepository.findById(userAuthDTO.getId())
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuario no encontrado con ID: " + userAuthDTO.getId()));

        // 3. Construye el UserPrincipal directamente desde la entidad
        return UserDetailsImpl.build(usuario);
    }
    
}
