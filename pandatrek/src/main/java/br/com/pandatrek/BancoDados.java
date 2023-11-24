package br.com.pandatrek;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDados {
    Connection conexao;

    static String host = "localhost";
    static String porta = "3306";
    static String schema = "pandatrek";
    static String usuario = "root";
    static String senha = "root";

    public void conectar() {
        try {
            String url = "jdbc:mysql://" + host + ":" + porta + "/" + schema + "?" + "useSSL=false";
            conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão feita com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco de dados", e);
        }
    }

    public void desconectar() {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão encerrada com sucesso!");
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao desconectar", e);
            }
        }
    }

    public Connection getConexao() {
        if (conexao == null) {
            conectar();
        }
        return conexao;
    }

}