/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Matthew Wang
 */
@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            String id = request.getParameter("id");
            String SKU = request.getParameter("SKU");
            double price = Double.parseDouble(request.getParameter("price"));
            String name = request.getParameter("name");
            String imageURL = request.getParameter("imageURL");

            ArrayList<ShoppingCartLineItem> shoppingCart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
            if (shoppingCart == null) {
              shoppingCart = new ArrayList<>();
              session.setAttribute("shoppingCart", shoppingCart);
            }
            
            Long countryID = (long) session.getAttribute("countryID");
            int stock = checkStockQuantity(SKU, countryID);
            int itemId = 0;
            boolean check = false;
            String result;

            for (int i = 0; i < shoppingCart.size(); i++) {
                if (shoppingCart.get(i).getSKU().equals(SKU)) {
                    check = true;
                    itemId = i;
                }
            }
            if (check) {
                if (shoppingCart.get(itemId).getQuantity() < stock) {
                    shoppingCart.get(itemId).setQuantity(shoppingCart.get(itemId).getQuantity() + 1);
                    session.setAttribute("shoppingCart", shoppingCart);
                    result = "Item successfully added into the cart!";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
                } else {
                    result = "Item not added to cart, not enough quantity available!";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
                }
            } else if (stock != 0) {
                ShoppingCartLineItem item = new ShoppingCartLineItem();
                item.setCountryID(countryID);
                item.setId(id);
                item.setImageURL(imageURL);
                item.setName(name);
                item.setPrice(price);
                item.setSKU(SKU);
                item.setQuantity(1);

                shoppingCart.add(item);

                session.setAttribute("shoppingCart", shoppingCart);
                result = "Item successfully added into the cart!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
            } else {
                result = "Item not added to cart, not enough quantity available!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
            }
        }

    }

    public int checkStockQuantity(String SKU, long countryID) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IslandFurnitureTeam1WS/webresources/entity.countryentity/")
                    .path("getQuantity")
                    .queryParam("SKU", SKU)
                    .queryParam("countryID", countryID);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            return Integer.parseInt(result);
        } catch (Exception e) {
            e.printStackTrace();
            int result = 0;
            return result;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
