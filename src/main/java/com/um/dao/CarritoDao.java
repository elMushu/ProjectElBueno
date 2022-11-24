package com.um.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.um.model.Carrito;
import com.um.mapper.CarritoMapper;

@Component
public class CarritoDao {
	
	@Autowired		
	private JdbcTemplate postgresTemplate;
	
	public boolean insertReg(Carrito objeto){
		boolean ok = false;		
		try{
			String comando = "INSERT INTO CARRITO(CARRITO_ID, PRODUCTO_ID, CANTIDAD, PRECIO) VALUES(?, ?, ?, TO_NUMBER(?, '99999.99'))";
			Object[] parametros = new Object[] {objeto.getCarritoId(), objeto.getProductoId(), objeto.getCantidad(), objeto.getPrecio()};
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - res.dao.PlatoDao|insertReg|:"+ex);
		}
		
		return ok;
	}
	
	public boolean updateReg(Carrito objeto){
		boolean ok = false;		
		try{
			String comando = "UPDATE CARRITO SET CANTIDAD = ?, PRECIO = TO_NUMBER(?, '99999.99') WHERE CARRITO_ID = ? AND PRODUCTO_ID = ?";
			Object[] parametros = new Object[] {objeto.getCantidad(), objeto.getPrecio(), objeto.getCarritoId(), objeto.getProductoId()};
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - res.dao.PlatoDao|insertReg|:"+ex);
		}		
		return ok;
	}	
	
	public boolean deleteReg(int carritoId, int productoId){
		boolean ok = false;		
		try{
			String comando = "DELETE FROM CARRITO WHERE CARRITO_ID = ? AND PRODUCTO_ID = ?";
			Object[] parametros = new Object[] {carritoId, productoId};	
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}
		}catch(Exception ex){
			System.out.println("Error - res.dao.PlatoDao|deleteReg|:"+ex);
		}		
		return ok;
	}
	
	public boolean existeReg( int carritoId, int productoId){	
		boolean ok = false;		
		try{
			String comando = "SELECT COUNT(*) FROM CARRITO WHERE CARRITO_ID = ? AND PRODUCTO_ID = ?";
			Object[] parametros = new Object[] {carritoId, productoId};
			if (postgresTemplate.queryForObject(comando, Integer.class, parametros) >= 1){
				ok = true;
			}			
		}catch(Exception ex){
			System.out.println("Error - res.dao.PlatoDao|existeReg|:"+ex);
		}		
		return ok;
	}
	
	public int maximoReg() {
		int maximo 			= 1;
		
		try{
			String comando = "SELECT COALESCE(MAX(CARRITO_ID)+1,1) AS MAXIMO FROM CARRITO";
			if (postgresTemplate.queryForObject(comando, Integer.class) >= 1){
				maximo = postgresTemplate.queryForObject(comando, Integer.class);
			}
			
		}catch(Exception ex){
			System.out.println("Error - com.um.dao.CarritoDao|maximoReg|:"+ex);

		}
		
		return maximo;
	}

	public Carrito mapeaRegId(int carritoId, int productoId){
		Carrito objeto = new Carrito();
		try{
			String comando ="SELECT CARRITO_ID, PRODUCTO_ID, CANTIDAD, PRECIO FROM CARRITO WHERE CARRITO_ID = ? AND PRODUCTO_ID = ?";
			Object[] parametros = new Object[] {carritoId, productoId};
			objeto = postgresTemplate.queryForObject(comando, new CarritoMapper(), parametros);			
		}catch(Exception ex){
 			System.out.println("Error - res.dao.PlatoDao|mapeaRegId|:"+ex);
 		}
		return objeto;
	}
	
	public List<Carrito> lisPorCarrito( String carritoId, String orden ){
		List<Carrito> lista = new ArrayList<Carrito>();
		try{
			String comando = "SELECT CARRITO_ID, PRODUCTO_ID, CANTIDAD, PRECIO FROM CARRITO WHERE CARRITO_ID = TO_NUMBER(?, '99999')"+orden;
			lista = postgresTemplate.query(comando, new CarritoMapper(), carritoId);
		}catch(Exception ex){
			System.out.println("Error - res.dao.CarritoDao|lisPorTipo|:"+ex);
		}
		return lista;
	}	
}