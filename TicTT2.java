package tictactoe;

/**
 *
 * @author DesyTrbl;
 */
import java.util.ArrayList;
import javax.swing.JOptionPane;

class ResultMM {

    String[] matrix;
    int score;
    int depth;

    public ResultMM(String[] matrix, int score, int depth) {
        this.matrix = matrix;
        this.score = score;
        this.depth = depth;
    }

    public void updateMatrix(String[] matrix) {
        this.matrix = matrix;
    }

    public int getScore() {
        return score;
    }

    public int getIntrus() {
        for (int i = 0; i < 9; i++) {
            if (matrix[i].equals("o")) {
                return i;
            }
        }
        return -1;
    }
}

class Controller {

    //gameXO is the game  
    static String[] gameXO = new String[9];
    //_sdepth is used to control the depth  
    static int sdepth;

    public Controller() {//reInitialise the gameXO when i create a new controller  
        for (int i = 0; i < 9; i++) {
            gameXO[i] = " ";
        }
    }

    public String inverse(String level) { //inverse level from MIN to MAX  
        return (level.equals("MIN")) ? "MAX" : "MIN";
    }

    public int getScore(String[] demo) { //return the score:  
        //if X win return -1;  
        //if O win return 1;  
        //else return 0, this mean draw  
        if ((demo[0].equalsIgnoreCase("x") && demo[1].equalsIgnoreCase("x") && demo[2].equalsIgnoreCase("x")) || (demo[3].equalsIgnoreCase("x") && demo[4].equalsIgnoreCase("x") && demo[5].equalsIgnoreCase("x"))
                || (demo[6].equalsIgnoreCase("x") && demo[7].equalsIgnoreCase("x") && demo[8].equalsIgnoreCase("x")) || (demo[0].equalsIgnoreCase("x") && demo[3].equalsIgnoreCase("x") && demo[6].equalsIgnoreCase("x"))
                || (demo[1].equalsIgnoreCase("x") && demo[4].equalsIgnoreCase("x") && demo[7].equalsIgnoreCase("x")) || (demo[2].equalsIgnoreCase("x") && demo[5].equalsIgnoreCase("x") && demo[8].equalsIgnoreCase("x"))
                || (demo[0].equalsIgnoreCase("x") && demo[4].equalsIgnoreCase("x") && demo[8].equalsIgnoreCase("x")) || (demo[2].equalsIgnoreCase("x") && demo[4].equalsIgnoreCase("x") && demo[6].equalsIgnoreCase("x"))) {
            return -1;
        }
        if ((demo[0].equalsIgnoreCase("o") && demo[1].equalsIgnoreCase("o") && demo[2].equalsIgnoreCase("o")) || (demo[3].equalsIgnoreCase("o") && demo[4].equalsIgnoreCase("o") && demo[5].equalsIgnoreCase("o"))
                || (demo[6].equalsIgnoreCase("o") && demo[7].equalsIgnoreCase("o") && demo[8].equalsIgnoreCase("o")) || (demo[0].equalsIgnoreCase("o") && demo[3].equalsIgnoreCase("o") && demo[6].equalsIgnoreCase("o"))
                || (demo[1].equalsIgnoreCase("o") && demo[4].equalsIgnoreCase("o") && demo[7].equalsIgnoreCase("o")) || (demo[2].equalsIgnoreCase("o") && demo[5].equalsIgnoreCase("o") && demo[8].equalsIgnoreCase("o"))
                || (demo[0].equalsIgnoreCase("o") && demo[4].equalsIgnoreCase("o") && demo[8].equalsIgnoreCase("o")) || (demo[2].equalsIgnoreCase("o") && demo[4].equalsIgnoreCase("o") && demo[6].equalsIgnoreCase("o"))) {
            return 1;
        }
        return 0;
    }

    public boolean drawGame(String[] demo) {
        //test if the game is draw.  
        //if demo is full, this mean that game is draw  
        //if demo still has empty square, this mean that the game isn't finished  
        for (int i = 0; i < 9; i++) {
            if (demo[i].equals(" ")) {
                return false;
            }
        }
        return true;
    }

    public boolean gameOver(String[] demo) {//if the score of the game is 0 then return false else we have a winner  
        return (getScore(demo) != 0) ? true : false;
    }

    public ArrayList<String[]> genere_succ(String[] demo, String level) {//generate successor  
        //if level is MAX, generate successor with o (o in lowercase)  
        //if level is MIN, generate successor with x (x in lowercase)
        //if demo has no successor, return null  
        ArrayList<String[]> succ = new ArrayList<String[]>();
        for (int i = 0; i < demo.length; i++) {
            if (demo[i].equals(" ")) {
                String[] child = new String[9];
                for (int j = 0; j < 9; j++) {
                    child[j] = demo[j];
                }
                if (level.equals("MAX")) {
                    child[i] = "o";
                } else {
                    child[i] = "x";
                }
                succ.add(child);
            }
        }
        return (succ.size() == 0) ? null : succ;
    }

    public ResultMM getResult(ArrayList<ResultMM> listScore, String level) {//this method is used to get the appropriate score  
        //if level is MAX, i search for the higher score in the nearer depth  
        //if level is MIN, i search for the lowest score in the nearer depth  
        ResultMM result = listScore.get(0);
        if (level.equals("MAX")) {
            for (int i = 1; i < listScore.size(); i++) {
                if ((listScore.get(i).getScore() > result.getScore())
                        || (listScore.get(i).getScore() == result.getScore() && listScore.get(i).depth < result.depth)) {
                    result = listScore.get(i);
                }
            }
        } else {
            for (int i = 1; i < listScore.size(); i++) {
                if ((listScore.get(i).getScore() < result.getScore())
                        || (listScore.get(i).getScore() == result.getScore() && listScore.get(i).depth < result.depth)) {
                    result = listScore.get(i);
                }
            }
        }
        return result;
    }

    public ResultMM MinMax(String[] demo, String level, int fils, int depth) {/*MinMax algorithm  
           * 1- generate successor  
           * 2- if no successor or game is finished return score   
           * 3- if there is successor  
           *      a) apply MinMax for each successor  
           *      b) after recursive call, i return the good score  
         */
        //1---------------  
        ArrayList<String[]> children = genere_succ(demo, level);
        //2------------------    
        if (children == null || gameOver(demo)) {
            return new ResultMM(demo, getScore(demo), depth);
        } else {//3------------------   
            ArrayList<ResultMM> listScore = new ArrayList<ResultMM>();
            //pass into each child  
            for (int i = 0; i < children.size(); i++) {//3 a)---------------  
                listScore.add(MinMax(children.get(i), inverse(level), 1, depth + 1));
            }
            //3 b)----------------  
            ResultMM res = getResult(listScore, level);
            if (fils == 1) {
                res.updateMatrix(demo);
            }
            return res;
        }
    }

    public int makeMove(int index) {
        gameXO[index] = "X";
        if (gameOver(gameXO)) {
            return -1;
        }
        if (drawGame(gameXO)) {
            return -2;
        }
        ResultMM res = MinMax(gameXO, "MAX", 0, 0);
        int i = res.getIntrus();
        gameXO[i] = "O";
        return i;
    }

    public void setGame() {
        for (int i = 0; i < 9; i++) {
            gameXO[i] = " ";
        }
    }
}

public class TicTT2 extends javax.swing.JFrame {

    //gameXO is the game  
    static String[] ply = new String[9];
    Controller xx = new Controller();

    public TicTT2() {
        initComponents();
        for(int i=0; i<9; i++){
            ply[i] = " ";
        }
    }

    public void cekWin() {
        int yy = xx.getScore(ply);
        boolean zz = xx.drawGame(ply);
        if (yy == -1) {
            JOptionPane.showMessageDialog(null, "X WIN");
            Reset();
        } else if (yy == 1) {
            JOptionPane.showMessageDialog(null, "O WIN");
            Reset();
        } else if (zz == true) {
            JOptionPane.showMessageDialog(null, "PERMAINAN SERI");
            Reset();
        }
    }

    public void Move(int a) {
        int y = xx.makeMove(a);
        if (y == 0) {
            ply[0] = "O";
            b1.setText("O");
            b1.setEnabled(false);
        } else if (y == 1) {
            ply[1] = "O";
            b2.setText("O");
            b2.setEnabled(false);
        } else if (y == 2) {
            ply[2] = "O";
            b3.setText("O");
            b3.setEnabled(false);
        } else if (y == 3) {
            ply[3] = "O";
            b4.setText("O");
            b4.setEnabled(false);
        } else if (y == 4) {
            ply[4] = "O";
            b5.setText("O");
            b5.setEnabled(false);
        } else if (y == 5) {
            ply[5] = "O";
            b6.setText("O");
            b6.setEnabled(false);
        } else if (y == 6) {
            ply[6] = "O";
            b7.setText("O");
            b7.setEnabled(false);
        } else if (y == 7) {
            ply[7] = "O";
            b8.setText("O");
            b8.setEnabled(false);
        } else if (y == 8) {
            ply[8] = "O";
            b9.setText("O");
            b9.setEnabled(false);
        }
    }

    public void Reset() {
        b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(true);
        b4.setEnabled(true);
        b5.setEnabled(true);
        b6.setEnabled(true);
        b7.setEnabled(true);
        b8.setEnabled(true);
        b9.setEnabled(true);
        b1.setText("");
        b2.setText("");
        b3.setText("");
        b4.setText("");
        b5.setText("");
        b6.setText("");
        b7.setText("");
        b8.setText("");
        b9.setText("");
        for (int i = 0; i < 9; i++) {
            ply[i] = " ";
        }
        xx.setGame();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bhome = new javax.swing.JButton();
        breset = new javax.swing.JButton();
        b1 = new javax.swing.JButton();
        b2 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        b4 = new javax.swing.JButton();
        b5 = new javax.swing.JButton();
        b6 = new javax.swing.JButton();
        b7 = new javax.swing.JButton();
        b8 = new javax.swing.JButton();
        b9 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Player vs Computer");
        setResizable(false);

        bhome.setBackground(new java.awt.Color(204, 204, 204));
        bhome.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        bhome.setText("HOME");
        bhome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhomeActionPerformed(evt);
            }
        });

        breset.setBackground(new java.awt.Color(204, 204, 204));
        breset.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        breset.setText("RESET");
        breset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bresetActionPerformed(evt);
            }
        });

        b1.setBackground(new java.awt.Color(255, 255, 204));
        b1.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        b1.setBorderPainted(false);
        b1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1ActionPerformed(evt);
            }
        });

        b2.setBackground(new java.awt.Color(204, 255, 255));
        b2.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b2ActionPerformed(evt);
            }
        });

        b3.setBackground(new java.awt.Color(255, 255, 204));
        b3.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b3ActionPerformed(evt);
            }
        });

        b4.setBackground(new java.awt.Color(204, 255, 255));
        b4.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b4ActionPerformed(evt);
            }
        });

        b5.setBackground(new java.awt.Color(255, 255, 204));
        b5.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b5ActionPerformed(evt);
            }
        });

        b6.setBackground(new java.awt.Color(204, 255, 255));
        b6.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b6ActionPerformed(evt);
            }
        });

        b7.setBackground(new java.awt.Color(255, 255, 204));
        b7.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b7ActionPerformed(evt);
            }
        });

        b8.setBackground(new java.awt.Color(204, 255, 255));
        b8.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b8ActionPerformed(evt);
            }
        });

        b9.setBackground(new java.awt.Color(255, 255, 204));
        b9.setFont(new java.awt.Font("Arial", 1, 64)); // NOI18N
        b9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b9ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Player vs Computer");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(b7, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                    .addComponent(bhome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(breset, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(b9, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b7, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b9, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bhome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(breset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b4ActionPerformed
        if (b4.isEnabled()) {
            b4.setText("X");
            b4.setEnabled(false);
            ply[3] = b4.getText();
            Move(3);
        }
        cekWin();
    }//GEN-LAST:event_b4ActionPerformed

    private void b7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b7ActionPerformed
        if (b7.isEnabled()) {
            b7.setText("X");
            ply[6] = b7.getText();
            b7.setEnabled(false);
            Move(6);
        }
        cekWin();
    }//GEN-LAST:event_b7ActionPerformed

    private void bhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhomeActionPerformed
        TicTT x = new TicTT();
        x.show();
        x.setSize(340, 320);
        x.setLocation(500, 200);
        dispose();
    }//GEN-LAST:event_bhomeActionPerformed

    private void bresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bresetActionPerformed
        Reset();
    }//GEN-LAST:event_bresetActionPerformed

    private void b1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1ActionPerformed
        if (b1.isEnabled()) {
            b1.setText("X");
            ply[0] = b1.getText();
            b1.setEnabled(false);
            Move(0);
        }
        cekWin();
    }//GEN-LAST:event_b1ActionPerformed

    private void b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b2ActionPerformed
        if (b2.isEnabled()) {
            b2.setText("X");
            ply[1] = b2.getText();
            b2.setEnabled(false);
            Move(1);
        }
        cekWin();
    }//GEN-LAST:event_b2ActionPerformed

    private void b3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b3ActionPerformed
        if (b3.isEnabled()) {
            b3.setText("X");
            ply[2] = b3.getText();
            b3.setEnabled(false);
            Move(2);
        }
        cekWin();
    }//GEN-LAST:event_b3ActionPerformed

    private void b5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b5ActionPerformed
        if (b5.isEnabled()) {
            b5.setText("X");
            ply[4] = b5.getText();
            b5.setEnabled(false);
            Move(4);
        }
        cekWin();
    }//GEN-LAST:event_b5ActionPerformed

    private void b6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b6ActionPerformed
        if (b6.isEnabled()) {
            b6.setText("X");
            ply[5] = b6.getText();
            b6.setEnabled(false);
            Move(5);
        }
        cekWin();
    }//GEN-LAST:event_b6ActionPerformed

    private void b8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b8ActionPerformed
        if (b8.isEnabled()) {
            b8.setText("X");
            ply[7] = b8.getText();
            b8.setEnabled(false);
            Move(7);
        }
        cekWin();
    }//GEN-LAST:event_b8ActionPerformed

    private void b9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b9ActionPerformed
        if (b9.isEnabled()) {
            b9.setText("X");
            ply[8] = b9.getText();
            b9.setEnabled(false);
            Move(8);
        }
        cekWin();
    }//GEN-LAST:event_b9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TicTT2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TicTT2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TicTT2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TicTT2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TicTT2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b1;
    private javax.swing.JButton b2;
    private javax.swing.JButton b3;
    private javax.swing.JButton b4;
    private javax.swing.JButton b5;
    private javax.swing.JButton b6;
    private javax.swing.JButton b7;
    private javax.swing.JButton b8;
    private javax.swing.JButton b9;
    private javax.swing.JButton bhome;
    private javax.swing.JButton breset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
