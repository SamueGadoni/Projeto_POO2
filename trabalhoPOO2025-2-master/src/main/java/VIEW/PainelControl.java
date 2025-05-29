package VIEW;

import dao.AlunoDAO;
import dao.mongoConexao;
import model.Aluno;

import javax.swing.BorderFactory;
import javax.swing.JLabel; // Import para JLabel
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Color; // Import para Color
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusAdapter; // Import para FocusAdapter
import java.awt.event.FocusEvent;   // Import para FocusEvent
import java.util.List;

public class PainelControl extends javax.swing.JFrame {

    private static final String FONT_SEGOE_UI = "Segoe UI";
    private static final Color PLACEHOLDER_COLOR = Color.GRAY;
    private static final Color TEXT_COLOR = Color.BLACK;

    private mongoConexao mongoConnection;
    private AlunoDAO alunoDAO;
    private DefaultTableModel tableModel;
    private String nomeOriginalSelecionado = null;

    // Placeholders para os campos de texto
    private static final String PLACEHOLDER_NOME = "Digite o nome do aluno";
    private static final String PLACEHOLDER_TURMA = "Digite a turma";
    private static final String PLACEHOLDER_NOTA1 = "Nota das atividades";
    private static final String PLACEHOLDER_NOTA2 = "Nota das provas";


    public PainelControl() {
        initComponents(); // Este método será bastante modificado
        centralizarJanela();
        configurarTabela();
        conectarBanco();
        adicionarListenersTabelaEJanela(); // Renomeado para clareza
    }

    private void centralizarJanela() {
        setLocationRelativeTo(null);
    }

    private void configurarTabela() {
        tableModel = new DefaultTableModel(
            new Object[]{"Nome", "Turma", "Nota 1 (Ativ.)", "Nota 2 (Provas)", "Média"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
    }

    private void conectarBanco() {
        try {
            this.mongoConnection = new mongoConexao();
            if (this.mongoConnection.getDatabase() != null) {
                this.alunoDAO = new AlunoDAO(this.mongoConnection);
                popularTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao conectar ao MongoDB.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro inicializando conexão com MongoDB: " + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarListenersTabelaEJanela() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (mongoConnection != null) {
                    System.out.println("Fechando conexão com MongoDB a partir do painel de controle...");
                    mongoConnection.close();
                }
            }
        });

        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                    int selectedRow = jTable1.getSelectedRow();
                    nomeOriginalSelecionado = tableModel.getValueAt(selectedRow, 0).toString();

                    // Ao selecionar na tabela, remove placeholder e preenche com dados reais
                    setTextFieldRealValue(jTextField1, nomeOriginalSelecionado, PLACEHOLDER_NOME);
                    setTextFieldRealValue(jTextField2, tableModel.getValueAt(selectedRow, 1).toString(), PLACEHOLDER_TURMA);
                    setTextFieldRealValue(jTextField3, tableModel.getValueAt(selectedRow, 2).toString(), PLACEHOLDER_NOTA1);
                    setTextFieldRealValue(jTextField4, tableModel.getValueAt(selectedRow, 3).toString(), PLACEHOLDER_NOTA2);
                }
            }
        });
    }

    private void popularTabela() {
        if (alunoDAO == null) {
            System.out.println("AlunoDAO não inicializado.");
            return;
        }
        tableModel.setRowCount(0);
        try {
            List<Aluno> alunos = alunoDAO.readAlunos();
            for (Aluno aluno : alunos) {
                tableModel.addRow(new Object[]{
                        aluno.getNome(),
                        aluno.getTurma(),
                        aluno.getNota1(),
                        aluno.getNota2(),
                        aluno.getMedia()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados dos alunos: " + e.getMessage(), "Erro de Leitura", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        // Restaura placeholders ao limpar
        setPlaceholder(jTextField1, PLACEHOLDER_NOME);
        setPlaceholder(jTextField2, PLACEHOLDER_TURMA);
        setPlaceholder(jTextField3, PLACEHOLDER_NOTA1);
        setPlaceholder(jTextField4, PLACEHOLDER_NOTA2);
        nomeOriginalSelecionado = null;
        jTable1.clearSelection();
    }

    // Método auxiliar para configurar placeholder
    private void setPlaceholder(javax.swing.JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(PLACEHOLDER_COLOR);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(TEXT_COLOR);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(PLACEHOLDER_COLOR);
                }
            }
        });
    }

    // Método auxiliar para definir valor real, removendo placeholder se necessário
    private void setTextFieldRealValue(javax.swing.JTextField textField, String value, String placeholder) {
        if (value != null && !value.isEmpty()) {
            textField.setText(value);
            textField.setForeground(TEXT_COLOR);
        } else {
            setPlaceholder(textField, placeholder);
        }
    }

    // Método para obter o valor real do campo, ignorando o placeholder
    private String getRealText(javax.swing.JTextField textField, String placeholder) {
        if (textField.getText().equals(placeholder)) {
            return ""; // Retorna vazio se for o placeholder
        }
        return textField.getText().trim();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        // Declaração dos JLabels para os campos
        jLabelNome = new javax.swing.JLabel();
        jLabelTurma = new javax.swing.JLabel();
        jLabelNota1 = new javax.swing.JLabel();
        jLabelNota2 = new javax.swing.JLabel();

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Painel de Controle - Alunos");

        jLabel1.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 30)); // Reduzido um pouco para dar espaço
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PAINEL DE CONTROLE - ALUNOS");

        // Configuração dos JLabels
        jLabelNome.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 14));
        jLabelNome.setText("Nome do Aluno:");

        jLabelTurma.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 14));
        jLabelTurma.setText("Turma:");

        jLabelNota1.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 14));
        jLabelNota1.setText("Nota 1 (Atividades):");

        jLabelNota2.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 14));
        jLabelNota2.setText("Nota 2 (Provas):");


        // Configuração dos JTextFields com placeholder
        configureTextFieldWithPlaceholder(jTextField1, PLACEHOLDER_NOME);
        configureTextFieldWithPlaceholder(jTextField2, PLACEHOLDER_TURMA);
        configureTextFieldWithPlaceholder(jTextField3, PLACEHOLDER_NOTA1);
        configureTextFieldWithPlaceholder(jTextField4, PLACEHOLDER_NOTA2);


        jTable1.setFont(new java.awt.Font(FONT_SEGOE_UI, 0, 14)); // NOI18N
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 16)); // NOI18N
        jButton1.setText("LIMPAR");
        jButton1.setToolTipText("Limpar campos de texto e seleção da tabela");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        jButton2.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 153, 0));
        jButton2.setText("CRIAR");
        jButton2.setToolTipText("Criar novo aluno com os dados dos campos");
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        jButton3.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 16)); // NOI18N
        jButton3.setForeground(new java.awt.Color(51, 51, 255));
        jButton3.setText("EDITAR");
        jButton3.setToolTipText("Editar aluno selecionado com os dados dos campos");
        jButton3.addActionListener(evt -> jButton3ActionPerformed(evt));

        jButton4.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.BOLD, 16)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 0, 0));
        jButton4.setText("DELETAR");
        jButton4.setToolTipText("Deletar aluno selecionado na tabela");
        jButton4.addActionListener(evt -> jButton4ActionPerformed(evt));

        // Layout GroupLayout - Precisa ser refeito para incluir os JLabels
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNome)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTurma)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNota1)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNota2)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabelTurma)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabelNota1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabelNota2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Método auxiliar para configurar JTextField com placeholder
    private void configureTextFieldWithPlaceholder(javax.swing.JTextField textField, String placeholder) {
        textField.setFont(new java.awt.Font(FONT_SEGOE_UI, java.awt.Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY), // Borda simples
            BorderFactory.createEmptyBorder(5, 8, 5, 8)  // Padding
        ));
        setPlaceholder(textField, placeholder); // Aplica a lógica do placeholder
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        String nome = getRealText(jTextField1, PLACEHOLDER_NOME);
        String turma = getRealText(jTextField2, PLACEHOLDER_TURMA);
        String nota1Str = getRealText(jTextField3, PLACEHOLDER_NOTA1);
        String nota2Str = getRealText(jTextField4, PLACEHOLDER_NOTA2);

        if (nome.isEmpty() || turma.isEmpty() || nota1Str.isEmpty() || nota2Str.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int nota1 = Integer.parseInt(nota1Str);
            int nota2 = Integer.parseInt(nota2Str);
            int media = (nota1 + nota2) / 2;

            Aluno novoAluno = new Aluno(nome, turma, nota1, nota2, media);
            alunoDAO.createAluno(novoAluno);
            JOptionPane.showMessageDialog(this, "Aluno criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            popularTabela();
            limparCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Notas devem ser números inteiros válidos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao criar aluno: " + e.getMessage(), "Erro de Criação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (nomeOriginalSelecionado == null || nomeOriginalSelecionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeNovo = getRealText(jTextField1, PLACEHOLDER_NOME);
        String turma = getRealText(jTextField2, PLACEHOLDER_TURMA);
        String nota1Str = getRealText(jTextField3, PLACEHOLDER_NOTA1);
        String nota2Str = getRealText(jTextField4, PLACEHOLDER_NOTA2);

        if (nomeNovo.isEmpty() || turma.isEmpty() || nota1Str.isEmpty() || nota2Str.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int nota1 = Integer.parseInt(nota1Str);
            int nota2 = Integer.parseInt(nota2Str);
            int media = (nota1 + nota2) / 2;

            alunoDAO.updateAlunosFull(nomeOriginalSelecionado, nomeNovo, turma, nota1, nota2, media);
            JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            popularTabela();
            limparCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Notas devem ser números inteiros válidos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao atualizar aluno: " + e.getMessage(), "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
         int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeParaDeletar = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja deletar o aluno: " + nomeParaDeletar + "?",
                "Confirmar Deleção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                alunoDAO.deleteAluno(nomeParaDeletar);
                JOptionPane.showMessageDialog(this, "Aluno deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                popularTabela();
                limparCampos();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao deletar aluno: " + e.getMessage(), "Erro de Deleção", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        limparCampos();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Ação ao pressionar Enter no campo nome (opcional)
    }
    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {
        // Ação ao pressionar Enter no campo nota 2 (opcional)
    }


    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PainelControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new PainelControl().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables

    // Declaração dos novos JLabels (precisam ser campos da classe se forem modificados pelo GUI builder)
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JLabel jLabelTurma;
    private javax.swing.JLabel jLabelNota1;
    private javax.swing.JLabel jLabelNota2;
}
