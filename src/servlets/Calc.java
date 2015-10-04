package servlets;

import calc.TestObject;
import calc.CalcOperations;
import calc.OperationType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by green on 05.10.2015.
 *
 * ��� �������-�����������
 *
 * ��� ���������� �������� ����� ����� ��������� ���������� ��� �������� � �������� �������� ��� ����
 *
 */
public class Calc extends HttpServlet {
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
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet CalcServlet</title>");
        out.println("</head>");
        out.println("<body>");

        try {

            // ���������� ����������
            double one = Double.valueOf(request.getParameter("one"));
            double two = Double.valueOf(request.getParameter("two"));
            String opearation = request.getParameter("operation");

            // ����������� ��� �������� ������
            HttpSession session = request.getSession(true);

            request.getServletContext().setAttribute("obj", new TestObject("TestName"));


            // ��������� ���� ��������
            OperationType operType = OperationType.valueOf(opearation.toUpperCase());

            // �����������
            double result = calcResult(operType, one, two);

            ArrayList<String> listOperations;

            // ��� ����� ������ ������� ����� ������
            if (session.isNew()) {
                listOperations = new ArrayList<String>();
            }
            else { // ����� �������� ������ �� ��������� ������
                listOperations = (ArrayList<String>) session.getAttribute("formula");
            }

            // ���������� ����� �������� � ������ � ������� ������
            listOperations.add(one + " " + operType.getStringValue() + " " + two + " = " + result);
            session.setAttribute("formula", listOperations);


            // ����� ���� ��������
            out.println("<h1>ID ����� ������ �����: " + session.getId() + "</h1>");

            out.println("<h3>������ �������� (�����:" + listOperations.size() + ") </h3>");

            for (String oper : listOperations) {
                out.println("<h3>" + oper + "</h3>");
            }


        } catch (Exception ex) {
            // �������������� ������������ � ������ ������
            out.println("<h3 style=\"color:red;\">�������� ������. ��������� ���������</h3>");
            out.println("<h3>����� ���������� ������ ���� one, two, operation</h3>");
            out.println("<h3>Operation ������ ��������� 1 �� 4 ��������: add, subtract, multiply, divide</h3>");
            out.println("<h3>�������� one � two ������ ���� �������</h3>");
            out.println("<h1>������</h1>");
            out.println("<h3>?one=1&two=3&operation=add</h3>");

        } finally {
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
        return "������� - �����������";
    }// </editor-fold>


    // �����������
    private double calcResult(OperationType operType, double one, double two) {
        double result = 0;
        switch (operType) {
            case ADD: {
                result = CalcOperations.add(one, two);
                break;
            }
            case SUBTRACT: {
                result = CalcOperations.subtract(one, two);
                break;
            }

            case DIVIDE: {
                result = CalcOperations.divide(one, two);
                break;
            }

            case MULTIPLY: {
                result = CalcOperations.multiply(one, two);
                break;
            }

        }

        return result;
    }
}
