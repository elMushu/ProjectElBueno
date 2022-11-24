package com.um.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.um.model.Orden;

public class OrdenMapper implements RowMapper<Orden> {
	
    @Override
    public Orden mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Orden objeto = new Orden();

    	objeto.setOrdenId(rs.getInt("ORDEN_ID"));
    	objeto.setMesa(rs.getInt("MESA"));
    	objeto.setPrecio(rs.getString("PRECIO"));
    	objeto.setCarritoId(rs.getInt("CARRITO_ID"));
    	objeto.setProductos(rs.getString("PRODUCTOS"));
    	objeto.setStatus(rs.getString("STATUS"));

        return objeto;
    }
}
