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

public class ReInformacion {
    public int getPerfil(Connection x, String codEst) {
        int perfil = -1;
        Data d = new Data();
        String consulta = "";
        try {
            consulta = "select i_perfil from registro.sa_usuario where i_codigo = " + codEst + "";
            perfil = Integer.parseInt(d.getValue(consulta, x));
        }
        catch (Exception ex) {
            perfil = -1;
        }
        return perfil;
    }

    public boolean estaMatriculado(Connection x, String codEst) {
        boolean res = false;
        Data d = new Data();
        String consulta = "";
        try {
            consulta = "select count(*) from re_periodo p , re_matricula m  where trunc(sysdate) between trunc(p.inicia) and trunc(p.termina)  and p.cod_per = m.cod_per  and m.cod_est = " + codEst + "" + "  and estado in ('M', 'E')";
            int matriculado = Integer.parseInt(d.getValue(consulta, x));
            if (matriculado > 0) {
                res = true;
            }
        }
        catch (Exception ex) {
            res = false;
        }
        return res;
    }

    public boolean estaContratado(Connection x, String codEst) {
        boolean res = false;
        Data d = new Data();
        String consulta = "";
        try {
            consulta = "select count(*) from RECURSOSH.rh_contrato where identificacion = '" + codEst + "' " + "  and activo = 1";
            int contratado = Integer.parseInt(d.getValue(consulta, x));
            if (contratado > 0) {
                res = true;
            }
        }
        catch (Exception ex) {
            res = false;
        }
        return res;
    }
    public boolean esIngeniero(Connection x, String codEst) {
        boolean res = false;
        Data d = new Data();
        String consulta = "";
        try {
            consulta = "select count(*) from USR_CJTYT.INGENIEROS where identificacion = '" + codEst + "' " + "  and estado = 1";
            int contratado = Integer.parseInt(d.getValue(consulta, x));
            if (contratado > 0) {
                res = true;
            }
        }
        catch (Exception ex) {
            res = false;
        }
        return res;
    }

    public String getDatos(Connection x, String codEst) {
        String res = "";
        String consulta = "";
        Data d = new Data();
        int perfil = this.getPerfil(x, codEst);
        ResultSet rs = null;
        res = "<div id='caja'></div><div id='contenido'><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" >";
        res = res + "<tr><td colspan='4' align=\"center\"><strong>DATOS PERSONALES</strong></td></tr>";
        if (perfil == 3 || perfil == 11) {
            try {
                consulta = "select * from (select e.cod_est codigo, e.nombres || ' ' || e.apellidos nombres, e.identificacion, pr.nom_zonpro, m.semestre, to_char(sysdate, 'dd/mm/yyyy hh:mi AM')from re_estudiante e, re_matricula m, programas pr\nwhere e.cod_est = " + codEst + "\n" + "  and e.cod_est = m.cod_est\n" + "  and m.estado = 'M'\n" + "  and pr.cod_zonpro = m.cod_zonpro\n" + "order by m.num_mat desc)\n" + "where rownum = 1";
                rs = d.getResultSet(consulta, x);
                res = res + "<tr>";
                res = res + "<td>";
                res = res + "<table  align=\"center\" cellpadding=\"0\" cellspacing=\"0\"";
                while (rs.next()) {
                    res = res + "<tr><td align='right'><strong>CODIGO:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(1) + "</td></tr>";
                    res = res + "<tr><td align='right'><strong>NOMBRE:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\" class='celda'><strong>&nbsp;&nbsp;" + rs.getString(2) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><strong>IDENTIFICACI\u00d3N:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(3) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><strong>PROGRAMA:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\" class='celda'><strong>&nbsp;&nbsp;" + rs.getString(4) + "</strong></td></tr>";
                    if (perfil == 3) {
                        res = res + "<tr><td align='right'><strong>SEMESTRE:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(5) + "</strong></td></tr>";
                        res = res + "<tr><td align='right'><strong>TIPO DE USUARIO:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;ESTUDIANTE UNIVERSIDAD MARIANA</strong></td></tr>";
                        boolean matriculado = this.estaMatriculado(x, codEst);
                        res = matriculado ? res + "<tr><td align='right'><strong>ESTADO:</strong></td><td style=\"font-family:arial;color:green;font-size:20px;\"><strong>&nbsp;&nbsp;MATRICULADO</strong></td></tr>" : res + "<tr><td align='right'><strong>ESTADO:</strong></td><td style=\"font-family:arial;color:red;font-size:20px;\"><strong>&nbsp;&nbsp;NO MATRICULADO</strong></td></tr>";
                    } else {
                        res = res + "<tr><td align='right'><strong>TIPO DE USUARIO:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;EGRESADO UNIVERSIDAD MARIANA</strong></td></tr>";
                    }
                    res = res + "<tr><td align='right'><br/><br/><strong>ENTRADA:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><br/><br/><strong>&nbsp;&nbsp;" + rs.getString(6) + "</strong></td></tr>";
                }
                res = res + "</table>";
                res = res + "</td>";
                consulta = "SELECT ruta FROM fotos WHERE identificacion=" + codEst + " OR identificacion=" + codEst + " OR identificacion=" + codEst + "1 OR identificacion=" + codEst + "1";
                String ruta = d.getValue(consulta, x);
                ruta = "http://serviap2009.umariana.edu.co/FotosEsts/" + ruta;
                if (ruta.equals("")) {
                    ruta = "images/sin_imagen.JPG";
                }
                res = res + "<td><center><img src='" + ruta + "' width=\"220px\" ></img></center></td>";
                res = res + "</tr>";
            }
            catch (Exception ex) {
                res = "";
            }
        } 
        else if(esIngeniero(x, codEst)){
            try {
                consulta = "select * from USR_CJTYT.INGENIEROS WHERE IDENTIFICACION = '"+codEst+"'";
                rs = d.getResultSet(consulta, x);
                res = res + "<tr>";
                res = res + "<td>";
                res = res + "<table  align=\"center\" cellpadding=\"0\" cellspacing=\"0\"";
                while (rs.next()) {
                    res = res + "<tr><td align='right'><strong>CODIGO:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(2) + "</td></tr>";
                    res = res + "<tr><td align='right'><strong>NOMBRE:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(3)+" "+rs.getString(4) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><strong>IDENTIFICACI\u00d3N:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(2) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><strong>DEPENDENCIA:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;CENTRO DE SERVICIOS INFORMATICOS</strong></td></tr>";
                    
                    res = res + "<tr><td align='right'><strong>TIPO DE USUARIO:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;CONTRATISTA</strong></td></tr>";
                    res = rs.getString(10).equals("1") ? res + "<tr><td align='right'><strong>ESTADO:</strong></td><td style=\"font-family:arial;color:green;font-size:20px;\"><strong>&nbsp;&nbsp;ACTIVO</strong></td></tr>" : res + "<tr><td align='right'><strong>ESTADO:</strong></td><td style=\"font-family:arial;color:red;font-size:20px;\"><strong>&nbsp;&nbsp;" + rs.getString(8) + "</strong></td></tr>";
                    
                }
                res = res + "</table>"; 
                res = res + "</td>";
                res = res + "</tr>";
            }
            catch (Exception ex) {
                res = "";
            }
        }
        else {
            try {
                consulta = "select * from (SELECT distinct con.nombres||' '||con.primer_ap||' '||con.segundo_ap nombres, con.identificacion, cc.nom_centro, con.categoria, to_char(cc.centro_costo), con.f_final, con.activo, decode(con.activo, '1', 'CONTRATADO', '2', 'NO CONTRATADO'), to_char(sysdate, 'dd/mm/yyyy hh:mi AM')                  FROM recursosh.rh_contrato con, recursosh.rh_tipo_puesto tp,centro_costo cc\n                 WHERE(con.identificacion = '" + codEst + "')\n" + "                 AND con.cod_tpuesto=tp.cod_tpuesto\n" + "                 AND cc.centro_costo= con.centro_costo\n" + "                 ORDER BY con.activo desc, con.f_final DESC) where rownum = 1";
                rs = d.getResultSet(consulta, x);
                res = res + "<tr>";
                res = res + "<td>";
                res = res + "<table  align=\"center\" cellpadding=\"0\" cellspacing=\"0\"";
                while (rs.next()) {
                    res = res + "<tr><td align='right'><strong>CODIGO:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(2) + "</td></tr>";
                    res = res + "<tr><td align='right'><strong>NOMBRE:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(1) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><strong>IDENTIFICACI\u00d3N:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(2) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><strong>DEPENDENCIA:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + rs.getString(3) + "</strong></td></tr>";
                    String categoria = this.darCategoria(rs.getString(4));
                    res = res + "<tr><td align='right'><strong>TIPO DE USUARIO:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><strong>&nbsp;&nbsp;" + categoria + "</strong></td></tr>";
                    res = rs.getString(7).equals("1") ? res + "<tr><td align='right'><strong>ESTADO:</strong></td><td style=\"font-family:arial;color:green;font-size:20px;\"><strong>&nbsp;&nbsp;" + rs.getString(8) + "</strong></td></tr>" : res + "<tr><td align='right'><strong>ESTADO:</strong></td><td style=\"font-family:arial;color:red;font-size:20px;\"><strong>&nbsp;&nbsp;" + rs.getString(8) + "</strong></td></tr>";
                    res = res + "<tr><td align='right'><br/><br/><strong>ENTRADA:</strong></td><td style=\"font-family:arial;color:blue;font-size:15px;\"><br/><br/><strong>&nbsp;&nbsp;" + rs.getString(9) + "</strong></td></tr>";
                }
                res = res + "</table>";
                res = res + "</td>";
                consulta = "SELECT ruta FROM fotos WHERE identificacion=" + codEst + " OR identificacion=" + codEst + " OR identificacion=" + codEst + "1 OR identificacion=" + codEst + "1";
                String ruta = d.getValue(consulta, x);
                ruta = "http://serviap2009.umariana.edu.co/FotosEsts/" + ruta;
                if (ruta.equals("")) {
                    ruta = "images/sin_imagen.JPG";
                }
                res = res + "<td><center><img src='" + ruta + "' width=\"210px\"></img></center></td>";
                res = res + "</tr>";
            }
            catch (Exception ex) {
                res = "";
            }
        }
        res = res + "</table></div>";
        return res;
    }

    public String darCategoria(String categoria) {
        String res = "";
        res = categoria.equals("DC") ? "DOCENTE DE TIEMPO COMPLETO" : (categoria.equals("DD") ? "DIRECTIVO / DECANO" : (categoria.equals("DM") ? "DOCENTE DE MEDIO TIEMPO" : (categoria.equals("DP") ? "DIRECTIVO DE PLANTA" : (categoria.equals("HC") ? "DOCENTE HORA CATEDRA" : (categoria.equals("PP") ? "PERSONAL PLANTA Y APOYO" : "DESCONOCIDO")))));
        return res;
    }

    public String darHistorialIngresos(Connection x, String codEst) {
        String res = "";
        Data d = new Data();
        ResultSet rs = null;
        String consulta = "";
        consulta = "select to_char(fec_ingreso, 'dd/mm/yyyy hh:mi AM') from ingreso_unimar where cod_est = " + codEst + "" + "    and tipo = 1 and fec_ingreso <= sysdate order by 1";
        res = "<div> <table id='hor-zebra' align=\"center\" cellpadding=\"0\" cellspacing=\"0\"> <thead>";
        rs = d.getResultSet(consulta, x);
        try {
            res = res + "<tr>";
            res = res + "<th scope='col'>N\u00b0</th><th>FECHA DE INGRESO</th></tr></thead><tbody>";
            int con = 1;
            while (rs.next()) {
                res = con == 1 ? res + "<tr class='odd'><td style=\"font-family:arial;color:green;font-size:15px;\"><strong>" + con + "</strong></td><td style=\"font-family:arial;color:green;font-size:15px;\"><strong>" + rs.getString(1) + "</strong></td><td style=\"font-family:arial;color:green;font-size:15px;\" align=\"center\"><strong>INGRESO VALIDO</strong></td></tr>" : res + "<tr class='odd'><td style=\"font-family:arial;color:red;font-size:12px;\">" + con + "</td><td style=\"font-family:arial;color:red;font-size:12px;\">" + rs.getString(1) + "</td><td style=\"font-family:arial;color:red;font-size:12px;\" align=\"center\">INGRESO NO VALIDO</td></tr>";
                ++con;
            }
        }
        catch (Exception ex) {
            res = "";
        }
        res = res + "</tbody></table></div>";
        return res;
    }

    public String darHistorialRefrigerios(Connection x, String codEst) {
        String res = "";
        Data d = new Data();
        ResultSet rs = null;
        String consulta = "";
        consulta = "select to_char(fec_ingreso, 'dd/mm/yyyy hh:mi AM') from ingreso_unimar where cod_est = " + codEst + "" + "    and tipo = 2 and fec_ingreso <= sysdate order by 1 desc";
        res = "<div style=\"height: 310px; overflow:scroll;\"> <table id='hor-zebra' align=\"center\" cellpadding=\"0\" cellspacing=\"0\"> <thead>";
        rs = d.getResultSet(consulta, x);
        try {
            res = res + "<tr>";
            res = res + "<th scope='col'>N\u00b0</th><th>FECHA DE ENTREGA</th></tr></thead><tbody>";
            int con = 1;
            while (rs.next()) {
                res = res + "<tr class='odd'><td>" + con + "</td><td>" + rs.getString(1) + "</td></tr>";
                ++con;
            }
        }
        catch (Exception ex) {
            res = "";
        }
        res = res + "</tbody></table></div>";
        return res;
    }

    public boolean tieneIngresos(Connection x, String codEst) {
        boolean tieneIngresos = false;
        Data d = new Data();
        String consulta = "";
        consulta = "select count(*) from ingreso_unimar where cod_est = " + codEst + " and tipo = 1 and fec_ingreso <= sysdate";
        int ingresos = Integer.parseInt(d.getValue(consulta, x));
        if (ingresos > 0) {
            tieneIngresos = true;
        }
        return tieneIngresos;
    }

    public boolean tieneRefrigerio(Connection x, String codEst) {
        boolean tieneIngresos = false;
        Data d = new Data();
        String consulta = "";
        consulta = "select count(*) from ingreso_unimar where cod_est = " + codEst + " and tipo = 2 and fec_ingreso <= sysdate";
        int ingresos = Integer.parseInt(d.getValue(consulta, x));
        if (ingresos > 0) {
            tieneIngresos = true;
        }
        return tieneIngresos;
    }
}
