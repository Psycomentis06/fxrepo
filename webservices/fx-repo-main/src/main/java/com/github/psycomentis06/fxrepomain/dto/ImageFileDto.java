package com.github.psycomentis06.fxrepomain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageFileDto extends FileDto {
    private HashMap<String, String> accentColor;
    private Set<HashMap<String, String>> colorPalette;
    private boolean landscape;
    private String perceptualHash;
    private String differenceHash;
    private String colorHash;
    private String averageHash;
    private Set<ImagePostDto> posts;

    public String getAccentColor() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        accentColor.forEach((k, v) -> sb.append("\"").append(k).append("\":\"").append(v).append("\","));
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    public void setAccentColor(HashMap<String, String> accentColor) {
        this.accentColor = accentColor;
    }

    public String getColorPalette() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        AtomicInteger index = new AtomicInteger(0);
        colorPalette.forEach(item -> {
            sb.append("{");
            item.forEach((k, v) -> sb.append("\"").append(k).append("\":\"").append(v).append("\","));
            sb.deleteCharAt(sb.length() - 1);
            sb.append("}");
            if (index.get() < colorPalette.size() - 1) {
                sb.append(",");
            }
            index.getAndIncrement();
        });
        sb.append("]");
        return sb.toString();
    }

    public void setColorPalette(Set<HashMap<String, String>> colorPalette) {
        this.colorPalette = colorPalette;
    }
}
