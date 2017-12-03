package pdfcombiner;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.apache.pdfbox.multipdf.PDFMergerUtility;


public class PDFCombiner extends JPanel
                             implements ActionListener {
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;
    PDFMergerUtility PDFMerger = new PDFMergerUtility();
    

    public PDFCombiner() {
        super(new BorderLayout());
        log = new JTextArea(10,40);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        fc = new JFileChooser();
        openButton = new JButton("OPEN PDF");
        openButton.addActionListener(this);
        saveButton = new JButton("SAVE AS ONE");
        saveButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(PDFCombiner.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    PDFMerger.addSource(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PDFCombiner.class.getName()).log(Level.SEVERE, null, ex);
                }
                log.append(file.getName() + newline);
            } 
            log.setCaretPosition(log.getDocument().getLength());

        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(PDFCombiner.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                PDFMerger.setDestinationFileName(file.getAbsolutePath());
                try {
                    PDFMerger.mergeDocuments();
                } catch (IOException ex) {
                    Logger.getLogger(PDFCombiner.class.getName()).log(Level.SEVERE, null, ex);
                }
                log.append("Combined into: " + file.getName() + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("PDF Combiner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new PDFCombiner());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}
