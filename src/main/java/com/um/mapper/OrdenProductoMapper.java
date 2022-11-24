package com.um.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.um.model.OrdenProducto;

public class OrdenProductoMapper implements RowMapper<OrdenProducto> {
	
    @Override
    public OrdenProducto mapRow(ResultSet rs, int rowNum) throws SQLException {
    	OrdenProducto objeto = new OrdenProducto();

    	objeto.setIdOrden(rs.getInt("ID"));
    	objeto.setIdProducto(rs.getInt("NOMBRE"));
    	objeto.setCantidad(rs.getInt("DESCRIPCION"));
    	objeto.setNota(rs.getString("PRECIO"));

        return objeto;
    }
}
