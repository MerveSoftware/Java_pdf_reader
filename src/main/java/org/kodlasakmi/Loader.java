package org.kodlasakmi;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class Loader {

    protected static @Nullable PDDocument loadPDF(File file) {
        try {
            // Eski sürümlerde loadNonSeq veya loadDocument kullanabilirsiniz
            return PDDocument.load(file, MemoryUsageSetting.setupMainMemoryOnly());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
