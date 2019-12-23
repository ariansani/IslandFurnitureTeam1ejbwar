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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author canyo
 */
@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {

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
            
        // get values from form
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String city = request.getParameter("country");
        String address = request.getParameter("address");
        int securityQn = Integer.parseInt(request.getParameter("securityQuestion"));
        String securityAns = request.getParameter("securityAnswer");
        int age = Integer.parseInt(request.getParameter("age"));
        int income = Integer.parseInt(request.getParameter("income"));
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
        HttpSession session = request.getSession();
        
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/memberws")
                .path("updateMemberProfile")
                .queryParam("name", name)
                .queryParam("phone", phone)
                .queryParam("city", city)
                .queryParam("address", address)
                .queryParam("securityQn", securityQn)
                .queryParam("securityAns", securityAns)
                .queryParam("age", age)
                .queryParam("income", income)
                .queryParam("password", password)
                .queryParam("email",email);
        
        Invocation.Builder invocationBuilder = target.request();
        Response res = invocationBuilder.put(Entity.entity("", "application/json"));
        
        String msg;
        
        if(res.getStatus() == Response.Status.OK.getStatusCode()) {
            msg = "Account updated successfully";
        }else {
            msg = "Failed to update account.";
        }
        session.setAttribute("updateMsg", msg);
        response.sendRedirect("ECommerce_GetMember");
            
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
