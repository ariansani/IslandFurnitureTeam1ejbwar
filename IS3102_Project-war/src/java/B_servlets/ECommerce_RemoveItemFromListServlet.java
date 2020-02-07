/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import HelperClasses.ShoppingCartLineItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Arian
 */
@WebServlet(name = "ECommerce_RemoveItemFromListServlet", urlPatterns = {"/ECommerce_RemoveItemFromListServlet"})
public class ECommerce_RemoveItemFromListServlet extends HttpServlet {

    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           
            HttpSession session = request.getSession();
             ArrayList<ShoppingCartLineItem> cart = (ArrayList<ShoppingCartLineItem>)(session.getAttribute("shoppingCart"));
             String result;
             int index=0;
             String[] SKUchecked=request.getParameterValues("delete");
        
             List<Integer> intList= new ArrayList<Integer>();
                for (String SKU : SKUchecked){
                for (ShoppingCartLineItem cartItem : cart){
                    if (cartItem.getSKU().equals(SKU))
                       index=cart.indexOf(cartItem);
                }
                intList.add(index);
            }
            Collections.sort(intList, Collections.reverseOrder());     
                
            for(int i : intList){
                cart.remove(i);
            }
                
            
            session.setAttribute("shoppingCart", cart);
            
            result="Item successfully removed from the cart!";
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
            
            
            
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
