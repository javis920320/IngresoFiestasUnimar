/*
 * Decompiled with CFR 0_102.
 */
package Gral;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Data {
    private String constr;
    private static String driver;
    private static String servidor;
    private static String puerto;
    private static String baseDatos;
    private static String usuario;
    private static String contrasena;
    private static String tipoUsu;
    private static String ruta;
    private static String psw;

    public Data() {
        driver = "jdbc:oracle:thin:@";
        String sistemaOP = System.getProperty("os.name");
        if (System.getProperty("os.name").equals("Windows 7"))
            ruta = "C:/LeerArchivo/conn_unimar";
        else
            ruta = "/oracle/conn_unimar";
        tipoUsu = "academicoprod";
        
    }

    public Connection getConnection() {
        Connection conn;
        block6 : {
            conn = null;
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                if (servidor == null || servidor.length() == 0) {
                    Data.leerFichero();
                    conn = this.conexionNueva(driver, servidor, puerto, baseDatos, usuario, contrasena);
                    this.cambiarRol(conn);
                    conn.close();
                    conn = this.conexionNueva(driver, servidor, puerto, baseDatos, usuario, contrasena);
                    break block6;
                }
                try {
                    conn = this.conexionNueva(driver, servidor, puerto, baseDatos, usuario, contrasena);
                    if (conn == null) {
                        Data.leerFichero();
                        conn = this.conexionNueva(driver, servidor, puerto, baseDatos, usuario, contrasena);
                        this.cambiarRol(conn);
                        conn.close();
                        conn = this.conexionNueva(driver, servidor, puerto, baseDatos, usuario, contrasena);
                    }
                }
                catch (Exception e) {
                    conn = null;
                }
            }
            catch (Exception e) {
                conn = null;
            }
        }
        return conn;
    }

    public Connection conexionNueva(String driver, String servidor, String puerto, String sid, String usuario, String pass) {
        Connection conn = null;
        try {
            this.constr = driver + servidor + ":" + puerto + ":" + sid;
            conn = DriverManager.getConnection(this.constr, "" + usuario + "", "" + pass + "");
        }
        catch (Exception e) {
            conn = null;
        }
        return conn;
    }

    public void cambiarRol(Connection x) {
        String sql = "";
        try {
            sql = "select sysadm.fun_conexion('" + psw + "','" + tipoUsu + "') from dual";
            String cadena = this.getValue(sql, x);
            servidor = cadena.split(",")[0];
            puerto = cadena.split(",")[1];
            baseDatos = cadena.split(",")[2];
            usuario = cadena.split(",")[3];
            contrasena = cadena.split(",")[4];
        }
        catch (Exception e) {
            e.getMessage();
        }
    }

    public static void leerFichero() {
        try {
            File arcConf = new File(ruta);
            FileInputStream fis = new FileInputStream(arcConf);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bre = new BufferedReader(isr);
            String textoArc = "";
            String linea = "";
            while ((linea = bre.readLine()) != null) {
                textoArc = textoArc + linea;
            }
            String[] datos = textoArc.split(";");
            String acceso = datos[0].split("=")[1];
            servidor = acceso.split(",")[0];
            puerto = acceso.split(",")[1];
            baseDatos = acceso.split(",")[2];
            usuario = acceso.split(",")[3];
            contrasena = acceso.split(",")[4];
            psw = datos[1].toString();
        }
        catch (Exception e) {
            e.getMessage();
        }
    }

    public boolean executeProcedure(String llamada, Connection x) {
        boolean res = false;
        try {
            CallableStatement cmd = x.prepareCall(llamada);
            cmd.executeUpdate();
            res = true;
        }
        catch (Exception ex) {
            res = false;
        }
        return res;
    }

    public ResultSet getResultSet(String sql, Connection x) {
        ResultSet rs;
        try {
            Statement cmd = x.createStatement();
            rs = cmd.executeQuery(sql);
        }
        catch (Exception ex) {
            rs = null;
        }
        return rs;
    }

    public String getValue(String sql, Connection x) {
        String res = "";
        try {
            Statement cmd = x.createStatement();
            ResultSet rs = cmd.executeQuery(sql);
            rs.next();
            res = rs.getString(1);
            rs.close();
            cmd.close();
        }
        catch (SQLException ex) {
            res = "";
        }
        return res;
    }

    public Blob RecuperarBLOB(String idBLOB, Connection x) {
        Blob bin = null;
        InputStream inStream = null;
        String sql = "";
        try {
            Statement st = x.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                bin = rs.getBlob(1);
                inStream = bin.getBinaryStream();
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex) {
            bin = null;
        }
        return bin;
    }

    public long getMaxCodigo(Connection x, String tabla) {
        Data d = new Data();
        return Long.parseLong(d.getValue("select nvl(max(codigo),0) from " + tabla, x));
    }

    public boolean executeSQL(Connection x, String query) {
        boolean res = false;
        try {
            Statement cmd = x.createStatement();
            cmd.executeUpdate(query);
            cmd.close();
            res = true;
        }
        catch (SQLException ex) {
            res = false;
        }
        return res;
    }
}
