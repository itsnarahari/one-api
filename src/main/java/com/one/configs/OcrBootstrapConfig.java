package com.one.configs;

import com.sun.jna.NativeLibrary;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ocr")
public class OcrBootstrapConfig {
    private String tessdataDir;
    private String jnaLibPath;

    public void setTessdataDir(String tessdataDir) { this.tessdataDir = tessdataDir; }
    public void setJnaLibPath(String jnaLibPath)   { this.jnaLibPath   = jnaLibPath; }

    // Spring instantiates this config early; the static block runs immediately.
    static {
        // no-op — instance init below does the work
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        if (jnaLibPath != null && !jnaLibPath.isBlank()) {
            // Let JNA know where to find the native libs
            NativeLibrary.addSearchPath("tesseract", jnaLibPath);
            NativeLibrary.addSearchPath("lept", jnaLibPath);
            System.setProperty("jna.library.path", jnaLibPath);
        }
        if (tessdataDir != null && !tessdataDir.isBlank()) {
            // Let Tess4J know where traineddata lives
            System.setProperty("tessdata.dir", tessdataDir);
        }
    }
}

