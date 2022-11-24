package com.um.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.um.model.Usuario;


public class UsuarioMapper implements RowMapper<Usuario> {
	
    @Override
    public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Usuario objeto = new Usuario();

    	objeto.setId(rs.getInt("ID"));
    	objeto.setNombre(rs.getString("NOMBRE"));
    	objeto.setEmail(rs.getString("EMAIL"));
    	objeto.setPassword(rs.getString("PASSWORD"));
    	objeto.setRoles(rs.getString("ROLES"));
    	objeto.setActivo(rs.getBoolean("ACTIVO"));

        return objeto;
    }
}
