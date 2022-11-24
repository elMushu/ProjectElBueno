package com.um.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.um.mapper.OrdenMapper;
import com.um.model.Orden;

@Component
public class OrdenDao {
	
	@Autowired
	private JdbcTemplate postgresTemplate;
	
	public List<Orden> listOrden() {
		List<Orden> lista = new ArrayList<Orden>();
		String query = "SELECT * FROM ORDEN ORDER BY ORDEN_ID ASC";
		
		lista = postgresTemplate.query(query, new OrdenMapper());

		return lista;
	}
	
	public Orden mapeaReg(String id) {
		Orden producto = new Orden();
		
		String query = "SELECT COUNT(*) FROM ORDEN WHERE ORDEN_ID = TO_NUMBER(?, '99999')";
		if(postgresTemplate.queryForObject(query, Integer.class, id) >= 1) {
			query = "SELECT * FROM ORDEN WHERE ORDEN_ID = TO_NUMBER(?, '99999')";
			producto = postgresTemplate.queryForObject(query, new OrdenMapper(), id);
		}
		
		return producto;
	}
	
	public int maximoReg() {
		int maximo 			= 1;
		
		try{
			String comando = "SELECT COALESCE(MAX(ORDEN_ID)+1,1) AS MAXIMO FROM ORDEN";
			if (postgresTemplate.queryForObject(comando, Integer.class) >= 1){
				maximo = postgresTemplate.queryForObject(comando, Integer.class);
			}
			
		}catch(Exception ex){
			System.out.println("Error - com.OrdenDao|maximoReg|:"+ex);

		}
		
		return maximo;
	}
	
	public boolean existeReg(int id){	
		boolean ok = false;		
		try{
			String comando = "SELECT COUNT(*) FROM ORDEN WHERE ORDEN_ID = ?";
			Object[] parametros = new Object[] {id};
			if (postgresTemplate.queryForObject(comando, Integer.class, parametros) >= 1){
				ok = true;
			}			
		}catch(Exception ex){
			System.out.println("Error - com.dao.PlatoDao|existeReg|:"+ex);
		}		
		return ok;
	}
	
	public boolean insertReg(Orden objeto){
		boolean ok = false;		
		try{
			String comando = "INSERT INTO ORDEN(ORDEN_ID, MESA, PRECIO, CARRITO_ID, PRODUCTOS, STATUS) "
					+ "VALUES(?, ?, TO_NUMBER(?, '99999.99'), ?, ?, ?)";
			Object[] parametros = new Object[] {objeto.getOrdenId(), objeto.getMesa(), objeto.getPrecio(),  
					objeto.getCarritoId(), objeto.getProductos(), objeto.getStatus()};
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - com.dao.OrdenDao|insertReg|:"+ex);
		}
		
		return ok;
	}
	
	public boolean updateReg(Orden objeto){
		boolean ok = false;		
		try{
			String comando = "UPDATE ORDEN SET MESA = ?, PRECIO = TO_NUMBER(?, '99999.99'), CARRITO_ID = ?, PRODUCTOS = ?, STATUS = ?"
					+ " WHERE ORDEN_ID = ?";
			Object[] parametros = new Object[] {objeto.getMesa(), objeto.getPrecio(), 
					objeto.getCarritoId(), objeto.getProductos(), objeto.getStatus(), objeto.getOrdenId() };
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - com.dao.OrdenDao|updateReg|:"+ex);
		}
		
		return ok;
	}
	
	public boolean updateStatus(String status, int id){
		boolean ok = false;		
		try{
			String comando = "UPDATE ORDEN SET STATUS = ?"
					+ " WHERE ORDEN_ID = ?";
			Object[] parametros = new Object[] {status, id};
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - com.dao.OrdenDao|updateReg|:"+ex);
		}
		
		return ok;
	}

	
	public boolean deleteReg(int id){
		boolean ok = false;		
		try{
			String comando = "DELETE FROM ORDEN WHERE ORDEN_ID = ?";
			Object[] parametros = new Object[] {id};
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - com.dao.OrdenDao|deleteReg|:"+ex);
		}
		
		return ok;
	}
	
}
