package cn.dendarii.ivan;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestAction extends BaseServlet {
    private static final long serialVersionUID = 1238154343508579921L;

    @Override
    public void process(HttpServletRequest request,
                        HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        // 打印拿到的参数
        Map<String, String[]> paramMap = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String[]> entry : paramMap.entrySet()) {
            sb.append(entry.getKey()).append(":");
            for (String val : entry.getValue()) {
                sb.append(val).append(",");
            }
            sb.setCharAt(sb.length() - 1, ';');
        }
        System.out.println(sb);
        // 向前端输出结果
        PrintWriter pw = response.getWriter();
        try {
            pw.write(new Random().nextInt() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        pw.flush();
        pw.close();
    }

}
