package com.um.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.um.mapper.UsuarioMapper;
import com.um.model.Usuario;

@Component
public class UsuarioDao {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private JdbcTemplate postgresTemplate;

	public Usuario buscaUsuarioPorEmail(String email) {
		Usuario objeto = new Usuario();
		String query = "SELECT ID, NOMBRE, EMAIL, PASSWORD, ROLES, ACTIVO FROM USUARIO WHERE EMAIL = ?";
		
		try {
			objeto = postgresTemplate.queryForObject(query, new UsuarioMapper(), email);
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | buscaUsuarioPorEmail | "+e);
		}
		
		return objeto;
	}

	public List<Usuario> listaUsuarios() {
		List<Usuario> lista = new ArrayList<Usuario>();
		String query = "SELECT * FROM USUARIO ORDER BY NOMBRE";
		
		try {
			lista = postgresTemplate.queryForList(query, Usuario.class);
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | listaUsuarios | "+e);
		}
		return lista;
	}

	public void grabaUsuario(Usuario objeto, int[] roles) {
		objeto.setPassword(bCryptPasswordEncoder.encode(objeto.getPassword()));
		
		String query = "SELECT NOMBRE FROM ROLE WHERE ID = ?";
		
		String grabaRoles = "-";
		
		if(roles != null) {
			for(int roleId : roles) {
				String role = postgresTemplate.queryForObject(query, String.class, roleId);
				grabaRoles += String.valueOf(role)+"-";
			}
		}
		
		Object[] parametros = new Object[]{
			objeto.getNombre(),objeto.getEmail(),objeto.getPassword(),grabaRoles
		};
		
		query = "INSERT INTO USUARIO(NOMBRE,EMAIL,PASSWORD,ROLES,ACTIVO) VALUES(?, ?, ?, ?, TRUE)";

		if(postgresTemplate.update(query, parametros) >= 1) {
			query = "SELECT ID FROM USUARIO WHERE NOMBRE = ? AND EMAIL = ?";
			
			parametros = new Object[]{
				objeto.getNombre(),objeto.getEmail()
			};
	
			int usuarioId = postgresTemplate.queryForObject(query, Integer.class, parametros);
	
			if(roles != null) {
				for(int roleId : roles) {
					query = "INSERT INTO USUARIO_ROLE(USUARIO_ID, ROLE_ID) VALUES( ?, ?)";
					postgresTemplate.update(query, new Object[]{usuarioId,roleId});
				}
			}
		}
	}

	public Usuario buscaUsuarioPorId(String id) {
		Usuario objeto = new Usuario();
		String query = "SELECT * FROM USUARIO WHERE ID = ?";
		
		try {
			objeto = postgresTemplate.queryForObject(query, Usuario.class, id);
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | buscaUsuarioPorId | "+e);
		}
	
		return objeto;
	}

	public boolean existeEmail(String email) {
		boolean ok = false;
		String query = "SELECT COUNT(*) FROM USUARIO WHERE EMAIL = ?";
		
		try {
			if(postgresTemplate.queryForObject(query, Integer.class, email) >= 1) {
				ok = true;
			}
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | existeEmail | "+e);
		}
		
		return ok;
	}

	public boolean bajaUsuario(int usuarioId) {
		boolean ok = false;
		String query = "UPDATE USUARIO SET ACTIVO = FALSE WHERE ID = ?";

		try {
			if(postgresTemplate.update(query, usuarioId) >= 1){
				ok = true;
			}
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | bajaUsuario | "+e);
		}
		
		return ok;
	}

	public boolean activarUsuario(int usuarioId) {
		boolean ok = false;
		String query = "UPDATE USUARIO SET ACTIVO = TRUE WHERE ID = ?";
		
		try {
			if(postgresTemplate.update(query, usuarioId) >= 1){
				ok = true;
			}
		} catch (Exception e) {
			System.out.println("ERROR : UsuarioDao | activarUsuario | "+e);
		}
		
		return ok;
	}

}
