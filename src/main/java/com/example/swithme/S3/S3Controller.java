package com.example.swithme.S3;

import com.example.swithme.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {

    private final S3UploadService s3UploadService;

    @PostMapping("/api/auth/image")
    public ApiResponseDto imageUpload(@RequestPart(required = false) MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return new ApiResponseDto("파일이 유효하지 않습니다.", HttpStatus.OK.value());
        }
        try {
            return new ApiResponseDto(s3UploadService.uploadFiles(multipartFile, "static"), HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponseDto("파일이 유효하지 않습니다.", HttpStatus.OK.value());
        }
    }
}