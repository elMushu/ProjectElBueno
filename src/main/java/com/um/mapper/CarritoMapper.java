package com.um.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.um.model.Carrito;

	public class CarritoMapper implements RowMapper<Carrito>{
		@Override
		public Carrito mapRow(ResultSet rs, int arg1) throws SQLException {
			
			Carrito objeto = new Carrito();
			
			objeto.setCarritoId(rs.getInt("CARRITO_ID"));
			objeto.setProductoId(rs.getInt("PRODUCTO_ID"));
			objeto.setCantidad(rs.getInt("CANTIDAD"));
			objeto.setPrecio(rs.getString("PRECIO"));
			
			return objeto;
		}
	}