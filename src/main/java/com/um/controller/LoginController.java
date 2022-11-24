package com.um.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.um.dao.CarritoDao;
import com.um.dao.OrdenDao;
import com.um.dao.ProductoDao;
import com.um.dao.UsuarioDao;
import com.um.model.Carrito;
import com.um.model.Orden;
import com.um.model.Producto;
import com.um.model.Usuario;


@Controller
public class LoginController {
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private ProductoDao productoDao;
	
	@Autowired
	private CarritoDao carritoDao;
	
	@Autowired
	private OrdenDao ordenDao;


	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login( ){
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	@RequestMapping(value={"/logout"}, method = RequestMethod.GET)
	public ModelAndView logout(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("logout");
		return modelAndView;
	}
	
	@RequestMapping(value="/inicio", method = RequestMethod.GET)
	public ModelAndView inicio(HttpSession session){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> grantedAuthorities = auth.getAuthorities();
		
		Usuario usuario = usuarioDao.buscaUsuarioPorEmail(auth.getName());
		String role 	= "";
		
		for (GrantedAuthority grantedAuthority : grantedAuthorities){
			role = role +"-"+grantedAuthority.getAuthority();
		}
		
		if(session != null) {
			session.setAttribute("usuario", usuario.getEmail());			
		}
		
		modelAndView.addObject("role", role); 
		modelAndView.addObject("usuario", "Bienvenido " + usuario.getNombre() + " (" + usuario.getEmail() + ")");
		modelAndView.setViewName("redirect:/menu");
		return modelAndView;
	}
	

	@RequestMapping(value="/menu", method = RequestMethod.GET)
	public ModelAndView menu(HttpSession session){
		
		if(session != null) {
			if(session.getAttribute("usuario") == null) {
				session.setAttribute("usuario", "X");
			}
			if(session.getAttribute("carrito") == null) {
				session.setAttribute("carrito", "0");
			}
			if(session.getAttribute("orden") == null) {
				session.setAttribute("orden", "0");
			}
			
		}
		
		ModelAndView modelAndView = new ModelAndView();
		List<Producto> lisProductos = productoDao.listProductos();
		
		modelAndView.setViewName("menu");
		modelAndView.addObject("lisProductos", lisProductos);
				
		return modelAndView;
	}
	
	
	@RequestMapping(value="/comidas", method = RequestMethod.GET)
	public ModelAndView comidas(){
		
		ModelAndView modelAndView = new ModelAndView();
		List<Producto> lisProductos = productoDao.listProductos();
		
		modelAndView.setViewName("comidas");
		modelAndView.addObject("lisProductos", lisProductos);

		return modelAndView;
	}
	
	@RequestMapping(value="/orden", method = RequestMethod.GET)
	public ModelAndView orden(Orden orden){
		
		ModelAndView modelAndView = new ModelAndView();
		List<Orden> lisOrdenes = ordenDao.listOrden();
		
		modelAndView.setViewName("orden");
		modelAndView.addObject("lisOrdenes", lisOrdenes);

		return modelAndView;
	}
	
	@RequestMapping(value="/pago", method = RequestMethod.GET)
	public ModelAndView pago(){
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("pago");

		return modelAndView;
	}
	
	@RequestMapping(value="/carrito", method = RequestMethod.GET)
	public ModelAndView carrito(HttpServletRequest request, HttpSession session){
		
		String id		= request.getParameter("producto")==null?"0":request.getParameter("producto");
		Producto producto = new Producto();
		Orden orden = new Orden();
		Carrito carrito = new Carrito();
		String carritoId = "0";
		String ordenId	= "0";
		
		if(session != null) {
			if(session.getAttribute("carrito") == null) {
				session.setAttribute("carrito", "0");
			}else {
				carrito.setCarritoId(Integer.parseInt(session.getAttribute("carrito").toString()));
				carritoId = session.getAttribute("carrito").toString();
			}
			if(session.getAttribute("orden") != "0"){
				ordenId = session.getAttribute("orden").toString();
				orden = ordenDao.mapeaReg(ordenId);
			}
		}
		
		HashMap<Integer,Float> mapaPrecio = new HashMap<Integer,Float>(); 
		List<Carrito> lisCarritos = carritoDao.lisPorCarrito(carritoId, "ORDER BY PRODUCTO_ID");
		float precioTotal = 0;
		for(Carrito carr : lisCarritos){
			float precioProducto = Float.valueOf(carr.getPrecio()) * carr.getCantidad();
			mapaPrecio.put(carr.getProductoId(), precioProducto);
			precioTotal += precioProducto;
		}
		
		HashMap<Integer,String> mapProductos = productoDao.mapTodos();
		ModelAndView modelAndView = new ModelAndView();
		
		if(productoDao.existeReg(Integer.parseInt(id))){
			producto = productoDao.mapeaReg(id);
			carrito.setProductoId(Integer.parseInt(id));
			carrito.setPrecio(producto.getPrecio());
		}
		
		modelAndView.setViewName("carrito");
		modelAndView.addObject("producto", producto);
		modelAndView.addObject("orden", orden);
		modelAndView.addObject("lisCarritos", lisCarritos);
		modelAndView.addObject("mapProductos", mapProductos);
		modelAndView.addObject("carrito", carrito);
		modelAndView.addObject("mapaPrecio", mapaPrecio);
		modelAndView.addObject("precioTotal", precioTotal);

		return modelAndView;
	}
	

	@RequestMapping(value="/grabarCarrito", method = RequestMethod.POST)
	public ModelAndView grabarCarrito(HttpServletRequest request, Carrito carrito, HttpSession session){
		
		String id = String.valueOf(carrito.getCarritoId());		
		ModelAndView modelAndView = new ModelAndView();
		
		String carritoId = "0";
		
		if(session != null) {
			if(session.getAttribute("carrito") != null) {
				carritoId = session.getAttribute("carrito").toString();				
			}
		}
		
		if(carritoDao.existeReg(carrito.getCarritoId(),carrito.getProductoId())) {
			carritoDao.updateReg(carrito);
		}else {
			if(carritoId.equals("0")) {
				id = String.valueOf(carritoDao.maximoReg());
			}else {
				id = carritoId; 		
			}
			carrito.setCarritoId(Integer.parseInt(id));
			carritoDao.insertReg(carrito);

			if(session != null) {							
				session.setAttribute("carrito", id);				
			}

		}		
				
		modelAndView.setViewName("redirect:/carrito");
		modelAndView.addObject("carrito", carrito);

		return modelAndView;
	}

	@RequestMapping(value="/editarProducto", method = RequestMethod.GET)
	public ModelAndView editarProducto(HttpServletRequest request){
		
		String id		= request.getParameter("id")==null?"0":request.getParameter("id");
		String nombre	= request.getParameter("nombre")==null?"-":request.getParameter("nombre");
		
		Producto producto = productoDao.mapeaReg(id);
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("editarProducto");
		modelAndView.addObject(  "producto", producto);
	
		return modelAndView;
	}
	
	@RequestMapping(value="/nuevaOrden", method = RequestMethod.GET)
	public ModelAndView nuevaOrden(HttpServletRequest request, HttpSession session){
		
		if(session != null) {
			session.setAttribute("carrito", "0");
			session.setAttribute("orden", "0");
		}
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("redirect:/menu");
		
		return modelAndView;
	}

	
	@RequestMapping(value="/grabarProducto", method = RequestMethod.POST)
	public ModelAndView grabarProducto(HttpServletRequest request, Producto producto){
		
		String id = String.valueOf(producto.getId());
		
		ModelAndView modelAndView = new ModelAndView();
		if(productoDao.existeReg(producto.getId())) {
			productoDao.updateReg(producto);
		}else {
			id = String.valueOf(productoDao.maximoReg());
			producto.setId(Integer.parseInt(id));
			productoDao.insertReg(producto);
		}
		
		modelAndView.setViewName("redirect:/editarProducto?id="+id);
		modelAndView.addObject("producto", producto);

		return modelAndView;
	}
	
	@RequestMapping(value="/grabarOrden", method = RequestMethod.POST)
	public ModelAndView grabarOrden(HttpServletRequest request, Orden orden, HttpSession session){
		
		String carritoId = "0";
		String ordenId = "0";
		String productos = "";
		
		if(session != null) {
			carritoId = session.getAttribute("carrito").toString();
			ordenId = session.getAttribute("orden").toString();
		}
		
		List<Carrito> lisCarritos = carritoDao.lisPorCarrito(carritoId, "ORDER BY PRODUCTO_ID");
		float precioTotal = 0;
		for(Carrito carrito : lisCarritos){
			float precioProducto = Float.valueOf(carrito.getPrecio()) * carrito.getCantidad();
			productos 	+= "Producto: "+productoDao.getNombre(carrito.getProductoId())+" Cantidad: "+carrito.getCantidad()+" Precio: "+precioProducto+", ";
			precioTotal += precioProducto;
		}
		
		orden.setPrecio(String.valueOf(precioTotal));
		orden.setCarritoId(Integer.parseInt(carritoId));
		orden.setOrdenId(Integer.parseInt(ordenId));
		orden.setProductos(productos);
		
		ModelAndView modelAndView = new ModelAndView();
		if(ordenDao.existeReg(orden.getOrdenId())) {
			ordenDao.updateReg(orden);
		}else {
			ordenId = String.valueOf(ordenDao.maximoReg());
			orden.setOrdenId(Integer.parseInt(ordenId));
			ordenDao.insertReg(orden);
			session.setAttribute("orden", ordenId);
		}
		
		modelAndView.setViewName("redirect:/carrito?");
		modelAndView.addObject("orden", orden);

		return modelAndView;
	}

	
	@RequestMapping(value="/borrarCarrito", method = RequestMethod.GET)
	public ModelAndView borrarCarrito(HttpServletRequest request){
		
		String carritoId	= request.getParameter("carritoId")==null?"0":request.getParameter("carritoId");
		String productoId	= request.getParameter("productoId")==null?"0":request.getParameter("productoId");
		
		ModelAndView modelAndView = new ModelAndView();
		if(carritoDao.existeReg(Integer.parseInt(carritoId), Integer.parseInt(productoId))) {
			carritoDao.deleteReg(Integer.parseInt(carritoId), Integer.parseInt(productoId));
		}
		
		modelAndView.setViewName("redirect:/carrito");

		return modelAndView;
	}
	
	@RequestMapping(value="/borrarProducto", method = RequestMethod.GET)
	public ModelAndView borrarProducto(HttpServletRequest request){
		
		String id		= request.getParameter("id")==null?"0":request.getParameter("id");
		
		ModelAndView modelAndView = new ModelAndView();
		if(productoDao.existeReg(Integer.parseInt(id))) {
			productoDao.deleteReg(Integer.parseInt(id));
		}
		
		modelAndView.setViewName("redirect:/comidas");

		return modelAndView;
	}
	
	@RequestMapping(value="/cambiarStatus", method = RequestMethod.GET)
	public ModelAndView cambiarStatus(HttpServletRequest request){
		
		String id		= request.getParameter("statusId")==null?"0":request.getParameter("statusId");
		
		if(ordenDao.existeReg(Integer.parseInt(id))) {
			ordenDao.updateStatus("Terminado", Integer.parseInt(id));
		}
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("redirect:/orden");

		return modelAndView;
	}
	
	@RequestMapping(value="/borrarOrden", method = RequestMethod.GET)
	public ModelAndView borrarOrden(HttpServletRequest request){
		
		String ordenId		= request.getParameter("ordenId")==null?"0":request.getParameter("ordenId");
		
		ModelAndView modelAndView = new ModelAndView();
		if(ordenDao.existeReg(Integer.parseInt(ordenId))) {
			ordenDao.deleteReg(Integer.parseInt(ordenId));
		}
		
		modelAndView.setViewName("redirect:/orden");

		return modelAndView;
	}
}
