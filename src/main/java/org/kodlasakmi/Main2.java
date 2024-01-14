package org.kodlasakmi;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main2 extends PDFTextStripper {

    static List<String> lines = new ArrayList<String>();
    public Main2() throws IOException {
    }

    public static void main(String[] args) throws IOException {

        List<Invoice> invoices = new ArrayList<>();
        String branch = "1000";

        String fileName = "/home/merve/text2.pdf";

        try (PDDocument document = Loader.loadPDF(new File(fileName))) {
            PDFTextStripper stripper = new Main2();
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            assert document != null;
            if (document != null) stripper.setEndPage(document.getNumberOfPages());
            else stripper.setEndPage(0);
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

                    // Increment i to the products
                    i = i + 18;

                    if ( ! lines.get( i - 1 ).contains("GM%") ) {

                        for ( int k = i; k < lines.size(); k++ ) {
                            if ( lines.get(k).contains("GM%") ) {
                                i = k + 1;
                                break;
                            }
                        }
                    }

                    List<String[]> items = new ArrayList<>();

                    for ( int j = i; j < lines.size() - 20; ) {

                        String[] item = {
                                lines.get(j),       // Item Number
                                lines.get( j + 1 ), // Description
                                lines.get( j + 2 ), // Quantity
                                lines.get( j + 3 ), // U/M
                                lines.get( j + 4 ), // Unit Price
                                lines.get( j + 5 ), // U/M
                                lines.get( j + 6 ), // Unit Cost
                                lines.get( j + 7 )  // GM%
                        };

                        // Go to next line item
                        // If it's another invoice, repeat the whole process and create a new invoice
                        if ( lines.get( j + 8 ).trim().startsWith("2001-") ) {
                            i = j + 8 - 1;
                            items.add( item );
                            break;
                        }

                        if ( lines.get( j + 9 ).trim().startsWith("2001-") ) {
                            i = j + 9 - 1;
                            item[1] = item[1] + lines.get( j + 8 );
                            items.add( item );
                            break;
                        }

                        if ( lines.get( j + 9 ).trim().contains("Branch") ) {
                            j = j + 30;

                            if ( lines.get( j + 1 ).trim().startsWith("2001-") ) {
                                items.add( item );
                                i = j;
                                break;
                            }

                            if ( lines.get( j + 2 ).trim().startsWith("2001-") ) {
                                items.add( item );
                                i = j + 1;
                                break;
                            }

                            if ( lines.get( j + 3 ).trim().startsWith("2001-") ) {
                                items.add( item );
                                i = j + 2;
                                break;
                            }
                        }

                        // If it doesn't start with 2001- and 2 more lines down we have the qty, that means that
                        // this is the next part number in the list
                        if ( lines.get( j + 10 ).trim().endsWith(".0000") ) {
                            j = j + 8;
                            items.add( item );
                            continue;
                        }

                        // If we made it this far, that means that we don't have a quantity two lines down, and
                        // the description was split into 2 and the other part of the description is on line 8.
                        item[1] = item[1] + lines.get( j + 8 );

                        // One more check. We could have it that the description is split into three lines.
                        // So we first check if the quantity is 3 lines down. If it is, great: it was only split into two
                        // and we can restart the process
                        if ( lines.get( j + 11 ).trim().endsWith(".0000") ) {
                            j = j + 9;
                            items.add( item );
                            continue;
                        }

                        // Otherwise, line 9 also has part of the description. So we need to add it
                        item[1] = item[1] + lines.get( j + 9 );

                        // For sure this is the next line number
                        j = j + 10;
                        items.add( item );
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



