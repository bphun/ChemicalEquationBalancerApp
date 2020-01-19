package com.bphan.ChemicalEquationBalancerAppServer.Models.ResponseModels;

import java.io.IOException;
import java.util.List;

// ImageProcessorResponse.java

public class ImageProcessorResponse {
    private List<Response> responses;

    private String requestId;
    private long requestProcessStartTimeMs, requestProcessEndTimeMs;

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> value) {
        this.responses = value;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getRequestProcessStartTimeMs() {
        return requestProcessStartTimeMs;
    }

    public void setRequestProcessStartTimeMs(long requestProcessStartTimeMs) {
        this.requestProcessStartTimeMs = requestProcessStartTimeMs;
    }

    public long getRequestProcessEndTimeMs() {
        return requestProcessEndTimeMs;
    }

    public void setRequestProcessEndTimeMs(long requestProcessEndTimeMs) {
        this.requestProcessEndTimeMs = requestProcessEndTimeMs;
    }
}

// Response.java

class Response {
    private List<TextAnnotation> textAnnotations;
    private FullTextAnnotation fullTextAnnotation;

    public List<TextAnnotation> getTextAnnotations() {
        return textAnnotations;
    }

    public void setTextAnnotations(List<TextAnnotation> value) {
        this.textAnnotations = value;
    }

    public FullTextAnnotation getFullTextAnnotation() {
        return fullTextAnnotation;
    }

    public void setFullTextAnnotation(FullTextAnnotation value) {
        this.fullTextAnnotation = value;
    }
}

// FullTextAnnotation.java

class FullTextAnnotation {
    private List<Page> pages;
    private String text;

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> value) {
        this.pages = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }
}

// Page.java

class Page {
    private List<Block> blocks;
    private PageProperty property;
    private long width;
    private long height;

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> value) {
        this.blocks = value;
    }

    public PageProperty getProperty() {
        return property;
    }

    public void setProperty(PageProperty value) {
        this.property = value;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long value) {
        this.width = value;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long value) {
        this.height = value;
    }
}

// Block.java

class Block {
    private List<Paragraph> paragraphs;
    private String blockType;
    private double confidence;
    private Bounding boundingBox;

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> value) {
        this.paragraphs = value;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String value) {
        this.blockType = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double value) {
        this.confidence = value;
    }

    public Bounding getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Bounding value) {
        this.boundingBox = value;
    }
}

// Bounding.java

class Bounding {
    private List<Vertex> vertices;

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> value) {
        this.vertices = value;
    }
}

// Vertex.java

class Vertex {
    private long x;
    private Long y;

    public long getX() {
        return x;
    }

    public void setX(long value) {
        this.x = value;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long value) {
        this.y = value;
    }
}

// Paragraph.java

class Paragraph {
    private Bounding boundingBox;
    private List<Word> words;
    private double confidence;

    public Bounding getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Bounding value) {
        this.boundingBox = value;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> value) {
        this.words = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double value) {
        this.confidence = value;
    }
}

// Word.java

class Word {
    private WordProperty property;
    private List<Symbol> symbols;
    private double confidence;
    private Bounding boundingBox;

    public WordProperty getProperty() {
        return property;
    }

    public void setProperty(WordProperty value) {
        this.property = value;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Symbol> value) {
        this.symbols = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double value) {
        this.confidence = value;
    }

    public Bounding getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Bounding value) {
        this.boundingBox = value;
    }
}

// WordProperty.java

class WordProperty {
    private List<PurpleDetectedLanguage> detectedLanguages;

    public List<PurpleDetectedLanguage> getDetectedLanguages() {
        return detectedLanguages;
    }

    public void setDetectedLanguages(List<PurpleDetectedLanguage> value) {
        this.detectedLanguages = value;
    }
}

// PurpleDetectedLanguage.java

class PurpleDetectedLanguage {
    private Locale languageCode;

    public Locale getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(Locale value) {
        this.languageCode = value;
    }
}

// Locale.java

enum Locale {
    af, sq, ar, hy, be, bn, bg, ca, zh, hr, cs, da, nl, en, et, fil, tl, fi, fr, de, el, gu, iw, hi, hu, is, id, it, ja,
    kn, km, ko, lo, lv, lt, mk, ms, ml, mr, ne, no, fa, pl, pt, pa, ro, ru, ru_PETR1708, sr, sr_Latn, sk, sl, es, sv,
    ta, te, th, tr, uk, vi, yi, am, grc, as, az, az_cyrl, eu, bs, my, ceb, chr, dv, dz, eo, gl, ka, ht, ga, jv, kk, ky,
    la, mt, mn, or, ps, sa, si, sw, syr, bo, ti, ur, uz, uz_cyrl, cy, zu, fy, mi, co, hmn, haw, yo, sd, ny, ku;

    public String toValue() {
        switch (this) {
        case en:
            return "en";
        default:
            return "unknown";
        }
    }

    public static Locale forValue(String value) throws IOException {
        if (value.equals("en"))
            return en;
        throw new IOException("Cannot deserialize Locale");
    }
}

// Symbol.java

class Symbol {
    private String text;
    private SymbolProperty property;
    private double confidence;
    private Bounding boundingBox;

    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }

    public SymbolProperty getProperty() {
        return property;
    }

    public void setProperty(SymbolProperty value) {
        this.property = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double value) {
        this.confidence = value;
    }

    public Bounding getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Bounding value) {
        this.boundingBox = value;
    }
}

// SymbolProperty.java

class SymbolProperty {
    private List<PurpleDetectedLanguage> detectedLanguages;
    private DetectedBreak detectedBreak;

    public List<PurpleDetectedLanguage> getDetectedLanguages() {
        return detectedLanguages;
    }

    public void setDetectedLanguages(List<PurpleDetectedLanguage> value) {
        this.detectedLanguages = value;
    }

    public DetectedBreak getDetectedBreak() {
        return detectedBreak;
    }

    public void setDetectedBreak(DetectedBreak value) {
        this.detectedBreak = value;
    }
}

// DetectedBreak.java

class DetectedBreak {
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type value) {
        this.type = value;
    }
}

// Type.java

enum Type {
    EOL_SURE_SPACE, LINE_BREAK, SPACE, HYPHEN;

    public String toValue() {
        switch (this) {
        case EOL_SURE_SPACE:
            return "EOL_SURE_SPACE";
        case LINE_BREAK:
            return "LINE_BREAK";
        case SPACE:
            return "SPACE";
        case HYPHEN:
            return "HYPHEN";
        }
        return null;
    }

    public static Type forValue(String value) throws IOException {
        if (value.equals("EOL_SURE_SPACE"))
            return EOL_SURE_SPACE;
        if (value.equals("LINE_BREAK"))
            return LINE_BREAK;
        if (value.equals("SPACE"))
            return SPACE;
        throw new IOException("Cannot deserialize Type");
    }
}

// PageProperty.java

class PageProperty {
    private List<FluffyDetectedLanguage> detectedLanguages;

    public List<FluffyDetectedLanguage> getDetectedLanguages() {
        return detectedLanguages;
    }

    public void setDetectedLanguages(List<FluffyDetectedLanguage> value) {
        this.detectedLanguages = value;
    }
}

// FluffyDetectedLanguage.java

class FluffyDetectedLanguage {
    private Locale languageCode;
    private long confidence;

    public Locale getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(Locale value) {
        this.languageCode = value;
    }

    public long getConfidence() {
        return confidence;
    }

    public void setConfidence(long value) {
        this.confidence = value;
    }
}

// TextAnnotation.java

class TextAnnotation {
    private Locale locale;
    private Bounding boundingPoly;
    private String description;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale value) {
        this.locale = value;
    }

    public Bounding getBoundingPoly() {
        return boundingPoly;
    }

    public void setBoundingPoly(Bounding value) {
        this.boundingPoly = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }
}
