package com.um.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.um.model.Role;

@Component
public class OrdenProductoDao {
	
	@Autowired
	private JdbcTemplate postgresTemplate;
	
	public List<Role> listRoles() {
		List<Role> lista = new ArrayList<Role>();
		String query = "SELECT * FROM EXA.ROLE ORDER BY ID";
		
		lista = postgresTemplate.queryForList(query, Role.class);

		return lista;
	}
	
}
