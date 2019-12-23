/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
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
import javax.ws.rs.client.WebTarget;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Arian
 */
@WebServlet(name = "ECommerce_GetMember", urlPatterns = {"/ECommerce_GetMember"})
public class ECommerce_GetMember extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet ECommerce_GetMember</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ECommerce_GetMember at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");

//CA4
              HttpSession session = request.getSession();
              String email= (String) request.getAttribute("memberEmail");
              
        
              Client client = ClientBuilder.newClient();
              WebTarget target = client
                      .target("http://localhost:8080/IS3102_WebService-Student/webresources/memberws")
                      .path("getMemberProfile")
                      .queryParam("email",email);
              Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
              Response res = invocationBuilder.get();
              
             
              if(res.getStatus()==Response.Status.OK.getStatusCode()){
                  Member member = res.readEntity(new GenericType<Member>(){
                  });
                  
                  String memberName = member.getName();
                  
                  session.setAttribute("member",member);
                  session.setAttribute("memberName",memberName);
                  
                  String url="/IS3102_Project-war/B/SG/memberProfile.jsp";
                  
                  String updateMsg = (String) session.getAttribute("updateMsg");
                  
                  if(updateMsg != null){
                      if(updateMsg.contains("success")){
                          url+= "?goodMsg=" + updateMsg;
                      }
                      else{
                          url+="?errMsg="+ updateMsg;
                      }
                      
                      session.removeAttribute("updateMsg");
                      
                  }
                  
                  response.sendRedirect(url);
                  
        
              }
              
              



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
