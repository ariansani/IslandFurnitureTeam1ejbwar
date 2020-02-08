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

/**
 *
 * @author canyo
 */
@WebServlet(name = "ECommerce_MinusFurnitureToListServlet", urlPatterns = {"/ECommerce_MinusFurnitureToListServlet"})
public class ECommerce_MinusFurnitureToListServlet extends HttpServlet {

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
        String result;
        try (PrintWriter out = response.getWriter()) {
          HttpSession session = request.getSession();
           String SKU = request.getParameter("SKU");
            ArrayList<ShoppingCartLineItem> cart = (ArrayList<ShoppingCartLineItem>) (session.getAttribute("shoppingCart"));
            String responseStr="";
            for (int i = 0; i < cart.size(); i++){
                if(cart.get(i).getSKU().equals(SKU)){
                    if(cart.get(i).getQuantity() == 1){
                        cart.remove(i);
                        result = "Item removed from cart!";
                        responseStr=("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
                    }
                    else{
                        result = "Item quantity reduced!";
                        cart.get(i).setQuantity(cart.get(i).getQuantity() - 1);
                        responseStr=("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
                    }
                    break;
                }
            }                           
            session.setAttribute("shoppingCart", cart);
            response.sendRedirect(responseStr);    
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
