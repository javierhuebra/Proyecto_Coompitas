package com.proyecto.coompitas.controllers;

import com.proyecto.coompitas.models.*;
import com.proyecto.coompitas.services.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CamaraController {

    private final UserService userService;
    private final CamaraService camaraService;

    private final PedidoService pedidoService;
    private final PedidoProductoService pedidoProductoService;
    private final EmailService emailService;
    private final DireccionService direccionService;

    public CamaraController(UserService userService,
                            CamaraService camaraService,
                            PedidoService pedidoService,
                            PedidoProductoService pedidoProductoService,
                            EmailService emailService,
                            DireccionService direccionService) {
        this.userService = userService;
        this.camaraService = camaraService;
        this.pedidoService = pedidoService;
        this.pedidoProductoService = pedidoProductoService;
        this.emailService = emailService;
        this.direccionService = direccionService;
    }


    //GET PARA RENDERIZAR LA PÁGINA DE LOS PROVEEDORES PARA CREAR LA CAMARA
    @GetMapping("/camara/proveedores")
    public String renderProveedores(HttpSession session, Model viewModel) {

        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);
            if (userLogueado.getRolUsuario() != 1) {
                System.out.println("No es un comprador, vuelve a home");
                return "redirect:/home";
            }

            viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario logueado a la pagina renderProveedoresPage
            viewModel.addAttribute("usersProveedor", userService.findAllUsersByRol(2));//Inserto todos los usuarios con rol proveedor en el modelo

            return "paginas_comprador/renderProveedoresPage";
        } else {
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }

    }

    //GET PARA LOS PRODUCTOS DE CADA PROVEEDOR (PARA CREAR LA CAMARA)
    @GetMapping("/camara/proveedores/catalogo/{idProveedor}")
    public String renderCatalogoProveedores(@PathVariable("idProveedor") Long idProveedor,
                                            @ModelAttribute("camara") Camara camara,//Podria pasar que primero se carguen el y despues se cree la camara, hay que ver... en proceso
                                            HttpSession session,
                                            Model viewModel) {

        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);//Busco el usuario logueado que va a abrir la camara
            User userProveedor = userService.findUserById(idProveedor);//Busco el usuario proveedor para acceder a sus productos y demas datos

            //Aca se podria poner una validacion que detecte a que proveedor se le esta haciendo el pedido por si cambian de pantalla(posiblemente mas adelante sea necesario)

            if (userLogueado.getEstado() != 1) { //Comprueba el estado del usuario logueado para ver si creamos un pedido vacio en este get o no
                Pedido pedidoVacio = new Pedido();//Creo un pedido vacio en este GET para que este disponible en el controlador de pedido y actualizarlo

                pedidoVacio.setComprador(userLogueado);//Seteo el usuario logueado como comprador del pedido
                pedidoService.crearPedido(pedidoVacio);//Guardo el pedido en la base de datos //Esto hay que sacarlo, hay que hacerlo inyectandolo al modelo y luego sacandolo del modelo cuando lo necesitemos asi no guaramos un pedido que no sabemos si va a proliferar en la base de datos

                userLogueado.setEstado(1);//Seteo el estado del usuario logueado en 1 (Creando pedido)
                userLogueado.setPasswordConfirmation(userLogueado.getPassword());//Lo importante es poner algo, no se guarda en la base de datos
                userService.updateUser(userLogueado);//Actualizo el usuario logueado en la base de datos
            }

            Pedido pedidoEnProceso = pedidoService.buscarPeidoSinCamara(userLogueado);//Busco el pedido en proceso del usuario logueado

            //Valido que el usuario logueado tenga productos de este proveedor -----------------
            if(pedidoEnProceso.getProductos() != null && !pedidoEnProceso.getProductos().isEmpty()){
                for (Producto producto : pedidoEnProceso.getProductos()) {
                    if (producto.getProveedor() != userProveedor) {
                        System.out.println("El usuario logueado no tiene productos de este proveedor");

                        List<Producto> listaProductos = new ArrayList<>();
                        pedidoEnProceso.setProductos(listaProductos);
                        pedidoService.crearPedido(pedidoEnProceso);//Actualizo el pedido en proceso en la base de datos
                        //return "redirect:/camara/proveedores/catalogo/"+producto.getProveedor().getId()+"?error=1";
                    }
                }
            }

            List<PedidoProducto> relacionesPedido = pedidoProductoService.buscarPorPedido(pedidoEnProceso.getId());
            viewModel.addAttribute("carrito", relacionesPedido);//Inserto el pedido en proceso en el modelo


            viewModel.addAttribute("userLogueado", userLogueado);//Inserto el usuario logueado a la pagina
            viewModel.addAttribute("userProveedor", userProveedor);//Inserto el usuario proveedor a la pagina
            viewModel.addAttribute("pedidoEnProceso", pedidoEnProceso);//Inserto el pedido en proceso a la pagina
            return "paginas_comprador/catalogoProveedorPage";
        } else {
            System.out.println("No hay usuario logueado");
            return "redirect:/login";
        }
    }

    //POST PARA CREAR UNA CAMARA
    @PostMapping("/camara/proveedores/crear/{idProveedor}")
    public String crearCamara(@PathVariable("idProveedor") Long idProveedor,
                              @ModelAttribute("camara") Camara camara,
                              @RequestParam("direccionId") Long direccionId,
                              HttpSession session) throws MessagingException {
        Long idLogueado = (Long) session.getAttribute("idLogueado");
        if (idLogueado != null) {
            User userLogueado = userService.findUserById(idLogueado);//Busco el usuario logueado que va a abrir la camara
            User userProveedor = userService.findUserById(idProveedor);//Busco el usuario proveedor

            camara.setCreador(userLogueado);//Seteo el usuario logueado como creador de la camara
            camara.setProveedor(userProveedor);//Seteo el usuario proveedor como proveedor de la camara, los otros valores vinieron en el modelo con el formulario
            camara.setDireccionEnvio(direccionService.findDireccionById(direccionId));//Seteo la direccion de envio de la camara
            camara.setEstadoDeLaCamara(1);

            //Agrego al usuario logueado como participante de la camara
            List<User> participantesDeLaCamara = new ArrayList<>(); //Como la lista de participantes esta vacia (null) la inicializo para poder agregarle el usuario logueado
            participantesDeLaCamara.add(userLogueado);
            camara.setParticipantes(participantesDeLaCamara);
            System.out.println(camara.getParticipantes().size());

            Pedido pedidoEnProceso = pedidoService.buscarPeidoSinCamara(userLogueado);//Busco el pedido en proceso del usuario logueado

            camaraService.createCamara(camara);//Creo la camara en la base de datos

            pedidoEnProceso.setCamara(camara);//Seteo la camara al pedido en proceso

            pedidoService.crearPedido(pedidoEnProceso);//Actualizo el pedido en proceso en la base de datos

            userLogueado.setEstado(0);//Seteo el estado del usuario logueado en 0 (Genérico)
            userLogueado.setPasswordConfirmation(userLogueado.getPassword());//Lo importante es poner algo, no se guarda en la base de datos
            userService.updateUser(userLogueado);//Actualizo el usuario logueado en la base de datos

            //Envio un correo al proveedor avisandole que se inicio una camara ---------------------------------------------------------------------
            String contenidoHtml = "<html><head><style>body { font-family: Arial, sans-serif; background-color: #f0f0f0; } " +
                    "h1 { color: #FFFBEB;background-color: #251749;} p { color: #263159; }</style></head><body>" +
                    "<h1>" + "La empresa " + userLogueado.getNombreEmpresa() + " ha iniciado una cámara" + "</h1>" +
                    "<p>Felicitaciones, están juntando compradores para hacerte un gran pedido!</p>" +
                    "<a href='https://www.youtube.com/'>www.coompas.com</a>" +
                    "</body></html>";

            try {//PARA HACER UN LOADER ACA HAY QUE EVALUAR CON JAVASCRIPT LA PETICION POST Y MIENTRAS ESPERAMOS LA RESPUESTA PONER VISIBLE EL LOADER
                emailService.enviarCorreo(camara.getProveedor().getEmail(), "La empresa " + userLogueado.getNombreEmpresa() + " ha iniciado una cámara", contenidoHtml);
                System.out.println("Correo enviado");
            } catch (MessagingException e) {
                System.out.println("Error al enviar el correo");

            }
            //------------------------------------------------------------------------------------------------------------------------------------

        }
        return "redirect:/home";
    }
}
