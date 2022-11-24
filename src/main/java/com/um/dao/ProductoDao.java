package com.um.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.um.mapper.ProductoMapper;
import com.um.model.Producto;

@Component
public class ProductoDao {
	
	@Autowired
	private JdbcTemplate postgresTemplate;
	
	public List<Producto> listProductos() {
		List<Producto> lista = new ArrayList<Producto>();
		String query = "SELECT * FROM PRODUCTO ORDER BY ID ASC";
		
		lista = postgresTemplate.query(query, new ProductoMapper());

		return lista;
	}
	
	public HashMap<Integer,String> mapTodos() {
		
		List<Producto> lista		= new ArrayList<Producto>();
		HashMap<Integer,String> mapa = new HashMap<Integer,String>();
		
		try{
			String comando = "SELECT * FROM PRODUCTO";
			lista = postgresTemplate.query(comando, new ProductoMapper());
			for(Producto producto: lista){
				mapa.put(producto.getId(), producto.getNombre());
			}			
		}catch(Exception ex){
			System.out.println("com.dao.ProductoDao|mapTodos:"+ex);
		}		
		return mapa;
	}
	
	public String getNombre(int productoId) {
		
		String nombre = "";
		
		String query = "SELECT COUNT(*) FROM PRODUCTO WHERE ID = ?";
		if(postgresTemplate.queryForObject(query, Integer.class, productoId) >= 1) {
			query = "SELECT NOMBRE FROM PRODUCTO WHERE ID = ?";
			nombre = postgresTemplate.queryForObject(query, String.class, productoId);
		}
		
		return nombre;
	}
	
	public Producto mapeaReg(String id) {
		Producto producto = new Producto();
		
		String query = "SELECT COUNT(*) FROM PRODUCTO WHERE ID = TO_NUMBER(?, '999')";
		if(postgresTemplate.queryForObject(query, Integer.class, id) >= 1) {
			query = "SELECT * FROM PRODUCTO WHERE ID = TO_NUMBER(?, '999')";
			producto = postgresTemplate.queryForObject(query, new ProductoMapper(), id);
		}
		
		return producto;
	}
	
	public int maximoReg() {
		int maximo 			= 1;
		
		try{
			String comando = "SELECT COALESCE(MAX(ID)+1,1) AS MAXIMO FROM PRODUCTO";
			if (postgresTemplate.queryForObject(comando, Integer.class) >= 1){
				maximo = postgresTemplate.queryForObject(comando, Integer.class);
			}
			
		}catch(Exception ex){
			System.out.println("Error - com.ProductoDao|maximoReg|:"+ex);

		}
		
		return maximo;
	}
	
	public boolean existeReg( int id){	
		boolean ok = false;		
		try{
			String comando = "SELECT COUNT(*) FROM PRODUCTO WHERE ID = ?";
			Object[] parametros = new Object[] {id};
			if (postgresTemplate.queryForObject(comando, Integer.class, parametros) >= 1){
				ok = true;
			}			
		}catch(Exception ex){
			System.out.println("Error - com.dao.PlatoDao|existeReg|:"+ex);
		}		
		return ok;
	}
	
	public boolean insertReg(Producto objeto){
		boolean ok = false;		
		try{
			String comando = "INSERT INTO PRODUCTO(ID, NOMBRE, DESCRIPCION, PRECIO, CATEGORIA) "
					+ "VALUES(?, ?, ?, TO_NUMBER(?, '99999.99'), ?)";
			Object[] parametros = new Object[] {objeto.getId(), objeto.getNombre(), objeto.getDescripcion(),  
					objeto.getPrecio(), objeto.getCategoria()};
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - com.dao.ProductoDao|insertReg|:"+ex);
		}
		
		return ok;
	}
	
	public boolean updateReg(Producto objeto){
		boolean ok = false;		
		try{
			String comando = "UPDATE PRODUCTO SET NOMBRE = ?, DESCRIPCION = ?, PRECIO = TO_NUMBER(?, '99999.99'), CATEGORIA = ? "
					+ " WHERE ID = ?";
			Object[] parametros = new Object[] {objeto.getNombre(), objeto.getDescripcion(), 
					objeto.getPrecio(), objeto.getCategoria(), objeto.getId() };
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - com.dao.ProductoDao|updateReg|:"+ex);
		}
		
		return ok;
	}
	
	public boolean deleteReg(int id){
		boolean ok = false;		
		try{
			String comando = "DELETE FROM PRODUCTO WHERE ID = ?";
			Object[] parametros = new Object[] {id};
			if (postgresTemplate.update(comando,parametros)==1){
				ok = true;
			}		
		}catch(Exception ex){
			System.out.println("Error - com.dao.ProductoDao|deleteReg|:"+ex);
		}
		
		return ok;
	}
	
}
