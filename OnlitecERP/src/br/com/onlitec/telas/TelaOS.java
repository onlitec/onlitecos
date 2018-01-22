/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.onlitec.telas;

import br.com.onlitec.dal.ModuloConexao;
import java.awt.HeadlessException;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;

/**
 *
 * @author alfreire
 */
public class TelaOS extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    // A linha abaixo cria uma variavel para armazenar um texto de acordo com o radio button selecionado
    private String tipoos;
    public String ok;
    public String num_os;

    /**
     * Creates new form TelaOS
     */
    public TelaOS() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void cadastrar_cliente() {

        String sql = "insert into tbclientes(nomedocli,fonecli) VALUES(?,?)";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtOsNomeCli.getText());
            pst.setString(2, txtOsFoneCli.getText());

            // Validação dos campod obrigatórios
            if ((txtOsNomeCli.getText().isEmpty()) || (txtOsFoneCli.getText().isEmpty())) {

                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios. Nome e Telefone do cliente");

            } else {
                // A linha abaixo cadastra o cliente no banco de dados

                int adcionado = pst.executeUpdate();
                if (adcionado > 0) {

                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso.");
                    txtOsPsqNomeCli.setText(txtOsNomeCli.getText());

                } else {
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Metodo para pesquisar clientes pelo nome
    public void pesquisar_cliente() {

        String sql = "select nomedocli as Nome, fonecli as Telefone,idcli as ID from tbclientes where nomedocli like ?";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteúdo da caixa de pesquisa para o interrego
            // Atenção ao "%" que é a continuação da string sql
            pst.setString(1, txtOsPsqNomeCli.getText() + "%");
            rs = pst.executeQuery();
            // A linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblOsPsqCli.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Metodo para setar os campos do formulario com o conteudo da tabela
    public void setar_campos() {
        int setar = tblOsPsqCli.getSelectedRow();
        txtOsIdCli.setText(tblOsPsqCli.getModel().getValueAt(setar, 2).toString());
        txtOsNomeCli.setText(tblOsPsqCli.getModel().getValueAt(setar, 0).toString());
        txtOsFoneCli.setText(tblOsPsqCli.getModel().getValueAt(setar, 1).toString());

        // A linha abaixo desabilita o botão adcionar
        btnOsAdCli.setEnabled(false);

    }

    // Metodo para cadastrar OS
    private void emitiros() {

        String sql = "insert into tbos(tipo,situacao,equipamento,defeito,servico,tecnico,valor,idcli) values (?,?,?,?,?,?,?,?)";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipoos);
            pst.setString(2, cboOsSituacao.getSelectedItem().toString());
            pst.setString(3, txtOsEqui.getText());
            pst.setString(4, txaDefeito.getText());
            pst.setString(5, txaOsServico.getText());
            pst.setString(6, cboTecnico.getSelectedItem().toString());
            // Substitui a virgula pelo ponto
            pst.setString(7, txtOsValor.getText().replace(",", "."));
            pst.setString(8, txtOsIdCli.getText());

            // Validação dos campos obrigatorios
            if ((txtOsIdCli.getText().isEmpty()) || (txtOsEqui.getText().isEmpty()) || (txaDefeito.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios (*).");

            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {

                    JOptionPane.showMessageDialog(null, "OS emitida com sucesso.");

                } else {

                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Metodo para pesquisar uma os
    private void pesquisar_os() {

        String num_os = JOptionPane.showInputDialog("Número da OS");

        String sql = "select * from tbos where os=" + num_os;

        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                txtOsNum.setText(rs.getString(1));
                txtDatOs.setText(rs.getString(2));
                // Criando um variavel para setar o radio botão
                String rbttipo = rs.getString(3);
                if (rbttipo.equals("OS")) {
                    radOs.setSelected(true);
                    tipoos = "OS";

                } else {
                    radOrca2.setSelected(true);
                    tipoos = "Orcamento";
                }
                cboOsSituacao.setSelectedItem(rs.getString(4));
                txtOsEqui.setText(rs.getString(5));
                txaDefeito.setText(rs.getString(6));
                txaOsServico.setText(rs.getString(7));
                cboTecnico.setSelectedItem(rs.getString(8));
                txtOsValor.setText(rs.getString(9));
                txtOsIdCli.setText(rs.getString(10));

                // Desabilitando campos
                btnEmitirOs.setEnabled(false);
                txtOsPsqNomeCli.setEnabled(false);
                tblOsPsqCli.setVisible(false);
                btnPesqOs.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(null, "OS não cadastrada");
            }

        } catch (SQLException e) {

        }
    }

    // Metodo para alterar uma OS
    private void alterar_os() {

        String sql;
        sql = "update tbos set tipo=?,situacao=?,equipamento=?,defeito=?,servico=?,tecnico=?,valor=?, where os=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipoos);
            pst.setString(2, cboOsSituacao.getSelectedItem().toString());
            pst.setString(3, txtOsEqui.getText());
            pst.setString(4, txaDefeito.getText());
            pst.setString(5, txaOsServico.getText());
            pst.setString(6, cboTecnico.getSelectedItem().toString());
            // Substitui a virgula pelo ponto
            pst.setString(7, txtOsValor.getText().replace(",", "."));
            pst.setString(8, txtOsNum.getText());

            // Validação dos campos obrigatorios
            if ((txtOsNum.getText().isEmpty()) || (txtOsEqui.getText().isEmpty()) || (txaDefeito.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios (*).");

            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {

                    JOptionPane.showMessageDialog(null, "OS alterada com sucesso.");

                    txtOsNum.setText(null);
                    txtDatOs.setText(null);
                    // Habilitar os objetos
                    btnEmitirOs.setEnabled(true);
                    txtOsPsqNomeCli.setEnabled(true);
                    tblOsPsqCli.setVisible(true);
                    btnPesqOs.setEnabled(true);

                } else {

                }
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    // Metodo para Excluir OS
    private void excluir_os() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja escluir esta OS ?", "Alenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {

            String sql = "delete from tbos where os=?";
            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtOsNum.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "OS excluídsa com sucesso");
                    limpar();
                    btnEmitirOs.setEnabled(true);
                    txtOsPsqNomeCli.setEnabled(true);
                    tblOsPsqCli.setVisible(true);
                    btnPesqOs.setEnabled(true);
                } else {
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        } else {
        }

    }

    public void limpar() {

        txtOsNum.setText(null);
        txtOsEqui.setText(null);
        txaDefeito.setText(null);
        txaOsServico.setText(null);
        txtOsValor.setText(null);
        txtOsIdCli.setText(null);
        txtOsPsqNomeCli.setText(null);
        tblOsPsqCli.setModel(DbUtils.resultSetToTableModel(rs));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDatOs = new javax.swing.JTextField();
        radOrca2 = new javax.swing.JRadioButton();
        radOs = new javax.swing.JRadioButton();
        txtOsNum = new javax.swing.JLabel();
        cboOsSituacao = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtOsPsqNomeCli = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOsPsqCli = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        txtOsIdCli = new javax.swing.JTextField();
        btnPesqOs = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtOsEqui = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtOsValor = new javax.swing.JTextField();
        txtOsNomeCli = new javax.swing.JTextField();
        txtOsFoneCli = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnOsAdCli = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaDefeito = new javax.swing.JTextArea();
        cboTecnico = new javax.swing.JComboBox<>();
        btnEmitirOs = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaOsServico = new javax.swing.JTextArea();
        jButton7 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setAutoscrolls(true);
        setMaximumSize(new java.awt.Dimension(1000, 800));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Nº OS");

        jLabel2.setText("Data");

        txtDatOs.setEditable(false);
        txtDatOs.setFont(new java.awt.Font("Noto Sans", 1, 9)); // NOI18N
        txtDatOs.setEnabled(false);

        buttonGroup1.add(radOrca2);
        radOrca2.setText("Orçamento");
        radOrca2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radOrca2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(radOs);
        radOs.setText("Ordem de Serviço");
        radOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radOsActionPerformed(evt);
            }
        });

        txtOsNum.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(radOrca2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radOs)
                        .addGap(0, 19, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtOsNum, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtDatOs))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDatOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtOsNum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radOrca2)
                    .addComponent(radOs))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cboOsSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Em analise", "Em execução", "Entrega OK", "Orçamento REPROVADO", "Aguardando Aprovação", "Aguardando Peças", "Abandonado pelo  Cliente", "Retornou" }));

        jLabel3.setText("Situação:");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        txtOsPsqNomeCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOsPsqNomeCliActionPerformed(evt);
            }
        });
        txtOsPsqNomeCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOsPsqNomeCliKeyReleased(evt);
            }
        });

        tblOsPsqCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nome", "Fone", "ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOsPsqCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOsPsqCliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblOsPsqCli);

        jLabel13.setText("ID");

        txtOsIdCli.setEditable(false);
        txtOsIdCli.setToolTipText("ID do cliente");
        txtOsIdCli.setPreferredSize(new java.awt.Dimension(65, 25));

        btnPesqOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/onlitec/icones/buscar.png"))); // NOI18N
        btnPesqOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesqOsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtOsPsqNomeCli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesqOs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOsIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel4)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtOsPsqNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesqOs)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txtOsIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(122, 122, 122))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
        );

        jLabel6.setText("Equipamento: *");

        txtOsEqui.setPreferredSize(new java.awt.Dimension(0, 25));

        jLabel7.setText("Defeito: *");

        jLabel8.setText("Serviço:");

        jLabel9.setText("Técnico:");

        jLabel10.setText("Valor Total:");

        txtOsValor.setText("0");
        txtOsValor.setToolTipText("");
        txtOsValor.setPreferredSize(new java.awt.Dimension(0, 25));

        txtOsNomeCli.setMinimumSize(new java.awt.Dimension(4, 25));
        txtOsNomeCli.setPreferredSize(new java.awt.Dimension(0, 25));

        txtOsFoneCli.setMinimumSize(new java.awt.Dimension(4, 25));
        txtOsFoneCli.setPreferredSize(new java.awt.Dimension(0, 25));

        jLabel5.setText("Nome Do Cliente / Razão Social: * ");

        jLabel12.setText("Telefone: *");

        btnOsAdCli.setText("Cadastrar");
        btnOsAdCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsAdCliActionPerformed(evt);
            }
        });

        jButton2.setText("Limpar Campos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txaDefeito.setColumns(20);
        txaDefeito.setRows(5);
        jScrollPane2.setViewportView(txaDefeito);

        cboTecnico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alessandro" }));
        cboTecnico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboTecnicoMouseClicked(evt);
            }
        });

        btnEmitirOs.setText("Emitir");
        btnEmitirOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmitirOsActionPerformed(evt);
            }
        });

        jButton4.setText("Pesquisar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Editar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Imprimir");

        txaOsServico.setColumns(20);
        txaOsServico.setRows(5);
        jScrollPane3.setViewportView(txaOsServico);

        jButton7.setText("Excluir OS");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3)
                            .addComponent(cboOsSituacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEmitirOs)
                        .addGap(54, 54, 54)
                        .addComponent(jButton4)
                        .addGap(61, 61, 61)
                        .addComponent(jButton5)
                        .addGap(46, 46, 46)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtOsNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtOsFoneCli, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnOsAdCli)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))))
                    .addComponent(jLabel6)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9)
                            .addComponent(cboTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtOsEqui, javax.swing.GroupLayout.PREFERRED_SIZE, 973, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboOsSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOsFoneCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOsAdCli)
                    .addComponent(jButton2))
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOsEqui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEmitirOs)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addGap(37, 37, 37))
        );

        setBounds(0, 0, 1000, 624);
    }// </editor-fold>//GEN-END:initComponents

    private void radOrca2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radOrca2ActionPerformed
        // Atribuindo um texto a variavel tipo se selecionado:
        tipoos = "Orcamento";
    }//GEN-LAST:event_radOrca2ActionPerformed

    private void radOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radOsActionPerformed
        // Atribuindo um texto a variavel tipo se selecionado:
        tipoos = "OS";
    }//GEN-LAST:event_radOsActionPerformed

    private void txtOsPsqNomeCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsPsqNomeCliKeyReleased
        // Chamando o metodo pesquisar cliente
        pesquisar_cliente();
    }//GEN-LAST:event_txtOsPsqNomeCliKeyReleased

    private void tblOsPsqCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOsPsqCliMouseClicked
        // Chamando o metodo setar_campos
        setar_campos();
    }//GEN-LAST:event_tblOsPsqCliMouseClicked

    private void btnOsAdCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsAdCliActionPerformed
        // TODO add your handling code here:
        cadastrar_cliente();

    }//GEN-LAST:event_btnOsAdCliActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // Ao abrir o formulario será marcado a opção orçamento para o tipo de documento:
        radOrca2.setSelected(true);
        tipoos = "Orcamento";

    }//GEN-LAST:event_formInternalFrameOpened

    private void cboTecnicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboTecnicoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTecnicoMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //limpar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtOsPsqNomeCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsPsqNomeCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsPsqNomeCliActionPerformed

    private void btnPesqOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesqOsActionPerformed
        // TODO add your handling code here:

        pesquisar_cliente();
    }//GEN-LAST:event_btnPesqOsActionPerformed

    private void btnEmitirOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmitirOsActionPerformed
        // TODO add your handling code here:
        //cadastrar_cliente();
        emitiros();
    }//GEN-LAST:event_btnEmitirOsActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        pesquisar_os();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        alterar_os();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        excluir_os();
    }//GEN-LAST:event_jButton7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEmitirOs;
    private javax.swing.JButton btnOsAdCli;
    private javax.swing.JButton btnPesqOs;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboOsSituacao;
    private javax.swing.JComboBox<Object> cboTecnico;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JRadioButton radOrca2;
    private javax.swing.JRadioButton radOs;
    private javax.swing.JTable tblOsPsqCli;
    private javax.swing.JTextArea txaDefeito;
    private javax.swing.JTextArea txaOsServico;
    private javax.swing.JTextField txtDatOs;
    private javax.swing.JTextField txtOsEqui;
    private javax.swing.JTextField txtOsFoneCli;
    private javax.swing.JTextField txtOsIdCli;
    private javax.swing.JTextField txtOsNomeCli;
    private javax.swing.JLabel txtOsNum;
    private javax.swing.JTextField txtOsPsqNomeCli;
    private javax.swing.JTextField txtOsValor;
    // End of variables declaration//GEN-END:variables
}
