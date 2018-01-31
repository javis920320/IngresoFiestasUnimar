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
    // Mensaje a visualizar
    String msg = "";    
    // Identificación de la persona que se registro
    String cod_est = request.getParameter("txtBus");
    if(cod_est == null)
        cod_est = "";
    // Define si existen datos registrados y mostrar el panel de historico
    int existenDatos = 0;
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title> Ingreso a Seminarios </title>
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
        <script language="javascript">
            var nav4 = window.Event ? true : false;
            function acceptNum(evt)
            {	
              // NOTE: Backspace = 8, Enter = 13, '0' = 48, '9' = 57, '.'= 46
              var key = nav4 ? evt.which : evt.keyCode;	
              //return (key <= 13 || (key >= 48 && key <= 57) || key==46);
              return (key <= 13 || (key >= 48 && key <= 57));
            }
                
            function setfocus() 
            { 
                document.frm.txtBus.focus(); 
                return; 
            } 
            
            function valida_frm(frm1)
            {
                if(confirm('Esta seguro de registrar el refrigerio?')){
                  if (trim(frm1.txtBus.value).length == 0)
                  {
                      alert("Debe pasar el codigo de barras del estudiante.");
                      document.frm1.txtBus.focus();
                      return 0;
                  }    
                  document.frm.op.value = "2";
                  document.frm.mostrar.value = "2";
                  document.frm.forzar.value = "1";
                  document.frm.submit();
                }
            }
            
            function trim(cadena)
		{
		  for(i=0; i<cadena.length; )
		  {
			if(cadena.charAt(i)==" ")
			  cadena=cadena.substring(i+1, cadena.length);
			else
			break;
		  }
		  for(i=cadena.length-1; i>=0; i=cadena.length-1)
		  {
			if(cadena.charAt(i)==" ")
			  cadena=cadena.substring(0,i);
			else
			break;
		  }
		  return cadena;
		}
                
                function valida(frm){
                    window.location.href = "BuscaPorNombre.jsp";
                }
        </script>
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
                                <td colspan="4" style="font-family:arial;color:red;font-size:15px;">
                                    <center><strong><%=msg%></strong></center>
                                </td>
                            </tr>
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
                                    <strong>Identificación:</strong>
                                    <input type="text" id="txtBus" name="txtBus" onkeypress="return acceptNum(event);" value="" BackColor="#99CCFF" BorderColor="#0033CC" ForeColor="Black"/>&nbsp;&nbsp;&nbsp;                                     
                                    <input type="submit" id="btnReg" name="btnReg" value="Registrar" BackColor="#95AFFF" Font-Bold="True" ForeColor="Black" Height="26px"/>  &nbsp;&nbsp;&nbsp;                                                                                                        
                                    <input type="button" id="btnRegNom" name="btnRegNom" value="Buscar por Nombre" onclick="valida();" BackColor="#95AFFF" Font-Bold="True" ForeColor="Black" Height="26px"/>                                                                                                         
                                </td>
                            </tr>  
                            <%
                                // Si el codigo del estudiante es diferente a vacio
                                if(!cod_est.equals("")){
                                    // Validar si esta inscrito al seminario para agregar el valor
                                    boolean inscrito = s.estaInscrito(cod_est, codigo, conexion);
                                    if(inscrito)
                                    {
                                        boolean registro = s.registrarIngresoSeminario(cod_est, codigo, conexion);
                                        if(registro)
                                            existenDatos = 1;
                                    }
                                %>                        
                            <tr>
                                <td colspan="4" align="center">
                                <br/>
                                <%=s.datosInscrito(cod_est, codigo, conexion)%>
                                </td>
                            </tr>
                              <%}
                            %>
                        </table>
                        </div>
                    </td>  
                    <%
                        if(existenDatos > 0 )
                        {
                    %>
                    <td>
                        <div style="vertical-align: bottom; width: 400px;" >
                        <div style="width: 400px; height: 400px;  overflow: scroll;" >
                            <br />
                            <table align="center" cellpadding="0" cellspacing="0" class="style2">     
                                <tr>
                                    <td align="center" colspan="4">
                                        <strong>HITORIAL INGRESO AL SEMINARIO</strong><br />   
                                        <br/>
                                    </td>
                                </tr>
                                <%if(!cod_est.equals(""))
                                {%>
                                <tr> 
                                    <td colspan="4" align="center">
                                    <%=s.darHistorialIngreso(cod_est, codigo, conexion)%>
                                    </td>
                                </tr>
                                <%}%>
                            </table>                            
                        </div>
                        </div>
                    </td>
                    <%}%>
                </tr>
                <tr>
                    <td><img src="../images/bottom.png" class="style1" width="100%"/></td>
                </tr>
            </table>
        </div>                
        <input type="HIDDEN" name="forzar" value="0"/>
    </form>  
    </body>
</html>