package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.*;
import com.proyecto.coompitas.services.CamaraService;
import com.proyecto.coompitas.services.PedidoProductoService;
import com.proyecto.coompitas.services.PedidoService;
import com.proyecto.coompitas.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class CamaraControllerView {

    private final CamaraService camaraService;
    private final UserService userService;
    private final PedidoProductoService pedidoProductoService;

    private final PedidoService pedidoService;

    public CamaraControllerView(CamaraService camaraService,
                                UserService userService,
                                PedidoProductoService pedidoProductoService,
                                PedidoService pedidoService) {
        this.camaraService = camaraService;
        this.userService = userService;
        this.pedidoProductoService = pedidoProductoService;
        this.pedidoService = pedidoService;
    }
    //GET PARA RENDERIZAR LA PÁGINA DE DETALLE DE LA CAMARA
    @GetMapping("/camara/{idCamara}")
    public String renderCamara(@PathVariable("idCamara") Long idCamara,
                               HttpSession session, Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            Camara camaraActual = camaraService.findCamara(idCamara);
            viewModel.addAttribute("camara", camaraActual);//Inserto la camara en el modelo para que se pueda usar en la página camaraPage
            viewModel.addAttribute("userLogueado", userService.findUserById(idLogueado));//Inserto el usuario logueado en el modelo para que se pueda usar en la página camaraPage

            List<Pedido> pedidosDeLaCamara = camaraActual.getPedidos();//Guardo los pedidos que tiene la camara
            List<PedidoProducto> registrosConProductoYCantidad = new ArrayList<>();//Creo una lista de PedidoProducto, que es la tabla intermedia que se genera cuando se guarda un producto en un pedido, pero le agregamos a mano la cantidad y la necesito tambien
            boolean estanTodosLosPedidosListos = true;

            for(Pedido pedido : pedidosDeLaCamara){//Busco los registros de relacion entre pedido y producto para cada pedido de la camara
                registrosConProductoYCantidad.addAll(pedidoProductoService.buscarPorPedido(pedido.getId()));//Agrego a la lista los registros de la tabla intermedia que se generan cuando se guarda un producto en un pedido
                if(pedido.getEstadoDelPedido() == 0){
                    estanTodosLosPedidosListos = false;
                }
            }


            viewModel.addAttribute("registrosProductoYCantidad", registrosConProductoYCantidad);//Inserto la lista de registros de la tabla intermedia en el modelo para que se pueda usar en la página camaraPage
            viewModel.addAttribute("usuarioLogueado", userService.findUserById(idLogueado));//Inserto el usuario logueado en el modelo para que se pueda usar en la página camaraPage")
            viewModel.addAttribute("pedidosListos", estanTodosLosPedidosListos);//Inserto el usuario logueado en el modelo para que se pueda usar en la página camaraPage")
            return "paginas_comprador/camaraPage";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

    }

    //GET PARA RENDERIZAR LOS PRODUCTOS DEL PROVEEDOR DE LA CAMARA A LA QUE SE VA A UNIR EL COMPRADOR PARTICIPANTE (EN LA PÁGINA DE UNIRSE A LA CAMARA)
    @GetMapping("/unirse/{idCamara}/productos")
    public String renderCatalogoParaUnirse(@PathVariable("idCamara") Long idCamara,
                                           HttpSession session, Model viewModel){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            Camara camaraActual = camaraService.findCamara(idCamara);
            User userLogueado = userService.findUserById(idLogueado);//Busco el usuario logueado que va a abrir la camara
            User userProveedor = camaraActual.getProveedor();//Extraigo el usuario que provee la camara

            if(userLogueado.getEstado() != 1){ //Comprueba el estado del usuario logueado para ver si creamos un pedido vacio en este get o no
                Pedido pedidoVacio = new Pedido();//Creo un pedido vacio en este GET para que este disponible en el controlador de pedido y actualizarlo

                pedidoVacio.setComprador(userLogueado);//Seteo el usuario logueado como comprador del pedido

                pedidoService.crearPedido(pedidoVacio);//Guardo el pedido en la base de datos //Esto hay que sacarlo, hay que hacerlo inyectandolo al modelo y luego sacandolo del modelo cuando lo necesitemos asi no guaramos un pedido que no sabemos si va a proliferar en la base de datos

                userLogueado.setEstado(1);//Seteo el estado del usuario logueado en 1 (Creando pedido)
                userLogueado.setPasswordConfirmation(userLogueado.getPassword());//Lo importante es poner algo, no se guarda en la base de datos
                userService.updateUser(userLogueado);//Actualizo el usuario logueado en la base de datos
            }

            Pedido pedidoEnProceso = pedidoService.buscarPeidoSinCamara(userLogueado);//Busco el pedido en proceso del usuario logueado
            List<PedidoProducto> relacionesPedido = pedidoProductoService.buscarPorPedido(pedidoEnProceso.getId());
            viewModel.addAttribute("carrito", relacionesPedido);//Inserto el pedido en proceso en el modelo

            viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario logueado a la pagina
            viewModel.addAttribute("userProveedor", userProveedor);//Inserto el usuario proveedor a la pagina
            viewModel.addAttribute("pedidoEnProceso", pedidoEnProceso);//Inserto el pedido en proceso a la pagina

            viewModel.addAttribute("camara", camaraActual);//Tambien voy a mandar la camara en el modelo, en el otro get que comparte la plantilla lo mande como @ModelAtribute
            session.setAttribute("camara", camaraActual);
            //Esto lo mando en el modelo para poder adaptar el codigo del PostMapping de armar el pedido

            viewModel.addAttribute("anexandoComprador", "true");//aca puede ir cualquier cosa por ahora, es solo para validar si buscar un el pedido en la bd o sacarlo del modelo

            return "paginas_comprador/catalogoProveedorPage";
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

    }

    //POST PARA ANEXAR UN USUARIO A UNA CAMARA
    @PostMapping("/unirse/agregarUsuario/{idCamara}")//Se puede hacer sin idCamara pero lo pongo igual pero tranquilamente se podria enviar el post sin ese id porque tengo la camara en la session
    public String anexarUsuario(@PathVariable("idCamara") Long idCamara,
                                HttpSession session){
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);//Busco el usuario logueado que va a abrir la camara
            Camara camaraActual = (Camara) session.getAttribute("camara");//Busco la camara actual que se esta trabajando
            camaraActual.getParticipantes().add(userLogueado);//Agrego el usuario logueado a la lista de participantes de la camara

            Pedido pedidoEnProceso = pedidoService.buscarPeidoSinCamara(userLogueado);//Busco el pedido en proceso del usuario logueado
            pedidoEnProceso.setCamara(camaraActual);//Seteo la camara al pedido en proceso
            pedidoService.crearPedido(pedidoEnProceso);//Actualizo el pedido en proceso en la base de datos

            //*---------------------------------
            //Aca debe estar el codigo para actualizar el precio de los pediodos que ya tiene la camara y los descuentos de las relaciones

            List<Pedido> pedidosEnLaCamara = pedidoService.buscarPedidosPorCamara(camaraActual.getId());//Busco los pedidos que tiene la camara

            for(Pedido pedido : pedidosEnLaCamara){//Busco los registros de relacion entre pedido y producto para cada pedido de la camara
                if(pedido.getId() != pedidoEnProceso.getId()){
                    for (PedidoProducto relacion : pedidoProductoService.buscarPorPedido(pedido.getId())) {
                        //Aca va el codigo para actualizar el precio de los pedidos que ya tiene la camara y los descuentos de las relaciones
                        //System.out.println(relacion.getCantidad());
                        //--------------------
                        for(Producto producto : pedidoEnProceso.getProductos()){
                            if(relacion.getProducto() == producto){
                                System.out.println(producto.getNombre());
                                //Aca tengo que actualizar la relación, borrarle el precio que tenia y ponerle el nuevo con el nuevo descuento
                                //Tambien debo actualizar el pedido, restarle al total el precio que tenia de esa relacion con su descuento anterior y sumarle el nuevo precio con el nuevo descuento
                                //Luego guardar en la bd tanto la relacion como el pedido
                                double precioAnterior = relacion.getPrecioProductos();
                                relacion.setDescuentoVigente(pedidoProductoService.buscarProductoEnPedido(producto.getId(), pedidoEnProceso.getId()).getDescuentoVigente());
                                relacion.setPrecioProductos(producto.getPrecio() * relacion.getCantidad() * (1 - (relacion.getDescuentoVigente() / 100)));

                                pedido.setPrecioTotal(pedido.getPrecioTotal() - precioAnterior + relacion.getPrecioProductos());
                                pedidoService.crearPedido(pedido);
                                pedidoProductoService.crearRelacion(relacion);
                            }
                        }

                    }
                }

            }
            System.out.println("-----");

            //---------------------------

            camaraService.createCamara(camaraActual);//Actualizo la camara en la base de datos

            userLogueado.setEstado(0);//Seteo el estado del usuario logueado en 0 (Genérico)
            userLogueado.setPasswordConfirmation(userLogueado.getPassword());//Lo importante es poner algo, no se guarda en la base de datos
            userService.updateUser(userLogueado);//Actualizo el usuario logueado en la base de datos

            session.removeAttribute("camara");//Borro la camara de la session (Con esto me tiró el error que me hizo cambiar de LAZY A EAGER en la relacion de camara con participantes)
                                                //Masomenos entiendo como fue, porque estamos tratando de acceder a las relaciones de la tabla principal que es camara una vez cerrada la sesión, igual debo reforzar este tema.
            return "redirect:/camara/"+idCamara;//Redirijo a la pagina de la camara
        }else{
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

    }
}
