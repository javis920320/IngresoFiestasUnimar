/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  Gral.Data
 */
package Sistemas;

import Gral.Data;
import java.sql.Connection;
import java.sql.ResultSet;

public class Seminario {
    public int getCodSeminario(Connection x) {
        int res = 0;
        Data d = new Data();
        try {
            res = Integer.parseInt(d.getValue("select valor from re_general where codigo = 18", x));
        }
        catch (Exception ex) {
            res = 0;
        }
        return res;
    }

    public String getNomSeminario(int pSeminario, Connection x) {
        String res = "";
        String consulta = "";
        Data d = new Data();
        try {
            consulta = "select nombre from seminarios where cod_concepto = " + pSeminario;
            res = d.getValue(consulta, x);
        }
        catch (Exception ex) {
            res = "";
        }
        return res;
    }

    public String datosInscrito(String pCodEst, int pSeminario, Connection x) {
        String res = "";
        String consulta = "";
        Data d = new Data();
        res = "<div id='caja'></div><div id='contenido'><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" >";
        res = res + "<tr><td colspan='4' align=\"center\"><strong>DATOS PERSONALES</strong></td></tr>";
        try {
            boolean inscrito = this.estaInscrito(pCodEst, pSeminario, x);
            if (inscrito) {
                consulta = "select iden_benef, nombres, NVL(institucion, '-'), to_char(sysdate, 'dd/mm/yyyy hh:mi AM') from inscrito_seminario where iden_benef = '" + pCodEst + "' and cod_seminario = " + pSeminario;
                ResultSet rs = d.getResultSet(consulta, x);
                res = res + "<tr>";
                res = res + "<td>";
                res = res + "<table  align=\"center\" cellpadding=\"0\" cellspacing=\"0\"";
                while (rs.next()) {
                    res = res + "<tr><td align='right'><strong>IDENTIFICACI\u00d3N:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(1) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><strong>NOMBRES:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(2) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><strong>INSTITUCI\u00d3N:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(3) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><br/><br/><strong>ENTRADA:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><br/><br/><strong>&nbsp;&nbsp;" + rs.getString(4) + "</strong></td></tr>";
                }
                res = res + "</table>";
                res = res + "</td>";
                consulta = "SELECT ruta FROM fotos WHERE identificacion=" + pCodEst + " OR identificacion=" + pCodEst + " OR identificacion=" + pCodEst + "1 OR identificacion=" + pCodEst + "1";
                String ruta = d.getValue(consulta, x);
                ruta = "http://serviap2009.umariana.edu.co/FotosEsts/" + ruta;
                if (ruta.equals("")) {
                    ruta = "../images/sin_imagen.JPG";
                }
                res = res + "<td><center><img src='" + ruta + "' width=\"220px\" ></img></center></td>";
                res = res + "</tr>";
                rs.close();
            } else {
                res = res + "<tr><td><table  align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"font-family:arial;color:RED;font-size:15px;\"><strong>NO SE ENCUENTRA INSCRITO AL SEMINARIO,</strong></td></tr> <tr><td style=\"font-family:arial;color:RED;font-size:15px;\"><strong>POR FAVOR REVISAR SU IDENTIFICACI\u00d3N</strong></td></tr></table></td><td><center><img src='../images/np.jpg' width=\"220px\" ></img></center></td>";
            }
        }
        catch (Exception ex) {
            res = "";
        }
        res = res + "</table></div>";
        return res;
    }

    public String buscarPorNombre(String pNombre, int pSeminario, Connection x) {
        String res = "";
        Data d = new Data();
        String consulta = "";
        res = "<div id='caja'></div><div id='contenido'><div style=\"width: 650px; height: 330px;  overflow: scroll;\" ><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" >";
        res = res + "<tr><td colspan='4' align=\"center\"><br/><br/><strong>PARTICIPANTES AL SEMINARIO</strong></td></tr>";
        try {
            consulta = "select iden_benef, nombres, NVL(institucion, '-') institucion from inscrito_seminario where cod_seminario = " + pSeminario + " and UPPER(nombres) like UPPER('%" + pNombre + "%')";
            ResultSet rs = d.getResultSet(consulta, x);
            res = res + "<tr>";
            res = res + "<td><br/><br/>";
            res = res + "<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\"";
            res = res + "<tr><td align=\"center\"><strong>IDENTIFICACI\u00d3N</strong></td><td align=\"center\"><strong>NOMBRES</strong></td><td align=\"center\"><strong>INSTITUCI\u00d3N</strong></td><td align=\"center\"><strong>REGISTRAR</strong></td></tr>";
            while (rs.next()) {
                res = res + "<tr><td align=\"center\">" + rs.getString(1) + "</td><td>" + rs.getString(2) + "</td><td>&nbsp;&nbsp; &nbsp;&nbsp; " + rs.getString(3) + "</td><td align=\"center\"><a href='Ingreso.jsp?txtBus=" + rs.getString(1) + "'><img src='../images/realizada.png'></a></td></tr>";
            }
            res = res + "</table>";
            res = res + "</td></tr>";
            rs.close();
        }
        catch (Exception ex) {
            res = "";
        }
        res = res + "</table></div></div>";
        return res;
    }

    public boolean estaInscrito(String pCodEst, int pSeminario, Connection x) {
        boolean res = false;
        Data d = new Data();
        try {
            String consulta = "select count(*) from inscrito_seminario where iden_benef = '" + pCodEst + "' and cod_seminario = " + pSeminario;
            int existe = Integer.parseInt(d.getValue(consulta, x));
            if (existe > 0) {
                res = true;
            }
        }
        catch (Exception ex) {
            res = false;
        }
        return res;
    }

    public boolean registrarIngresoSeminario(String pCodEst, int pSeminario, Connection x) {
        boolean res = false;
        Data d = new Data();
        try {
            String consulta = "select tipo_benef from inscrito_seminario where iden_benef = '" + pCodEst + "' and cod_seminario =" + pSeminario;
            String tipo = d.getValue(consulta, x);
            consulta = "insert into sysadm.contro_ingreso_seminario values (" + pSeminario + ", " + pCodEst + ", '" + tipo + "', sysdate)";
            res = d.executeSQL(x, consulta);
        }
        catch (Exception ex) {
            res = false;
        }
        return res;
    }

    public String darHistorialIngreso(String pCodEst, int pSeminario, Connection x) {
        String res = "";
        Data d = new Data();
        String consulta = "select to_char(fecha_hora, 'dd/mm/yyyy hh:mi AM') from sysadm.contro_ingreso_seminario where cod_seminario = " + pSeminario + " and iden_benef =" + pCodEst;
        res = "<div> <table id='hor-zebra' align=\"center\" cellpadding=\"0\" cellspacing=\"0\"> <thead>";
        ResultSet rs = d.getResultSet(consulta, x);
        try {
            res = res + "<tr>";
            res = res + "<th scope='col'>N\u00b0</th><th>FECHA DE INGRESO</th></tr></thead><tbody>";
            int con = 1;
            while (rs.next()) {
                res = res + "<tr class='odd'><td style=\"font-family:arial;color:green;font-size:15px;\"><strong>" + con + "</strong></td><td style=\"font-family:arial;color:green;font-size:15px;\"><strong>" + rs.getString(1) + "</strong></td><td style=\"font-family:arial;color:green;font-size:15px;\" align=\"center\"><strong>INGRESO VALIDO</strong></td></tr>";
                ++con;
            }
            rs.close();
        }
        catch (Exception ex) {
            res = "";
        }
        res = res + "</tbody></table></div>";
        return res;
    }
}
