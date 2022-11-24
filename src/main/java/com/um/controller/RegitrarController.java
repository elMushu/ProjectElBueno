package com.um.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.um.dao.UsuarioDao;
import com.um.model.Usuario;


@Controller
public class RegitrarController {
	
	@Autowired
	private UsuarioDao usuarioDao;

	@RequestMapping(value={"/registrar"}, method = RequestMethod.GET)
	public ModelAndView registro(){
		ModelAndView modelAndView = new ModelAndView();
		Usuario usuario 		  = new Usuario();
		modelAndView.addObject("usuario", usuario);
		
		modelAndView.addObject("usuario", usuario);
		modelAndView.setViewName("registrar");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registrar", method = RequestMethod.POST)
	public ModelAndView registrarUsuario(Usuario usuario) {
		ModelAndView modelAndView = new ModelAndView();	
		int[] usuarioRoles = new int [1];
		usuarioRoles[0] = 1;
		
		usuarioDao.grabaUsuario(usuario, usuarioRoles);
		    
		modelAndView.setViewName("registrar");
		return modelAndView;
	}
	
}