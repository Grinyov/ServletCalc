package servlets;

import calc.TestObject;
import calc.CalcOperations;
import calc.OperationType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by green on 05.10.2015.
 *
 * Наш сервлет-калькулятор
 *
 * Для выполнений операций нужно через параметры передавать ему операнды и желаемую операцию над ними
 *
 */
public class Calc extends HttpServlet {

    public static final String SESSION_MAP = "sessionMap";
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

        // мапа для записи операций из контекста

        Map<String, List> sessionMap = (Map<String,List>)request.getServletContext().getAttribute(SESSION_MAP);

        if (sessionMap==null) {
            sessionMap = new HashMap<String, List>();
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet CalcServlet</title>");
        out.println("</head>");
        out.println("<body>");

        try {

            // считывание параметров
            double one = Double.valueOf(request.getParameter("one"));
            double two = Double.valueOf(request.getParameter("two"));
            String opearation = request.getParameter("operation");

            // определение или создание сессии
            HttpSession session = request.getSession(true);

            request.getServletContext().setAttribute("obj", new TestObject("TestName"));


            // получение типа операции
            OperationType operType = OperationType.valueOf(opearation.toUpperCase());

            // калькуляция
            double result = calcResult(operType, one, two);

            ArrayList<String> listOperations;

            // для новой сессии создаем новый список
            if (session.isNew()) {
                listOperations = new ArrayList<String>();
            } else { // иначе получаем список из атрибутов сессии
                listOperations = (ArrayList<String>) session.getAttribute("formula");
            }

            // добавление новой операции в список и атрибут сессии
            listOperations.add(one + " " + operType.getStringValue() + " " + two + " = " + result);
            session.setAttribute("formula", listOperations);

            out.println("<table cellpadding=\"20\">");
            out.println("<tr>");
            out.println("<td style=\"vertical-align:top;\">");


            // вывод всех операций
            out.println("<h1>"+ session.getId() +"</h1>");

            for (String oper : listOperations) {
                out.println("<h3>" + oper + "</h3>");
            }

            sessionMap.put(session.getId(), listOperations);
            getServletContext().setAttribute(SESSION_MAP, sessionMap);


            out.println("</td>");
            out.println("<td style=\"vertical-align:top;\">");

            for (Map.Entry<String, List> entry : sessionMap.entrySet()) {
                String sessionId = entry.getKey();
                List listOper = entry.getValue();

                out.println("<h1 style=\"color:red\">" + sessionId + "</h1>");

                for (Object str : listOper) {
                    out.println("<h3>" + str + "</h3>");
                }


            }


            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");


        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);

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
        return "Сервлет - калькулятор";
    }// </editor-fold>

    // калькуляция
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