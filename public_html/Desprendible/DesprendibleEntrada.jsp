<!DOCTYPE html>
<%  response.setHeader("Cache-control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires",0);%>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.sql.*"%>
<jsp:useBean id="d" class="Gral.Data" scope="request"/>
<jsp:useBean id="in" class="Sistemas.ReInformacion" scope="request"/>
<%
    Connection conexion = d.getConnection();
    String cod_est = request.getParameter("sCod");
    //cod_est="59814301";
    int perfil = in.getPerfil(conexion, cod_est);
    boolean puedeImprimir = false;
    String consulta = "";
    if(perfil == 3)
        puedeImprimir = in.estaMatriculado(conexion, cod_est);        
    else if(perfil == 11)
        puedeImprimir = true;
    else
        puedeImprimir = in.estaContratado(conexion, cod_est);
        
    if(puedeImprimir)
    {
        if(perfil == 3 || perfil == 11)
        {
            consulta = "select e.cod_est codigo, e.nombres || ' ' || e.apellidos nombres, e.identificacion, pr.nom_zonpro prog, m.semestre "+
                " from re_estudiante e, re_matricula m, programas pr "+
                " where e.cod_est = "+cod_est+""+
                "   and e.cod_est = m.cod_est "+
                "   and m.estado = 'M' "+
                "   and pr.cod_zonpro = m.cod_zonpro "+
                " order by m.num_mat desc";
        }
        else
        {
            consulta = "SELECT con.identificacion codigo, con.nombres||' '||con.primer_ap||' '||con.segundo_ap nombres, con.identificacion, "+
                        " cc.nom_centro prog, '-' semestre "+
                        " FROM recursosh.rh_contrato con, recursosh.rh_tipo_puesto tp,centro_costo cc "+
                        " WHERE(con.identificacion = '"+cod_est+"') "+
                        "   AND con.cod_tpuesto=tp.cod_tpuesto "+
                        "   AND cc.centro_costo= con.centro_costo "+
                        "   and con.activo = 1"+
                        " ORDER BY con.activo desc, con.f_final DESC";
        }
        
        String query = "SELECT ruta FROM fotos WHERE identificacion="+cod_est+" OR identificacion="+cod_est+" OR identificacion="+cod_est+"1 OR identificacion="+cod_est+"1";
        String ruta =  d.getValue(query, conexion);   
        if (ruta.equals(""))
        {
            query = "select identificacion  from re_estudiante where cod_est = "+cod_est;
            String identificacion = d.getValue(query, conexion);
            
            query = "SELECT ruta FROM fotos WHERE identificacion="+identificacion+" OR identificacion="+identificacion+" OR identificacion="+identificacion+"1 OR identificacion="+identificacion+"1";
            ruta =  d.getValue(query, conexion);
            
            if (ruta.equals(""))
            {
                ruta = "images/sin_imagen2.JPG";
            }
        }
        
        ruta = ruta.replace("\\", "/");        
        
        response.sendRedirect("http://serviap2010.umariana.edu.co/pzdocentes/pazsalvodoc?nr=44&con="+consulta+"&img="+ruta+"");
    }
    else
    {
        out.print("<script>alert('No puede generar la boleta de entrada.')</script>");
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Desprendible de Entrada</title>
    </head>
    <body></body>
</html>