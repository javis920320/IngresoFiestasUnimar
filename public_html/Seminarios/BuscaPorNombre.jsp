<!DOCTYPE html>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="java.sql.*"%>
<jsp:useBean id="d" class="Gral.Data" scope="request"/>
<jsp:useBean id="s" class="Sistemas.Seminario" scope="request"/>
<%
    // Conexion 
    Connection conexion = d.getConnection();
    // Saber el seminario que se encuentra actio 
    int codigo = s.getCodSeminario(conexion);
    // Nombre del seminario
    String nombre = s.getNomSeminario(codigo, conexion);
    // Apellidos o nombres de la persona que se desea buscar
    String participante = request.getParameter("txtBus");
    if(participante == null)
        participante = "";
        
    String resultados = "";
    if(!participante.equals("")){
        resultados = s.buscarPorNombre(participante, codigo, conexion);
    }
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Buscar Participante por Nombre</title>        
        <style type="text/css">
            .style1
            {
                width: 757px;
            }
            .style2
            {
                width: 90%;                
            }
            body {
                font-family:Arial;
                font-size: small;
                text-shadow: none;
                text-decoration: none;
                text-transform: none;
                letter-spacing: 0em;
                word-spacing: 0em;
                line-height: 1.2;
            }
            
            #caja {
               border-radius:10px; -moz-border-radius:10px; /* Firefox */ -webkit-border-radius:10px; /* Safari y Chrome */ /* Otros estilos */ border:0px solid #333; background: White ; width:100%; padding:5px;
               opacity:0.6;
               filter:alpha(opacity=40);
               width: 669px; height: 347px;
               position: absolute;
               z-index: 1;
               left:45px;               
            }
            
            #contenido {               
               opacity:1;
               filter:alpha(opacity=100);
               position: absolute;
               z-index: 3;
               margin: 10px 10px 10px 10px;
               left: 55px;
            }
            
            #hor-zebra
            {
                    font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
                    font-size: 12px;                                        
                    text-align: left;
                    border-collapse: collapse;
            }
            #hor-zebra th
            {
                    font-size: 14px;
                    font-weight: normal;
                    padding: 10px 8px;
                    color: #039;                    
            }
            #hor-zebra td
            {
                    padding: 8px;
                    color: Black;
            }
            #hor-zebra .odd
            {
                    background: #e8edff; 
            }
            
        </style>
    </head>
    <body background="../images/UNIMAR.JPG" onload="setfocus();">
        <form name="frm" action="">
            <div>
                <table cellpadding="0" cellspacing="0" class="style1">
                    <tr>
                        <td><img src="../images/logo.png" class="style1"/></td>
                    </tr>
                    <tr style="background-image: url('../images/fondo.gif')">
                        <td>
                            <div style="vertical-align: bottom; width: 100%; height: 547px;">
                                <br />
                                <br />
                                <table align="center" cellpadding="0" cellspacing="0" class="style2">
                                    <tr>
                                        <td colspan="4">
                                            &nbsp;
                                        </td>                                    
                                    </tr>
                                    <tr>
                                        <td align="center" colspan="4">
                                        <strong><%=nombre%></strong><br />
                                        <br />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="4" align="center">
                                            <strong>Apellidos / Nombres:</strong>
                                            <input type="text" id="txtBus" name="txtBus" value="" BackColor="#99CCFF" BorderColor="#0033CC" ForeColor="Black"/>&nbsp;&nbsp;&nbsp;                                     
                                            <input type="submit" id="btnBus" name="btnBus" value="Buscar" BackColor="#95AFFF" Font-Bold="True" ForeColor="Black" Height="26px"/>                                                                                                                                                
                                        </td>
                                    </tr> 
                                    <tr>
                                        <td colspan="4" align="center">
                                        <br/>
                                            <%
                                                if(!resultados.equals(""))
                                                {
                                                    out.print(resultados);
                                                }
                                            %>
                                        </td>
                                    </tr>                                    
                                </table>
                            </div>
                        </td>
                    </tr>                    
                    <tr>
                        <td><img src="../images/bottom.png" class="style1" width="100%"/></td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>