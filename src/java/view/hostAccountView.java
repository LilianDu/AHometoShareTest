/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import business.HostBusinessLayer;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 29751
 */
public class hostAccountView extends HttpServlet {

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
       
        String email = request.getParameter("email");
        String btnUpdate = request.getParameter("update");
        String btnDelete = request.getParameter("delete");
   
        HostBusinessLayer hostBusiness = new HostBusinessLayer();

        if(btnUpdate != null){ //click update password button

            String old_password = request.getParameter("old_pwd");
            String new_password = request.getParameter("new_pwd");
            String confirm_new_pwd = request.getParameter("confirm_new_pwd");

           
            if(hostBusiness.passwordCorrect(email, old_password)){ //match old password
                if(new_password.equals(old_password))
                    request.setAttribute("new_pwd_info", "Please enter a different password with old one");
                    if(!confirm_new_pwd.equals(new_password))
                        request.setAttribute("confirm_pwd_info", "Please enter the matched new password.");
                else{//update password into database
                    try{
                        hostBusiness.updateHost(new_password,hostBusiness.getHostByEmail(email).getHostID()); 
                        
                        if(hostBusiness.passwordCorrect(email, new_password))
                           request.setAttribute("update_info", "Update password successfully.");
                        else 
                           request.setAttribute("update_info", "Update password was not success."); 
                    }catch(Exception e){
                        request.setAttribute("update_info", "Update password failed.Please check database connection");
                    }
                    
                }
                RequestDispatcher rd = request.getRequestDispatcher("hostAccountSettings.jsp");  
                rd.forward(request,response);
            }
            else{
                request.setAttribute("old_pwd_info", "Please enter a correct current password");
                RequestDispatcher rd = request.getRequestDispatcher("hostAccountSettings.jsp");  
                rd.forward(request,response);  
            }
        }
        if(btnDelete != null){ // click delete account button
            hostBusiness.deleteHost(hostBusiness.getHostByEmail(email).getHostID());  
            response.sendRedirect("index.jsp");
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
