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
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;

    Connection conexao;

    private ArrayList<Organizacao> organizacoes;

    public Main() {
        // Inicializa a lista de organizações recuperando dados do banco de dados
        organizacoes = new ArrayList<>();

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
        scrollPane = new JScrollPane(table);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Data Cadastro");
        model.addColumn("Ativo");
        model.addColumn("Descrição");

        // Preenche a tabela com os dados recuperados do banco de dados
        for (Organizacao org : organizacoes) {
            model.addRow(new Object[] {
                    org.getId(),
                    org.getNome(),
                    org.getDataCadastro(),
                    org.getAtivo(),
                    org.getDescricao()
            });
        }
        table.setModel(model);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Duplo clique
                    int row = table.getSelectedRow();
                    if (row != -1) { // Nenhuma linha selecionada
                        // Obtém os dados da linha selecionada
                        String id = table.getValueAt(row, 0).toString();
                        String nome = table.getValueAt(row, 1).toString();
                        String ativo = table.getValueAt(row, 3).toString();
                        String descricao = table.getValueAt(row, 4).toString();

                        JFrame telaEdicao = new JFrame("Editar Organização");
                        JPanel panelEdicao = new JPanel();
                        panelEdicao.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                        panelEdicao.setLayout(new GridLayout(8, 2, 10, 10));

                        // Campos preenchidos com os dados da linha selecionada para edição
                        JLabel labelId = new JLabel("Id:");
                        JTextField campoId = new JTextField(id);
                        campoId.setEditable(false);

                        JLabel labelNome = new JLabel("Nome:");
                        JTextField campoNome = new JTextField(nome);

                        JLabel labelAtivo = new JLabel("Ativo (1 para sim, 0 para não):");
                        JTextField campoAtivo = new JTextField(ativo);

                        JLabel labelDescricao = new JLabel("Descrição:");
                        JTextField campoDescricao = new JTextField(descricao);

                        JButton btnSalvarEdicao = new JButton("Salvar Edição");
                        panelEdicao.add(labelId);
                        panelEdicao.add(campoId);
                        panelEdicao.add(labelNome);
                        panelEdicao.add(campoNome);
                        panelEdicao.add(labelAtivo);
                        panelEdicao.add(campoAtivo);
                        panelEdicao.add(labelDescricao);
                        panelEdicao.add(campoDescricao);

                        panelEdicao.add(btnSalvarEdicao);

                        telaEdicao.add(panelEdicao);
                        telaEdicao.setSize(400, 300);
                        telaEdicao.setVisible(true);
                        telaEdicao.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        btnSalvarEdicao.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    String query = "UPDATE organizacao SET nome = ?, ativo = ?, excluido = ?, descricao = ? WHERE id = ?";
                                    PreparedStatement ps = conexao.prepareStatement(query);
                                    ps.setString(1, campoNome.getText());
                                    ps.setInt(2, Integer.valueOf(ativo));
                                    ps.setInt(3, 0); // Não exclusão
                                    ps.setString(4, campoDescricao.getText());
                                    ps.setInt(5, Integer.valueOf(id)); // Define o ID para a cláusula WHERE
                                    ps.executeUpdate();
                                    ps.close();

                                    JOptionPane.showMessageDialog(null, "Organização atualizada com sucesso!");

                                    telaEdicao.dispose(); // Fecha a janela após edição
                                    atualizarTabelaOrganizacoes();
                                } catch (SQLException ex) {
                                    System.out.println("Erro ao atualizar organização: " + ex.getMessage());
                                }
                            }
                        });
                    }
                }
            }
        });

        panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));
        panel.setLayout(new BorderLayout(12, 12));
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
            String query = "SELECT * FROM organizacao o ORDER BY o.id";
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
        JPanel panelOrganizacao = new JPanel();
        panelOrganizacao.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panelOrganizacao.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel labelNome = new JLabel("Nome:");
        JTextField campoNome = new JTextField();

        JLabel labelAtivo = new JLabel("Ativo (1 para sim, 0 para não):");
        JTextField campoAtivo = new JTextField("1");

        JLabel labelExcluido = new JLabel("Excluído (1 para sim, 0 para não):");
        JTextField campoExcluido = new JTextField("0");

        JLabel labelDescricao = new JLabel("Descrição:");
        JTextField campoDescricao = new JTextField();

        JButton btnSalvar = new JButton("Salvar");

        panelOrganizacao.add(labelNome);
        panelOrganizacao.add(campoNome);
        panelOrganizacao.add(labelAtivo);
        panelOrganizacao.add(campoAtivo);
        panelOrganizacao.add(labelExcluido);
        panelOrganizacao.add(campoExcluido);
        panelOrganizacao.add(labelDescricao);
        panelOrganizacao.add(campoDescricao);
        panelOrganizacao.add(btnSalvar);

        telaOrganizacao.add(panelOrganizacao);
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
                    ps.setTimestamp(2, new Timestamp(new Date().getTime())); // Momento atual
                    ps.setInt(3, Integer.parseInt(campoAtivo.getText()));
                    ps.setInt(4, Integer.parseInt(campoExcluido.getText()));
                    ps.setString(5, campoDescricao.getText());

                    ps.executeUpdate();
                    ps.close();

                    JOptionPane.showMessageDialog(null, "Organização adicionada com sucesso!");

                    telaOrganizacao.dispose(); // Fecha a janela após adição
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
