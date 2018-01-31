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
    String cod_est = request.getParameter("txtBus");
    String consulta = "";
    boolean accion = false;
    String msg = "";
    int perfil = 0;
    String opcion = request.getParameter("op");
    String botonForzar="0";
    if(cod_est == null)
    {
        cod_est = "-1";
    }
    
    //1 Historial de ingreso 
    //2 Historial de refrigerios
    String mostrarPanel = request.getParameter("mostrar");
    if(mostrarPanel == null)
    {
        mostrarPanel = "0";
    }  
    
    if(mostrarPanel.equals("1"))
    {
        opcion = "1";
    }
    else
    {
        opcion = "2";
    }
    
    
    perfil = in.getPerfil(conexion, cod_est);
    
    boolean puedeRegistrarse = false;
    
    if(!cod_est.equals("-1"))
    {
        if(perfil == 3)
        {
            boolean estaMatriculado = in.estaMatriculado(conexion, cod_est);
            if(estaMatriculado)
                puedeRegistrarse = true;
        }    
        else if(perfil == 11)
            puedeRegistrarse = true;
        else  
        {
            boolean estaContratado = in.estaContratado(conexion, cod_est);
            if(estaContratado){
                puedeRegistrarse = true;
            }
            
            boolean esIngeniero = in.esIngeniero(conexion, cod_est);
            if(esIngeniero){
                puedeRegistrarse = true;
            }
        }
    }
    
    int valor = 0;
    
    if(puedeRegistrarse)
    {   
        consulta = "select sistemas.seq_ingreso_unimar.nextval from dual";
        long cod = Long.parseLong(d.getValue(consulta, conexion));
        consulta = "INSERT INTO ingreso_unimar VALUES ("+cod+", "+cod_est+", sysdate, "+opcion+")";
        if(opcion.equals("2"))
        {
            String forzar = request.getParameter("forzar");
            String query = "select count(*) from ingreso_unimar where tipo = 2 and cod_est = "+cod_est+" and trunc(sysdate) = trunc(fec_ingreso)";
            int vecesRecibido = Integer.parseInt(d.getValue(query, conexion));
            
            if(vecesRecibido > 0 && forzar.equals("0"))
            {
                out.print("<script>alert('Usted ya tiene registrado un refrigerio');</script>");
                botonForzar="1";
            }
            else if(vecesRecibido > 0 || forzar.equals("1"))
            {
                accion = d.executeSQL(conexion, consulta);
                valor = 1;
            }
            else if(vecesRecibido == 0 && forzar.equals("0"))
            {
                accion = d.executeSQL(conexion, consulta);
            }                   
        }
        else if(opcion.equals("1"))
        {
            String forzar = request.getParameter("forzar");
            String query = "select count(*) from ingreso_unimar where tipo = 1 and cod_est = "+cod_est+" and trunc(sysdate) = trunc(fec_ingreso)";
            int vecesRecibido = Integer.parseInt(d.getValue(query, conexion));
            if(vecesRecibido > 0 && forzar.equals("0"))
            {
                out.print("<script>alert('Ya realizo el ingreso a la Universidad Mariana');</script>");
                valor = 1;
                d.executeSQL(conexion, consulta);
            }    
            else
            {
                accion = d.executeSQL(conexion, consulta);
            } 
        }        
        
        if(accion)
        {
            if(perfil == 3)
                msg = "Ingreso de Estudiante Registrado Correctamente.";
            else if(perfil == 11)
                msg = "Ingreso de Egresado Registrado Correctamente.";
            else                
                msg = "Ingreso de Funcionario Registrado Correctamente.";
        }
        else
        {
            msg = "No se Realizado el Registro."; 
        }
    } 
    else if( !cod_est.equals("-1"))
    {
        msg = "No Registra como Funcionario o Estudiante Activo.";
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Ingreso Fiestas Unimar</title>
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
               width: 690px; height: 347px;
               position: absolute;
               z-index: 1;
               left:45px;               
            }
            
            .celda{
            height: auto;
            width: 330px;
            }
            
            #contenido {               
               opacity:1;
               filter:alpha(opacity=100);
               position: absolute;
               z-index: 3;
               margin: 10px 10px 10px 10px;
               left: 50px;
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
                  document.frm.forzar.value = "0";
                  document.frm.submit();
                }
            }
            
            function valida_forzar(frm)
            {
                if(confirm('Esta seguro de registrar el refrigerio?')){                  
                  document.frm.txtBus.value ="<%=cod_est%>";
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
        </script>
    </head>
    <body background="images/UNIMAR.JPG" onload="setfocus();">
    <%
    if(mostrarPanel.equals("1") || mostrarPanel.equals("2"))
    {%>    
    <form name="frm" action="">
        <div>
            <table cellpadding="0" cellspacing="0" class="style1">
                <tr>
                    <td><img src="images/logo.png" class="style1"/></td>
                </tr>
                <tr style="background-image: url('images/fondo.gif')">
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
                                    <strong>REGISTRO DE ENTRADA A LAS FIESTAS UNIMAR</strong><br />
                                    <br />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4" align="center">
                                    <strong>Codigo de Barras:</strong>
                                    <input type="text" id="txtBus" name="txtBus" onkeypress="return acceptNum(event);" value="<%if(valor ==1){out.print(""+cod_est+"");}else{out.print("");}%>" BackColor="#99CCFF" BorderColor="#0033CC" ForeColor="Black"/>&nbsp;&nbsp;&nbsp;                                     
                                    <%if(mostrarPanel.equals("1"))%>
                                        <input type="submit" id="btnReg" name="btnReg" value="Registrar" BackColor="#95AFFF" Font-Bold="True" ForeColor="Black" Height="26px"/>                                                                                                         
                                </td>
                            </tr>
                        <%
                        if(puedeRegistrarse)
                        {
                            if(!cod_est.equals("-1"))
                            {                               
                            %>
                            <tr>
                                <td colspan="4" align="center">
                                <br/>
                                <%=in.getDatos(conexion, cod_est)%>
                                </td>
                            </tr>
                            <%}
                        }%>
                        </table>
                        </div>
                    </td>  
                    <%
                    if(mostrarPanel.equals("1"))
                    {
                    %>
                    <td>
                        <div style="vertical-align: bottom; width: 400px;" >
                        <div style="width: 400px; height: 400px;  overflow: scroll;" >
                            <br />
                            <table align="center" cellpadding="0" cellspacing="0" class="style2">                                                           
                                
                                <tr>
                                    <td align="center" colspan="4">
                                        <strong>HITORIAL INGRESO UNIMAR</strong><br />   
                                        <br/>
                                    </td>
                                </tr>
                                <%if(!cod_est.equals("-1"))
                                {%>
                                <tr> 
                                    <td colspan="4" align="center">
                                    <%=in.darHistorialIngresos(conexion, cod_est)%>
                                    </td>
                                </tr>
                                <%}%>
                            </table>                            
                        </div>
                        </div>
                    </td>  
                    <%}
                    if(mostrarPanel.equals("2"))
                    {
                    %>
                    <td>
                        <div style="vertical-align: bottom; width: 270px;" >
                        <div style="width: 260px; height: 400px;  " >
                            <br />
                            <table align="center" cellpadding="0" cellspacing="0" class="style2" >
                                <tr>
                                    <td align="center" colspan="4">
                                        <strong>REFRIGERIOS ENTREGADOS</strong><br />   
                                        <br/>
                                    </td>
                                </tr>
                                <%if(!cod_est.equals("-1"))
                                {%>
                                <tr> 
                                    <td colspan="4" align="center">
                                    <%=in.darHistorialRefrigerios(conexion, cod_est)%>
                                    </td>
                                </tr>
                                <%}%>
                                <tr>                                    
                                    <td align="center">     
                                        <br/>
                                        <br/>
                                        <input type="button" id="btnRegRef" name="btnRegRef" onclick="valida_frm(frm);" value="Registrar Refrigerio" BackColor="#95AFFF" Font-Bold="True" ForeColor="Black" Height="26px"/>                                                                                                                                                 
                                    </td>
                                </tr>
                                <tr>
                                    <%if(botonForzar.equals("1"))
                                    {%>
                                    <td align="center">     
                                        <br/>
                                        <br/>
                                        <input type="button" id="btnRegForzado" name="btnRegForzado" onclick="valida_forzar(frm);" value="Registrar Refrigerio Forzado" BackColor="#95AFFF" Font-Bold="True" ForeColor="Black" Height="26px"/>                                                                                                                                                 
                                    </td>
                                    <%}%>
                                </tr>
                            </table>                            
                        </div>
                        </div>
                    </td>
                    <%}%>
                </tr>
                <tr>
                    <td><img src="images/bottom.png" class="style1" width="100%"/></td>
                </tr>
            </table>
        </div>
        <input type="HIDDEN" name="op" value="<%=opcion%>"/>
        <input type="HIDDEN" name="mostrar" value="<%=mostrarPanel%>"/>
        <input type="HIDDEN" name="forzar" value="0"/>
    </form>
    <%}
    else
    {%>
        La P&aacute;gina no fue Encontrada.
    <%}
    %>
    </body>
</html>