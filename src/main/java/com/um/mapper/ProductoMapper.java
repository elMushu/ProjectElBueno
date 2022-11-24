package com.um.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.um.model.Producto;

public class ProductoMapper implements RowMapper<Producto> {
	
    @Override
    public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Producto objeto = new Producto();

    	objeto.setId(rs.getInt("ID"));
    	objeto.setNombre(rs.getString("NOMBRE"));
    	objeto.setDescripcion(rs.getString("DESCRIPCION"));
    	objeto.setPrecio(rs.getString("PRECIO"));
    	objeto.setCategoria(rs.getString("CATEGORIA"));

        return objeto;
    }
}
