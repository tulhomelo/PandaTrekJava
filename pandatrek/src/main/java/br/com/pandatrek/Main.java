package br.com.pandatrek;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;

public class Main {
    private JFrame frame;
    private JPanel panel;
    private JButton btnAddOrganizacao;
    private JTable table;

    Connection conexao;

    private ArrayList<Organizacao> organizacoes;
    private ArrayList<Evento> eventos;

    public Main() {
        // Inicializa a lista de organizações recuperando dados do banco de dados
        organizacoes = new ArrayList<>();
        eventos = new ArrayList<>();

        BancoDados bd = new BancoDados();
        conexao = bd.getConexao();

        recuperarOrganizacoes();

        frame = new JFrame("PandaTrek");
        panel = new JPanel();
        btnAddOrganizacao = new JButton("Adicionar Organização");
        btnAddOrganizacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarOrganizacao();
            }
        });

        // Configuração da tabela para exibir organizações
        table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Data Cadastro");
        model.addColumn("Ativo");
        model.addColumn("Excluído");
        model.addColumn("Data Exclusão");
        model.addColumn("Descrição");

        // Preenche a tabela com os dados recuperados do banco de dados
        for (Organizacao org : organizacoes) {
            model.addRow(new Object[] {
                    org.getId(),
                    org.getNome(),
                    org.getDataCadastro(),
                    org.getAtivo(),
                    org.getExcluido(),
                    org.getDataExclusao(),
                    org.getDescricao()
            });
        }
        table.setModel(model);

        panel.setLayout(new BorderLayout());
        panel.add(btnAddOrganizacao, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void recuperarOrganizacoes() {
        try {
            // Consulta SQL para recuperar registros da tabela organizacao
            String query = "SELECT * FROM organizacao o ORDER BY o.nome";
            PreparedStatement ps = conexao.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                Date dataCadastro = rs.getDate("data_cadastro");
                int ativo = rs.getInt("ativo");
                int excluido = rs.getInt("excluido");
                Date dataExclusao = rs.getDate("data_exclusao");
                String descricao = rs.getString("descricao");

                organizacoes.add(new Organizacao(id, nome, dataCadastro, ativo, excluido, dataExclusao, descricao));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private void adicionarOrganizacao() {
        JFrame telaOrganizacao = new JFrame("Adicionar Organização");
        telaOrganizacao.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel labelId = new JLabel("ID:");
        JTextField campoId = new JTextField();
        campoId.setEditable(false);

        JLabel labelNome = new JLabel("Nome:");
        JTextField campoNome = new JTextField();

        JLabel labelDataCadastro = new JLabel("Data Cadastro:");
        JTextField campoDataCadastro = new JTextField();
        campoDataCadastro.setEditable(false);

        JLabel labelAtivo = new JLabel("Ativo:");
        JTextField campoAtivo = new JTextField();

        JLabel labelExcluido = new JLabel("Excluído:");
        JTextField campoExcluido = new JTextField();

        JLabel labelDataExclusao = new JLabel("Data Exclusão:");
        JTextField campoDataExclusao = new JTextField();
        campoDataExclusao.setEditable(false);

        JLabel labelDescricao = new JLabel("Descrição:");
        JTextField campoDescricao = new JTextField();

        JButton btnSalvar = new JButton("Salvar");

        telaOrganizacao.add(labelId);
        telaOrganizacao.add(campoId);
        telaOrganizacao.add(labelNome);
        telaOrganizacao.add(campoNome);
        telaOrganizacao.add(labelDataCadastro);
        telaOrganizacao.add(campoDataCadastro);
        telaOrganizacao.add(labelAtivo);
        telaOrganizacao.add(campoAtivo);
        telaOrganizacao.add(labelExcluido);
        telaOrganizacao.add(campoExcluido);
        telaOrganizacao.add(labelDataExclusao);
        telaOrganizacao.add(campoDataExclusao);
        telaOrganizacao.add(labelDescricao);
        telaOrganizacao.add(campoDescricao);
        telaOrganizacao.add(btnSalvar);

        telaOrganizacao.setSize(400, 300);
        telaOrganizacao.setVisible(true);
        telaOrganizacao.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "INSERT INTO organizacao (nome, data_cadastro, ativo, excluido, descricao) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement ps = conexao.prepareStatement(query);
                    ps.setString(1, campoNome.getText());
                    ps.setTimestamp(2, new Timestamp(new Date().getTime()));
                    ps.setInt(3, Integer.parseInt(campoAtivo.getText()));
                    ps.setInt(4, Integer.parseInt(campoExcluido.getText()));
                    ps.setString(5, campoDescricao.getText());

                    ps.executeUpdate();
                    ps.close();

                    JOptionPane.showMessageDialog(null, "Organização adicionada com sucesso!");

                    telaOrganizacao.dispose(); // Fecha a janela após adicionar a organização
                    atualizarTabelaOrganizacoes();
                } catch (SQLException ex) {
                    System.out.println("Erro ao adicionar organização: " + ex.getMessage());
                }
            }
        });
    }

    private void atualizarTabelaOrganizacoes() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpa a tabela

        // Recupera os dados do banco de dados e preenche a tabela novamente
        try {
            String query = "SELECT * FROM organizacao o ORDER BY o.nome";
            PreparedStatement ps = conexao.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                Date dataCadastro = rs.getDate("data_cadastro");
                int ativo = rs.getInt("ativo");
                int excluido = rs.getInt("excluido");
                Date dataExclusao = rs.getDate("data_exclusao");
                String descricao = rs.getString("descricao");

                model.addRow(new Object[] {
                        id,
                        nome,
                        dataCadastro,
                        ativo,
                        excluido,
                        dataExclusao,
                        descricao
                });
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar tabela de organizações: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
