package com.himanism.hcharityapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityPhotosDto {
    private String coverPhoto;
    private String qrCode;
    private List<String> photos;
}
