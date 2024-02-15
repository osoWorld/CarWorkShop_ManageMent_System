package UtilsFeatures;

import javax.swing.*;
import java.awt.print.*;

public class Utils {
    public void printItemsReceipt(JTextPane textReceiptArea) {

        String receiptText = textReceiptArea.getText();

        if (!receiptText.isEmpty()) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Billing System");
            // Create a PageFormat with the size of the paper
            PageFormat pageFormat = job.defaultPage();
            Paper paper = new Paper();
            paper.setSize(300, 500); // Set your preferred size
            pageFormat.setPaper(paper);

            // Set the printable area to the size of the JTextArea
            double width = pageFormat.getWidth();
            double height = pageFormat.getHeight();
            textReceiptArea.setSize((int) width, (int) height);

            // Create a Printable from the JTextArea
            Printable printable = textReceiptArea.getPrintable(null, null);

            // Set the Printable and PageFormat for the PrinterJob
            job.setPrintable(printable, pageFormat);

            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
