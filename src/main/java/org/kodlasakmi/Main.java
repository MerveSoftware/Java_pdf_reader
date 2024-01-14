package org.kodlasakmi;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition; // Değişiklik burada

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends PDFTextStripper {

    static List<String> lines = new ArrayList<String>();
    private static String anotherFileName;

    public Main() throws IOException {
    }

    public static void main(String[] args) throws IOException {

        List<Invoice> invoices = new ArrayList<>();
        String branch = "1000";


        String fileName = "/home/merve/text2.pdf";//kendi dosya yolumu verdim

// Eski Kullanım
// try (PDDocument document = PDDocument.load(new File(fileName))) {

// Yeni Kullanım
         try (PDDocument document = Loader.loadPDF(new File(fileName))) {
             PDFTextStripper stripper = new Main();
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage(document.getNumberOfPages());
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);

            // Loop through the lines
            for ( int i = 0; i < lines.size(); i++ ) {

                Invoice invoice = new Invoice();

                if ( lines.get(i).startsWith("2001-") ) {
                    invoice.setInvoice_number( lines.get(i) );
                    invoice.setType( lines.get( i + 1 ) );
                    invoice.setAccount( lines.get( i + 2 ) );
                    invoice.setName( lines.get( i + 3 ) );
                    invoice.setJob_num( lines.get( i + 4) );
                    invoice.setCashier_user( lines.get( i + 5 ) );
                    invoice.setSis_mat_br( branch );
                    invoice.setDate( lines.get( i + 6 ) );
                    invoice.setTotal( lines.get( i + 7 ) );
                    invoice.setCost( lines.get( i + 8 ) );
                    invoice.setGm_perc( "0" );

                    List<String[]> items = new ArrayList<>();

                    for ( int j = i + 1; j < lines.size(); j++ ) {

                        if ( lines.get( j ).startsWith("2001-") ) {
                            i = j - 1;
                            break;
                        }

                        if ( lines.get(j).endsWith(".0000")
                                && (lines.get( j + 1 ).contains("EA") || lines.get( j + 1 ).contains("EACH"))
                                && (lines.get( j + 3 ).contains("EA") || lines.get( j + 3 ).contains("EACH")) ) { // Found Quantity
                            String[] item = {
                                    lines.get( j - 2 ), // Item Number
                                    lines.get( j - 1 ), // Description
                                    lines.get( j ),     // Quantity
                                    lines.get( j + 1 ), // U/M
                                    lines.get( j + 2 ), // Unit Price
                                    lines.get( j + 3 ), // U/M
                                    lines.get( j + 4 ), // Unit Cost
                                    lines.get( j + 5 )  // GM%
                            };

                            items.add( item );
                        }
                    }

                    invoice.setItems( items );
                }

                // Add invoice to invoices List
                if ( invoice.getAccount() != null ) {
                    invoices.add( invoice );
                }
            }
        }

        for ( Invoice invoice : invoices ) {

            System.out.println( invoice );
        }
    }

    @Override
    protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
        lines.add(str);
    }
}