package com.example.app1;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MemberServletTest {

    private MemberServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new MemberServlet();
        servlet.init();
    }

    @Test
    void doGetReturns404WhenIdIsMissing() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).setContentType("text/html; charset=UTF-8");
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found");
        verify(response, never()).getWriter();
    }

    @Test
    void doGetReturns404WhenIdIsUnknown() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn("unknown");

        servlet.doGet(request, response);

        verify(response).setContentType("text/html; charset=UTF-8");
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found");
        verify(response, never()).getWriter();
    }

    @Test
    void doGetRendersMemberPageForKnownId() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter html = new StringWriter();

        when(request.getParameter("id")).thenReturn("lemeshynskyi");
        when(response.getWriter()).thenReturn(new PrintWriter(html));

        servlet.doGet(request, response);

        verify(response).setContentType("text/html; charset=UTF-8");
        verify(response, never()).sendError(anyInt(), anyString());

        String page = html.toString();
        assertTrue(page.contains("<h1>Лемешинський Олексій Сергійович</h1>"));
        assertTrue(page.contains("href=\"index.html\""));
        assertTrue(page.contains("href=\"member?id=miloserdov\""));
        assertTrue(page.contains(">Мілосердов В.В.<"));
        assertTrue(page.contains("href=\"https://kpi.ua\""));
    }
}
