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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Matthew Wang
 */
@WebServlet(name = "ECommerce_PaymentServlet", urlPatterns = {"/ECommerce_PaymentServlet"})
public class ECommerce_PaymentServlet extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
      /* TODO output your page here. You may use following sample code. */
      HttpSession session = request.getSession();
      ArrayList<ShoppingCartLineItem> cart = (ArrayList<ShoppingCartLineItem>) session.getAttribute("shoppingCart");
      ArrayList<Integer> qtyLeft = (ArrayList<Integer>) session.getAttribute("qtyLeft");
      long countryID = (long) session.getAttribute("countryID");
      long memberID = (long) session.getAttribute("memberID");
      boolean result = false;
      double totalPrice = 0;
      
      for (ShoppingCartLineItem item : cart) {
        totalPrice += item.getPrice() * item.getQuantity();
      }
      
      long salesRecordID = createSalesRecord(memberID, countryID, totalPrice);
      if (salesRecordID != 0) {
        for (ShoppingCartLineItem item : cart) {
          result = createLineItemRecord(salesRecordID, Long.parseLong(item.getId()), item.getQuantity(), countryID);
          if (!result) {
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Failed to create line item record.");
            break;
          }
        }
        //After transaction clear the cart of items
        cart.clear();
        qtyLeft.clear();
        session.setAttribute("qtyLeft",qtyLeft);
        session.setAttribute("shoppingCart", cart);
        response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg="
                + "Thank you for shopping at Island Furniture. You have checkout successfully!");
      } else {
        response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Transaction failed.");
      }
    } catch (Exception ex) {
      response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Failed to checkout.");
    }
  }
  
  private long createSalesRecord(long memberID, long countryID, double totalPrice) {
    try {
      Client client = ClientBuilder.newClient();
      WebTarget target = client
              .target("http://localhost:8080/IslandFurnitureTeam1WS/webresources/commerce")
              .path("createEcommerceTransactionRecord")
              .queryParam("memberID", memberID)
              .queryParam("countryID", countryID)
              .queryParam("amountPaid", totalPrice);
      Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
      Response res = invocationBuilder.put(Entity.entity(String.valueOf(memberID), MediaType.APPLICATION_JSON));
      if (res.getStatus() == 201) {
        long salesRecordID = Long.parseLong(res.readEntity(String.class));
        return salesRecordID;
      }
      else {
        return 0;
      }
    } catch (Exception ex) {
      return 0;
    }
  }
  
  private boolean createLineItemRecord(long salesRecordID, long itemID, int quantity, long countryID) {
    try {
      Client client = ClientBuilder.newClient();
      WebTarget target = client
              .target("http://localhost:8080/IslandFurnitureTeam1WS/webresources/commerce")
              .path("createECommerceLineItemRecord")
              .queryParam("salesRecordID", salesRecordID)
              .queryParam("itemID", itemID)
              .queryParam("quantity", quantity)
              .queryParam("countryID", countryID);
      Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
      Response res = invocationBuilder.put(Entity.entity(String.valueOf(itemID), MediaType.APPLICATION_JSON));
      
      return res.getStatus() == 201;
    } catch (Exception ex) {
      return false;
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
